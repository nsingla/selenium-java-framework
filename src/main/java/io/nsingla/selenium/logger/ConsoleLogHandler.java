package io.nsingla.selenium.logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Created by nsingla
 */
public class ConsoleLogHandler implements WebDriverListener {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleLogHandler.class);
    private final Level levelOfThrowingAssertion;
    // Add list of urls that you want to exclude from console log error check
    private final List<String> blacklistedURLs = Arrays.asList(
        "pbs.twimg.com"
    );

    /**
     * @param levelOfThrowingAssertion choose one of four levels of throwing assertion:
     *                                 Level.OFF - every browser console log will be allowed
     *                                 Level.SEVERE - throws new {@link ConsoleLogError} if ERROR appears on browser console
     *                                 Level.WARNING - throws new {@link ConsoleLogError} if WARNING appears on browser console
     *                                 Level.INFO - throws new {@link ConsoleLogError} if INFO appears on browser console
     */
    public ConsoleLogHandler(Level levelOfThrowingAssertion) {
        this.levelOfThrowingAssertion = levelOfThrowingAssertion;
    }

    private List<LogEntry> filterBlacklistedURLs(List<LogEntry> nonFilteredEntries) {
        List<LogEntry> filtered = new ArrayList<>();
        for (LogEntry logEntry : nonFilteredEntries) {
            boolean matchedWithBlackListed = false;
            for (String blackListedUrl : blacklistedURLs) {
                if (logEntry.getMessage().contains(blackListedUrl)) {
                    matchedWithBlackListed = true;
                    break;
                }
            }
            if (!matchedWithBlackListed) {
                filtered.add(logEntry);
            }
        }
        return filtered;
    }

    private void checkBrowserConsoleLogForErrors(WebDriver driver) {
        Logs logs = driver.manage().logs();
        LogEntries logEntries = logs.get(LogType.BROWSER);

        List<LogEntry> errorEntries = logEntries.getAll().stream()
            .filter(entry -> entry.getLevel().intValue() >= levelOfThrowingAssertion.intValue())
            .collect(Collectors.toList());

        List<LogEntry> filteredErrorEntries = filterBlacklistedURLs(errorEntries);
        if (!filteredErrorEntries.isEmpty()) {
            for (LogEntry logEntry : filteredErrorEntries) {
                throw new ConsoleLogError("Browser console ERROR: \n" + logEntry.getMessage());
            }
        }
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeGetCurrentUrl(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterGetCurrentUrl(String result, WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeGetTitle(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterGetTitle(WebDriver driver, String result) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeFindElements(WebDriver driver, By locator) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeGetPageSource(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterGetPageSource(WebDriver driver, String result) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeClose(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterClose(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeQuit(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeGetWindowHandles(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterGetWindowHandles(WebDriver driver, Set<String> result) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeGetWindowHandle(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterGetWindowHandle(WebDriver driver, String result) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeExecuteScript(WebDriver driver, String script, Object[] args) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeExecuteAsyncScript(WebDriver driver, String script, Object[] args) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterExecuteAsyncScript(WebDriver driver, String script, Object[] args, Object result) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforePerform(WebDriver driver, Collection<Sequence> actions) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterPerform(WebDriver driver, Collection<Sequence> actions) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void beforeResetInputState(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }

    @Override
    public void afterResetInputState(WebDriver driver) {
        checkBrowserConsoleLogForErrors(driver);
    }
}
