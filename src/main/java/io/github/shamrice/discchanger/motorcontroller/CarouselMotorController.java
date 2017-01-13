package io.github.shamrice.discchanger.motorcontroller;

/**
 * Created by Erik on 1/11/2017.
 */

import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 * Concrete class for disc carousel motor controls.
 */
public class CarouselMotorController extends MotorController {

    public CarouselMotorController(GpioPinDigitalOutput digitalOutputA, GpioPinDigitalOutput digitalOutputB)  {
        super(digitalOutputA, digitalOutputB);
    }

    public void step() {
        isRunning = true;
        try {
            if (direction == Direction.FORWARD) {
                digitalOutputA.setState(true);
                digitalOutputB.setState(false);
                Thread.sleep(50);
                digitalOutputA.setState(false);
                digitalOutputB.setState(false);
            } else {
                digitalOutputA.setState(false);
                digitalOutputB.setState(true);
                Thread.sleep(50);
                digitalOutputA.setState(false);
                digitalOutputB.setState(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
