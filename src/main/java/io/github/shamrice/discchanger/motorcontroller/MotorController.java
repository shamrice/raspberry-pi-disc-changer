package io.github.shamrice.discchanger.motorcontroller;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;

import java.util.List;

/**
 * Created by Erik on 1/11/2017.
 */

/**
 * Abstract class for motor controls that implements the shared functionality
 * for all motors in device.
 */
public abstract class MotorController {

    protected static boolean isRunning = false;
    protected Direction direction = Direction.FORWARD;
    protected int motorPinA;
    protected int motorPinB;
    protected List<GpioPinDigitalInput> sensorInputs;

    public MotorController(MotorConfiguration motorConfiguration) {
        this.motorPinA = motorConfiguration.getMotorPinA();
        this.motorPinB = motorConfiguration.getMotorPinB();
        this.sensorInputs = motorConfiguration.getSensorInputs();
    }

    public abstract void start();
    public abstract void stop();

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
