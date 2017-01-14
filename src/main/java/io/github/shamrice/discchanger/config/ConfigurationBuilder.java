package io.github.shamrice.discchanger.config;

import com.pi4j.io.gpio.*;
import io.github.shamrice.discchanger.config.definitions.Definitions;
import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Erik on 1/13/2017.
 */
public class ConfigurationBuilder {

    private static final GpioController gpioController = GpioFactory.getInstance();
    private static Properties configProperties = new Properties();

    public static Configuration build() throws IOException {

        /* load config properties from config file */
        InputStream configInput = new FileInputStream(Definitions.CONFIG_FILE_NAME);
        configProperties.load(configInput);
        configInput.close();

        Configuration configuration = new Configuration();

        /* Add motor configurations to config */
        configuration.addMotorConfiguration(buildCarouselMotorConfiguration());

        return configuration;
    }

    private static MotorConfiguration buildCarouselMotorConfiguration() {

        MotorConfiguration carouselMotorConfiguration = null;

        /* set up carousel sensor pin information */
        Pin carouselSensorPin1 = RaspiPin.getPinByName("GPIO " + configProperties.getProperty(Definitions.CAROUSEL_SENSOR1_PIN));
        Pin carouselSensorPin2 = RaspiPin.getPinByName("GPIO " + configProperties.getProperty(Definitions.CAROUSEL_SENSOR2_PIN));

        GpioPinDigitalInput carouselMotorSensorPinA = gpioController.provisionDigitalInputPin(carouselSensorPin1, Definitions.CAROUSEL_SENSOR1_PIN, PinPullResistance.PULL_DOWN);
        GpioPinDigitalInput carouselMotorSensorPinB = gpioController.provisionDigitalInputPin(carouselSensorPin2, Definitions.CAROUSEL_SENSOR2_PIN, PinPullResistance.PULL_DOWN);

        List<GpioPinDigitalInput> carouselSensors = new ArrayList<GpioPinDigitalInput>();
        carouselSensors.add(carouselMotorSensorPinA);
        carouselSensors.add(carouselMotorSensorPinB);

        /* set up carousel motor pin information */
        int carouselMotorPin1 = Integer.parseInt(configProperties.getProperty(Definitions.CAROUSEL_MOTOR_CONTROLLER_PIN1));
        int carouselMotorPin2 = Integer.parseInt(configProperties.getProperty(Definitions.CAROUSEL_MOTOR_CONTROLLER_PIN2));

        /* create configuration */
        carouselMotorConfiguration = new MotorConfiguration(Definitions.CAROUSEL_MOTOR_CONTROLLER, carouselMotorPin1, carouselMotorPin2, carouselSensors);

        return carouselMotorConfiguration;
    }


}
