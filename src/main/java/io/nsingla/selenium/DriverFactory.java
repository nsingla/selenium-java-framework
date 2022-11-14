package io.nsingla.selenium;

import io.nsingla.constants.SeleniumConstants;
import io.nsingla.selenium.enums.TestMode;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.logging.Level;

public class DriverFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverFactory.class);

    private WebDriver driver;
    private TestMode mode;
    private Browser browser;
    private boolean headless;
    private String downloadPath;
    private String locale;

    public DriverFactory(DriverFactoryBuilder builder) {
        this.browser = builder.getBrowser();
        this.mode = builder.getTestMode();
        this.headless = builder.isHeadless();
        this.downloadPath = builder.getDownloadPath();
        this.locale = builder.getLocale();
        this.driver = createDriver();
    }

    public static class DriverFactoryBuilder {
        private TestMode testMode;
        private Browser browser;
        private Boolean headless;
        private String downloadPath;
        private String locale;

        public TestMode getTestMode() {
            return testMode;
        }

        public DriverFactoryBuilder setTestMode(TestMode testMode) {
            this.testMode = testMode;
            return this;
        }

        public Browser getBrowser() {
            return browser;
        }

        public DriverFactoryBuilder setBrowser(Browser browser) {
            this.browser = browser;
            return this;
        }

        public Boolean isHeadless() {
            return headless;
        }

        public DriverFactoryBuilder setHeadless(Boolean headless) {
            this.headless = headless;
            return this;
        }

        public String getDownloadPath() {
            return downloadPath;
        }

        public DriverFactoryBuilder setDownloadPath(String downloadPath) {
            this.downloadPath = downloadPath;
            return this;
        }

        public String getLocale() {
            return locale;
        }

        public DriverFactoryBuilder setLocale(String locale) {
            this.locale = locale;
            return this;
        }

        public DriverFactory build() {
            return new DriverFactory(this);
        }
    }

    private WebDriver createDriver() {
        try {
            switch (mode) {
                case REMOTE:
                    String seleniumPort = SeleniumConstants.SELENIUM_GRID_PORT;
                    String seleniumHost = SeleniumConstants.SELENIUM_GRID_HOST;
                    LOGGER.debug("Starting driver on " + seleniumHost + ":" + seleniumPort);
                    String server = "http://" + seleniumHost + ":" + seleniumPort + "/wd/hub";
                    driver = getRemoteDriver(server);
                    break;
                case LOCAL:
                    driver = getLocalDriver();
                    break;
                default:
                    throw new InvalidParameterException("Unexpected test mode: " + mode);
            }
        } catch (MalformedURLException e) {
            throw new AssertionError(e.getMessage());
        }

        return driver;
    }

    private WebDriver getLocalDriver() {
        File downloaDir = new File(downloadPath);
        if (!downloaDir.exists()) {
            downloaDir.mkdir();
        }
        ChromeOptions options = createDefaultDriverOptions(ChromeOptions.class);
        setDefaultChromeOptions(options);
        return new ChromeDriver(options);
    }

    private RemoteWebDriver getRemoteDriver(String server) throws MalformedURLException {
        LOGGER.info("server: " + server);

        RemoteWebDriver result = new RemoteWebDriver(new URL(server), getDriverOptions());

        result.setFileDetector(new LocalFileDetector());
        return result;
    }

    private AbstractDriverOptions getDriverOptions() {
        AbstractDriverOptions options = null;
        if (Browser.FIREFOX.equals(browser)) {
            LOGGER.info("Starting FirefoxDriver........ ");
            options = createDefaultDriverOptions(FirefoxOptions.class);
            setDefaultFirefoxOptions((FirefoxOptions) options);
        } else if (Browser.CHROME.equals(browser)) {
            LOGGER.info("Starting ChromeDriver........ ");
            options = createDefaultDriverOptions(ChromeOptions.class);
            setDefaultChromeOptions((ChromeOptions) options);
        } else if (Browser.SAFARI.equals(browser)) {
            LOGGER.info("Starting SafariDriver........ ");
            options = createDefaultDriverOptions(SafariOptions.class);
        } else if (Browser.EDGE.equals(browser)) {
            LOGGER.info("Starting SafariDriver........ ");
            options = createDefaultDriverOptions(EdgeOptions.class);
        }
        return options;

    }

    private <T extends MutableCapabilities> T createDefaultDriverOptions(Class<T> clazz) {
        try {
            T options = clazz.getDeclaredConstructor().newInstance();

            LoggingPreferences logs = new LoggingPreferences();
            logs.enable(LogType.BROWSER, Level.ALL);
            logs.enable(LogType.DRIVER, Level.ALL);
            logs.enable(LogType.PERFORMANCE, Level.ALL);
            options.setCapability(CapabilityType.LOGGING_PREFS, logs);
            options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, PageLoadStrategy.NORMAL);
            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            return options;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            LOGGER.error("Failed to create driver capabilities", e);
        }
        return null;
    }

    private void setDefaultChromeOptions(ChromeOptions options) {
        HashMap<String, Object> chromePrefs = new HashMap<>();
        // Set Custom Download Dir for downloads in chrome
        if (downloadPath != null) {
            chromePrefs.put("download.default_directory", downloadPath);
        }
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("download.directory_upgrade", true);

        options.addArguments("--allow-outdated-plugins", "--no-sandbox", "--start-maximized");
        options.setExperimentalOption("prefs", chromePrefs);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);

        if (headless) {
            // note: the window size in headless is not limited to the display size
            options.addArguments("headless", "disable-gpu", "window-size=1920,1080");
        }

        if (StringUtils.isNotEmpty(locale)) {
            options.addArguments("--lang=" + locale);
        }
    }

    private void setDefaultFirefoxOptions(FirefoxOptions options) {
        FirefoxProfile ffProfile = new FirefoxProfile();
        // Set Custom Download Dir for downloads
        if (downloadPath != null) {
            ffProfile.setPreference("browser.download.folderList", 2);
            ffProfile.setPreference("browser.download.dir", downloadPath);
        }
        ffProfile.setPreference("browser.download.manager.showWhenStarting", false);
        ffProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/msword, application/csv, "
            + "application/vnd.ms-powerpoint, application/ris, text/csv, image/png, application/pdf, "
            + "text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, "
            + "application/download, application/octet-stream, application/xls, application/vnd.ms-excel, "
            + "application/x-xls, application/x-ms-excel, application/msexcel, application/x-msexcel, "
            + "application/x-excel");

        options.setHeadless(headless);
        options.setLogLevel(FirefoxDriverLogLevel.fromLevel(SeleniumConstants.CONSOLE_LOG_LEVEL));
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        if (StringUtils.isNotEmpty(locale)) {
            ffProfile.setPreference("intl.accept_languages", locale);
        }
        options.setProfile(ffProfile);
    }

    public WebDriver getDriver() {
        return driver;
    }
}
