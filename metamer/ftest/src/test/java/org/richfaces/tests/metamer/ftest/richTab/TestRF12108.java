package org.richfaces.tests.metamer.ftest.richTab;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

public class TestRF12108 extends AbstractWebDriverTest{

    @FindByJQuery(value = "input[id*='notHandled']")
    private WebElement inputNotHandledCorrectly;

    @FindByJQuery(value = "span[id*='out']")
    private WebElement outputGenerated;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/rf-12108.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12108")
    public void testStatusIsClearedWhenRequestCompleted() {
        final String expectedOutput = "Should be rendered aside as well!";
        inputNotHandledCorrectly.sendKeys(expectedOutput);

        waitAjax(driver).until(new Predicate<WebDriver>() {

            @Override
            public boolean apply(WebDriver arg0) {
                String actualOutput = outputGenerated.getText();
                return expectedOutput.equals(actualOutput);
            }
        });
        }
}
