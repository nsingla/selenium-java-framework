package io.nsingla;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GoogleSearchPage extends BasePage {

    @FindBy(css = "#search")
    WebElement searchResultElement;

    public GoogleSearchPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.get();
    }

    public void search(String text) {
        searchBox.sendKeys(text);
        pageActions.pressEnter();
        waitActions.waitForElementPresent(searchResultElement);
    }
}
