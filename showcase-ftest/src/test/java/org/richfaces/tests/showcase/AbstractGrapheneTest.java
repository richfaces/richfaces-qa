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
package org.richfaces.tests.showcase;

import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Iterator;

import org.jboss.arquillian.ajocado.ajaxaware.AjaxAwareInterceptor;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc and Juraj Huska</a>
 * @version $Revision$
 */
public abstract class AbstractGrapheneTest extends AbstractShowcaseTest {

    @Drone
    protected AjaxSelenium selenium;

    @BeforeMethod(groups = { "arquillian" })
    public void loadPage() {

        selenium.getCommandInterceptionProxy().registerInterceptor(new AjaxAwareInterceptor());

        String addition = getAdditionToContextRoot();

        this.contextRoot = getContextRoot();

        selenium.open(URLUtils.buildUrl(contextRoot, addition));
    }

    /* ***********************************************************************************************************************
     * ajocado specific methods **************************************************************************
     * **********************************************
     */

    /**
     * Wait for presention of given element for given timeout
     *
     * @param element the element which should be displayed
     * @param timeout the time for which the presention of element will be checked
     * @return true when elements is found in given timeout, false otherwise
     */
    public boolean waitForElementPresent(JQueryLocator element, long timeout) {

        long end = System.currentTimeMillis() + timeout;

        while (System.currentTimeMillis() < end) {

            if (selenium.isElementPresent(element)) {

                return true;
            }
        }

        return false;
    }

    /**
     * Erases the content of input
     *
     * @param input
     */
    public void eraseInput(JQueryLocator input) {

        selenium.type(input, "");
    }

    /**
     * Fills input with string
     *
     * @param input
     * @param value
     */
    public void fillAnyInput(JQueryLocator input, String value) {

        selenium.type(input, value);
    }

    /**
     * Checks whether there is particular message and checks whether the message is correct
     *
     * @param errorMessageLocator
     * @param errorMessage
     * @param shouldErrorMessagePresented
     */
    public void isThereErrorMessage(JQueryLocator errorMessageLocator, String errorMessage, boolean shouldErrorMessagePresented) {

        if (shouldErrorMessagePresented) {
            assertTrue(selenium.getText(errorMessageLocator).contains(errorMessage), errorMessage + " /// should be presented!");
        } else {
            assertFalse(selenium.isElementPresent(errorMessageLocator), errorMessage + " /// should not be presented!");

        }
    }

    /**
     * Checks whether there is particular message and checks whether the message is correct
     *
     * @param infoMessageLocator
     * @param infoMessage
     * @param shouldBeInfoMessagePresented
     */
    public void isThereInfoMessage(JQueryLocator infoMessageLocator, String infoMessage, boolean shouldBeInfoMessagePresented) {

        isThereErrorMessage(infoMessageLocator, infoMessage, shouldBeInfoMessagePresented);
    }

    /**
     * Fills input with string of length defined via parameter, the string is always the same
     *
     * @param input
     * @param lengthOfString
     */
    public void fillInputWithStringOfLength(JQueryLocator input, int lengthOfString) {

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= lengthOfString; i++) {

            sb.append("x");
        }

        selenium.type(input, sb.toString());
    }

    /**
     * test whether all rows in the table contains empty strings
     *
     * @return true if there is a row in the table with empty string, false otherwise
     */
    public boolean testWhetherTableContainsNonEmptyStrings(JQueryLocator table) {

        JQueryLocator tr = jq(table.getRawLocator() + " > tr");

        for (Iterator<JQueryLocator> i = tr.iterator(); i.hasNext();) {

            boolean result = testWhetherRowContainsNonEmptyStrings(i.next());

            if (result) {

                return true;
            }

        }

        return false;

    }

    protected void checkContextMenuRenderedAtCorrectPosition(JQueryLocator target, Point offset, boolean invokedByRightClick,
        JQueryLocator contextMenu, final int TOLERANCE) {
        if (invokedByRightClick) {
            tryToInvokeContextMenu(target, offset, contextMenu);
        } else {
            selenium.clickAt(target, offset);
        }

        waitGui.failWith(new RuntimeException("The context menu should be visible")).timeout(2000)
            .until(elementVisible.locator(contextMenu));

        Point actualContextMenuPosition = selenium.getElementPosition(contextMenu);
        Point targetPosition = selenium.getElementPosition(target);
        Point expectedContextMenuPosition = targetPosition.add(offset);

        boolean isXInTolerance = (actualContextMenuPosition.getX() > expectedContextMenuPosition.getX() - TOLERANCE)
            && (actualContextMenuPosition.getX() < expectedContextMenuPosition.getX() + TOLERANCE);
        boolean isYInTolerance = (actualContextMenuPosition.getY() > expectedContextMenuPosition.getY() - TOLERANCE)
            && (actualContextMenuPosition.getY() < expectedContextMenuPosition.getY() + TOLERANCE);
        assertTrue(isXInTolerance, "The X coordinate is wrong!");
        assertTrue(isYInTolerance, "The Y coordinate is wrong!");
    }

    protected void tryToInvokeContextMenu(JQueryLocator node, Point point, JQueryLocator contextMenu) {
        for (int i = 0; i < 7; i++) {
            selenium.clickAt(node, point);
            selenium.contextMenuAt(node, point);

            waitGui.dontFail().timeout(1000).until(elementVisible.locator(contextMenu));
            if (selenium.isVisible(contextMenu)) {
                break;
            }
        }
    }

    /* ***************************************************************************************************
     * help methods ************************************************************** *************************************
     */

    /**
     * tests whether the rows tds contains some non empty strings
     *
     * @param row
     * @return true if contains empty strings, false otherwise
     */
    private boolean testWhetherRowContainsNonEmptyStrings(JQueryLocator row) {

        JQueryLocator td = jq(row.getRawLocator() + " > td");

        for (Iterator<JQueryLocator> i = td.iterator(); i.hasNext();) {

            String tdInTable = selenium.getText(i.next()).trim();

            if (tdInTable.isEmpty()) {

                return true;
            }
        }

        return false;
    }
}
