package io.github.shamrice.discchanger.service.listeners;

import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import io.github.shamrice.discchanger.service.SerialDiscChangerService;

import java.io.IOException;

/**
 * Created by Erik on 2/8/2017.
 */
public class SerialDataListener implements SerialDataEventListener {

    public void dataReceived(SerialDataEvent serialDataEvent) {
        //must read data otherwise buffer will grow forever.
        try {

            SerialDiscChangerService.commandStack.add(serialDataEvent.getAsciiString());

            System.out.println("ASCII DATA: " + serialDataEvent.getAsciiString());

        } catch (IOException ex) {
            ex.printStackTrace();;
        }
    }
}
