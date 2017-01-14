package io.github.shamrice.discchanger.config;

import com.pi4j.io.gpio.*;
import io.github.shamrice.discchanger.config.definitions.Definitions;
import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 1/13/2017.
 */
public class ConfigurationFactory {

    public static Configuration build() {

        Configuration configuration = new Configuration();
        final GpioController gpioController = GpioFactory.getInstance();

        /* Carousel Motor Configuration */
        GpioPinDigitalInput carouselMotorSensorPinA = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_21, Definitions.CAROUSEL_SENSOR_PIN1, PinPullResistance.PULL_DOWN);
        GpioPinDigitalInput carouselMotorSensorPinB = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_22, Definitions.CAROUSEL_SENSOR_PIN2, PinPullResistance.PULL_DOWN);

        List<GpioPinDigitalInput> carouselSensors = new ArrayList<GpioPinDigitalInput>();
        carouselSensors.add(carouselMotorSensorPinA);
        carouselSensors.add(carouselMotorSensorPinB);

        MotorConfiguration carouselMotorConfiguration = new MotorConfiguration(Definitions.CAROUSEL_MOTOR_CONTROLLER, 8, 9, carouselSensors);

        /* Add motor configurations to config */
        configuration.addMotorConfiguration(carouselMotorConfiguration);

        return configuration;

    }
}
