package org.richfaces.tests.metamer.ftest.myPackage;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

public class MyFirstTest extends AbstractWebDriverTest {

    @FindBy(css = "[id$=myComponentId]")
    private MyCustomPageFragment fragment;
    private String injectedText;

    @Override
    public String getComponentTestPagePath() {
        return "myComponent/sample1.xhtml";
    }

    @Test
    public void testClickButtonChangesInputText() {
        assertEquals(fragment.getInnerInputElement().getAttribute("value"), "some text");
        Graphene.guardAjax(fragment.getInnerButtonElement()).click();
        Graphene.waitAjax().until().element(fragment.getInnerInputElement()).value().equalToIgnoreCase("changed text");
    }

    @Test
    public void testComponentHasThreeElements() {
        assertEquals(fragment.getInnerElementsSize(), 3, "There should be 3 elements.");
    }

    @Test
    public void testElementsTexts() {
        assertEquals(fragment.getInnerSpanElement().getText(), "some text in a span");
        assertEquals(fragment.getInnerButtonElement().getAttribute("value"), "some button");
        assertEquals(fragment.getInnerInputElement().getAttribute("value"), "some text");
    }

    @Test
    @UseWithField(field = "injectedText", value = { "text1", "text2", "text3" }, valuesFrom = ValuesFrom.STRINGS)
    public void testTypingSomeTextSendsAjax() {
        fragment.getInnerInputElement().clear();
        fragment.getInnerInputElement().sendKeys(injectedText);
        getMetamerPage().blur(WaitRequestType.XHR);
        Graphene.waitAjax().until().element(fragment.getInnerInputElement()).value().equalToIgnoreCase(injectedText);
    }
}
