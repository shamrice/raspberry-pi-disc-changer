package io.github.shamrice.discchanger;

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

    /**
     * Entry point.
     * @param args - command line arguments.
     */
    public static void main(String[] args) {
        final GpioController gpioController = GpioFactory.getInstance();

        final GpioPinDigitalOutput motorOutputA = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_08, "Output1", PinState.LOW);
        final GpioPinDigitalOutput motorOutputB = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_09, "Output1", PinState.LOW);
        final GpioPinDigitalOutput relayPin1 = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "RelayPin1", PinState.LOW);

        final MotorController carouselMotorController = new CarouselMotorController(motorOutputA, motorOutputB);

        GpioPinDigitalInput sensorPin = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_22, "SensorPin1", PinPullResistance.PULL_UP);

        sensorPin.addListener(new GpioPinListenerDigital() {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
                System.out.println("DiscCount: " + discCount);

                if (discCount >= 10) {
                    System.out.println("STOP");
                    carouselMotorController.stop();
                    isRunning = false;
                    System.exit(1);
                }


                if (gpioPinDigitalStateChangeEvent.getState().isHigh()) {
                    discCount++;
                }
            }
        });

        carouselMotorController.start();

        try {
            while (isRunning) {
                Thread.sleep(5000);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
