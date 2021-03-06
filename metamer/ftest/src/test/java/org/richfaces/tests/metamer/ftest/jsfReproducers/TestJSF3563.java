package org.richfaces.tests.metamer.ftest.jsfReproducers;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.AndExpression;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

@Templates("plain")
@IssueTracking(value = { "https://java.net/jira/browse/JAVASERVERFACES-3430",
        "https://java.net/jira/browse/JAVASERVERFACES-3563" })
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
    @Skip(expressions = { @AndExpression({ On.Container.OtherThanEAPWithVersion.Under649.class }),
            @AndExpression({ On.Container.EAP70.class, On.JSF.VersionLowerThan23.class }) })
    public void testJSF3563() {
        assertEquals(output.getText(), "initial");
        Graphene.guardAjax(customButton).click();
        Graphene.waitGui().until().element(output).text().equalTo("after click");
    }
}
