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
package org.richfaces.tests.showcase.dropDownMenu;

import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSideMenu extends AbstractGrapheneTest {

    /* ****************************************************************************
     * Locators****************************************************************************
     */

    protected JQueryLocator optionList = jq("td.optionList");
    protected String firstLvlLabels = "div[id$=label]";
    protected String styleOfSecondLvlOptions = "div.rf-ddm-pos";

    /* ****************************************************************************
     * Tests****************************************************************************
     */

    @Test
    public void testShowSubOptions() {

        hoverOverOptionAndCheckTheSubmenu(0);

        hoverOverOptionAndCheckTheSubmenu(1);

        hoverOverOptionAndCheckTheSubmenu(2);

    }

    /* **********************************************************************************************************
     * Help methods
     * **********************************************************************************************************
     */

    private void hoverOverOptionAndCheckTheSubmenu(int numberOfSubmenu) {

        JQueryLocator optionLabel = jq(optionList.getRawLocator() + ":eq(" + numberOfSubmenu + ") " + firstLvlLabels);
        JQueryLocator optionSecondLvlStyle = jq(styleOfSecondLvlOptions + ":eq(" + numberOfSubmenu + ") > div");

        boolean isDisplayedBeforeHovering = selenium.isVisible(optionSecondLvlStyle);
        assertFalse(isDisplayedBeforeHovering, "The second lvl options should not be displayed since the "
            + "was no hovering over " + optionLabel + " yet");

        guardNoRequest(selenium).fireEvent(optionLabel, Event.MOUSEOVER);

        boolean isDisplayedAfterHovering = selenium.isVisible(optionSecondLvlStyle);
        assertTrue(isDisplayedAfterHovering, "The second lvl options should be displayed "
            + "since there was hover over element " + optionLabel);

    }

    protected String getSampleLabel() {
        return "Side Menu";
    }
}
