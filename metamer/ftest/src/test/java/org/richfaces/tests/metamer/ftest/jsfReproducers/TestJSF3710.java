package org.richfaces.tests.metamer.ftest.jsfReproducers;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

@Templates("plain")
public class TestJSF3710 extends AbstractWebDriverTest {

    @FindBy(css = "[id$=submitButton]")
    private WebElement submitButton;
    @FindBy(css = "[id$=table]")
    private WebElement table;

    private void checkTable() {
        assertEquals(table.findElement(By.tagName("caption")).getText(), "My Caption");
        assertEquals(table.findElement(By.tagName("thead")).getText(), "My Header");
        assertEquals(table.findElement(By.tagName("tfoot")).getText(), "My Footer");
    }

    @Override
    public String getComponentTestPagePath() {
        return "jsfReproducers/jsf-3710.xhtml";
    }

    @Test
    @Skip(On.Container.OtherThanEAP64WithVersion.Under649.class)
    public void testJSF3710() {
        checkTable();
        Graphene.guardHttp(submitButton).click();
        checkTable();
    }
}
