package io.github.shamrice.discchanger;

import io.github.shamrice.discchanger.config.Configuration;
import io.github.shamrice.discchanger.config.ConfigurationBuilder;
import io.github.shamrice.discchanger.motorcontroller.Direction;

import java.io.IOException;

/**
 * Created by Erik on 1/11/2017.
 */
public class DiscChangerStarter {

    /**
     * Entry point.
     * @param args - command line arguments.
     */
    public static void main(String[] args) throws InterruptedException {

        int numDiscsToSpin = 0;

        if (args.length == 1) {
            numDiscsToSpin = Integer.parseInt(args[0]);
            System.out.println("Spinning " + numDiscsToSpin + " discs.");
        }

        Configuration config = null;

        try {
          config = ConfigurationBuilder.build();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (null != config) {

            DiscChangerDevice discChangerDevice = DiscChangerDevice.getInstance();
            discChangerDevice.setConfiguration(config);

            /* Rotate in one direction and then back to start position */
            discChangerDevice.rotateCarousel(numDiscsToSpin, Direction.BACKWARD);

            System.out.println("Done... sleeping 2 second");
            Thread.sleep(2000);

            discChangerDevice.rotateCarousel(numDiscsToSpin, Direction.FORWARD);

            discChangerDevice.shutdown();

        }

        System.exit(0);
    }
}
