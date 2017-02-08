package io.github.shamrice.discchanger.motorcontroller.positionLookup;

/**
 * Created by Erik on 1/19/2017.
 */
public class CarouselPositionLookup {


    /**
     *
     * @param highCount
     * Number of high counts recorded from the carousel position sensor.
     * @return
     * disc location based on the number of "highs" were counted using the carousel position
     * sensor. Returns -1 when none match known values.
     */
    public int getPosition(int highCount) {

        int discLocation = -1;

        //we discard 9 because 9 appears twice in the sensor matrix and is not reliable to get an accurate disc number from
        //without further needless processing. Instead, the carousel will just spin until it finds the next unique highCount.

        /* least sig dig = high count
            most sig dig = high count * 2
            Door offset + 50 discs
            Sensor 1 = 21
         */

        switch(highCount){
            case 1:
                discLocation = 71;
                break;
            case 2:
                discLocation = 92;
                break;
            case 3:
                discLocation = 113;
                break;
            case 4:
                discLocation = 134;
                break;
            case 5: //actually 5.5
                discLocation = 155;
                break;
            case 6:
                discLocation = 176;
                break;
            case 7:
                discLocation = 197;
                break;
            case 8:
                discLocation = 18;
                break;
            case 9:
                discLocation = 39;
                break;
            case 10:
                discLocation = 60;
                break;
        }

        return discLocation;
    }
}
