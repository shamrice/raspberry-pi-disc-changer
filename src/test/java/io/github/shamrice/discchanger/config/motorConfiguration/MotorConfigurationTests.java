package io.github.shamrice.discchanger.config.motorConfiguration;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import io.github.shamrice.discchanger.config.Configuration;
import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;
import mockit.Expectations;
import mockit.Mock;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Erik on 1/14/2017.
 */
public class MotorConfigurationTests {

    private final String TEST_MOTOR_NAME = "TEST_MOTOR";
    private final String MOCKED_INPUT_NAME = "MOCK_INPUT";

    @Mocked
    private GpioPinDigitalInput anyDigitalInput;

    @Test
    public void setGetMotorNameTest() {

        MotorConfiguration motorConfiguration = new MotorConfiguration();
        motorConfiguration.setMotorName(TEST_MOTOR_NAME);

        assertTrue(motorConfiguration.getMotorName().equals(TEST_MOTOR_NAME));
    }

    @Test
    public void setGetMotorPinATest() {

        int testPinNum = 1;
        MotorConfiguration config = new MotorConfiguration();
        config.setMotorPinA(testPinNum);

        assertTrue(config.getMotorPinA() == testPinNum);
    }

    @Test
    public void setGetMotorPinBTest() {
        int testPinNum = 1;
        MotorConfiguration config = new MotorConfiguration();
        config.setMotorPinB(testPinNum);

        assertTrue(config.getMotorPinB() == testPinNum);
    }

    @Test
    public void getSetSensorInputsTest() {
        List<GpioPinDigitalInput> testSensorInputs = new ArrayList<GpioPinDigitalInput>();

        new Expectations() {{
            anyDigitalInput.getName();
            returns(MOCKED_INPUT_NAME);

            anyDigitalInput.getState();
            returns(PinState.LOW);
        }};

        testSensorInputs.add(anyDigitalInput);

        MotorConfiguration config = new MotorConfiguration();
        config.setSensorInputs(testSensorInputs);

        assertTrue(config.getSensorInputs().size() == 1);
        assertTrue(config.getSensorInputs().get(0).getName().equals(MOCKED_INPUT_NAME));
        assertTrue(config.getSensorInputs().get(0).getState() == PinState.LOW);
    }

    @Test
    public void parameterConstructorTest() {

        int testMotorPinA = 1;
        int testMotorPinB = 2;

        List<GpioPinDigitalInput> testSensorInputs = new ArrayList<GpioPinDigitalInput>();

        new Expectations() {{
            anyDigitalInput.getName();
            returns(MOCKED_INPUT_NAME);
        }};

        testSensorInputs.add(anyDigitalInput);

        MotorConfiguration config = new MotorConfiguration(TEST_MOTOR_NAME, testMotorPinA, testMotorPinB, testSensorInputs);

        assertTrue(config.getMotorName().equals(TEST_MOTOR_NAME));
        assertTrue(config.getMotorPinA() == testMotorPinA);
        assertTrue(config.getMotorPinB() == testMotorPinB);
        assertTrue(config.getSensorInputs().get(0).getName().equals(MOCKED_INPUT_NAME));

    }
}
