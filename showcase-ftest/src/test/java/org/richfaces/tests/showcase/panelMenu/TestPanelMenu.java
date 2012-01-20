/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.panelMenu;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestPanelMenu extends AbstractAjocadoTest {

    /* **************************************************************************
     * Constants**************************************************************************
     */

    private final String FIRST_LVL_GR = "form#form div.rf-pm-top-gr";
    private final String FIRST_LVL_GR_HDR = " > div[id$=hdr]";
    private final String FIRST_LVL_GR_CNT = " > div[id$=cnt]";
    private final String SECOND_LVL_ITEMS = " > div.rf-pm-itm";
    private final String CURRENT_SELECTION_INFO = ".rf-p";

    /* ****************************************************************************
     * Tests, relies on particular structure of panel menu, dive only to the second level of that structure
     * ****************************************************************************
     */

    @Test
    public void testSelection() {

        JQueryLocator firstLvlGroups = jq(FIRST_LVL_GR);

        int numberOfFirstLvlFolders = selenium.getCount(firstLvlGroups);

        for (int i = 0; i < numberOfFirstLvlFolders; i++) {

            String firstLvlLocator = FIRST_LVL_GR + ":eq(" + i + ")";

            guardXhr(selenium).click(jq(firstLvlLocator + FIRST_LVL_GR_HDR));

            JQueryLocator secondLvlItems = jq(firstLvlLocator + FIRST_LVL_GR_CNT + SECOND_LVL_ITEMS);

            int numberOfSecondLvlItems = selenium.getCount(secondLvlItems);

            for (int j = 0; j < numberOfSecondLvlItems; j++) {

                JQueryLocator item = jq(firstLvlLocator + FIRST_LVL_GR_CNT + " > div.rf-pm-itm" + ":eq(" + j + ")");

                guardXhr(selenium).click(item);

                String itemFromMenu = getAsStringInCurrentSelectionInfo(selenium.getText(item));
                String itemFromCurrentPanelInfo = selenium.getText(jq(CURRENT_SELECTION_INFO));

                assertEquals(itemFromCurrentPanelInfo, itemFromMenu, "The current selection info does not work");
            }
        }

    }

    /* ******************************************************************************************
     * Help methods******************************************************************************************
     */

    /**
     * Formats the line parameter to fulfill the format 'Item_number_number selected'
     */
    private String getAsStringInCurrentSelectionInfo(String line) {

        String ret = line.replaceAll(" ", "_");
        ret = ret.replace(".", "_");
        ret += " selected";

        return ret;
    }

}
