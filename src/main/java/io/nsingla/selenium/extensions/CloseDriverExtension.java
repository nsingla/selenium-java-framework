package io.nsingla.selenium.extensions;

import io.nsingla.junit5.utils.NamingUtils;
import io.nsingla.selenium.SeleniumBase;
import io.nsingla.selenium.logger.ConsoleLogger;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.Browser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseDriverExtension implements AfterEachCallback {

    private static final Logger logger = LoggerFactory.getLogger(CloseDriverExtension.class);

    @Override
    public void afterEach(ExtensionContext context) {
        SeleniumBase base = ((SeleniumBase) context.getTestInstance()
            .orElseThrow(() -> new RuntimeException("Selenium base is not present.")));
        WebDriver driver = base.getDriver();
        if (driver != null) {
            if (base.getBrowser().is(Browser.CHROME.browserName())) {
                Logs logs = driver.manage().logs();
                try {
                    ConsoleLogger.printConsoleEntries(logs, NamingUtils.getTestName(context));
                } catch (NullPointerException ex) {
                    logger.error("No console logs were available.");
                }
            }
            logger.debug("Closing driver for {}: {}", NamingUtils.getTestName(context), driver.hashCode());
            driver.quit();
        } else {
            logger.debug("Driver object was not created");
        }
    }

}
