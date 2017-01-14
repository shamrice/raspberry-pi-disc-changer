package io.github.shamrice.discchanger.config;


import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Erik on 1/14/2017.
 */
public class ConfigurationTests {

    final String TEST_MOTOR_NAME1 = "TEST_MOTOR_NAME1";
    final String TEST_MOTOR_NAME2 = "TEST_MOTOR_NAME2";

    private Configuration config;
    private MotorConfiguration testMotorConfig1;
    private MotorConfiguration testMotorConfig2;
    private List<MotorConfiguration> testMotorConfigs;

    @Before
    public void setUp() {
       config = new Configuration();
        testMotorConfig1 = new MotorConfiguration(TEST_MOTOR_NAME1, 1, 2, null);
        testMotorConfig2 = new MotorConfiguration(TEST_MOTOR_NAME2, 3, 4, null);
        testMotorConfigs = new ArrayList<MotorConfiguration>();
        testMotorConfigs.add(testMotorConfig1);
        testMotorConfigs.add(testMotorConfig2);
    }

    @Test
    public void getMotorConfigurationsTest() {

        config.addMotorConfiguration(testMotorConfig1);
        assertTrue(config.getMotorConfigurations().get(0) == testMotorConfig1);
    }

    @Test
    public void setMotorConfigurationTest() {

        config.setMotorConfiguration(testMotorConfigs);
        assertTrue(config.getMotorConfigurations() == testMotorConfigs);
    }

    @Test
    public void addMotorConfigurationTest() {

        config.addMotorConfiguration(testMotorConfig1);
        assertTrue(config.getMotorConfigurations().size() == 1);

        config.addMotorConfiguration(testMotorConfig2);;

        assertTrue(config.getMotorConfigurations().size() == 2);
        assertTrue(config.getMotorConfigurations().get(1).getMotorName() == TEST_MOTOR_NAME2);
    }

    @Test
    public void getMotorConfigurationByNameTest() {
        config.addMotorConfiguration(testMotorConfig1);
        config.addMotorConfiguration(testMotorConfig2);

        assertTrue(config.getMotorConfigurationByName(TEST_MOTOR_NAME1) == testMotorConfig1);
    }
}
