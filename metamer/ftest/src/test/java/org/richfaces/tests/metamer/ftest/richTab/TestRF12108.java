package org.richfaces.tests.metamer.ftest.richTab;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

public class TestRF12108 extends AbstractWebDriverTest {

    @FindByJQuery(value = "input[id*='notHandled']")
    private WebElement inputNotHandledCorrectly;

    @FindByJQuery(value = "span[id*='out']")
    private WebElement outputGenerated;

    @Override
    public String getComponentTestPagePath() {
        return "richTabPanel/rf-12108.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12108")
    public void testStatusIsClearedWhenRequestCompleted() {
        final String expectedOutput = "Should be rendered aside as well!";
        Graphene.guardAjax(inputNotHandledCorrectly).sendKeys(expectedOutput);

        waitAjax(driver).until(new Predicate<WebDriver>() {

            @Override
            public boolean apply(WebDriver arg0) {
                String actualOutput = outputGenerated.getText();
                return expectedOutput.equals(actualOutput);
            }
        });
    }
}
