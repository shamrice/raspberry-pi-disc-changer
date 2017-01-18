package io.github.shamrice.discchanger.displayController;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import io.github.shamrice.discchanger.DiscChangerDevice;
import io.github.shamrice.discchanger.config.displayConfiguration.DisplayConfiguration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

/**
 * Created by Erik on 1/18/2017.
 */
public class SSD1306_I2CDisplayController implements DisplayController{

    private final short SSD1306_I2C_ADDRESS = 0x3C;
    private final short SSD1306_SETCONTRAST = 0x81;
    private final short SSD1306_DISPLAYALLON_RESUME = 0xA4;
    private final short SSD1306_DISPLAYALLON = 0xA5;
    private final short SSD1306_NORMALDISPLAY = 0xA6;
    private final short SSD1306_INVERTDISPLAY = 0xA7;
    private final short SSD1306_DISPLAYOFF = 0xAE;
    private final short SSD1306_DISPLAYON = 0xAF;
    private final short SSD1306_SETDISPLAYOFFSET = 0xD3;
    private final short SSD1306_SETCOMPINS = 0xDA;
    private final short SSD1306_SETVCOMDETECT = 0xDB;
    private final short SSD1306_SETDISPLAYCLOCKDIV = 0xD5;
    private final short SSD1306_SETPRECHARGE = 0xD9;
    private final short SSD1306_SETMULTIPLEX = 0xA8;
    private final short SSD1306_SETLOWCOLUMN = 0x00;
    private final short SSD1306_SETHIGHCOLUMN = 0x10;
    private final short SSD1306_SETSTARTLINE = 0x40;
    private final short SSD1306_MEMORYMODE = 0x20;
    private final short SSD1306_COLUMNADDR = 0x21;
    private final short SSD1306_PAGEADDR = 0x22;
    private final short SSD1306_COMSCANINC = 0xC0;
    private final short SSD1306_COMSCANDEC = 0xC8;
    private final short SSD1306_SEGREMAP = 0xA0;
    private final short SSD1306_CHARGEPUMP = 0x8D;
    private final short SSD1306_EXTERNALVCC = 0x1;
    private final short SSD1306_SWITCHCAPVCC = 0x2;

    private final short SSD1306_ACTIVATE_SCROLL = 0x2F;
    private final short SSD1306_DEACTIVATE_SCROLL = 0x2E;
    private final short SSD1306_SET_VERTICAL_SCROLL_AREA = 0xA3;
    private final short SSD1306_RIGHT_HORIZONTAL_SCROLL = 0x26;
    private final short SSD1306_LEFT_HORIZONTAL_SCROLL = 0x27;
    private final short SSD1306_VERTICAL_AND_RIGHT_HORIZONTAL_SCROLL = 0x29;
    private final short SSD1306_VERTICAL_AND_LEFT_HORIZONTAL_SCROLL = 0x2A;

    private final int width = 128;
    private final int height = 64;

    private I2CDevice device;
    private byte[] buffer;
    private BufferedImage image;
    private Graphics2D graphics;
    private Font font;
    private DisplayConfiguration config;
    
