package io.nsingla.selenium.actions;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PageActions {

    private static final Logger logger = LoggerFactory.getLogger(PageActions.class);

    private final WebDriver driver;
    private final WaitActions waitActions;
    private final BooleanChecks booleanChecks;

    public PageActions(WebDriver driver) {
        this.driver = driver;
        this.waitActions = new WaitActions(driver);
        this.booleanChecks = new BooleanChecks(driver);
    }

    /**
     * Performs a Left Click action against the specified object.
     *
     * @param text The text that is displayed in the link
     */
    public void clickOnLink(String text) {
        driver.findElement(By.linkText(text)).click();
        logger.info("{} link was clicked.", text);
    }

    /**
     * Performs a Left Click action against the specified object.
     *
     * @param text The partial text that is displayed in the link
     */
    public void clickOnPartialLink(String text) {
        driver.findElement(By.partialLinkText(text)).click();
        logger.info("{} partial link was clicked.", text);
    }

    /**
     * Used to interact with a web element by "Clicking On It" using
     * findElement(By), WebDriverWait and elementToBeClickable.
     *
     * @param byType       name of the variable used to store the locator path of the web
     *                     element interacting with
     * @param failTestCase if set to true the test case will fail after the intelligent
     *                     (explicit) wait time
     * @param timeout      intelligent (explicit) time to wait before failing the test
     */
    public void ifExistsPerformClick(By byType, Boolean failTestCase, long timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            if (wait.until(ExpectedConditions.elementToBeClickable(byType)) != null) {
                WebElement element = driver.findElement(byType);
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + element.getLocation().y + ")");
                Actions actions = new Actions(driver);
                actions.moveToElement(element).click().perform();
                logger.info("Clicking Element {}", byType);
            }
        } catch (Exception e) {
            if (failTestCase) {
                logger.info("TIMEOUT EXCEPTION element does not exist after explicit wait of {} seconds - {}", timeout, byType);
                Assertions.fail(
                    "TIMEOUT EXCEPTION element does not exist after waiting " + timeout + " seconds - " + byType);
            }
        }
    }

    /**
     * Performs a Click action against the specified object.
     *
     * @param by The selector to execute the Click on.
     */
    public void click(By by) {
        click(driver.findElement(by));
        logger.info("Clicking Element {}", by);
    }

    /**
     * Performs a Click action against the specified object.
     *
     * @param element The selector to execute the Click on.
     */
    public void click(WebElement element) {
        waitActions.waitForElementIsClickable(element);
        element.click();
        logger.info("{} was clicked", element.toString());
    }

    /**
     * This will search through each element in the provided selector and click
     * the first one that is clickable.
     *
     * @param by The selector to search through and eventually click
     */
    public void clickOneOf(By by) {
        waitActions.waitForElementPresent(by);
        for (WebElement element : driver.findElements(by)) {
            if (booleanChecks.isElementDisplayed(element)) {
                if (booleanChecks.isElementEnabled(element)) {
                    try {
                        click(element);
                    } catch (NoSuchElementException mseE) {
                        logger.warn("Element was not found, continuing to loop through", mseE);
                    }
                }
            }
        }
    }

    /**
     * This will find all elements in the provided selector and click all the
     * elements as they are found.
     *
     * @param by The selector to find all elements of and click
     */
    public void clickAll(By by) {
        waitActions.waitForElementPresent(by);
        for (WebElement element : driver.findElements(by)) {
            if (booleanChecks.isElementDisplayed(element)) {
                if (booleanChecks.isElementEnabled(element)) {
                    click(element);
                }
            }
        }
    }

    /**
     * Performs a Left Click action against the specified object.
     *
     * @param by The selector to execute the Left Click on.
     */
    public void leftClick(By by) {
        waitActions.waitForElementIsClickable(by);
        Actions builder = new Actions(driver);
        builder.click(driver.findElement(by)).perform();
        logger.info("{} was left clicked", by.toString());
    }

    /**
     * Performs a Tab action against the specified object.
     *
     * @param by The selector to execute the Tab against.
     */
    public void pressTab(By by) {
        driver.findElement(by).sendKeys(Keys.TAB);
        logger.info("Test has entered a TAB key");
    }

    /**
     * Performs a Tab action without an object.
     */
    public void pressTab() {
        Actions tab = new Actions(driver);
        tab.sendKeys(Keys.TAB).build().perform();
        logger.info("Test has entered a TAB key");
    }

    /**
     * Performs an Enter action without an object.
     */
    public void pressEnter() {
        Actions enter = new Actions(driver);
        enter.sendKeys(Keys.ENTER).build().perform();
        logger.info("Test has pressed the ENTER key");
    }

    /**
     * Performs a Enter action against the specified object.
     *
     * @param by The selector to execute the Enter against.
     */
    public void pressEnter(By by) {
        driver.findElement(by).sendKeys(Keys.ENTER);
        logger.info("Test has entered a ENTER key");
    }

    /**
     * Performs an Return action without an object.
     */
    public void pressReturn() {
        Actions returnPress = new Actions(driver);
        returnPress.sendKeys(Keys.RETURN).build().perform();
        logger.info("Test has pressed the RETURN key");
    }

    /**
     * Performs a Return action against the specified object.
     *
     * @param by The selector to execute the Return against.
     */
    public void pressReturn(By by) {
        driver.findElement(by).sendKeys(Keys.RETURN);
        logger.info("Test has entered a RETURN key");
    }

    /**
     * Performs a Right Click action against the give object to open a Context
     * Menu, then selects the specified element from the Context Menu that
     * opens.
     *
     * @param contextMenu The Context Menu to execute the Right Click on.
     * @param by          The selector to choose from the Context Menu.
     */
    public void rightClick(WebElement contextMenu, By by) {
        logger.info("Attempting Right Click on {} menu choice", contextMenu);
        Actions builder = new Actions(driver);
        builder.contextClick(contextMenu).click(driver.findElement(by)).perform();
        logger.info("Test has opened {} and clicked", contextMenu);
    }

    /**
     * Performs a clear action against the specified TextArea.
     *
     * @param by The selector to clear.
     */
    public void clearTextArea(By by) {
        driver.findElement(by).clear();
        logger.info("Cleared Text Area");
    }

    /**
     * Performs a sendKeys action against the specified TextArea.
     *
     * @param by   The selector to type into.
     * @param text The text to type into the selector.
     */
    public void typeText(By by, String text) {
        driver.findElement(by).sendKeys(text);
        logger.info("\"{}\" has been typed into {}", text, by.toString());
    }

    /**
     * Used to interact with a web element by "Sending Text To It" using
     * findElement(By), WebDriverWait and elementSendKeys, and calls the "clear"
     * method before typing.
     *
     * @param byType    is the name of the variable used to store the locator path of
     *                  the web element to interact with
     * @param text      is the string variable being passed that will be entered/typed
     *                  into a field
     * @param timeout   is the intelligent (explicit) wait time before failing the
     *                  test
     * @param delayTime is a hard (implicit) wait prior to looking for element to
     *                  exist
     */
    public void ifExistsType(By byType, String text, long timeout, int delayTime) {
        waitActions.pauseTest(delayTime);
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            if (wait.until(ExpectedConditions.visibilityOfElementLocated(byType)) != null) {
                WebElement element = driver.findElement(byType);
                element.clear();
                element.sendKeys(text);
                logger.info("Sending text to Element {}", byType);
            }
        } catch (Exception e) {
            logger.info("TIMEOUT EXCEPTION element does not exist after explicit wait of {} seconds - {}", timeout, byType);
            Assertions.fail("TIMEOUT EXCEPTION element does not exist after explicit wait of " + timeout + " seconds - "
                + byType);
        }
    }

    /**
     * Selects a set of text inside the selector.
     *
     * @param by   The selector to select text inside.
     * @param text The text to be selected.
     */
    public void selectByText(By by, String text) {
        waitActions.waitForElementPresent(by);
        new Select(driver.findElement(by)).selectByVisibleText(text);
    }

    public void hoverOverElement(By by) {
        WebElement element = driver.findElement(by);
        hoverOverElement(element);
    }

    /**
     * Hovers over a {@link WebElement}
     *
     * @param element The selector, which will be hovered over
     */
    public void hoverOverElement(WebElement element) {
        Actions builder = new Actions(driver);
        Actions hoverOverElement = builder.moveToElement(element);
        hoverOverElement.perform();
        // If it is IE then there is an issue where the hover over keeps
        // flickering
        // https://code.google.com/p/selenium/wiki/InternetExplorerDriver#Hovering_Over_Elements
        if (driver instanceof InternetExplorerDriver) {
            // move away to avoid the flicker
            Actions moveAway = builder.moveByOffset(200, 0);
            moveAway.perform();
        }
    }

    public void moveToThenClickOnElement(By by) {
        moveToThenClickOnElement(driver.findElement(by));
    }

    public void moveToThenClickOnElement(WebElement element) {
        Actions myAction = new Actions(driver);
        myAction.moveToElement(element).click().perform();
    }

    public void moveToThenDoubleClickOnElement(By by) {
        moveToThenDoubleClickOnElement(driver.findElement(by));
    }

    public void moveToThenDoubleClickOnElement(WebElement element) {
        Actions myAction = new Actions(driver);
        myAction.moveToElement(element).doubleClick().perform();
    }

    /**
     * Sometimes, clickable links will not be clicked if they're not visible on
     * the browser window during a selenium test. See
     * http://stackoverflow.com/questions
     * /12035023/selenium-webdriver-cant-click-on-a-link-outside-the-page
     *
     * @param by the selector representing the element to scroll to.
     */
    public void scrollToThenClickElement(By by) {
        WebElement element = driver.findElement(by);
        int elementPosition = element.getLocation().getY();
        String js = String.format("window.scroll(0, %s)", elementPosition);
        ((JavascriptExecutor) driver).executeScript(js);
        element.click();
    }

    /**
     * If there's an element covering something that's in the same y coordinate,
     * the developer may want to go a little above the element to prevent that
     * interference.
     *
     * @param by     the selector representing the element to scroll to.
     * @param offset the offset, in pixels, to shift up by.
     */
    public void scrollToThenClickElement(By by, int offset) {
        WebElement element = driver.findElement(by);
        int elementPosition = (element.getLocation().getY() - offset);
        String js = String.format("window.scroll(0, %s)", elementPosition);
        ((JavascriptExecutor) driver).executeScript(js);
        element.click();
    }

    /**
     * Selects the specified menu item from the menu
     *
     * @param by   the selector representing the menu to select from
     * @param text the item to select
     */
    public void selectMenuItem(By by, String text) {
        WebElement menu = driver.findElement(by);
        Select dropDown = new Select(menu);
        try {
            dropDown.selectByVisibleText(text);
        } catch (NoSuchElementException e) {
            dropDown.selectByValue(text);
        }
    }

    /**
     * Selects a menu {@link WebElement} selector index
     * @param by    The selector to select
     * @param index The number, which element will be selected from the dropdown
     */
    public void selectMenuByIndex(By by, int index) {
        WebElement menu = driver.findElement(by);
        Select dropDown = new Select(menu);
        dropDown.selectByIndex(index);
    }

    public void submitForm(By by) {
        WebElement formElem = driver.findElement(by);
        formElem.submit();
    }

    /**
     * Drag and drop from {@code source} to {@code target}
     *
     * @param source The selector, which will be dropped
     * @param target The selector, to which will be dropped
     */
    public void dragAndDrop(By source, By target) {
        Actions builder = new Actions(driver);

        Action dragAndDrop = builder.clickAndHold(driver.findElement(source)).moveToElement(driver.findElement(target))
            .release(driver.findElement(target)).build();

        dragAndDrop.perform();
    }

    /**
     * First action is to click and hold on a source and drag it to a target
     * area. And then a second action of releasing the source in the target
     * area. Having these actions separated is necessary if the web element of
     * the target changes after the source is hovered over the target.
     *
     * @param source the selector of an item that is to be moved or dragged
     * @param target the selector of the target area
     */
    public void dragAndThenDrop(By source, By target) {
        Actions builder = new Actions(driver);

        // Select and hold, then drag, and then hover an element over a target
        Action drag = builder.clickAndHold(driver.findElement(source)).moveToElement(driver.findElement(target)).build();
        drag.perform();

        // While hovering get the target element again and then perform the
        // release
        Action drop = builder.release(driver.findElement(target)).build();
        drop.perform();
    }

}
