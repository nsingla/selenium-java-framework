package io.nsingla.selenium.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class BooleanChecks {

    private static final Logger logger = LoggerFactory.getLogger(BooleanChecks.class);

    private final WebDriver driver;

    public BooleanChecks(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * This will tell you if the actualText contains the expectedText
     *
     * @param actualText   The actual text that is found in the Web Element(s)
     * @param expectedText The expected text that you want to search for in the actual text
     * @return If the expectedText is found in the actualText
     */
    public static boolean doesTextContainText(String actualText, String expectedText) {
        if (actualText.contains(expectedText)) {
            logger.debug("'{}' was found within the text '{}'", expectedText, actualText);
            return true;
        } else {
            logger.debug("'{}' was not found within the text '{}'", expectedText, actualText);
            return false;
        }
    }

    /**
     * Detects if an object is currently displayed on the page
     *
     * @param by The selector to search for if it's displayed
     * @return True if the web element is displayed
     */
    public boolean isElementDisplayed(By by) {
        boolean present = false;
        try {
            present = driver.findElement(by).isDisplayed();
            logger.debug("{} is displayed on the page", by.toString());
            return present;
        } catch (NoSuchElementException e) {
            logger.debug("{} is not displayed on the page", by.toString());
            return present;
        }
    }

    /**
     * Detects if an object is currently displayed on the page
     *
     * @param element The selector to search for if it's displayed
     * @return True if the web element is displayed
     */
    public boolean isElementDisplayed(WebElement element) {
        boolean present = false;
        try {
            present = element.isDisplayed();
            logger.debug("{} is displayed on the page", element.toString());
            return present;
        } catch (NoSuchElementException e) {
            logger.debug("{} is not displayed on the page", element.toString());
            return present;
        }
    }

    /**
     * Detects if an object is currently enabled
     *
     * @param by The selector to search for if it's enabled
     * @return Returns true if the web element is enabled
     */
    public boolean isElementEnabled(By by) {
        boolean enabled = driver.findElement(by).isEnabled();
        if (enabled) {
            logger.debug("{} is enabled", by.toString());
            return true;
        } else {
            logger.debug("{} is not enabled", by.toString());
            return false;
        }
    }

    /**
     * Detects if an object is currently enabled
     *
     * @param element The selector to search for if it's enabled
     * @return Returns true if the web element is enabled
     */
    public boolean isElementEnabled(WebElement element) {
        boolean enabled = element.isEnabled();
        if (enabled) {
            logger.debug("{} is enabled", element.toString());
            return true;
        } else {
            logger.debug("{} is not enabled", element.toString());
            return false;
        }
    }

    /**
     * Detects if the web element is actively selected
     *
     * @param by The selector to search for if it's selected
     * @return Returns true if the web element is selected
     */
    public boolean isElementSelected(By by) {
        boolean selected = driver.findElement(by).isSelected();
        if (selected) {
            logger.debug("{} is selected", by.toString());
            return true;
        } else {
            logger.debug("{} is not selected", by.toString());
            return false;
        }
    }

    /**
     * Detects if the web element is actively selected
     *
     * @param element The selector to search for if it's selected
     * @return Returns true if the web element is selected
     */
    public boolean isElementSelected(WebElement element) {
        boolean selected = element.isSelected();
        if (selected) {
            logger.debug("{} is selected", element.toString());
            return true;
        } else {
            logger.debug("{} is not selected", element.toString());
            return false;
        }
    }

    /**
     * Detects if a Web Element is exists in the HTML
     *
     * @param by The selector to search for if it exists
     * @return Returns true if the web element is found
     */
    public boolean doesElementExist(By by) {
        boolean exists = driver.findElements(by).size() > 0;
        if (exists) {
            logger.debug("{} exists in page", by.toString());
            return true;
        } else {
            logger.debug("{} does not exist in page", by.toString());
            return false;
        }
    }

    /**
     * Detects if a Web Element is exists in the HTML
     *
     * @param elements The selector to search for if it exists
     * @return Returns true if the web element is found
     */
    public boolean doesElementExist(List<WebElement> elements) {
        boolean exists = (elements).size() > 0;
        if (exists) {
            logger.debug("{} exists in page", elements.toString());
            return true;
        } else {
            logger.debug("{} does not exist in page", elements.toString());
            return false;
        }
    }

    /**
     * Detects if a child exists within a parent Web Element
     *
     * @param parent The parent element to search
     * @param child  The child element to search for
     * @return If a particular child is found in the parent element
     */
    public boolean hasChild(By parent, By child) {
        return hasChild(driver.findElement(parent), child);
    }

    /**
     * Detects if a child exists within a parent Web Element
     *
     * @param parent The parent element to search
     * @param child  The child element to search for
     * @return If a particular child is found in the parent element
     */
    public boolean hasChild(WebElement parent, By child) {
        return doesElementExist(parent.findElements(child));
    }

    /**
     * This method returns whether a browser alert dialog is currently displayed over the browser
     *
     * @return True if an alert is currently displayed
     */
    public boolean isBrowserAlertDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException ignore) {
            return false;
        }
    }

    /**
     * Checks if the browser is closed or not
     *
     * @return True if the browser is closed
     */
    public boolean isBrowserClosed() {
        try {
            return driver.getWindowHandles().isEmpty();
        } catch (UnreachableBrowserException | NoSuchWindowException ignored) {
            // if browser is closed, driver will throw this exception
            return true;
        }
    }

    /**
     * This method returns if text is present in a Web Element
     *
     * @param by   The selector to check for the text
     * @param text The text to search the element for
     * @return True if the text is found in the element
     */
    public boolean isTextPresentInElement(By by, String text) {
        return isTextPresentInElement(driver.findElement(by), text);
    }

    /**
     * This method returns if text is present in a Web Element
     *
     * @param element The element to check for the text
     * @param text    The text to search the element for
     * @return True if the text is found in the element
     */
    public boolean isTextPresentInElement(WebElement element, String text) {
        return doesTextContainText(element.getText(), text);
    }

    /**
     * This method returns if text is present in a Web Element.
     *
     * @param by   The selector to check for the text
     * @param text The text to search the element for
     * @return If the text is found in the element or not
     */
    public boolean isTextPresentInElementWithReplace(By by, String text) {
        return isTextPresentInElementWithReplace(by, text);
    }

    /**
     * Checks if a specified Web Element contains text or not.
     *
     * @param by   The selector representing the text input field
     * @param text The text, which is checked against the selector
     * @return True if element contains text
     */
    public boolean doesElementContainText(By by, String text) {
        WebElement inputText = driver.findElement(by);
        return !inputText.getAttribute(text).isEmpty();
    }

    /**
     * This method returns if a Web Element contains a class
     *
     * @param by        The selector to search for the class
     * @param className The class to search for within the element
     * @return If the class exists in the element
     */
    public boolean doesElementHaveClass(By by, String className) {
        return doesElementHaveClass(driver.findElement(by), className);
    }

    /**
     * This method returns if a Web Element contains a class
     *
     * @param element   The element to search for the class
     * @param className The class to search for within the element
     * @return If the class exists in the element
     */
    public boolean doesElementHaveClass(WebElement element, String className) {
        return doesElementHaveAttributeValue(element, "class", className);
    }

    /**
     * This method returns if one of the Web Elements contain a class
     *
     * @param by        The selector to search for the class
     * @param className The class to search for within the elements
     * @return If the class exists in one of the elements
     */
    public boolean doesAnElementHaveClass(By by, String className) {
        return doesAnElementHaveClass(driver.findElements(by), className);
    }

    /**
     * This method returns if one of the Web Elements contain a class
     *
     * @param elements  The list of elements to search for the class
     * @param className The class to search for within the elements
     * @return If the class exists in one of the elements
     */
    public boolean doesAnElementHaveClass(List<WebElement> elements, String className) {
        return doesAnElementHaveAttributeValue(elements, "class", className);
    }

    /**
     * This method returns if a Web Element contains a style attribute
     *
     * @param by        The selector to search for the class
     * @param styleAttr The style attribute to search for within the element
     * @return If the style attribute exists in the element
     */
    public boolean doesElementHaveStyleAttr(By by, String styleAttr) {
        return doesElementHaveStyleAttr(driver.findElement(by), styleAttr);
    }

    /**
     * This method returns if a Web Element contains a style attribute
     *
     * @param element   The element to search for the class
     * @param styleAttr The style attribute to search for within the element
     * @return If the style attribute exists in the element
     */
    public boolean doesElementHaveStyleAttr(WebElement element, String styleAttr) {
        return doesElementHaveAttributeValue(element, "style", styleAttr);
    }

    /**
     * This method returns if a Web Element contains a certain attribute value
     *
     * @param by             The selector to search for the attribute
     * @param attributeName  The attribute name to search for within the element
     * @param attributeValue The attribute value to search for within the attribute
     * @return If the attribute value exists in the element
     */
    public boolean doesElementHaveAttributeValue(By by, String attributeName, String attributeValue) {
        return doesElementHaveAttributeValue(driver.findElement(by), attributeName, attributeValue);
    }

    /**
     * This method returns if a Web Element contains a certain attribute value
     *
     * @param element        The web element to search for the attribute
     * @param attributeName  The attribute name to search for within the element
     * @param attributeValue The attribute value to search for within the attribute
     * @return If the attribute value exists in the element
     */
    public boolean doesElementHaveAttributeValue(WebElement element, String attributeName, String attributeValue) {
        if (element.getAttribute(attributeName).contains(attributeValue)) {
            logger.debug("'{}' was found within the {} attribute of the web element: {}", attributeValue, attributeName, element.toString());
            return true;
        } else {
            logger.debug("'{}' was not found within the {} attribute of the web element: {}", attributeValue, attributeName, element.toString());
            return false;
        }
    }

    /**
     * This method returns if one of a list of Web Elements contain a certain attribute value
     *
     * @param by             The selector to search for the attribute
     * @param attributeName  The attribute name to search for within the elements
     * @param attributeValue The attribute value to search for within the attribute
     * @return If the attribute value exists in one of the elements
     */
    public boolean doesAnElementHaveAttributeValue(By by, String attributeName, String attributeValue) {
        return doesAnElementHaveAttributeValue(driver.findElements(by), attributeName, attributeValue);
    }

    /**
     * This method returns if one of a list of Web Elements contain a certain attribute value
     *
     * @param elements       The list of web elements to search for the attribute
     * @param attributeName  The attribute name to search for within the element
     * @param attributeValue The attribute value to search for within the attribute
     * @return If the attribute value exists in one of the elements
     */
    public boolean doesAnElementHaveAttributeValue(List<WebElement> elements, String attributeName, String attributeValue) {
        for (WebElement element : elements) {
            if (element.getAttribute(attributeName).contains(attributeValue)) {
                logger.debug("'{}' was found within the {} attribute of the web elements: {}", attributeValue, attributeName, element.toString());
                return true;
            }
        }
        logger.debug("'{}' was not found within the {} attribute of the web elements: {}", attributeValue, attributeName, elements.toString());
        return false;
    }

    /**
     * This method returns true if element is clickable after a number of seconds
     *
     * @param by The selector of the element
     * @return true if the element is clickable
     */
    public boolean isClickable(By by) {
        WaitActions waitActions = new WaitActions(driver);
        try {
            waitActions.waitForElementIsClickable(by);
            return true;
        } catch (Exception e) {
            logger.debug("Waiting for element to be clickable threw ", e);
            return false;
        }
    }
}
