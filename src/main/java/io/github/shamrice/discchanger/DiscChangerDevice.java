package io.github.shamrice.discchanger;

import io.github.shamrice.discchanger.config.Configuration;
import io.github.shamrice.discchanger.config.definitions.Definitions;
import io.github.shamrice.discchanger.motorcontroller.CarouselMotorController;

/**
 * Created by Erik on 1/14/2017.
 */
public class DiscChangerDevice {

    private Configuration configuration;
    private CarouselMotorController carouselMotorController;
    private static DiscChangerDevice instance = null;

    private DiscChangerDevice() {}

    public static DiscChangerDevice getInstance() {
        if (instance == null) {
            instance = new DiscChangerDevice();
        }

        return instance;
    }

    public void setConfiguration (Configuration configuration) {
        this.configuration = configuration;

        //reconfigure with new configuration.
        instance.configure();
    }

    private void configure() {
        if (null != configuration) {
            carouselMotorController = new CarouselMotorController(configuration.getMotorConfigurationByName(Definitions.CAROUSEL_MOTOR_CONTROLLER));
        }
    }

    public void rotateCarousel(int numDiscs) {
        carouselMotorController.spinNumDiscs(numDiscs);
    }


}
