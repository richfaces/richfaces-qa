/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.tests.metamer.ftest.richPanel;

import static org.richfaces.tests.metamer.ftest.BasicAttributes.bodyClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerClass;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.panel.TextualRichFacesPanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richPanel/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 22733 $
 */
public class TestPanel extends AbstractWebDriverTest {

    private final Attributes<PanelAttributes> panelAttributes = getAttributes();

    @FindBy(css = "div[id$=panelWithHeader]")
    private TextualRichFacesPanel panelWithHeader;

    @FindBy(css = "div[id$=panelWithoutHeader]")
    private TextualRichFacesPanel panelWithoutHeader;

    @Override
    public String getComponentTestPagePath() {
        return "richPanel/simple.xhtml";
    }

    @Test
    @CoversAttributes("bodyClass")
    @Templates(value = "plain")
    public void testBodyClass() {
        testStyleClass(panelWithHeader.advanced().getBodyElement(), bodyClass);
        testStyleClass(panelWithoutHeader.advanced().getBodyElement(), bodyClass);
    }

    @Test
    @CoversAttributes("header")
    @Templates(value = "plain")
    public void testHeader() {
        panelAttributes.set(PanelAttributes.header, "new header");

        assertEquals(panelWithHeader.getHeaderText(), "header of panel",
            "Header of the first panel should not change (facet defined).");
        assertEquals(panelWithoutHeader.getHeaderText(), "new header",
            "Header of the second panel.");
    }

    @Test
    @CoversAttributes("headerClass")
    @Templates(value = "plain")
    public void testHeaderClass() {
        testStyleClass(panelWithHeader.advanced().getHeaderElement(), headerClass);
    }

    @Test(groups = "smoke")
    @Templates(value = "plain")
    public void testInit() {
        assertPresent(panelWithHeader.advanced().getRootElement(),
            "Panel with header should be present and visible on the page.");
        assertPresent(panelWithoutHeader.advanced().getRootElement(),
            "Panel without header should be present and visible on the page.");

        assertPresent(panelWithHeader.advanced().getHeaderElement(), "The first panel should have a header.");
        assertNotPresent(panelWithoutHeader.advanced().getHeaderElement(),
            "The second panel should not have any header.");

        assertPresent(panelWithHeader.advanced().getBodyElement(), "The first panel should have a body.");
        assertPresent(panelWithoutHeader.advanced().getBodyElement(), "The second panel should have a body.");

        assertTrue(panelWithHeader.getHeaderText().endsWith("header of panel"));
        assertTrue(panelWithHeader.getBodyText().startsWith("Lorem ipsum"),
            "First panel's body should start with \"Lorem ipsum\".");
        assertTrue(panelWithoutHeader.getBodyText().startsWith("Nulla ornare"),
            "Second panel's body should start with \"Nulla ornare\".");
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        Action clickAction = new Actions(driver).click(panelWithHeader.advanced().getRootElement()).build();
        testFireEvent(panelAttributes, PanelAttributes.onclick, clickAction);

        clickAction = new Actions(driver).click(panelWithoutHeader.advanced().getRootElement()).build();
        testFireEvent(panelAttributes, PanelAttributes.onclick, clickAction);
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        Action dblClickAction = new Actions(driver).doubleClick(panelWithHeader.advanced().getRootElement()).build();
        testFireEvent(panelAttributes, PanelAttributes.ondblclick, dblClickAction);

        dblClickAction = new Actions(driver).doubleClick(panelWithoutHeader.advanced().getRootElement()).build();
        testFireEvent(panelAttributes, PanelAttributes.ondblclick, dblClickAction);
    }

    @Test
    @CoversAttributes("onkeydown")
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEventWithJS(panelWithHeader.advanced().getRootElement(), Event.KEYDOWN, panelAttributes,
            PanelAttributes.onkeydown);
        testFireEventWithJS(panelWithoutHeader.advanced().getRootElement(), Event.KEYDOWN, panelAttributes,
            PanelAttributes.onkeydown);
    }

    @Test
    @CoversAttributes("onkeypress")
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEventWithJS(panelWithHeader.advanced().getRootElement(), Event.KEYPRESS, panelAttributes,
            PanelAttributes.onkeypress);
        testFireEventWithJS(panelWithoutHeader.advanced().getRootElement(), Event.KEYPRESS, panelAttributes,
            PanelAttributes.onkeypress);
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEventWithJS(panelWithHeader.advanced().getRootElement(), Event.KEYUP, panelAttributes,
            PanelAttributes.onkeyup);
        testFireEventWithJS(panelWithoutHeader.advanced().getRootElement(), Event.KEYUP, panelAttributes,
            PanelAttributes.onkeyup);
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEventWithJS(panelWithHeader.advanced().getRootElement(), panelAttributes, PanelAttributes.onmousedown);
        testFireEventWithJS(panelWithoutHeader.advanced().getRootElement(), panelAttributes, PanelAttributes.onmousedown);
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEventWithJS(panelWithHeader.advanced().getRootElement(), panelAttributes, PanelAttributes.onmousemove);
        testFireEventWithJS(panelWithoutHeader.advanced().getRootElement(), panelAttributes, PanelAttributes.onmousemove);
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        // TODO 2013-02-01 JJa: try implement using WebDriver API (doesn't work for now)
        testFireEventWithJS(panelWithHeader.advanced().getRootElement(), Event.MOUSEOUT, panelAttributes,
            PanelAttributes.onmouseout);
        testFireEventWithJS(panelWithoutHeader.advanced().getRootElement(), Event.MOUSEOUT, panelAttributes,
            PanelAttributes.onmouseout);
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        // TODO 2013-02-01 JJa: try implement using WebDriver API (doesn't work for now)
        testFireEventWithJS(panelWithHeader.advanced().getRootElement(), Event.MOUSEOVER, panelAttributes,
            PanelAttributes.onmouseover);
        testFireEventWithJS(panelWithoutHeader.advanced().getRootElement(), Event.MOUSEOVER, panelAttributes,
            PanelAttributes.onmouseover);
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEventWithJS(panelWithHeader.advanced().getRootElement(), panelAttributes, PanelAttributes.onmouseup);
        testFireEventWithJS(panelWithoutHeader.advanced().getRootElement(), panelAttributes, PanelAttributes.onmouseup);
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        panelAttributes.set(PanelAttributes.rendered, Boolean.FALSE);

        assertNotVisible(panelWithHeader, "First panel should not be rendered when rendered=false.");
        assertNotVisible(panelWithoutHeader, "Second panel should not be rendered when rendered=false.");
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(panelWithHeader.advanced().getRootElement());
        testStyle(panelWithoutHeader.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(panelWithHeader.advanced().getRootElement());
        testStyleClass(panelWithoutHeader.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("title")
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(panelWithHeader.advanced().getRootElement());
        testTitle(panelWithoutHeader.advanced().getRootElement());
    }
}
