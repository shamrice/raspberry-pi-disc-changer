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
}
