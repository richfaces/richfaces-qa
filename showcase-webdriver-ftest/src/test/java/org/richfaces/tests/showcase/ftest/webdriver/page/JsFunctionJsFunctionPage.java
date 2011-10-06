package org.richfaces.tests.showcase.ftest.webdriver.page;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class JsFunctionJsFunctionPage implements ShowcasePage {

    @FindBy(xpath = "//*[@class='example-cnt']//span[text()='Alex']")
    private WebElement nameAlex; 
    @FindBy(xpath = "//*[@class='example-cnt']//span[text()='John']")
    private WebElement nameJohn;
    @FindBy(xpath = "//*[@class='example-cnt']//span[text()='Kate']")
    private WebElement nameKate;
    private Map<String, WebElement> names;    
    @FindBy(xpath = "//*[@class='example-cnt']//b/span")
    private WebElement output;
    
    
    @Override
    public String getDemoName() {
        return "jsFunction";
    }

    @Override
    public String getSampleName() {
        return "jsFunction";
    }

    public WebElement getOutput() {
        return output;
    }
    
    public Map<String, WebElement> getNames() {
        if (names == null) {
            Map<String, WebElement> newNames = new HashMap<String, WebElement>();
            newNames.put("Alex", nameAlex);
            newNames.put("John", nameJohn);
            newNames.put("Kate", nameKate);
            names = Collections.unmodifiableMap(newNames);
        }
        return names;
    }
    
}
