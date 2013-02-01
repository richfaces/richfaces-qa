/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.richPanel;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.bodyClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerClass;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.panelAttributes;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.test.selenium.support.ui.ElementNotPresent;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richPanel/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 22733 $
 */
public class TestRichPanel extends AbstractWebDriverTest {

    @Page
    private PanelPage page;

    private ElementNotPresent elemNotPresent = ElementNotPresent.getInstance();
    private ElementPresent elemPresent = ElementPresent.getInstance();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanel/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(elemPresent.element(page.panelWithHeader).apply(driver), "Panel with header should be present and visible on the page.");
        assertTrue(elemPresent.element(page.panelWithoutHeader).apply(driver), "Panel without header should be present and visible on the page.");

        assertTrue(elemPresent.element(page.headersWithHeader).apply(driver), "The first panel should have a header.");
        assertTrue(elemNotPresent.element(page.headersWithoutHeader).apply(driver), "The second panel should not have any header.");

        assertTrue(elemPresent.element(page.bodiesWithHeader).apply(driver), "The first panel should have a body.");
        assertTrue(elemPresent.element(page.bodiesWithoutHeader).apply(driver), "The second panel should have a body.");

        assertTrue(page.headersWithHeader.getText().endsWith("header of panel"));
        assertTrue(page.bodiesWithHeader.getText().startsWith("Lorem ipsum"), "First panel's body should start with \"Lorem ipsum\".");
        assertTrue(page.bodiesWithoutHeader.getText().startsWith("Nulla ornare"), "Second panel's body should start with \"Nulla ornare\".");
    }

    @Test
    public void testBodyClass() {
        testStyleClass(page.bodiesWithHeader, bodyClass);
        testStyleClass(page.bodiesWithoutHeader, bodyClass);
    }

    @Test
    public void testHeader() {
        panelAttributes.set(PanelAttributes.header, "new header");

        assertTrue(page.headersWithHeader.getText().equals("header of panel"), "Header of the first panel should not change (facet defined).");
        assertTrue(page.headersWithoutHeader.getText().equals("new header"), "Header of the second panel.");
    }

    @Test
    public void testHeaderClass() {
        testStyleClass(page.headersWithHeader, headerClass);
    }

    @Test
    public void testOnclick() {
        Action clickAction = new Actions(driver).click(page.panelWithHeader).build();
        testFireEvent(panelAttributes, PanelAttributes.onclick, clickAction);

        clickAction = new Actions(driver).click(page.panelWithoutHeader).build();
        testFireEvent(panelAttributes, PanelAttributes.onclick, clickAction);
    }

    @Test
    public void testOndblclick() {
        Action dblClickAction = new Actions(driver).doubleClick(page.panelWithHeader).build();
        testFireEvent(panelAttributes, PanelAttributes.ondblclick, dblClickAction);

        dblClickAction = new Actions(driver).doubleClick(page.panelWithoutHeader).build();
        testFireEvent(panelAttributes, PanelAttributes.ondblclick, dblClickAction);
    }

    @Test
    public void testOnkeydown() {
        testFireEventWithJS(page.panelWithHeader, Event.KEYDOWN, panelAttributes, PanelAttributes.onkeydown);
        testFireEventWithJS(page.panelWithoutHeader, Event.KEYDOWN, panelAttributes, PanelAttributes.onkeydown);
    }

    @Test
    public void testOnkeypress() {
        testFireEventWithJS(page.panelWithHeader, Event.KEYPRESS, panelAttributes, PanelAttributes.onkeypress);
        testFireEventWithJS(page.panelWithoutHeader, Event.KEYPRESS, panelAttributes, PanelAttributes.onkeypress);
    }

    @Test
    public void testOnkeyup() {
        testFireEventWithJS(page.panelWithHeader, Event.KEYUP, panelAttributes, PanelAttributes.onkeyup);
        testFireEventWithJS(page.panelWithoutHeader, Event.KEYUP, panelAttributes, PanelAttributes.onkeyup);
    }

    @Test
    public void testOnmousedown() {
        Action mouseDownAction = new Actions(driver).moveToElement(page.panelWithHeader).clickAndHold(page.panelWithHeader).build();
        testFireEvent(panelAttributes, PanelAttributes.onmousedown, mouseDownAction);

        mouseDownAction = new Actions(driver).moveToElement(page.panelWithoutHeader).clickAndHold(page.panelWithHeader).build();
        testFireEvent(panelAttributes, PanelAttributes.onmousedown, mouseDownAction);
    }

    @Test
    public void testOnmousemove() {
        Action mouseMoveAction = new Actions(driver).moveToElement(page.panelWithHeader, 3, 3).build();
        testFireEvent(panelAttributes, PanelAttributes.onmousemove, mouseMoveAction);

        mouseMoveAction = new Actions(driver).moveToElement(page.panelWithoutHeader, 3, 3).build();
        testFireEvent(panelAttributes, PanelAttributes.onmousemove, mouseMoveAction);
    }

    @Test
    public void testOnmouseout() {
        // TODO 2013-02-01 JJa: try implement using WebDriver API (doesn't work for now)
        testFireEventWithJS(page.panelWithHeader, Event.MOUSEOUT, panelAttributes, PanelAttributes.onmouseout);
        testFireEventWithJS(page.panelWithoutHeader, Event.MOUSEOUT, panelAttributes, PanelAttributes.onmouseout);
    }

    @Test
    public void testOnmouseover() {
        // TODO 2013-02-01 JJa: try implement using WebDriver API (doesn't work for now)
        testFireEventWithJS(page.panelWithHeader, Event.MOUSEOVER, panelAttributes, PanelAttributes.onmouseover);
        testFireEventWithJS(page.panelWithoutHeader, Event.MOUSEOVER, panelAttributes, PanelAttributes.onmouseover);
    }

    @Test
    public void testOnmouseup() {
        Action mouseUpAction = new Actions(driver).moveToElement(page.panelWithHeader).clickAndHold().release().build();
        testFireEvent(panelAttributes, PanelAttributes.onmouseup, mouseUpAction);

        mouseUpAction = new Actions(driver).moveToElement(page.panelWithoutHeader).clickAndHold().release().build();
        testFireEvent(panelAttributes, PanelAttributes.onmouseup, mouseUpAction);
    }

    @Test
    public void testRendered() {
        panelAttributes.set(PanelAttributes.rendered, Boolean.FALSE);

        assertTrue(elemNotPresent.element(page.panelWithHeader).apply(driver),
            "First panel should not be rendered when rendered=false.");
        assertTrue(elemNotPresent.element(page.panelWithoutHeader).apply(driver),
            "Second panel should not be rendered when rendered=false.");
    }

    @Test
    public void testStyle() {
        testStyle(page.panelWithHeader);
        testStyle(page.panelWithoutHeader);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(page.panelWithHeader);
        testStyleClass(page.panelWithoutHeader);
    }

    @Test
    public void testTitle() {
        testTitle(page.panelWithHeader);
        testTitle(page.panelWithoutHeader);
    }
}
