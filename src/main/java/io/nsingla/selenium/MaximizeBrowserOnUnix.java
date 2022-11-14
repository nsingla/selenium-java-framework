package io.nsingla.selenium;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

/**
 * Created by nsingla
 */
public class MaximizeBrowserOnUnix {

    /**
     * Maximizes the browser on a unix system
     * @param driver {@link WebDriver}, which will send the commands to the browser
     */
    public static void maximizeOnUnixSystems(WebDriver driver) {
        Point targetPosition = new Point(0, 0);
        driver.manage().window().setPosition(targetPosition);

        int width;
        int height;

        GraphicsDevice[] monitors = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

        if (monitors.length > 1) {
            width = monitors[1].getDisplayMode().getWidth();
            height = monitors[1].getDisplayMode().getHeight();
        } else {
            java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            width = (int) screenSize.getWidth();
            height = (int) screenSize.getHeight();
        }

        driver.manage().window().setSize(new Dimension(width, height));
    }

}
