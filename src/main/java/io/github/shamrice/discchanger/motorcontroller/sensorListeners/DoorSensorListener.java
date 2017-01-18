package io.github.shamrice.discchanger.motorcontroller.sensorListeners;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import io.github.shamrice.discchanger.motorcontroller.DoorMotorController;

/**
 * Created by Erik on 1/17/2017.
 */
public class DoorSensorListener implements GpioPinListenerDigital {

    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
        if (gpioPinDigitalStateChangeEvent.getState().isLow()) {
            DoorMotorController.setIsRunningFalse();
        }
    }
}
