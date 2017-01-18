package io.github.shamrice.discchanger.motorcontroller;

import com.pi4j.io.gpio.*;
import io.github.shamrice.discchanger.config.definitions.Definitions;
import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;
import io.github.shamrice.discchanger.motorcontroller.sensorListeners.DoorSensorListener;

/**
 * Created by Erik on 1/17/2017.
 */
public class DoorMotorController extends MotorController {

    private enum DoorState { OPEN, CLOSED }

    private GpioPinDigitalOutput motorOutputPinA;
    private GpioPinDigitalOutput motorOutputPinB;
    private DoorState currentDoorState = DoorState.OPEN;

    public DoorMotorController(MotorConfiguration motorConfiguration){
        super(motorConfiguration);

        GpioController gpioController = GpioFactory.getInstance();

        Pin motorPinA = RaspiPin.getPinByName("GPIO " + super.motorPinA);
        Pin motorPinB = RaspiPin.getPinByName("GPIO " + super.motorPinB);

        motorOutputPinA = gpioController.provisionDigitalOutputPin(motorPinA, Definitions.DOOR_MOTOR_CONTROLLER_PIN1, PinState.LOW);
        motorOutputPinB = gpioController.provisionDigitalOutputPin(motorPinB, Definitions.DOOR_MOTOR_CONTROLLER_PIN2, PinState.LOW);

        /** Causes motor to hiccup **/
     //   motorOutputPinA.setShutdownOptions(true, PinState.LOW, PinPullResistance.PULL_DOWN);
     //   motorOutputPinB.setShutdownOptions(true, PinState.LOW, PinPullResistance.PULL_DOWN);

        super.sensorInputs.get(0).addListener(new DoorSensorListener());

        //close door on construction
        start();
    }

    @Override
    public void start() {

        isRunning = true;

        switch (super.direction) {
            case FORWARD:
                motorOutputPinA.setState(true);
                motorOutputPinB.setState(false);
                break;
            case BACKWARD:
                motorOutputPinB.setState(true);
                motorOutputPinA.setState(false);
                break;

        }

        long start = System.currentTimeMillis();

        while (isRunning) {

            long current = System.currentTimeMillis();
            if ((current - start) > 2000){
                isRunning = false;
            }

        }

        stop();
    }

    @Override
    public void stop() {
        motorOutputPinA.setState(false);
        motorOutputPinB.setState(false);
    }

    public static synchronized void setIsRunningFalse() {
        isRunning = false;
    }
}
