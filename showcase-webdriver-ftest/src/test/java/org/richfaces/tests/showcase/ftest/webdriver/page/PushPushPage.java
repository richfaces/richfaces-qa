package org.richfaces.tests.showcase.ftest.webdriver.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PushPushPage implements ShowcasePage {

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
