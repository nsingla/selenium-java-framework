package io.nsingla.constants;

import io.nsingla.selenium.enums.TestMode;
import org.openqa.selenium.remote.Browser;

import java.io.File;
import java.util.logging.Level;

public class SeleniumConstants {

    // Constants for Selenium
    public static final String SELENIUM_GRID_HOST = System.getProperty("seleniumHost", "localhost");
    public static final String SELENIUM_GRID_PORT = System.getProperty("seleniumPort", "4444");
    public static final String APP_URL = System.getProperty("url", "https://www.google.com");
    public static final Level CONSOLE_LOG_LEVEL = Level.parse(System.getProperty("consoleloglevel", Level.OFF.getName()));
    public static final String TEST_MODE = System.getProperty("mode", TestMode.LOCAL.name());
    public static final String BROWSER = System.getProperty("browser", Browser.CHROME.browserName());
    public static final String LOCALE = System.getProperty("locale", "en");
    public static final String DOWNLOAD_PATH = System.getProperty("downloadPath", System.getProperty("user.home") + File.separator + "Downloads" + File.separator);
    public static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "false"));

}
