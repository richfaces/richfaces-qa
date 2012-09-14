package org.richfaces.tests.metamer.ftest.a4jCommandButton;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.test.selenium.support.ui.ElementNotPresent;
import org.jboss.test.selenium.support.ui.TextEquals;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 *
 */
public class CommandButtonPage extends MetamerPage {

    public static final String STRING_RF1 = "RichFaces 4";
    public static final String STRING_RF1_X2 = "RichFaces 4RichFaces 4";
    public static final String STRING_RF2 = "RichFa";//first 6 characters
    public static final String STRING_RF3 = "RICHFACES 4";
    public static final String STRING_RF_UNICODE = "RichFaces 4š";
    public static final String STRING_RF_UNICODE_UPPERCASE = "RICHFACES 4Š";
    public static final String STRING_UNICODE1 = "ľščťžýáíéňô";
    public static final String STRING_UNICODE2 = "ľščťžý";
    public static final String STRING_UNICODE3 = "ĽŠČŤŽÝÁÍÉŇÔ";
    public static final String STRING_ACTIONLISTENER_MSG = "action listener invoked";
    public static final String STRING_ACTION_MSG = "action invoked";
    public static final String STRING_EXECUTE_CHECKER_MSG = "executeChecker";

    @FindBy(css = "input[id$=input]")
    public WebElement input;
    @FindBy(css = "input[id$=a4jCommandButton]")
    public WebElement button;
    @FindBy(css = "span[id$=output1]")
    public WebElement output1;
    @FindBy(css = "span[id$=output2]")
    public WebElement output2;
    @FindBy(css = "span[id$=output3]")
    public WebElement output3;
    private WebDriver driver = GrapheneContext.getProxy();

    public void typeToInputAndSubmitAndWaitUntilOutput1Changes(String s) {
        typeToInputAndSubmit(s);
        new WebDriverWait(driver).until(TextEquals.getInstance().element(output1).text(s));
    }

    public void typeToInputAndSubmitAndWaitUntilOutput2ChangesToText(String testValue, String expectedText) {
        typeToInputAndSubmit(testValue);
        new WebDriverWait(driver, 5).until(TextEquals.getInstance().element(output2).text(expectedText));
    }

    public void typeToInputAndSubmit(String s) {
        input.clear();
        input.sendKeys(s);
        Graphene.guardXhr(button).click();
    }

    public void typeToInputAndSubmitWithoutRequest(String s) {
        input.clear();
        input.sendKeys(s);
        Graphene.guardNoRequest(button).click();
    }

    public String getRequestTime() {
        return requestTime.getText();
    }

    public void verifyOutput1Text() {
        verifyOutput1Text(STRING_RF1);
    }

    public void verifyOutput2Text() {
        verifyOutput2Text(STRING_RF2);
    }

    public void verifyOutput3Text() {
        verifyOutput3Text(STRING_RF3);
    }

    public void verifyOutput1Text(String s) {
        verifyOutputText(output1, s);
    }

    public void verifyOutput2Text(String s) {
        verifyOutputText(output2, s);
    }

    public void verifyOutput3Text(String s) {
        verifyOutputText(output3, s);
    }

    private void verifyOutputText(WebElement elem, String text) {
        assertEquals(elem.getText(), text);
    }

    public void assertButtonNotPresent() {
        assertTrue(ElementNotPresent.getInstance().element(button).apply(driver), "Button should not be on page.");
    }

    public void assertButtonValue(String value) {
        assertEquals(button.getAttribute("value"), value, "Button's value did not change");
    }
}
