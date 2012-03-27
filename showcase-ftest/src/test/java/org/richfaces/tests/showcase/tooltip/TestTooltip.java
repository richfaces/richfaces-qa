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
package org.richfaces.tests.showcase.tooltip;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestTooltip extends AbstractAjocadoTest {

    /* ************************************************************************************
     * Constants***********************************************************************************
     */

    private final String TOOLTIP_TEXT_AJAX = "This tool-tip content was rendered on server\n"
        + " tooltips requested: 0";

    /* *******************************************************************************
     * Locators ****************************************************************** *************
     */

    // private JQueryLocator clientTooltipActivatingArea = jq("div[class*='rf-p tooltip-text']:eq(0)");
    // private JQueryLocator clientWithDelayTooltipActivatingArea = jq("div[class*='rf-p tooltip-text']:eq(1)");
    // private JQueryLocator ajaxTooltipActivatingArea = jq("div[id$=sample3]");
    private JQueryLocator ajaxClickTooltipActivatingArea = jq("div[class*='rf-p tooltip-text']:eq(3)");

    private JQueryLocator tooltip = jq("div[class*='rf-tt tooltip']:visible");

    /* ***************************************************************************************************
     * Tests, other tooltips should be tested as well, but they are not called when the mouseover is fired, should be
     * investigated later on *********************************************************************
     * ******************************
     */

    // @Test
    public void testClientTooltip() {

    }

    // @Test
    public void testClientWithDelayTooltip() {

    }

    // @Test
    public void testAjaxTooltip() {

    }

    @Test
    public void testAjaxClickTooltip() {

        guardXhr(selenium).clickAt(ajaxClickTooltipActivatingArea, new Point(5, 5));

        String actualTooltipText = selenium.getText(tooltip);
        assertEquals(actualTooltipText, TOOLTIP_TEXT_AJAX, "The tool tip text is different!");
    }

}
