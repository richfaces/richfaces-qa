package org.richfaces.tests.metamer.ftest.jsfReproducers;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

@Templates("plain")
public class TestJSF4007 extends AbstractWebDriverTest {

    @FindBy(css = "[id$=add]")
    private WebElement addValidatorLink;
    @FindBy(css = "[id$=msg]")
    private WebElement msg;
    @FindBy(css = "[id$=submit]")
    private WebElement submitButton;
    @FindBy(css = "[id$=validatedInput]")
    private TextInputComponentImpl validatedInput;

    @Override
    public String getComponentTestPagePath() {
        return "jsfReproducers/jsf-4007.xhtml";
    }

    @Test
    @Skip(On.Container.OtherThanEAP64WithVersion.Under649.class)
    public void testJSF4007() {
        Graphene.guardHttp(addValidatorLink).click();

        Graphene.guardHttp(submitButton).click();
        Graphene.waitGui().until().element(msg).text().contains("Validation Error: Value is required.");

        Graphene.guardHttp(submitButton).click();
        Graphene.waitGui().until().element(msg).text().contains("Validation Error: Value is required.");

        validatedInput.sendKeys("12345");
        Graphene.guardHttp(submitButton).click();
        Graphene.waitGui().until().element(msg).is().not().visible();
    }
}
