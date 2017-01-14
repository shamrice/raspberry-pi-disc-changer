package io.github.shamrice.discchanger.motorcontroller;

import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;
import mockit.Mocked;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Erik on 1/14/2017.
 */
public class MotorControllerTests {

    private class TestMotorController extends MotorController {

        public TestMotorController(MotorConfiguration config){
            super(config);
        }

        @Override
        public void start() {}

        @Override
        public void stop(){}
    }

    @Mocked
    private MotorConfiguration anyMotorConfiguration;

    @Test
    public void setGetDirectionTest() {
        MotorController controller = new TestMotorController(anyMotorConfiguration);

        controller.setDirection(Direction.FORWARD);
        assertTrue(controller.getDirection() == Direction.FORWARD);
    }

    @Test
    public void isRunningDefaultsToFalseOnConstructionTest() {
        MotorController controller = new TestMotorController(anyMotorConfiguration);

        assertFalse(controller.isRunning());
    }
}
