package org.richfaces.tests.showcase.ftest.webdriver.page.a4jPush;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

public class PushPage implements ShowcasePage {

    @FindBy(xpath = "//*[@class='example-cnt']//td/label/../../td/div")
    private WebElement date;

    @Override
    public String getDemoName() {
        return "push";
    }

    @Override
    public String getSampleName() {
        return "push";
    }

    public WebElement getDate() {
        return date;
    }

}
