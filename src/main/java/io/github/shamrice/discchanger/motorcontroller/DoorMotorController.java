package io.github.shamrice.discchanger.motorcontroller;

import com.pi4j.io.gpio.*;
import io.github.shamrice.discchanger.config.definitions.Definitions;
import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;
import io.github.shamrice.discchanger.motorcontroller.sensorListeners.CarouselSensorListener;
import io.github.shamrice.discchanger.motorcontroller.sensorListeners.DoorSensorListener;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Erik on 1/17/2017.
 */
public class DoorMotorController extends MotorController {

    private static Direction staticDirection = Direction.FORWARD;
    private GpioPinDigitalOutput motorOutputPinA;
    private GpioPinDigitalOutput motorOutputPinB;
    private static ConcurrentHashMap<String, PinState> pinStates = new ConcurrentHashMap<String, PinState>();

    public DoorMotorController(MotorConfiguration motorConfiguration){
        super(motorConfiguration);

        GpioController gpioController = GpioFactory.getInstance();

        Pin motorPinA = RaspiPin.getPinByName("GPIO " + super.motorPinA);
        Pin motorPinB = RaspiPin.getPinByName("GPIO " + super.motorPinB);

        motorOutputPinA = gpioController.provisionDigitalOutputPin(motorPinA, Definitions.DOOR_MOTOR_CONTROLLER_PIN1, PinState.LOW);
        motorOutputPinB = gpioController.provisionDigitalOutputPin(motorPinB, Definitions.DOOR_MOTOR_CONTROLLER_PIN2, PinState.LOW);

        //set initial pin state for all sensors.
        for(GpioPinDigitalInput sensorInput : super.sensorInputs) {
            pinStates.put(sensorInput.getName(), sensorInput.getState());
            sensorInput.addListener(new DoorSensorListener(sensorInput.getName()));
        }

        for(PinState states : pinStates.values()) {
            System.out.println("DOOR: " + states.getName() + " : " + states.getValue());
        }

    }

    @Override
    public void start() {

        switch (staticDirection) {
            case FORWARD:
                if (pinStates.get(Definitions.DOOR_SENSOR_PIN2).isLow()) {
                    motorOutputPinA.setState(true);
                    motorOutputPinB.setState(false);
                    isRunning = true;
                }
                break;
            case BACKWARD:
                if (pinStates.get(Definitions.DOOR_SENSOR_PIN1).isLow()) {
                    motorOutputPinB.setState(true);
                    motorOutputPinA.setState(false);
                    isRunning = true;
                }
                break;
        }

        long start = System.currentTimeMillis();

        while (isRunning) {

            long current = System.currentTimeMillis();
            if ((current - start) > 2000){
                System.out.println("Timeout. Stopping.");
                isRunning = false;
            }
        }

        stop();
    }

    @Override
    public void stop() {
        System.out.println("Stop method on door controller.");
        motorOutputPinA.setState(false);
        motorOutputPinB.setState(false);
    }

    public void init() {
        staticDirection = Direction.FORWARD;
        start();
    }

    public void setStaticDirection(Direction direction) {
        staticDirection = direction;
    }

    public static synchronized void setPinStateByName(String sensorName, PinState newState) {
        pinStates.replace(sensorName, newState);

        if (pinStates.get(Definitions.DOOR_SENSOR_PIN1).isLow() && staticDirection == Direction.FORWARD){
            System.out.println("SENSOR IS LOW AND DIRECTION FORWARD -- STOPPING.");
            isRunning = false;
        } else if (pinStates.get(Definitions.DOOR_SENSOR_PIN2).isLow() && staticDirection == Direction.BACKWARD) {
            System.out.println("SENSOR IS LOW AND DIRECTION BACKWARD -- STOPPING.");
            isRunning = false;
        }
    }

    public static synchronized void setIsRunningFalse() {
        isRunning = false;
    }
}
