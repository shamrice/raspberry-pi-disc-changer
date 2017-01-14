package io.github.shamrice.discchanger;

import io.github.shamrice.discchanger.config.Configuration;
import io.github.shamrice.discchanger.config.ConfigurationFactory;
import io.github.shamrice.discchanger.config.definitions.Definitions;
import io.github.shamrice.discchanger.motorcontroller.*;

/**
 * Created by Erik on 1/11/2017.
 */
public class DiscChangerStarter {

    /**
     * Entry point.
     * @param args - command line arguments.
     */
    public static void main(String[] args) {

        int numDiscsToSpin = 0;

        if (args.length == 1) {
            numDiscsToSpin = Integer.parseInt(args[0]);
            System.out.println("Spinning " + numDiscsToSpin + " discs.");
        }

        Configuration config = ConfigurationFactory.build();

        DiscChangerDevice discChangerDevice = DiscChangerDevice.getInstance();
        discChangerDevice.setConfiguration(config);

        discChangerDevice.getCarouselMotorController().spinNumDiscs(numDiscsToSpin);

        System.exit(0);
    }
}
