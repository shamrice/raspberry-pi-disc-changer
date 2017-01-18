package io.github.shamrice.discchanger.motorcontroller.sensorListeners;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import io.github.shamrice.discchanger.motorcontroller.DoorMotorController;

import java.util.List;

/**
 * Created by Erik on 1/17/2017.
 */
public class DoorSensorListener implements GpioPinListenerDigital {

    private String sensorName;

    public DoorSensorListener(String sensorName){
        this.sensorName = sensorName;
    }

    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {



        //DoorMotorController.setIsRunningFalse();

        DoorMotorController.setPinStateByName(sensorName, gpioPinDigitalStateChangeEvent.getState());
    }
}
