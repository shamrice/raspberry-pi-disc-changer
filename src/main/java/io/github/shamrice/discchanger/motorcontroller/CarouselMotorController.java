package io.github.shamrice.discchanger.motorcontroller;

/**
 * Created by Erik on 1/11/2017.
 */

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.SoftPwm;

import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;
import io.github.shamrice.discchanger.motorcontroller.sensorListeners.CarouselSensorListener;

import java.util.concurrent.ConcurrentHashMap;


/**
 * Concrete class for disc carousel motor controls.
 */
public class CarouselMotorController extends MotorController {

    final int minSpinPwm = 50;
    final int maxSpinPwm = 190;

    private static int discCount = 0;
    private static int numDiscsToSpin = 0;
    private static boolean isRunning = false;
    private static ConcurrentHashMap<String, PinState> pinStates = new ConcurrentHashMap<String, PinState>();

    public CarouselMotorController(MotorConfiguration motorConfiguration)  {
        super(motorConfiguration);

        SoftPwm.softPwmCreate(super.motorPinA, 0, 255);
        SoftPwm.softPwmCreate(super.motorPinB, 0, 255);

        //set initial pin state for all sensors.
        for(GpioPinDigitalInput sensorInput : super.sensorInputs) {
            pinStates.put(sensorInput.getName(), sensorInput.getState());

            sensorInput.addListener(new CarouselSensorListener(sensorInput.getName()));
        }

        for(PinState states : pinStates.values()) {
            System.out.println(states.getName() + " : " + states.getValue());
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        SoftPwm.softPwmStop(motorPinA);
    }

    public static synchronized void setPinStateByName(String sensorName, PinState newState) {

        pinStates.replace(sensorName, newState);
        checkDiscTravelled();
    }

    private static synchronized void checkDiscTravelled() {

        boolean allHigh = true;
        for (PinState pinState: pinStates.values()) {
            System.out.print(":" + pinState.getValue() + " ");
            if (pinState.isLow()) {
                allHigh = false;
            }
        }
        if (allHigh) {
            discCount++;
        }

        System.out.print(discCount + "\n");

        if (discCount >= numDiscsToSpin) {
            System.out.println("STOP");
            isRunning = false;
        }
    }

    public void spinNumDiscs(int numDiscsToSpin) {

        if (numDiscsToSpin > 0) {
            CarouselMotorController.numDiscsToSpin = numDiscsToSpin;
            isRunning = true;

            double pwmValue = minSpinPwm;
            boolean isMaxSpeed = false;
            boolean isSlowed = false;

            double discToSlowAt = numDiscsToSpin * .80;

            pwmValue = (minSpinPwm + numDiscsToSpin) / 1.3;
            if (pwmValue > maxSpinPwm)
                pwmValue = maxSpinPwm;

            try {
                while (isRunning) {

                    /**
                     * TODO - Move this to listener related method. Polling in while loop eats 100% CPU. Should just threadsleep.
                     */
                    if (discCount > discToSlowAt) {
                        pwmValue = 50;
                        isSlowed = true;
                        SoftPwm.softPwmWrite(motorPinA, ((int) pwmValue));
                    }

                    if (!isMaxSpeed && !isSlowed) {

                        for (int i = 0; i <= pwmValue; i++) {
                            SoftPwm.softPwmWrite(motorPinA, i);
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
    }
}
