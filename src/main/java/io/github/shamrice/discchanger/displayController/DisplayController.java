package io.github.shamrice.discchanger.displayController;

import java.io.IOException;

/**
 * Created by Erik on 1/18/2017.
 */
public interface DisplayController {

    void setFont(String name, int size);
    void drawString(String text, int x, int y);
    void drawDiscNum(int num);
    void clearScreen() throws IOException;

}