    public SSD1306_I2CDisplayController(DisplayConfiguration displayConfiguration) throws IOException, I2CFactory.UnsupportedBusNumberException {

        this.config = displayConfiguration;

        // get the I2C bus to communicate on
        I2CBus i2c = I2CFactory.getInstance(this.config.getI2cBus());
        device = i2c.getDevice(this.config.getI2cAddress());
     //   I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
     //   device = i2c.getDevice(SSD1306_I2C_ADDRESS);

        command(SSD1306_DISPLAYOFF);
        command(SSD1306_SETDISPLAYCLOCKDIV);
        command(0x80);
        command(SSD1306_SETMULTIPLEX);
        command(0x3F);
        command(SSD1306_SETDISPLAYOFFSET);
        command(0x0);
        command(SSD1306_SETSTARTLINE | 0x0);
        command(SSD1306_CHARGEPUMP);
        command(0x14);
        command(SSD1306_MEMORYMODE);
        command(0x00);
        command(SSD1306_SEGREMAP | 0x1);
        command(SSD1306_COMSCANDEC);
        command(SSD1306_SETCOMPINS);
        command(0x12);
        command(SSD1306_SETCONTRAST);
        command(0xCF);
        command(SSD1306_SETPRECHARGE);
        command(0xF1);
        command(SSD1306_SETVCOMDETECT);
        command(0x40);
        command(SSD1306_DISPLAYALLON_RESUME);
        command(SSD1306_NORMALDISPLAY);
        command(SSD1306_DEACTIVATE_SCROLL);

        command(SSD1306_DISPLAYON);

        //default buffer to empty
        buffer = new byte[width * (height / 8)];
        for (int i = 0; i < width * (height / 8); i++) {
            buffer[i] = (byte) 0x00;
        }

        image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        graphics = image.createGraphics();

    }
    
    public void setFont(String name, int size){
        font = new Font(name, Font.PLAIN, size);
    }

    public void drawString(String text, int x, int y){
        try {
            clearScreen();

            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
            graphics = image.createGraphics();
            graphics.setFont(new Font("Monospaced", Font.PLAIN, 20));
            graphics.setColor(Color.WHITE);
            graphics.drawString(text, x, y);

            //build buffer based on graphics byte image.
            Raster r = image.getRaster();
            for (int scrY = 0; scrY < height; scrY++) {
                for (int scrX = 0; scrX < width; scrX++) {
                    setPixel(scrX, scrY, (r.getSample(scrX, scrY, 0) > 0));
                }
            }

            //write to screen.
            for (int i = 0; i < buffer.length; i++) {
                device.write(0x40, buffer[i]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void drawDiscNum(int num){

        try {

            clearScreen();

            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
            graphics = image.createGraphics();
            graphics.setFont(new Font("Monospaced", Font.PLAIN, 20));
            graphics.setColor(Color.WHITE);
            graphics.drawString("D I S C", 16, 22);

            graphics.setFont(new Font("Monospaced", Font.PLAIN, 36));
            graphics.drawString(String.valueOf(num), 35, 56);

            //build buffer based on graphics byte image.
            Raster r = image.getRaster();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    setPixel(x, y, (r.getSample(x, y, 0) > 0));
                }
            }

            //write to screen.

            for (int i = 0; i < buffer.length; i++) {
                device.write(0x40, buffer[i]);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void drawBootScreen() {
        try {

            clearScreen();

            image = ImageIO.read(new File(this.config.getBootImageLocation()));
            graphics = image.createGraphics();
            //graphics.setFont(new Font("Monospaced", Font.PLAIN, 20));
            graphics.setColor(Color.WHITE);
            graphics.drawImage(image, 0, 0, null);

            //build buffer based on graphics byte image.
            Raster r = image.getRaster();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    setPixel(x, y, (r.getSample(x, y, 0) > 0));
                }
            }

            //write to screen.

            for (int i = 0; i < buffer.length; i++) {
                device.write(0x40, buffer[i]);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void command(int command)  throws IOException {
        int control = 0x00;
        device.write(control, (byte)command);
    }

    private void setPixel(int x, int y, boolean isOn)  {
        if (isOn) {
            buffer[x + (y / 8) * width] |= (1 << (y & 7));
        } else {
            buffer[x + (y / 8) * width] &= ~(1 << (y & 7));
        }
    }

    public void clearScreen() throws IOException {

        //default buffer to empty
        buffer = new byte[width * (height / 8)];
        for (int i = 0; i < width * (height / 8); i++) {
            buffer[i] = (byte) 0x00;
        }

        for (int i = 0; i < buffer.length; i++) {
            device.write(0x40, buffer[i]);
        }
    }
}
