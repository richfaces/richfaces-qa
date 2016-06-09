package org.richfaces.tests.metamer.ftest.jsfReproducers;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

@Templates("plain")
public class TestJSF3545 extends AbstractWebDriverTest {

    @FindBy(css = "[id$=switchToTab0]")
    private WebElement switchToTab0Link;
    @FindBy(css = "[id$=switchToTab1]")
    private WebElement switchToTab1Link;
    @FindBy(css = "[id$=tab0txt]")
    private WebElement tab0TxtInput;
    @FindBy(css = "[id$=tab1txt]")
    private WebElement tab1TxtInput;

    @Override
    public String getComponentTestPagePath() {
        return "jsfReproducers/jsf-3545.xhtml";
    }

    @Test
    @Skip(On.Container.OtherThanEAP64WithVersion.Under649.class)
    public void testJSF3545() {
        Graphene.guardAjax(switchToTab1Link).click();
        Graphene.waitGui().until().element(tab1TxtInput).value().equalTo("Tab 1 value");
        Graphene.guardAjax(switchToTab0Link).click();
        Graphene.waitGui().until().element(tab0TxtInput).value().equalTo("Tab 0 value");
    }
}
