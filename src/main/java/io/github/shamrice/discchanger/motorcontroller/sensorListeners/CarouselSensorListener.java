package io.github.shamrice.discchanger.motorcontroller.sensorListeners;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.SoftPwm;
import io.github.shamrice.discchanger.motorcontroller.CarouselMotorController;


/**
 * Created by Erik on 1/14/2017.
 */
public class CarouselSensorListener implements GpioPinListenerDigital {

    private String sensorName;

    public CarouselSensorListener(String sensorName) {
        this.sensorName = sensorName;
    }

    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

        CarouselMotorController.setPinStateByName(this.sensorName, event.getState());

    }
}
