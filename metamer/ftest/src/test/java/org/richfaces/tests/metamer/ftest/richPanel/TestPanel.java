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
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.test.selenium.support.ui.ElementNotPresent;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.page.fragments.impl.panel.RichFacesPanel;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richPanel/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 22733 $
 */
public class TestPanel extends AbstractWebDriverTest {

    @FindBy( css = "div[id$=panelWithHeader]" )
    private RichFacesPanel panelWithHeader;

    @FindBy( css = "div[id$=panelWithoutHeader]" )
    private RichFacesPanel panelWithoutHeader;

    private ElementNotPresent elemNotPresent = ElementNotPresent.getInstance();
    private ElementPresent elemPresent = ElementPresent.getInstance();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanel/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(elemPresent.element(panelWithHeader.getRoot()).apply(driver), "Panel with header should be present and visible on the page.");
        assertTrue(elemPresent.element(panelWithoutHeader.getRoot()).apply(driver), "Panel without header should be present and visible on the page.");

        assertTrue(elemPresent.element(panelWithHeader.getHeader()).apply(driver), "The first panel should have a header.");
        assertTrue(elemNotPresent.element(panelWithoutHeader.getHeader()).apply(driver), "The second panel should not have any header.");

        assertTrue(elemPresent.element(panelWithHeader.getBody()).apply(driver), "The first panel should have a body.");
        assertTrue(elemPresent.element(panelWithoutHeader.getBody()).apply(driver), "The second panel should have a body.");

