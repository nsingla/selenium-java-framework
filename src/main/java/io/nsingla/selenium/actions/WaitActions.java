package io.nsingla.selenium.actions;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class WaitActions {

    private static final int defaultWait = 90;
    private static final Logger logger = LoggerFactory.getLogger(WaitActions.class);
    private final WebDriver driver;

    public WaitActions(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Returns the default wait attribute that is being used with all Wait
     * methods.
     *
     * @return The default wait attribute
     */
    public static int getDefaultWait() {
        return defaultWait;
    }

    public void pauseTest() {
        pauseTest(defaultWait);
    }

    /**
     * Pauses the test for the default number of seconds. This function is used
     * to aid in keeping a test synched with the app.
     *
     * @param seconds To wait
     */
    public void pauseTest(int seconds) {
        logger.debug("Pausing test execution for {} seconds", seconds);
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            logger.error("InterruptedException occurred when pausing test", e);
        }
        logger.debug("Resuming test playback.");
    }

    /**
     * Waits for a specified object to be present in the HTML.
     *
     * @param by The selector to wait for (Can be a By or WebElement)
     * @return {@link WebElement}
     */
    public WebElement waitForElementPresent(By by) {
        logger.debug("Waiting for {} to be present on the page...", by.toString());
        ExpectedCondition<WebElement> condition = ExpectedConditions.presenceOfElementLocated(by);
        return waitWithCondition(condition, defaultWait, "Element by " + by.toString() + " still not present " +
            "after " + defaultWait + " seconds wait");
    }

    /**
     * Waits for a specified object to be present in the HTML.
     *
     * @param webElement The selector to wait for (Can be a By or WebElement)
     * @return {@link WebElement}
     */
    public WebElement waitForElementPresent(WebElement webElement) {
        logger.debug("Waiting for {} to be present on the page...", webElement.toString());
        ExpectedCondition<WebElement> condition = ExpectedConditions.visibilityOf(webElement);
        return waitWithCondition(condition, defaultWait, "Element by " + webElement.toString() + " still not present " +
            "after " + defaultWait + " seconds wait");
    }

    /**
     * Waits for a specified object to be present in the HTML with a custom wait
     *
     * @param webElement The selector to wait for (Can be a By or WebElement)
     * @param waitTime   The amount to wait
     * @return {@link WebElement}
     */
    public WebElement waitForElementPresent(WebElement webElement, int waitTime) {
        logger.debug("Waiting for {} to be present on the page...", webElement.toString());
        ExpectedCondition<WebElement> condition = ExpectedConditions.visibilityOf(webElement);
        return waitWithCondition(condition, waitTime, "Element by " + webElement.toString() + " still not present " +
            "after " + waitTime + " seconds wait");
    }

    /**
     * Waits for a specified object to be present in the HTML.
     *
     * @param by The selector to wait for (Can be a By or WebElement)
     * @return {@link WebElement}
     */
    public WebElement waitForElementToBeVisible(By by) {
        logger.debug("Waiting for {} to be present on the page...", by.toString());
        ExpectedCondition<WebElement> condition = ExpectedConditions.visibilityOfElementLocated(by);
        return waitWithCondition(condition, defaultWait, "Element by " + by.toString() + " still not present " +
            "after " + defaultWait + " seconds wait");
    }

    /**
     * Waits for a specified object to NOT be present in the HTML.
     *
     * @param by The selector to wait for
     */
    public void waitForElementNotPresent(By by) {
        logger.debug("Waiting for {} to not be present on the page.", by.toString());
        waitWithCondition(not(presenceOfElementLocated(by)), defaultWait, "Element by " + by.toString() + " still present " +
            "after " + defaultWait + " seconds wait");
    }

    /**
     * Waits for a specified object to be clickable in the HTML.
     *
     * @param by The selector to wait for
     * @return {@link WebElement}
     */
    public WebElement waitForElementIsClickable(By by) {
        logger.debug("Waiting for {} to be clickable...  Waiting for {} seconds", by.toString(), defaultWait);
        ExpectedCondition<WebElement> condition = ExpectedConditions.elementToBeClickable(by);
        return waitWithCondition(condition, defaultWait, "Element by " + by.toString() + " still not clickable " +
            "after " + defaultWait + " seconds wait");
    }

    /**
     * Waits for a specified object to be clickable in the HTML.
     *
     * @param webElement - The selector to wait for
     * @return {@link WebElement}
     */
    public WebElement waitForElementIsClickable(WebElement webElement) {
        logger.debug("Waiting for {} to be clickable...  Waiting for {} seconds", webElement.toString(), defaultWait);
        ExpectedCondition<WebElement> condition = ExpectedConditions.elementToBeClickable(webElement);
        return waitWithCondition(condition, defaultWait, "Element by " + webElement.toString() + " still not clickable " +
            "after " + defaultWait + " seconds wait");
    }

    /**
     * Waits for a specified object to NOT be clickable in the HTML.
     *
     * @param by - The selector to wait for
     */
    public void waitForElementIsNotClickable(By by) {
        logger.debug("Waiting for {} to not be clickable.", by.toString());
        ExpectedCondition<Boolean> condition = ExpectedConditions.not(elementToBeClickable(by));
        waitWithCondition(condition, defaultWait, "Element by " + by.toString() + " still clickable " +
            "after " + defaultWait + " seconds wait");
    }

    /**
     * Waits for a specified object to NOT be clickable in the HTML.
     *
     * @param webElement - The selector to wait for
     */
    public void waitForElementIsNotClickable(WebElement webElement) {
        logger.debug("Waiting for {} to not be clickable.", webElement.toString());
        ExpectedCondition<Boolean> condition = ExpectedConditions.not(elementToBeClickable(webElement));
        waitWithCondition(condition, defaultWait, "Element by " + webElement.toString() + " still clickable " +
            "after " + defaultWait + " seconds wait");
    }

    /**
     * Waits for a specified object to NOT be visible in the HTML.
     *
     * @param by The selector to wait for
     */
    public void waitForElementToBeNotVisible(By by) {
        logger.debug("Waiting for {} to be visible on the page..", by.toString());
        waitWithCondition(invisibilityOfElementLocated(by), defaultWait, "Element by " + by.toString() + " still visible after " + defaultWait + " " +
            "seconds wait");
    }

    /**
     * Waits for a specified string to be present in a particular element on the
     * page.
     *
     * @param by   The element that you are looking for the text to show in.
     * @param text The String to wait for
     */
    public void waitForTextPresentInElement(By by, String text) {
        logger.debug("Waiting for text '{}' to be present in {}", text, by.toString());
        logger.trace("By.xpath html/body represents the entire document");
        waitWithCondition(not(textToBePresentInElementLocated(by, text)), defaultWait, "Element " + by.toString() + " with text: " + text + " not " +
            "present even after waiting for " + defaultWait + " seconds");
    }

    /**
     * Waits for a specified string to NOT be present in a particular element on
     * the page.
     *
     * @param by   The element that you are looking for the text to not be in.
     * @param text The String to wait for
     */
    public void waitForTextNotPresentInElement(By by, String text) {
        logger.debug("Waiting for text '{}' to be present in {}", text, by.toString());
        logger.trace("By.xpath html/body represents the entire document");
        waitWithCondition(not(textToBePresentInElementLocated(by, text)), defaultWait, "Element by: " + by.toString() + " still contains text: " +
            text + " after waiting for " + defaultWait + " seconds");
    }

    public void waitForFrameThenSwitchToIt(By by) {
        waitForFrameThenSwitchToIt(by, defaultWait);
    }

    public void waitForFrameThenSwitchToIt(By by, int seconds) {
        waitForFrame(by, seconds);
    }

    public void waitForFrame(String frame) {
        logger.info("Switching to frame {}", frame);
        waitWithCondition(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame), defaultWait, "Frame with name : " + frame + " not found");
    }

    /**
     * Wait for frame to appear
     *
     * @param by      The selector of the frame
     * @param seconds The amount to wait
     */
    public void waitForFrame(By by, int seconds) {
        logger.debug("Waiting for {} to be visible on the page for {} seconds", by.toString(), seconds);
        waitWithCondition(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by), seconds, "Frame with name selector : " + by.toString() + " not found");

    }

    public void waitForAlert(int timeout) {
        logger.debug("Waiting for alert to be present");
        waitWithCondition(ExpectedConditions.alertIsPresent(), defaultWait, "Alert not found");
    }

    public void waitForAlert() {
        logger.debug("Waiting for alert to be present");
        waitWithCondition(ExpectedConditions.alertIsPresent(), defaultWait, "Alert not found");
    }

    /**
     * Wait for animation to finish on the page
     *
     * @param cssLocator The element's css locator
     */
    public void waitUntilAnimationIsDone(String cssLocator) {
        ExpectedCondition<Boolean> expectation = driver -> {
            assert driver != null;
            return ((JavascriptExecutor) driver).executeScript("return jQuery(\"" + cssLocator + "\").is(':animated')").toString()
                .equalsIgnoreCase("false");
        };
        waitWithCondition(expectation, defaultWait, "Animation still present even after waiting for " + defaultWait + " seconds");
    }

    /**
     * Wait for child element to be present in DOM
     *
     * @param parentElement The parent {@link WebElement}
     * @param childLocator  The child locator
     * @return {@link WebElement}
     */
    public WebElement waitForChildElementToAppear(WebElement parentElement, By childLocator) {
        return waitWithCondition(ExpectedConditions.visibilityOf(parentElement.findElement(childLocator)), defaultWait, "Child element" +
            childLocator.toString() + " didnot appear even after a wait of " + defaultWait + " seconds");
    }

    /**
     * Wait for the "document.readyState" to become complete (which means DOM is ready)
     */
    public void waitForPageToLoad() {
        ExpectedCondition<Boolean> expectation = driver -> {
            assert driver != null;
            return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
                .equalsIgnoreCase("complete");
        };
        waitWithCondition(expectation, defaultWait, "Page haven't loaded in " + defaultWait + " seconds");
    }

    /**
     * Used to switch between frames using frameToBeAvailableAndSwitchToIt &
     * WebDriverWait.
     *
     * @param frame name of the frame to switch to
     */
    public void ifFrameExistsSwitchTo(String frame) {
        waitForFrame(frame);
        driver.switchTo().frame(frame);
    }

    public void switchToDefaultFrame() {
        driver.switchTo().defaultContent();
    }

    /**
     * Generic wait condition
     *
     * @param <T>                 The expected class of the responseType
     * @param condition           The {@link ExpectedCondition}
     * @param timeout             Time to wait
     * @param messageForException The message, which will be used for the exception
     * @return T of given Class
     */
    public <T> T waitWithCondition(ExpectedCondition<T> condition, int timeout, String messageForException) {
        int cycles = 12;
        int count = 0;
        while (count < cycles) {
            try {
                FluentWait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(timeout / cycles)).pollingEvery(Duration.ofMillis(200));
                return wait.until(condition);
            } catch (StaleElementReferenceException e) {
                logger.debug("Trying to recover from a stale element reference exception");
                count = count + 1;
            } catch (TimeoutException | NoSuchElementException e) {
                count = count + 1;
            }
        }
        throw new Error(messageForException + ": " + condition);
    }

}
