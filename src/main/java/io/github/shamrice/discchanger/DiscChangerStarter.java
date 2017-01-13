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


    /**
     * Entry point.
     * @param args - command line arguments.
     */
    public static void main(String[] args) {

        //Gpio.wiringPiSetup();

        final GpioController gpioController = GpioFactory.getInstance();

        SoftPwm.softPwmCreate(8, 0, 255);

        //final GpioPinDigitalOutput motorOutputA = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_08, "Output1", PinState.LOW);
        final GpioPinDigitalOutput motorOutputB = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_09, "Output1", PinState.LOW);
        final GpioPinDigitalOutput relayPin1 = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "RelayPin1", PinState.LOW);

        //final CarouselMotorController carouselMotorController = new CarouselMotorController(motorOutputA, motorOutputB);

        GpioPinDigitalInput sensorPin = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_22, "SensorPin1", PinPullResistance.PULL_DOWN);

        prevState = sensorPin.getState();
/*
        sensorPin.addListener(new GpioPinListenerDigital() {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {




                if (discCount >= 10) {
                    System.out.println("STOP");
                    carouselMotorController.stop();
                    isRunning = false;
                    System.exit(1);
                }


              //  if (event.getState().isHigh()) {
                    discCount++;
              //  }

            }
        });
*/
       // carouselMotorController.start();

        int discCount = 0;

        try {
            //while (isRunning) {
                for (int i = 0; i <= 255; i++){
                    SoftPwm.softPwmWrite(8, i);
                    Thread.sleep(25);
                }

                for (int i = 255; i >= 0; i--) {
                    SoftPwm.softPwmWrite(8, i);
                    Thread.sleep(25);
                }
                //isRunning = false;

           // }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SoftPwm.softPwmStop(8);

        //carouselMotorController.stop();
    }
}
