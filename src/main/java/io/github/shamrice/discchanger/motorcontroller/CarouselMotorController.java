package io.github.shamrice.discchanger.motorcontroller;

/**
 * Created by Erik on 1/11/2017.
 */

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.SoftPwm;
import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete class for disc carousel motor controls.
 */
public class CarouselMotorController extends MotorController {

    final int minSpinPwm = 50;
    final int maxSpinPwm = 190;

    private int discCount = 0;

    private List<PinState> prevPinStates = new ArrayList<PinState>();

    public CarouselMotorController(MotorConfiguration motorConfiguration)  {
        super(motorConfiguration);

        SoftPwm.softPwmCreate(super.motorPinA, 0, 255);
        SoftPwm.softPwmCreate(super.motorPinB, 0, 255);

        //set initial pin state for all sensors.
        for(GpioPinDigitalInput sensorInput : super.sensorInputs) {
            prevPinStates.add(sensorInput.getState());
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public void spinNumDiscs(int numDiscsToSpin) {
        double pwmValue = minSpinPwm;
        boolean isMaxSpeed = false;
        boolean isSlowed = false;

        double discToSlowAt = numDiscsToSpin * .80;

        pwmValue = (minSpinPwm + numDiscsToSpin) / 1.3;
        if (pwmValue > maxSpinPwm)
            pwmValue = maxSpinPwm;

        try {
            while (isRunning) {

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
            Thread.sleep(1000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
