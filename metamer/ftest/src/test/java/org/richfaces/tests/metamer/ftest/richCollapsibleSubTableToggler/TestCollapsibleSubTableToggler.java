/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTableToggler;

import static org.jboss.arquillian.ajocado.Graphene.elementNotVisible;
import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;

import static org.jboss.arquillian.ajocado.dom.Event.DBLCLICK;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEDOWN;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEUP;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.arquillian.ajocado.dom.Attribute.SRC;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.collapsibleSubTableTogglerAttributes;
import static org.richfaces.tests.metamer.ftest.richCollapsibleSubTableToggler.CollapsibleSubTableTogglerAttributes.collapsedIcon;
import static org.richfaces.tests.metamer.ftest.richCollapsibleSubTableToggler.CollapsibleSubTableTogglerAttributes.collapsedLabel;
import static org.richfaces.tests.metamer.ftest.richCollapsibleSubTableToggler.CollapsibleSubTableTogglerAttributes.expandedIcon;
import static org.richfaces.tests.metamer.ftest.richCollapsibleSubTableToggler.CollapsibleSubTableTogglerAttributes.expandedLabel;
import static org.richfaces.tests.metamer.ftest.richCollapsibleSubTableToggler.CollapsibleSubTableTogglerAttributes.rendered;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.model.CollapsibleSubTable;
import org.richfaces.tests.metamer.ftest.model.CollapsibleSubTableToggler;
import org.richfaces.tests.metamer.ftest.model.DataTable;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22737 $
 */
public class TestCollapsibleSubTableToggler extends AbstractGrapheneTest {

    private static final String IMAGE_URL = "/resources/images/star.png";
    private static final String LABEL = "Label";

    DataTable dataTable = new DataTable(pjq("table.rf-dt[id$=richDataTable]"));

    JQueryLocator link = jq("a");
    JQueryLocator image = jq("img");

    @Inject
    @Use(empty = true)
    Event event;

