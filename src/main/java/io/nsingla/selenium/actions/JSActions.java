package io.nsingla.selenium.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSActions {

    private static final Logger logger = LoggerFactory.getLogger(JSActions.class);

    private final WebDriver driver;

    public JSActions(WebDriver driver) {
        this.driver = driver;
    }

    private boolean isBrowserJavascriptExecutor() {
        return driver instanceof JavascriptExecutor;
    }

    private JavascriptExecutor getJavascriptExecutorDriver() {
        if (isBrowserJavascriptExecutor()) {
            return (JavascriptExecutor) driver;
        } else {
            logger.warn("Browser is not able to execute JavaScript.");
            return null;
        }
    }

    /**
     * Executes the provided {@code scriptText} using {@link JavascriptExecutor}
     *
     * @param scriptText The script's text, which will be executed
     * @return {@link String} of the output
     */
    public String executeJQuery(String scriptText) {
        JavascriptExecutor jsExec = getJavascriptExecutorDriver();
        String output = "";
        if (jsExec != null) {
            logger.info("Following JavaScript is going to be executed against browser: {}", scriptText);
            output = (String) jsExec.executeScript(scriptText);
            logger.info("JavaScript execution is complete.");
        }
        return output;
    }

    /**
     * Executes the provided {@code scriptText} using {@link JavascriptExecutor}
     *
     * @param scriptText The script's text, which will be executed
     */
    public void executeScript(String scriptText) {
        JavascriptExecutor jsExec = getJavascriptExecutorDriver();
        if (jsExec != null) {
            jsExec.executeScript(scriptText);
            logger.info("Following JavaScript was executed against browser: {}", scriptText);
        }
    }

    /**
     * Executes the provided {@code scriptText} using {@link JavascriptExecutor} against a selector
     *
     * @param scriptText The script's text, which will be executed
     * @param by         The selector, which the script will be executed against
     */
    public void executeScript(String scriptText, By by) {
        JavascriptExecutor jsExec = getJavascriptExecutorDriver();
        if (jsExec != null) {
            jsExec.executeScript(scriptText, driver.findElement(by));
            logger.info("Following JavaScript: '{}' was executed against: {}", scriptText, by.toString());
        }
    }

    /**
     * Executes the provided {@code scriptText} using {@link JavascriptExecutor} against a selector
     *
     * @param scriptText The script's text, which will be executed
     * @param element    The selector, which the script will be executed against
     */
    public void executeScript(String scriptText, WebElement element) {
        JavascriptExecutor jsExec = getJavascriptExecutorDriver();
        if (jsExec != null) {
            jsExec.executeScript(scriptText, element);
            logger.info("Following JavaScript: '{}' was executed against: {}", scriptText, element.toString());
        }
    }

    /**
     * Executes the provided {@code scriptText} using {@link JavascriptExecutor} against a selector
     *
     * @param scriptText The script's text, which will be executed
     * @param otherText  The selector, which the script will be executed against
     * @return {@link WebElement} against which the script was executed
     */
    public WebElement executeScript(String scriptText, String otherText) {
        JavascriptExecutor jsExec = getJavascriptExecutorDriver();
        WebElement element = null;
        if (jsExec != null) {
            element = (WebElement) jsExec.executeScript(scriptText, otherText);
            logger.info("Following JavaScript: '{}' was executed against: {}", scriptText, otherText);
        }
        return element;
    }

    public void makeElementVisible(WebElement element) {
        String js = "arguments[0].style.visibility='visible';arguments[0].style.display='inline-block';";
        executeScript(js, element);
    }

    public void hideAnElement(WebElement element) {
        String js = "arguments[0].style.visibility='hidden';";
        executeScript(js, element);
    }
}
