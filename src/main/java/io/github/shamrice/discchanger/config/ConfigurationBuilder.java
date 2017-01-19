package io.github.shamrice.discchanger.config;

import com.pi4j.io.gpio.*;
import io.github.shamrice.discchanger.config.definitions.Definitions;
import io.github.shamrice.discchanger.config.displayConfiguration.DisplayConfiguration;
import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;
import io.github.shamrice.discchanger.displayController.DisplayController;
import io.github.shamrice.discchanger.displayController.SSD1306_I2CDisplayController;

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
        configuration.addMotorConfiguration(buildDoorMotorConfiguration());
        configuration.setDisplayConfiguration(buildDisplayConfiguration());

        return configuration;
    }

    private static MotorConfiguration buildCarouselMotorConfiguration() {

        MotorConfiguration carouselMotorConfiguration = null;

        /* set up carousel sensor pin information */
        Pin carouselSensorPin1 = RaspiPin.getPinByName("GPIO " + configProperties.getProperty(Definitions.CAROUSEL_SENSOR1_PIN));
        Pin carouselSensorPin2 = RaspiPin.getPinByName("GPIO " + configProperties.getProperty(Definitions.CAROUSEL_SENSOR2_PIN));
        Pin carouselSensorPin3 = RaspiPin.getPinByName("GPIO " + configProperties.getProperty(Definitions.CAROUSEL_SENSOR3_PIN));

        GpioPinDigitalInput carouselMotorSensorPinA = gpioController.provisionDigitalInputPin(carouselSensorPin1, Definitions.CAROUSEL_SENSOR1_PIN, PinPullResistance.PULL_DOWN);
        carouselMotorSensorPinA.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        GpioPinDigitalInput carouselMotorSensorPinB = gpioController.provisionDigitalInputPin(carouselSensorPin2, Definitions.CAROUSEL_SENSOR2_PIN, PinPullResistance.PULL_DOWN);
        carouselMotorSensorPinB.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        GpioPinDigitalInput carouselMotorSensorPinC = gpioController.provisionDigitalInputPin(carouselSensorPin3, Definitions.CAROUSEL_SENSOR3_PIN, PinPullResistance.PULL_DOWN);
        carouselMotorSensorPinC.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);


        List<GpioPinDigitalInput> carouselSensors = new ArrayList<GpioPinDigitalInput>();
        carouselSensors.add(carouselMotorSensorPinA);
        carouselSensors.add(carouselMotorSensorPinB);
        carouselSensors.add(carouselMotorSensorPinC);

        /* set up carousel motor pin information */
        int carouselMotorPin1 = Integer.parseInt(configProperties.getProperty(Definitions.CAROUSEL_MOTOR_CONTROLLER_PIN1));
        int carouselMotorPin2 = Integer.parseInt(configProperties.getProperty(Definitions.CAROUSEL_MOTOR_CONTROLLER_PIN2));

        /* create configuration */
        carouselMotorConfiguration = new MotorConfiguration(Definitions.CAROUSEL_MOTOR_CONTROLLER, carouselMotorPin1, carouselMotorPin2, carouselSensors);

        return carouselMotorConfiguration;
    }

    public static MotorConfiguration buildDoorMotorConfiguration() {
        MotorConfiguration doorMotorConfiguration = null;

        /* door motor sensor */
        Pin doorSensorPin1 = RaspiPin.getPinByName("GPIO " + configProperties.getProperty(Definitions.DOOR_SENSOR_PIN1));
        Pin doorSensorPin2 = RaspiPin.getPinByName("GPIO " + configProperties.getProperty(Definitions.DOOR_SENSOR_PIN2));

        System.out.println("Pin1: " + doorSensorPin1.getAddress() + " Pin2: " + doorSensorPin2.getAddress());

        GpioPinDigitalInput doorMotorSensorPinA = gpioController.provisionDigitalInputPin(doorSensorPin1, Definitions.DOOR_SENSOR_PIN1, PinPullResistance.PULL_DOWN);
        doorMotorSensorPinA.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        GpioPinDigitalInput doorMotorSensorPinB = gpioController.provisionDigitalInputPin(doorSensorPin2, Definitions.DOOR_SENSOR_PIN2, PinPullResistance.PULL_DOWN);
        doorMotorSensorPinB.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        List<GpioPinDigitalInput> doorSensors = new ArrayList<GpioPinDigitalInput>();
        doorSensors.add(doorMotorSensorPinA);
        doorSensors.add(doorMotorSensorPinB);

        /* set up door motor pin information */
        int doorMotorPin1 = Integer.parseInt(configProperties.getProperty(Definitions.DOOR_MOTOR_CONTROLLER_PIN1));
        int doorMotorPin2 = Integer.parseInt(configProperties.getProperty(Definitions.DOOR_MOTOR_CONTROLLER_PIN2));

        /* create configuration */
        doorMotorConfiguration = new MotorConfiguration(Definitions.DOOR_MOTOR_CONTROLLER, doorMotorPin1, doorMotorPin2, doorSensors);

        return doorMotorConfiguration;
    }

    public static DisplayConfiguration buildDisplayConfiguration()  {
        DisplayConfiguration displayConfiguration = null;

        try {

            int i2cAddress = Integer.parseInt(configProperties.getProperty(Definitions.DISPLAY_I2C_ADDRESS));
            int i2cBus = Integer.parseInt(configProperties.getProperty(Definitions.DISPLAY_I2C_BUS));
            String bootImage = configProperties.getProperty(Definitions.DISPLAY_BOOT_IMAGE_LOCATION);
            String idleImage = configProperties.getProperty(Definitions.DISPLAY_IDLE_IMAGE_LOCATION);

            displayConfiguration = new DisplayConfiguration(i2cAddress, i2cBus, bootImage, idleImage);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return displayConfiguration;
    }

}
