package io.github.shamrice.discchanger.motorcontroller;

/**
 * Created by Erik on 1/11/2017.
 */

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.SoftPwm;

import io.github.shamrice.discchanger.config.definitions.Definitions;
import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;
import io.github.shamrice.discchanger.motorcontroller.positionLookup.CarouselPositionLookup;
import io.github.shamrice.discchanger.motorcontroller.sensorListeners.CarouselSensorListener;

import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Concrete class for disc carousel motor controls.
 */
public class CarouselMotorController extends MotorController  {

    final int minSpinPwm = 40;
    final int maxSpinPwm = 120;

    private static int discCount = 0;
    private static int numDiscsToSpin = 0;
    private static int positionHighCount = 0;
    private static ConcurrentHashMap<String, PinState> pinStates = new ConcurrentHashMap<String, PinState>();

    public CarouselMotorController(MotorConfiguration motorConfiguration)  {
        super(motorConfiguration);

        SoftPwm.softPwmCreate(super.motorPinA, 0, 255);
        SoftPwm.softPwmCreate(super.motorPinB, 0, 255);

        //set initial pin state for all sensors.
        for(GpioPinDigitalInput sensorInput : super.sensorInputs) {
            pinStates.put(sensorInput.getName(), sensorInput.getState());
            sensorInput.addListener(new CarouselSensorListener(sensorInput.getName()));

            System.out.println(sensorInput.getName() + " : " + sensorInput.getState());
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        SoftPwm.softPwmWrite(motorPinA, 0);
        SoftPwm.softPwmWrite(motorPinB, 0);
    }

    public int init() {

        CarouselPositionLookup carouselPositionLookup = new CarouselPositionLookup();
        int tempCurrentDiscNum = -1;

        //loop while currentDiscNum is unknown.
        while (tempCurrentDiscNum < 0) {

            boolean  highCountFinished = false;
            boolean highFound = false;

            PinState sensorStartState = pinStates.get(Definitions.CAROUSEL_SENSOR3_PIN);

            direction = Direction.FORWARD;
            spinCarousel(45);

            if (sensorStartState.isLow()) {
                while (!highCountFinished) {

                    PinState currentSensorState = pinStates.get(Definitions.CAROUSEL_SENSOR3_PIN);

                    if (!highFound && (sensorStartState.getValue() != currentSensorState.getValue()) && currentSensorState.isHigh()){
                        System.out.println("high found!");
                        highFound = true;
                    }

                    if (currentSensorState.isLow() && highFound) {
                        System.out.println("high Count scan complete.");
                        System.out.println("Attempting to look up: " + positionHighCount);
                        tempCurrentDiscNum = carouselPositionLookup.getPosition(positionHighCount);
                        positionHighCount = 0;
                        highCountFinished = true;
                    }
                }
            }
        }

        stop();
        System.out.println("Current disc num=" + tempCurrentDiscNum);
        return tempCurrentDiscNum;
    }

    public static void setPinStateByName(String sensorName, PinState newState) {

        pinStates.replace(sensorName, newState);

        //3rd pin is not used to check if disc has travelled.
        if (!sensorName .equals(Definitions.CAROUSEL_SENSOR3_PIN)) {
            checkDiscTravelled();
        }
    }

    private static synchronized void checkDiscTravelled() {

        if (pinStates.get(Definitions.CAROUSEL_SENSOR1_PIN).isHigh() && pinStates.get(Definitions.CAROUSEL_SENSOR2_PIN).isHigh()) {
            discCount++;
            if (pinStates.get(Definitions.CAROUSEL_SENSOR3_PIN).isHigh()) {
                positionHighCount++;
            }
        }

        for(PinState state : pinStates.values()) {
            System.out.print(state.getValue() + ", ");
        }

        System.out.print(discCount + "\n");

        if (discCount >= numDiscsToSpin) {
          //  System.out.println("STOP");
            isRunning = false;
        }
    }

    public void spinNumDiscs(int numDiscsToSpin, Direction direction) {

        //reset disc count.
        discCount = 0;

        if (numDiscsToSpin > 0) {

            if (numDiscsToSpin > 200) numDiscsToSpin = 200;

            CarouselMotorController.numDiscsToSpin = numDiscsToSpin;
            isRunning = true;
            this.direction = direction;

            double pwmValue = minSpinPwm;
            boolean isMaxSpeed = false;
            boolean isSlowed = false;

            //equation gets weird over 200, so carousel is limited to one full spin.
            double discToSlowAt = numDiscsToSpin * (.80);// * (1 + (numDiscsToSpin * 0.001)));

            pwmValue = (minSpinPwm + numDiscsToSpin) / 1.3;
            if (pwmValue > maxSpinPwm)
                pwmValue = maxSpinPwm;

            try {
                while (isRunning) {

                    /**
                     * TODO - Move this to listener related method. Polling in while loop eats 100% CPU. Should just threadsleep.
                     */
                    if (discCount > discToSlowAt) {
                        pwmValue = 40;
                        isSlowed = true;
                        spinCarousel((int) pwmValue);
                    }

                    if (!isMaxSpeed && !isSlowed) {

                        for (int i = 0; i <= pwmValue; i++) {
                            spinCarousel(i);
                            if (discCount > discToSlowAt) {
                                break;
                            }
                            Thread.sleep(25);
                        }
                        isMaxSpeed = true;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            stop();
        }

        CarouselMotorController.numDiscsToSpin = 0;
        CarouselMotorController.discCount = 0;
    }

    private void spinCarousel(int speed) {
        switch(direction) {
            case FORWARD:
                SoftPwm.softPwmWrite(motorPinA, speed);;
                break;
            case BACKWARD:
                SoftPwm.softPwmWrite(motorPinB, speed);
                break;
        }
    }
}

