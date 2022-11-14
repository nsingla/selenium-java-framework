package io.nsingla.selenium;

import io.nsingla.constants.SeleniumConstants;
import io.nsingla.junit5.TestBase;
import io.nsingla.junit5.utils.NamingUtils;
import io.nsingla.selenium.enums.TestMode;
import io.nsingla.selenium.extensions.CloseDriverExtension;
import io.nsingla.selenium.extensions.ScreenshotExtension;
import io.nsingla.selenium.logger.ConsoleLogHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ExtendWith(CloseDriverExtension.class)
@ExtendWith(ScreenshotExtension.class)
public class SeleniumBase extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(SeleniumBase.class);

    private WebDriver driver;
    private final TestMode mode;
    private final Browser browser;
    private final boolean headless;
    private String locale;
    private String downloadPath;

    public SeleniumBase() {
        mode = getTestMode();
        browser = getBrowser();
        headless = isHeadless();
        downloadPath = getDownloadPath();
        locale = getLocale();
    }

    @BeforeEach
    public void startDriver(TestInfo testInfo) {
        DriverFactory df = new DriverFactory.DriverFactoryBuilder()
            .setBrowser(browser)
            .setTestMode(mode)
            .setHeadless(headless)
            .setLocale(locale)
            .setDownloadPath(downloadPath)
            .build();
        this.driver = df.getDriver();

        String os = System.getProperty("os.name");
        logger.info("Current Operating System: " + os);

        Dimension initialDimension = driver.manage().window().getSize();
        if (!headless && mode.equals(TestMode.LOCAL) && (os.toLowerCase().contains("linux") || os.toLowerCase().contains("mac"))) {
            MaximizeBrowserOnUnix.maximizeOnUnixSystems(driver);
        } else {
            driver.manage().window().maximize();
        }
        logger.info("Window size: {} and {} after maximize", initialDimension, driver.manage().window().getSize());
        driver.manage().deleteAllCookies(); // Start clean driver session
        if (browser.is(Browser.CHROME.browserName())) {
            logger.info("CONSOLE LOG LEVEL: " + SeleniumConstants.CONSOLE_LOG_LEVEL.getName());
            WebDriverListener listener = new ConsoleLogHandler(SeleniumConstants.CONSOLE_LOG_LEVEL);
            driver = new EventFiringDecorator(listener).decorate(driver);
        }
        logger.debug("Driver for {} started with hash: {}", NamingUtils.getTestName(testInfo), driver.hashCode());
    }

    private TestMode getTestMode() {
        return Optional.of(TestMode.valueOf(SeleniumConstants.TEST_MODE)).orElseThrow(() -> new RuntimeException(SeleniumConstants.TEST_MODE + " is not a valid test mode option"));
    }

    public Browser getBrowser() {
        if (Browser.CHROME.is(SeleniumConstants.BROWSER.toLowerCase())) {
            return Browser.CHROME;
        } else if (Browser.IE.is(SeleniumConstants.BROWSER.toLowerCase())) {
            return Browser.IE;
        } else if (Browser.SAFARI.is(SeleniumConstants.BROWSER.toLowerCase())) {
            return Browser.SAFARI;
        } else if (Browser.EDGE.is(SeleniumConstants.BROWSER.toLowerCase())) {
            return Browser.EDGE;
        } else if (Browser.OPERA.is(SeleniumConstants.BROWSER.toLowerCase())) {
            return Browser.OPERA;
        } else if (Browser.HTMLUNIT.is(SeleniumConstants.BROWSER.toLowerCase())) {
            return Browser.HTMLUNIT;
        } else if (Browser.FIREFOX.is(SeleniumConstants.BROWSER.toLowerCase())) {
            return Browser.FIREFOX;
        } else {
            throw new RuntimeException(SeleniumConstants.BROWSER + " is not a valid browser option");
        }
    }

    private boolean isHeadless() {
        return SeleniumConstants.HEADLESS;
    }

    private String getLocale() {
        return SeleniumConstants.LOCALE;
    }

    private String getDownloadPath() {
        return SeleniumConstants.DOWNLOAD_PATH;
    }

    public SeleniumBase setDriver(WebDriver driver) {
        this.driver = driver;
        return this;
    }

    public WebDriver getDriver() {
        return driver;
    }

}
