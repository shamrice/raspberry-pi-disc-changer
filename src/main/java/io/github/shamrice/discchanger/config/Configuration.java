package io.github.shamrice.discchanger.config;

import io.github.shamrice.discchanger.config.motorconfiguration.MotorConfiguration;
import io.github.shamrice.discchanger.displayController.DisplayController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 1/13/2017.
 */
public class Configuration {

    private List<MotorConfiguration> motorConfigurations = new ArrayList<MotorConfiguration>();
    private DisplayController displayController;

    public void setMotorConfiguration(List<MotorConfiguration> motorConfigurations) {
        this.motorConfigurations = motorConfigurations;
    }

    public void addMotorConfiguration(MotorConfiguration motorConfiguration) {
        this.motorConfigurations.add(motorConfiguration);
    }

    public List<MotorConfiguration> getMotorConfigurations() {
        return motorConfigurations;
    }

    public MotorConfiguration getMotorConfigurationByName(String motorConfigurationName) {

        MotorConfiguration motorConfigurationResult = null;

        for(MotorConfiguration motorConfiguration : this.motorConfigurations) {
            if (motorConfiguration.getMotorName().equals(motorConfigurationName)){
                motorConfigurationResult = motorConfiguration;
            }
        }

        return motorConfigurationResult;
    }

    public void setDisplayController(DisplayController displayController) {
        this.displayController = displayController;
    }

    public DisplayController getDisplayController() {
        return this.displayController;
    }

}
