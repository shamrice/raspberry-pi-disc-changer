package io.github.shamrice.discchanger.service;

import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.serial.*;
import io.github.shamrice.discchanger.DiscChangerDevice;
import io.github.shamrice.discchanger.config.Configuration;
import io.github.shamrice.discchanger.config.ConfigurationBuilder;
import io.github.shamrice.discchanger.service.listeners.SerialDataListener;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Erik on 2/8/2017.
 */
public class SerialDiscChangerService implements DiscChangerService {

    private final Serial serial;
    private Configuration configuration;
    private DiscChangerDevice discChangerDevice;
    private boolean isRunning = false;
    private boolean isIntialized = false;
    public static ConcurrentLinkedQueue<String> commandStack = new ConcurrentLinkedQueue<>();

    public SerialDiscChangerService() throws IOException, I2CFactory.UnsupportedBusNumberException {

        serial = SerialFactory.createInstance();
        serial.addListener(new SerialDataListener());

        configuration = ConfigurationBuilder.build();
        discChangerDevice = DiscChangerDevice.getInstance();
        discChangerDevice.setConfiguration(configuration);

        isIntialized = initialize();
    }

    public void run() throws InterruptedException{

        if (isIntialized) {

            System.out.println("Disc changer service is running.");

            isRunning = true;
            while (isRunning) {
                if (commandStack.size() > 0) {
                    processCommandStack();
                }

                Thread.sleep(1000);
            }
        }
    }

    private void processCommandStack() {
        while (commandStack.size() > 0) {
            String command = commandStack.remove();

            System.out.println("Command=" + command);

            if (command.trim().equals("disc:50")) {
                rotateToDisc(50);
            } else if (command.trim().equals("shutdown")) {
                shutdown();
                isRunning = false;
            }
        }
    }

    public boolean initialize() {

        try {

            SerialConfig config = new SerialConfig();
            config.device(SerialPort.getDefaultPort())
                    .baud(Baud._38400)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .flowControl(FlowControl.NONE);

            serial.open(config);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;

    }

    public void shutdown() {
        discChangerDevice.shutdown();
    }

    public void rotateToDisc(int discNum) {
        discChangerDevice.rotateCarouselToDisc(discNum);
    }
}