    Event[] events = new Event[] { DBLCLICK, MOUSEDOWN, MOUSEUP };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsibleSubTableToggler/simple.xhtml");
    }

    @Test
    @Use(field = "event", value = "events")
    public void testEvent() {
        collapsibleSubTableTogglerAttributes.set(CollapsibleSubTableTogglerAttributes.event, Event.DBLCLICK);

        TogglerTester togglerTester = new TogglerTester(image, image);
        togglerTester.event = Event.DBLCLICK;
        togglerTester.testToggler();
    }

    @Test
    public void testRendered() {
        collapsibleSubTableTogglerAttributes.set(rendered, false);

        assertEquals(dataTable.getTogglerCount(), 0);
    }

    @Test
    public void testCollapsedLabel() {
        collapsibleSubTableTogglerAttributes.set(collapsedIcon, "none");
        collapsibleSubTableTogglerAttributes.set(collapsedLabel, LABEL);

        new TogglerTester(image, link) {
            String expandedImageUrl;

            @Override
            public void verifyBefore() {
                assertTrue(selenium.isVisible(togglerExpanded));
                assertFalse(selenium.isVisible(togglerCollapsed));
                expandedImageUrl = selenium.getAttribute(togglerExpanded.getAttribute(SRC));

            }

            @Override
            public void verifyMiddle() {
                assertFalse(selenium.isVisible(togglerExpanded));
                assertTrue(selenium.isVisible(togglerCollapsed));
                assertEquals(selenium.getText(togglerCollapsed), LABEL);
            }

            @Override
            public void verifyAfter() {
                assertTrue(selenium.isVisible(togglerExpanded));
                assertFalse(selenium.isVisible(togglerCollapsed));
                assertEquals(selenium.getAttribute(togglerExpanded.getAttribute(SRC)), expandedImageUrl);
            }
        }.testToggler();
    }

    @Test
    public void testExpandedLabel() {
        collapsibleSubTableTogglerAttributes.set(expandedLabel, LABEL);

        new TogglerTester(link, image) {

            @Override
            public void verifyBefore() {
                assertTrue(selenium.isVisible(togglerExpanded));
                assertFalse(selenium.isVisible(togglerCollapsed));
                assertEquals(selenium.getText(togglerExpanded), LABEL);
            }

            @Override
            public void verifyMiddle() {
                assertFalse(selenium.isVisible(togglerExpanded));
                assertTrue(selenium.isVisible(togglerCollapsed));
            }

            @Override
            public void verifyAfter() {
                assertTrue(selenium.isVisible(togglerExpanded));
                assertFalse(selenium.isVisible(togglerCollapsed));
                assertEquals(selenium.getText(togglerExpanded), LABEL);
            }
        }.testToggler();
    }

    @Test
    public void testExpandedIcon() {
        collapsibleSubTableTogglerAttributes.set(expandedIcon, IMAGE_URL);

        new TogglerTester(image, image) {

            @Override
            public void verifyBefore() {
                assertTrue(selenium.isVisible(togglerExpanded));
                assertFalse(selenium.isVisible(togglerCollapsed));
                assertTrue(selenium.getAttribute(togglerExpanded.getAttribute(SRC)).contains(IMAGE_URL));
            }

            @Override
            public void verifyMiddle() {
                assertFalse(selenium.isVisible(togglerExpanded));
                assertTrue(selenium.isVisible(togglerCollapsed));
            }

            @Override
            public void verifyAfter() {
                assertTrue(selenium.isVisible(togglerExpanded));
                assertFalse(selenium.isVisible(togglerCollapsed));
                assertTrue(selenium.getAttribute(togglerExpanded.getAttribute(SRC)).contains(IMAGE_URL));
            }
        }.testToggler();
    }

    @Test
    public void testCollapsedIcon() {
        collapsibleSubTableTogglerAttributes.set(collapsedIcon, IMAGE_URL);

        new TogglerTester(image, image) {

            String expandedImageUrl;

            @Override
            public void verifyBefore() {
                assertTrue(selenium.isVisible(togglerExpanded));
                assertFalse(selenium.isVisible(togglerCollapsed));
                expandedImageUrl = selenium.getAttribute(togglerExpanded.getAttribute(SRC));
            }

            @Override
            public void verifyMiddle() {
                assertFalse(selenium.isVisible(togglerExpanded));
                assertTrue(selenium.isVisible(togglerCollapsed));
                assertTrue(selenium.getAttribute(togglerCollapsed.getAttribute(SRC)).contains(IMAGE_URL));
            }

            @Override
            public void verifyAfter() {
                assertTrue(selenium.isVisible(togglerExpanded));
                assertFalse(selenium.isVisible(togglerCollapsed));
                assertEquals(selenium.getAttribute(togglerExpanded.getAttribute(SRC)), expandedImageUrl);
            }
        }.testToggler();
    }

    public class TogglerTester {

        Event event = Event.CLICK;

        JQueryLocator expander;
        JQueryLocator collapser;
        CollapsibleSubTableToggler togglerRoot;

        CollapsibleSubTable subtable;
        JQueryLocator togglerExpanded;
        JQueryLocator togglerCollapsed;

        public TogglerTester(JQueryLocator expander, JQueryLocator collapser) {
            this.expander = expander;
            this.collapser = collapser;
        }

        public void testToggler() {
            for (int i = 1; i <= 2; i++) {
                subtable = dataTable.getSubtable(i);
                togglerRoot = dataTable.getToggler(i);
                togglerExpanded = dataTable.getToggler(i).getExpanded().getChild(expander);
                togglerCollapsed = dataTable.getToggler(i).getCollapsed().getChild(collapser);

                verifyBefore();

                if (event == Event.CLICK) {
                    selenium.click(togglerExpanded);
                } else {
                    selenium.fireEvent(togglerRoot, event);
                }
                waitGui.until(elementNotVisible.locator(subtable));

                verifyMiddle();

                if (event == Event.CLICK) {
                    selenium.click(togglerCollapsed);
                } else {
                    selenium.fireEvent(togglerRoot, event);
                }
                waitGui.until(elementVisible.locator(subtable));

                verifyAfter();
            }
        }

        public void verifyBefore() {
        }

        public void verifyMiddle() {
        }

        public void verifyAfter() {
        }
    }
}
