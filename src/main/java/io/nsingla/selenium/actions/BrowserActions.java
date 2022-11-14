package io.nsingla.selenium.actions;

import org.openqa.selenium.Alert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BrowserActions {

    private static final Logger logger = LoggerFactory.getLogger(BrowserActions.class);

    private final WebDriver driver;

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Returns the Operation System's bit arch
     *
     * @return 64 or 32
     */
    public static int getOSBitArch() {
        String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
        return arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64") ? 64 : 32;
    }

    /**
     * Refresh the page
     */
    public void refresh() {
        logger.info("Refreshing page: {}", driver.getCurrentUrl());
        driver.navigate().refresh();
    }

    /**
     * This will return the current URL the browser is at.
     *
     * @return The URL the browser is currently at.
     */
    public String getCurrentURL() {
        String current = driver.getCurrentUrl();
        logger.info("Current URL is: {}", current);
        return current;
    }

    /**
     * This will accept a browser alert
     */
    public void acceptAlert() {
        WaitActions actions = new WaitActions(driver);
        actions.waitForAlert();
        Alert alert = switchToAlert();
        if (alert != null) {
            alert.accept();
        }
    }

    /**
     * This will dismiss a browser alert
     */
    public void dismissAlert() {
        WaitActions actions = new WaitActions(driver);
        actions.waitForAlert();
        Alert alert = switchToAlert();
        if (alert != null) {
            alert.dismiss();
            driver.switchTo().defaultContent();
        }
    }

    /**
     * This will switch context to a browser alert.
     *
     * @return {@link Alert} if it's present, otherwise null
     */
    public Alert switchToAlert() {
        try {
            return driver.switchTo().alert();
        } catch (Exception e) {
            logger.error("Exception occurred when trying to switch to the alert", e);
        }
        return null;
    }

    public LogEntries getLogs(String logType) {
        return driver.manage().logs().get(logType);
    }

    public List<LogEntry> getLogs(Level level, String logType) {
        List<LogEntry> logEntries = driver.manage().logs().get(logType).getAll();
        return logEntries.stream().filter(logEntry -> logEntry.getLevel().equals(level)).collect(Collectors.toList());
    }

    public void maximizeWindow() {
        // add some code to maximize browser window
        driver.manage().window().maximize();
    }

    public void setViewportSize(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    /**
     * The implicit wait will tell to the web driver to wait for certain amount
     * of time before it throws a "No Such Element Exception". Implicit wait
     * will accept 2 parameters, the first parameter will accept the time as an
     * integer value and the second parameter will accept the time measurement
     * in terms of SECONDS, MINUTES, MILISECOND, MICROSECONDS, NANOSECONDS,
     * DAYS, HOURS, etc.
     *
     * @param time The amount to wait
     * @param unit The time unit
     */
    public void setImplicitWait(int time, TimeUnit unit) {
        driver.manage().timeouts().implicitlyWait(time, unit);
        logger.warn("Implicit Wait time was set to {} {}", time, unit);
    }

    /**
     * Closes the current browser window, will quit the browser if it's the last
     * window currently open.
     */
    public void closeBrowser() {
        driver.close();
    }

    /**
     * Quits out of the current WebDriver session, this closes all windows and
     * simulates the browser closing completely.
     */
    public void quitBrowser() {
        driver.quit();
    }

    /**
     * Returns the title of the current web page.
     *
     * @return String containing the title of the current page.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    public void navigateBack() {
        driver.navigate().back();
    }

}
