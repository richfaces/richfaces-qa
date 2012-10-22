package org.richfaces.tests.metamer.ftest.a4jJSFunction;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

public class TestRF12510 extends AbstractWebDriverTest<TestRF12510.RF12510Page> {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jJSFunction/rf-12510.xhtml");
    }

    @Test(groups = "4.3")
    @IssueTracking("https://issues.jboss.org/browse/RFPL-12510")
    public void testTriggerJSFunctionAndSeeHowManyTimesCalledMethodOnData() {
        int beforeTrigger = page.parseCounterToInt();
        page.triggerJSFunction();

        int afterTrigger = page.parseCounterToInt();
        assertEquals(afterTrigger, beforeTrigger + 1,
            "Counter which counts number of invocations of jsFunction method on attribute data was called more than once!");
    }

    public class RF12510Page extends MetamerPage {

        @FindBy(css = "span[id$=showcounter]")
        private WebElement counter;

        @FindBy(id = "trigger")
        private WebElement jsFunctionTrigger;

        public void triggerJSFunction() {
            jsFunctionTrigger.click();
        }

        public int parseCounterToInt() {
            return Integer.parseInt(counter.getText());
        }

        public WebElement getCounter() {
            return counter;
        }

        public void setCounter(WebElement counter) {
            this.counter = counter;
        }

        public WebElement getJsFunctionTrigger() {
            return jsFunctionTrigger;
        }

        public void setJsFunctionTrigger(WebElement jsFunctionTrigger) {
            this.jsFunctionTrigger = jsFunctionTrigger;
        }
    }

    @Override
    protected RF12510Page createPage() {
        return new RF12510Page();
    }
}
