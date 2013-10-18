package org.richfaces.tests.metamer.ftest.richTab;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

public class TestRF12108 extends AbstractWebDriverTest{

    @FindByJQuery(value = "input[id*='notHandled']")
    private WebElement inputNotHandledCorrectly;

    @FindByJQuery(value = "span[id*='out']")
    private WebElement outputGenerated;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/rf-12108.xhtml");
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12108")
    public void testStatusIsClearedWhenRequestCompleted() {
        String expectedOutput = "Should be rendered aside as well!";
        inputNotHandledCorrectly.sendKeys(expectedOutput);

        String actualOutput = outputGenerated.getText();
        assertEquals(actualOutput, expectedOutput, "The AJAX update is corrupted, when triggered from the second tab!");
    }
}
