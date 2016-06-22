package org.richfaces.tests.metamer.ftest.myPackage;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyCustomPageFragment {

    @FindBy(css = "input[type=button]")
    private WebElement innerButtonElement;
    @FindByJQuery(value = "> *")
    private List<WebElement> innerElements;
    @FindBy(className = "myInputClass")
    private WebElement innerInputElement;
    @FindBy(tagName = "span")
    private WebElement innerSpanElement;
    @Root
    private WebElement rootElement;

    public WebElement getInnerButtonElement() {
        return innerButtonElement;
    }

    public List<WebElement> getInnerElements() {
        return innerElements;
    }

    public int getInnerElementsSize() {
        return innerElements.size();
    }

    public WebElement getInnerInputElement() {
        return innerInputElement;
    }

    public WebElement getInnerSpanElement() {
        return innerSpanElement;
    }

    public WebElement getRootElement() {
        return rootElement;
    }
}
