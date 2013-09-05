package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.statusAttributes;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 * Tests for a4jStatus JS API. Tests are done via testFireEvent
 *
 * @author <a href="mailto:manovotn@redhat.com"> Matej Novotny </a>
 *
 */
public class TestStatusJSApi extends AbstractWebDriverTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jStatus/withoutFacets.xhtml");
    }

    @FindByJQuery("span[id$=status]")
    private WebElement statusMessage;

    @FindByJQuery("span[id$=status] span.rf-st-start")
    private WebElement workingElement;

    @FindByJQuery("span[id$=status] span.rf-st-stop")
    private WebElement stopElement;

    @FindByJQuery("span[id$=status] span.rf-st-error")
    private WebElement errorElement;

    @FindByJQuery("input[id$=invalidateSessionButton]")
    private WebElement invalidateSession;

    private void setAndAssertInitialState() {
        invalidateSession.click();
        assertNotVisible(workingElement, "WebElement with text " + workingElement.getText() + " should not be visible");
        assertNotVisible(errorElement, "WebElement with text " + errorElement.getText() + " should not be visible");
        assertVisible(stopElement, "WebElement with text " + stopElement.getText() + " should be visible");
    }

    @Test
    public void testJsStart() {
        setAndAssertInitialState();
        testFireEvent(statusAttributes, StatusAttributes.onstart, new Action() {
            @Override
            public void perform() {
                executeJS("RichFaces.component('" + statusMessage.getAttribute("id") + "').start()");
            }
        });

    }

    @Test
    public void testJsStop() {
        setAndAssertInitialState();
        testFireEvent(statusAttributes, StatusAttributes.onstop, new Action() {
            @Override
            public void perform() {
                executeJS("RichFaces.component('" + statusMessage.getAttribute("id") + "').stop()");
            }
        });
    }

    @Test
    public void testJsError() {
        setAndAssertInitialState();
        testFireEvent(statusAttributes, StatusAttributes.onerror, new Action() {
            @Override
            public void perform() {
                executeJS("RichFaces.component('" + statusMessage.getAttribute("id") + "').error()");
            }
        });
    }
}
