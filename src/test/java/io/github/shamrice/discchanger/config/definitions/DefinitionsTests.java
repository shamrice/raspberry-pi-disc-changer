package io.github.shamrice.discchanger.config.definitions;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
/**
 * Created by Erik on 1/14/2017.
 */
public class DefinitionsTests {

    @Test
    public void definitionsStringsTest() {

        assertTrue(Definitions.CAROUSEL_MOTOR_CONTROLLER == "CAROUSEL_MOTOR_CONTROLLER");
        assertTrue(Definitions.DOOR_MOTOR_CONTROLLER == "DOOR_MOTOR_CONTROLLER");
        assertTrue(Definitions.DISC_DRIVE_MOTOR_CONTROLLER == "DISC_DRIVE_MOTOR_CONTROLLER");

        assertTrue(Definitions.CAROUSEL_SENSOR1_PIN ==  "CAROUSEL_SENSOR1_PIN");
        assertTrue(Definitions.CAROUSEL_SENSOR2_PIN ==  "CAROUSEL_SENSOR2_PIN");
        assertTrue(Definitions.CAROUSEL_MOTOR_CONTROLLER_PIN1 == "CAROUSEL_MOTOR_CONTROLLER_PIN1");
        assertTrue(Definitions.CAROUSEL_MOTOR_CONTROLLER_PIN2 == "CAROUSEL_MOTOR_CONTROLLER_PIN2");

    }
}
