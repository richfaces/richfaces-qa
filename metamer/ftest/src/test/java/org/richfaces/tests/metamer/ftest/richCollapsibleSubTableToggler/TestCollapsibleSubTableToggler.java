/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.richfaces.tests.metamer.ftest.richCollapsibleSubTableToggler.CollapsibleSubTableTogglerAttributes.rendered;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.richfaces.fragment.collapsibleSubTableToggler.RichFacesCollapsibleSubTableToggler;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.richCollapsibleSubTable.AbstractCollapsibleSubTableTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCollapsibleSubTableToggler extends AbstractCollapsibleSubTableTest {

    private static final String IMAGE_URL = "/resources/images/star.png";
    private static final String LABEL = "Label";
    private final Attributes<CollapsibleSubTableTogglerAttributes> attributes = getAttributes();

    private Event event = Event.CLICK;
    private Event[] events = new Event[]{ Event.DBLCLICK, Event.CLICK };

    private Boolean toggleByImage = Boolean.TRUE;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsibleSubTableToggler/simple.xhtml");
    }

    @Test
    @Templates("plain")
    public void testCollapsedIcon() {
        attributes.set(CollapsibleSubTableTogglerAttributes.collapsedIcon, IMAGE_URL);

        new TogglerTester() {

            private String expandedImageUrl;

            @Override
            public void verifyBeforeCollapsion() {
                assertTrue(toggler.isExpanded());
                expandedImageUrl = toggler.getVisibleImage().getAttribute("src");
            }

            @Override
            public void verifyAfterCollapsion() {
                assertFalse(toggler.isExpanded());
                assertTrue(toggler.getVisibleImage().getAttribute("src").contains(IMAGE_URL));
            }

            @Override
            public void verifyAfterExpansion() {
                assertTrue(toggler.isExpanded());
                assertEquals(toggler.getVisibleImage().getAttribute("src"), expandedImageUrl);
            }
        }.test();
    }

    @Test
    @Templates("plain")
    public void testCollapsedLabel() {
        attributes.set(CollapsibleSubTableTogglerAttributes.collapsedIcon, "none");
        attributes.set(CollapsibleSubTableTogglerAttributes.collapsedLabel, LABEL);

        new TogglerTester() {
            private String expandedImageUrl;

            @Override
            public void verifyBeforeCollapsion() {
                assertTrue(toggler.isExpanded());
                assertFalse(Utils.isVisible(toggler.getVisibleLabel()));
                expandedImageUrl = toggler.getVisibleImage().getAttribute("src");

            }

            @Override
            public void verifyAfterCollapsion() {
                assertFalse(toggler.isExpanded());
                assertEquals(toggler.getVisibleLabel().getText(), LABEL);
            }

            @Override
            public void verifyAfterExpansion() {
                assertTrue(toggler.isExpanded());
                assertFalse(Utils.isVisible(toggler.getVisibleLabel()));
                assertEquals(toggler.getVisibleImage().getAttribute("src"), expandedImageUrl);
            }
        }.test();
    }

    @Test
    @Templates("plain")
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "events")
    public void testEvent() {
        attributes.set(CollapsibleSubTableTogglerAttributes.event, event);
        new TogglerTester().test();
    }

    @Test
    @Templates("plain")
    public void testExpandedIcon() {
        attributes.set(CollapsibleSubTableTogglerAttributes.expandedIcon, IMAGE_URL);

        new TogglerTester() {

            @Override
            public void verifyBeforeCollapsion() {
                assertTrue(toggler.isExpanded());
                assertTrue(toggler.getVisibleImage().getAttribute("src").contains(IMAGE_URL));
            }

            @Override
            public void verifyAfterCollapsion() {
                assertFalse(toggler.isExpanded());
            }

            @Override
            public void verifyAfterExpansion() {
                assertTrue(toggler.isExpanded());
                assertTrue(toggler.getVisibleImage().getAttribute("src").contains(IMAGE_URL));
            }
        }.test();
    }

    @Test
    @Templates("plain")
    public void testExpandedLabel() {
        attributes.set(CollapsibleSubTableTogglerAttributes.expandedLabel, LABEL);

        new TogglerTester() {

            @Override
            public void verifyBeforeCollapsion() {
                assertTrue(toggler.isExpanded());
                assertEquals(toggler.getVisibleLabel().getText(), LABEL);
            }

            @Override
            public void verifyAfterCollapsion() {
                assertFalse(toggler.isExpanded());
                assertFalse(Utils.isVisible(toggler.getVisibleLabel()));
            }

            @Override
            public void verifyAfterExpansion() {
                assertTrue(toggler.isExpanded());
                assertEquals(toggler.getVisibleLabel().getText(), LABEL);
            }
        }.test();
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        attributes.set(rendered, Boolean.FALSE);
        assertFalse(getSubTable(Boolean.TRUE).advanced().getTableToggler().isVisible());
        assertFalse(getSubTable(Boolean.FALSE).advanced().getTableToggler().isVisible());
        attributes.set(rendered, Boolean.TRUE);
        assertTrue(getSubTable(Boolean.TRUE).advanced().getTableToggler().isVisible());
        assertTrue(getSubTable(Boolean.FALSE).advanced().getTableToggler().isVisible());
    }

    private class TogglerTester {

        private CollapsibleSubTableWithEmployees subtable;
        protected RichFacesCollapsibleSubTableToggler toggler;

        public TogglerTester() {
        }

        public void test() {
            for (int i = 1; i <= 2; i++) {
                subtable = getSubTable(i == 1);

                toggler = subtable.advanced().getTableToggler();
                toggler.setToggleEvent(event);
                if (toggleByImage) {
                    toggler.setToggleByImage();
                } else {
                    toggler.setToggleByLabel();
                }
                assertTrue(subtable.advanced().isExpanded(), "Table should be expanded.");

                verifyBeforeCollapsion();

                toggler.toggle();
                subtable.advanced().waitUntilTableCollapses().perform();

                verifyAfterCollapsion();

                toggler.toggle();
                subtable.advanced().waitUntilTableExpands().perform();

                verifyAfterExpansion();
            }
        }

        protected void verifyAfterCollapsion() {
        }

        protected void verifyAfterExpansion() {
        }

        protected void verifyBeforeCollapsion() {
        }
    }
}
