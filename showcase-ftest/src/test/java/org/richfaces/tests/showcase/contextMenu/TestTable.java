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
package org.richfaces.tests.showcase.contextMenu;

import static org.jboss.arquillian.ajocado.Ajocado.elementVisible;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Iterator;

import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestTable extends AbstractContextMenuTest {

    /* ***************************************************
     * Locators***************************************************
     */
    private JQueryLocator contextMenuItem = jq(contextMenu.getRawLocator() + " .rf-ctx-itm-lbl");

    private JQueryLocator pricesTds = jq(".rf-edt-b .rf-edt-tbl tbody tr > td:eq(1) tr");
    private JQueryLocator selectedTr = jq(".rf-edt-r-sel.rf-edt-r-act");

    private JQueryLocator vendorInputPopup = jq("input[type=text]:eq(0)");
    private JQueryLocator modelInputPopup = jq("input[type=text]:eq(1)");
    private JQueryLocator priceInputPopup = jq("input[type=text]:eq(2)");
    private JQueryLocator closeButtonPopup = jq("input[type=button]");

    /* *********************************************
     * Tests*********************************************
     */
    @Test
    public void testViewBackingDataByContextMenu() {

        for (Iterator<JQueryLocator> i = pricesTds.iterator(); i.hasNext();) {
            JQueryLocator priceTd = i.next();
            selenium.click(priceTd);

            int priceFromTable = Integer.valueOf(selenium.getText(priceTd).trim());
            String venderAndModelFromTable = selenium.getText(selectedTr);

            selenium.contextMenuAt(priceTd, new Point(3, 3));
            waitGui.failWith(new RuntimeException("The context menu should be visible")).timeout(2000)
                .until(elementVisible.locator(contextMenu));

            selenium.click(contextMenuItem);

            waitGui.failWith(new RuntimeException("The popup triggered from context menu is not visible within 3 sec"))
                .timeout(3000).until(elementVisible.locator(vendorInputPopup));

            int priceFromPopup = Integer.valueOf(selenium.getValue(priceInputPopup));
            String vendorFromPopup = selenium.getValue(vendorInputPopup);
            String modelFromPopup = selenium.getValue(modelInputPopup);

            assertEquals(priceFromPopup, priceFromTable,
                "The price from popup should be the same as in the table for particular selected row.");
            assertTrue(venderAndModelFromTable.contains(vendorFromPopup),
                "The popup sould contains vendor name from selected table row!");
            assertTrue(venderAndModelFromTable.contains(modelFromPopup),
                "The popup sould contains model name from selected table row!");

            selenium.click(closeButtonPopup);
        }
    }

    @Test
    public void testContextMenuRenderedAtCorrectPosition() {

        checkContextMenuRenderedAtCorrectPosition( jq(pricesTds.getRawLocator() + ":eq(0)"), new Point(3,3), true );
    }
}
