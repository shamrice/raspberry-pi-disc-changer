package io.github.shamrice.discchanger;

import io.github.shamrice.discchanger.config.Configuration;
import io.github.shamrice.discchanger.config.ConfigurationBuilder;
import io.github.shamrice.discchanger.motorcontroller.Direction;
import io.github.shamrice.discchanger.service.DiscChangerService;
import io.github.shamrice.discchanger.service.SerialDiscChangerService;

/**
 * Created by Erik on 1/11/2017.
 */
public class DiscChangerStarter {

    /**
     * Entry point.
     * @param args - command line arguments.
     */
    public static void main(String[] args) throws InterruptedException {

        try {
            DiscChangerService service = new SerialDiscChangerService();
            service.run();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*
        int numDiscsToSpin = 0;
        boolean displayInfo = false;
        boolean stop = false;
        boolean doorStart = false;
        boolean doorStop = false;
        boolean rotateToDisc = false;
        Direction direction = Direction.FORWARD;

        if (args.length >= 1) {

            if (args[0].equals("--info") || args[0].equals("-i"))
                displayInfo = true;
            else if (args[0].equals("--stop") || args[0].equals("-s"))
                stop = true;
            else if (args[0].equals("--door") || args[0].equals("-d")){
                if (args[1].equals("--start") || args[1].equals("-s")) {
                    doorStart = true;
                    if (args[2].toLowerCase().equals("backward")){
                        direction = Direction.BACKWARD;
                    }
                } else if (args[1].equals("--stop") || args[1].equals("-st")) {
                    doorStop = true;
                }
            } else if (args[0].equals("--carousel") || args[0].equals("-c")){
                numDiscsToSpin = Integer.parseInt(args[1]);
                rotateToDisc = true;
            }
            else {
                try {
                    if (args[1].toLowerCase().equals("backward")) {
                        direction = Direction.BACKWARD;
                    }
                    numDiscsToSpin = Integer.parseInt(args[0]);
                    System.out.println("Spinning " + numDiscsToSpin + " discs.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    numDiscsToSpin = -1;
                    printUsage();
                }
            }

            try {
                Configuration config = ConfigurationBuilder.build();

                if (null != config) {

                    DiscChangerDevice discChangerDevice = DiscChangerDevice.getInstance();
                    discChangerDevice.setConfiguration(config);

                    if (displayInfo)
                       discChangerDevice.printDeviceInfo();
                    else if (stop)
                        discChangerDevice.stopCarousel();

                    else if (doorStart){
                        discChangerDevice.moveDoor(direction);
                    } else if (doorStop) {
                        System.out.println("Stopping door motor.");
                        discChangerDevice.stopDoor();
                    } else if (rotateToDisc) {
                        discChangerDevice.rotateCarouselToDisc(numDiscsToSpin);
                    } else if (numDiscsToSpin > 0 && !rotateToDisc) {
                        discChangerDevice.rotateCarousel(numDiscsToSpin, direction);
                    }

                    discChangerDevice.shutdown();
                    }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            printUsage();
        }

       // System.exit(0);
       */
    }

    public static void printUsage() {
        System.out.println("Usages:");
        System.out.println("\t--info -i\tDisplay device information.");
        System.out.println("\t--stop -s\tStop carousel motor.");
        System.out.println("\t--door -d\tRun door motor.");
        System.out.println("\t\t--start -s\tStart door motor");
        System.out.println("\t\t\t--direction -d\tDirection {FORWARD|BACKWARD}");
        System.out.println("\t\t--stop -st\tStop door motor.");
        System.out.println("\t--carousel -c\tRun carousel motor");
        System.out.println("\t\t{NUMBER}\t disc to stop carousel at");
        System.out.println("\t{NUMBER} {FORWARD|BACKWARD}\tSpin carousel number of discs specified in direction");
        System.out.println("\t--help -h\tDisplay help.");

        System.out.println();
    }
}
