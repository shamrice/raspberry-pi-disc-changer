package io.github.shamrice.discchanger.config.displayConfiguration;

import com.pi4j.io.i2c.I2CBus;

/**
 * Created by Erik on 1/18/2017.
 */
public class DisplayConfiguration {

    private int i2cAddress;
    private int i2cBus;
    private String bootImageLocation;
    private String idleImageLocation;

    public DisplayConfiguration(int i2cAddress, int i2cBus, String bootImageLocation, String idleImageLocation) {
        this.i2cAddress = i2cAddress;
        this.i2cBus = i2cBus;
        this.bootImageLocation = bootImageLocation;
        this.idleImageLocation = idleImageLocation;
    }

    public int getI2cAddress() {
        return i2cAddress;
    }

    public int getI2cBus() {
        return i2cBus;
    }

    public String getBootImageLocation() {
        return bootImageLocation;
    }

    public String getIdleImageLocation() {
        return idleImageLocation;
    }
}
