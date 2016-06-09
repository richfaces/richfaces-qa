package org.richfaces.tests.metamer.ftest.jsfReproducers;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

@Templates("plain")
public class TestJSF3563 extends AbstractWebDriverTest {

    @FindBy(css = "[id$=button]")
    private WebElement customButton;
    @FindBy(css = "[id$=output]")
    private WebElement output;

    @Override
    public String getComponentTestPagePath() {
        return "jsfReproducers/jsf-3563.xhtml";
    }

    @Test
    @Skip(On.Container.OtherThanEAP64WithVersion.Under649.class)
    public void testJSF3563() {
        assertEquals(output.getText(), "initial");
        Graphene.guardAjax(customButton).click();
        Graphene.waitGui().until().element(output).text().equalTo("after click");
    }
}
