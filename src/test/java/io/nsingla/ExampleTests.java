package io.nsingla;

import io.nsingla.selenium.SeleniumBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

public class ExampleTests extends SeleniumBase {

    private WebDriver webDriver;

    @BeforeEach
    public void instatiateDriver() {
        this.webDriver = getDriver();
    }

    @Test
    public void testGoogle() {
        GoogleSearchPage searchPage = new GoogleSearchPage(webDriver);
        searchPage.search("Something");
    }


}
