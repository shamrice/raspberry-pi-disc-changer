package io.github.shamrice.discchanger;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import io.github.shamrice.discchanger.motorcontroller.*;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * Created by Erik on 1/11/2017.
 */
public class DiscChangerStarter {

    static int discCount = 0;
    static boolean isRunning = true;
    static PinState prevState = PinState.LOW;
    static PinState prevStateB = PinState.LOW;
    final static int motorPwmPin = 8;
    final static int maxPwm = 190;
    final static int minPwn = 50;

    static PinState sensorAState;
    static PinState sensorBState;

    static int numDiscsToSpin = 25;

    /**
     * Entry point.
     * @param args - command line arguments.
     */
    public static void main(String[] args) {

        if (args.length == 1) {
            numDiscsToSpin = Integer.parseInt(args[0]);
            System.out.println("Spinning " + numDiscsToSpin + " discs.");
        }

        final GpioController gpioController = GpioFactory.getInstance();

        SoftPwm.softPwmCreate(motorPwmPin, minPwn, maxPwm);

        //final GpioPinDigitalOutput motorOutputA = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_08, "Output1", PinState.LOW);
        final GpioPinDigitalOutput motorOutputB = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_09, "Output1", PinState.LOW);
        final GpioPinDigitalOutput relayPin1 = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "RelayPin1", PinState.LOW);

        //final MotorController carouselMotorController = new CarouselMotorController(motorOutputA, motorOutputB);

        GpioPinDigitalInput sensorPin = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_21, "SensorPin1", PinPullResistance.PULL_DOWN);
        GpioPinDigitalInput sensorPin2 = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_22, "SensorPin2", PinPullResistance.PULL_DOWN);

        sensorAState = sensorPin.getState();
        sensorBState = sensorPin.getState();


       // sensorPin.setDebounce(10);

        prevState = sensorPin.getState();
        prevStateB = sensorPin2.getState();

        sensorPin2.addListener(new GpioPinListenerDigital() {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

                PinState curState = event.getState();

                if (curState != prevState) {
                    prevState = curState;
                }
                sensorBState = curState;

                if (sensorAState.isHigh() && sensorBState.isHigh()) {
                    discCount++;
                }
                System.out.println(sensorAState.getValue() + " " + sensorBState.getValue() + " DISC COUNT: " + discCount);


                if (discCount >= numDiscsToSpin) {
                    System.out.println("STOP");
                    SoftPwm.softPwmStop(motorPwmPin);
                    // carouselMotorController.stop();
                    isRunning = false;
                    //System.exit(1);
                }

            }
        });

        sensorPin.addListener(new GpioPinListenerDigital() {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

                PinState curState = event.getState();

                if (curState != prevState) {
                    prevState = curState;
                }

                sensorAState = curState;

                if (sensorAState.isHigh() && sensorBState.isHigh()) {
                    discCount++;
                }


                System.out.println(sensorAState.getValue() + " " + sensorBState.getValue() + " DISC COUNT: " + discCount);


             //   System.out.println(discCount + " " + curState.getName());

                if (discCount >= numDiscsToSpin) {
                    System.out.println("STOP");
                    SoftPwm.softPwmStop(motorPwmPin);
                   // carouselMotorController.stop();
                    isRunning = false;
                    //System.exit(1);
                }

            }
        });

        double pwmValue = minPwn;
        boolean isMaxSpeed = false;
        boolean isSlowed = false;

        double discToSlowAt = numDiscsToSpin * .80;

        pwmValue = (minPwn + numDiscsToSpin) / 1.3;
        if (pwmValue > maxPwm)
            pwmValue = maxPwm;

        try {
            while (isRunning) {

                if (discCount > discToSlowAt) {
                    pwmValue = 50;
                    isSlowed = true;
                    SoftPwm.softPwmWrite(motorPwmPin, ((int) pwmValue));
                }

                if (!isMaxSpeed && !isSlowed) {

                    for (int i = 0; i <= pwmValue; i++) {
                        SoftPwm.softPwmWrite(motorPwmPin, i);
                        if (discCount > discToSlowAt) {
                            break;
                        }
                        Thread.sleep(25);
                    }
                    isMaxSpeed = true;
                }
            }
            Thread.sleep(1000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SoftPwm.softPwmStop(motorPwmPin);

    }
}