        assertTrue(panelWithHeader.getHeader().getText().endsWith("header of panel"));
        assertTrue(panelWithHeader.getBody().getText().startsWith("Lorem ipsum"), "First panel's body should start with \"Lorem ipsum\".");
        assertTrue(panelWithoutHeader.getBody().getText().startsWith("Nulla ornare"), "Second panel's body should start with \"Nulla ornare\".");
    }

    @Test
    public void testBodyClass() {
        testStyleClass(panelWithHeader.getBody(), bodyClass);
        testStyleClass(panelWithoutHeader.getBody(), bodyClass);
    }

    @Test
    public void testHeader() {
        panelAttributes.set(PanelAttributes.header, "new header");

        assertTrue(panelWithHeader.getHeader().getText().equals("header of panel"), "Header of the first panel should not change (facet defined).");
        assertTrue(panelWithoutHeader.getHeader().getText().equals("new header"), "Header of the second panel.");
    }

    @Test
    public void testHeaderClass() {
        testStyleClass(panelWithHeader.getHeader(), headerClass);
    }

    @Test
    public void testOnclick() {
        Action clickAction = new Actions(driver).click(panelWithHeader.getRoot()).build();
        testFireEvent(panelAttributes, PanelAttributes.onclick, clickAction);

        clickAction = new Actions(driver).click(panelWithoutHeader.getRoot()).build();
        testFireEvent(panelAttributes, PanelAttributes.onclick, clickAction);
    }

    @Test
    public void testOndblclick() {
        Action dblClickAction = new Actions(driver).doubleClick(panelWithHeader.getRoot()).build();
        testFireEvent(panelAttributes, PanelAttributes.ondblclick, dblClickAction);

        dblClickAction = new Actions(driver).doubleClick(panelWithoutHeader.getRoot()).build();
        testFireEvent(panelAttributes, PanelAttributes.ondblclick, dblClickAction);
    }

    @Test
    public void testOnkeydown() {
        testFireEventWithJS(panelWithHeader.getRoot(), Event.KEYDOWN, panelAttributes, PanelAttributes.onkeydown);
        testFireEventWithJS(panelWithoutHeader.getRoot(), Event.KEYDOWN, panelAttributes, PanelAttributes.onkeydown);
    }

    @Test
    public void testOnkeypress() {
        testFireEventWithJS(panelWithHeader.getRoot(), Event.KEYPRESS, panelAttributes, PanelAttributes.onkeypress);
        testFireEventWithJS(panelWithoutHeader.getRoot(), Event.KEYPRESS, panelAttributes, PanelAttributes.onkeypress);
    }

    @Test
    public void testOnkeyup() {
        testFireEventWithJS(panelWithHeader.getRoot(), Event.KEYUP, panelAttributes, PanelAttributes.onkeyup);
        testFireEventWithJS(panelWithoutHeader.getRoot(), Event.KEYUP, panelAttributes, PanelAttributes.onkeyup);
    }

    @Test
    public void testOnmousedown() {
        Action mouseDownAction = new Actions(driver).moveToElement(panelWithHeader.getRoot()).clickAndHold(panelWithHeader.getRoot()).build();
        testFireEvent(panelAttributes, PanelAttributes.onmousedown, mouseDownAction);

        mouseDownAction = new Actions(driver).moveToElement(panelWithoutHeader.getRoot()).clickAndHold(panelWithHeader.getRoot()).build();
        testFireEvent(panelAttributes, PanelAttributes.onmousedown, mouseDownAction);
    }

    @Test
    public void testOnmousemove() {
        Action mouseMoveAction = new Actions(driver).moveToElement(panelWithHeader.getRoot(), 3, 3).build();
        testFireEvent(panelAttributes, PanelAttributes.onmousemove, mouseMoveAction);

        mouseMoveAction = new Actions(driver).moveToElement(panelWithoutHeader.getRoot(), 3, 3).build();
        testFireEvent(panelAttributes, PanelAttributes.onmousemove, mouseMoveAction);
    }

    @Test
    public void testOnmouseout() {
        // TODO 2013-02-01 JJa: try implement using WebDriver API (doesn't work for now)
        testFireEventWithJS(panelWithHeader.getRoot(), Event.MOUSEOUT, panelAttributes, PanelAttributes.onmouseout);
        testFireEventWithJS(panelWithoutHeader.getRoot(), Event.MOUSEOUT, panelAttributes, PanelAttributes.onmouseout);
    }

    @Test
    public void testOnmouseover() {
        // TODO 2013-02-01 JJa: try implement using WebDriver API (doesn't work for now)
        testFireEventWithJS(panelWithHeader.getRoot(), Event.MOUSEOVER, panelAttributes, PanelAttributes.onmouseover);
        testFireEventWithJS(panelWithoutHeader.getRoot(), Event.MOUSEOVER, panelAttributes, PanelAttributes.onmouseover);
    }

    @Test
    public void testOnmouseup() {
        Action mouseUpAction = new Actions(driver).moveToElement(panelWithHeader.getRoot()).clickAndHold().release().build();
        testFireEvent(panelAttributes, PanelAttributes.onmouseup, mouseUpAction);

        mouseUpAction = new Actions(driver).moveToElement(panelWithoutHeader.getRoot()).clickAndHold().release().build();
        testFireEvent(panelAttributes, PanelAttributes.onmouseup, mouseUpAction);
    }

    @Test
    public void testRendered() {
        panelAttributes.set(PanelAttributes.rendered, Boolean.FALSE);

        assertTrue(elemNotPresent.element(panelWithHeader.getRoot()).apply(driver),
            "First panel should not be rendered when rendered=false.");
        assertTrue(elemNotPresent.element(panelWithoutHeader.getRoot()).apply(driver),
            "Second panel should not be rendered when rendered=false.");
    }

    @Test
    public void testStyle() {
        testStyle(panelWithHeader.getRoot());
        testStyle(panelWithoutHeader.getRoot());
    }

    @Test
    public void testStyleClass() {
        testStyleClass(panelWithHeader.getRoot());
        testStyleClass(panelWithoutHeader.getRoot());
    }

    @Test
    public void testTitle() {
        testTitle(panelWithHeader.getRoot());
        testTitle(panelWithoutHeader.getRoot());
    }
}
