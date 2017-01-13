package io.github.shamrice.discchanger.motorcontroller;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 * Created by Erik on 1/11/2017.
 */

/**
 * Abstract class for motor controls that implements the shared functionality
 * for all motors in device.
 */
public abstract class MotorController {

    protected boolean isRunning = false;
    protected Direction direction = Direction.FORWARD;
    protected GpioPinDigitalOutput digitalOutputA;
    protected GpioPinDigitalOutput digitalOutputB;

    public MotorController(GpioPinDigitalOutput digitalOutputA, GpioPinDigitalOutput digitalOutputB) {
        this.digitalOutputA = digitalOutputA;
        this.digitalOutputB = digitalOutputB;
    }

    public void start() {

        isRunning = true;

        if (direction == Direction.FORWARD) {
            digitalOutputA.setState(true);
            digitalOutputB.setState(false);
        } else {
            digitalOutputA.setState(false);
            digitalOutputB.setState(true);
        }
    }

    public void stop() {

        isRunning = false;

        digitalOutputA.setState(false);
        digitalOutputB.setState(false);
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
