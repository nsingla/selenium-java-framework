package io.nsingla;

import io.nsingla.selenium.actions.BrowserActions;
import io.nsingla.selenium.actions.PageActions;
import io.nsingla.selenium.actions.WaitActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class BasePage extends LoadableComponent<BasePage> {

    protected WebDriver driver;
    protected PageActions pageActions;
    protected WaitActions waitActions;

    @FindBy(css ="[name='q']")
    public WebElement searchBox;
    public BasePage(WebDriver driver) {
        this.driver = driver;
        pageActions = new PageActions(driver);
        waitActions = new WaitActions(driver);
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        driver.get("https://google.com");
        waitActions.waitForElementPresent(searchBox);
    }
}
