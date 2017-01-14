package io.github.shamrice.discchanger.config.motorconfiguration;

import com.pi4j.io.gpio.GpioPinDigitalInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 1/13/2017.
 */
public class MotorConfiguration {

    private int motorPinA;
    private int motorPinB;
    private List<GpioPinDigitalInput> sensorInputs = new ArrayList<GpioPinDigitalInput>();
    private String motorName;

    public MotorConfiguration() {}

    public MotorConfiguration(String motorName, int motorPinA, int motorPinB, List<GpioPinDigitalInput> sensorInputs) {
        this.motorName = motorName;
        this.motorPinA = motorPinA;
        this.motorPinB = motorPinB;
        this.sensorInputs = sensorInputs;
    }

    public void setMotorPinA(int motorPinA) {
        this.motorPinA = motorPinA;
    }

    public void setMotorPinB(int motorPinB) {
        this.motorPinB = motorPinB;
    }

    public void setSensorInputs(List<GpioPinDigitalInput> sensorInputs) {
        this.sensorInputs = sensorInputs;
    }

    public void setMotorName(String motorName) {
        this.motorName = motorName;
    }

    public int getMotorPinA() {
        return motorPinA;
    }

    public int getMotorPinB() {
        return motorPinB;
    }

    public List<GpioPinDigitalInput> getSensorInputs() {
        return sensorInputs;
    }

    public String getMotorName() {
        return this.motorName;
    }
}
