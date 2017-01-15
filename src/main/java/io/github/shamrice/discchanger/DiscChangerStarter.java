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
        boolean displayInfo = false;
        boolean stop = false;

        if (args.length == 1) {

            if (args[0].equals("--info") || args[0].equals("-i"))
                displayInfo = true;
            else if (args[0].equals("--stop") || args[0].equals("-s"))
                stop = true;
            else {
                try {
                    numDiscsToSpin = Integer.parseInt(args[0]);
                    System.out.println("Spinning " + numDiscsToSpin + " discs.");
                } catch (NumberFormatException numFmtEx) {
                    System.out.println("Not a number.");
                    numDiscsToSpin = -1;
                    printUsage();
                }

            }


            try {
                Configuration config = null;
                config = ConfigurationBuilder.build();

                if (null != config) {

                    DiscChangerDevice discChangerDevice = DiscChangerDevice.getInstance();
                    discChangerDevice.setConfiguration(config);

                    if (displayInfo)
                        discChangerDevice.printDeviceInfo();
                    else if (stop)
                        discChangerDevice.stopCarousel();

                    else if (numDiscsToSpin > 0) {

                    /* Rotate in one direction and then back to start position */
                        discChangerDevice.rotateCarousel(numDiscsToSpin, Direction.BACKWARD);

                        System.out.println("Done... sleeping 2 second");
                        Thread.sleep(2000);

                        discChangerDevice.rotateCarousel(numDiscsToSpin, Direction.FORWARD);
                    }

                    discChangerDevice.shutdown();

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            printUsage();
        }

        System.exit(0);
    }

    public static void printUsage() {
        System.out.println("Usages:");
        System.out.println("\t--info -i\tDisplay device information.");
        System.out.println("\t--stop -s\tStop carousel motor.");
        System.out.println("\t--help -h\tDisplay help.");
        System.out.println("\t{NUMBER} \tSpin number of numbers specified.");
        System.out.println();
    }
}
