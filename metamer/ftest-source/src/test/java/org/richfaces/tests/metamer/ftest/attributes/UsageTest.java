package org.richfaces.tests.metamer.ftest.attributes;

import static org.jboss.arquillian.ajocado.Ajocado.elementNotVisible;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.tooltipAttributes;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.direction;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.hideEvent;

import java.net.URL;

import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.component.Positioning;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.richTooltip.TooltipModel;
import org.testng.annotations.Test;


public class UsageTest extends AbstractAjocadoTest {
    
    JQueryLocator panel = pjq("div[id$=panel]");
    TooltipModel tooltip = new TooltipModel(jq(".rf-tt"), panel);
    
    @Test
    public void test1() {
        tooltipAttributes.set(direction, Positioning.auto);
    }
    
    @Test
    public void testHideEvent() {
        tooltipAttributes.set(hideEvent, "mouseup");

        tooltip.recall();

        selenium.mouseUpAt(panel, new Point(5, 5));
        waitGui.until(elementNotVisible.locator(tooltip));
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTooltip/simple.xhtml");
    }
}
