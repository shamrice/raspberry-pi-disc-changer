package io.github.shamrice.discchanger.service;

/**
 * Created by Erik on 2/8/2017.
 */
public interface DiscChangerService {

    boolean initialize();

    void run() throws InterruptedException;

    void shutdown();

    void rotateToDisc(int discNum);

}
