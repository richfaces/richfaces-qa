package org.richfaces.tests.metamer.ftest.richTabPanel;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.tabPanel.RichFacesTabPanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

public class TestRF13278 extends AbstractWebDriverTest {

    @FindByJQuery("div[id$='tabPanel']")
    private RichFacesTabPanel tabPanel;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/rf-13278.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13278")
    public void testTabHeaderIsUpdatedPerAjax() {
        assertSecondTabHeaderEquals("0 clicks");

        FirstTabContent firstContent = tabPanel.switchTo(0).getContent(FirstTabContent.class);
        Graphene.guardAjax(firstContent.clickMe).click();

        assertSecondTabHeaderEquals("1 clicks");
    }

    private void assertSecondTabHeaderEquals(String text) {
        tabPanel.switchTo(1);
        String headerText = tabPanel.advanced().getActiveHeaderElement().getText().trim();
        assertEquals(headerText, text);
    }

    public class FirstTabContent {

        @FindBy(tagName = "a")
        private WebElement clickMe;
    }
}
