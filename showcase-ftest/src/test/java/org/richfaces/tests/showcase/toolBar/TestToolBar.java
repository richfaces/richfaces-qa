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
package org.richfaces.tests.showcase.toolBar;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestToolBar extends AbstractAjocadoTest {

    /* **********************************************************************
     * Constants **********************************************************************
     */

    private final String CONTEXT = "/showcase/images/icons";

    private final String[] EXPECTED_ELEMENTS = { CONTEXT + "/create_doc.gif", CONTEXT + "/create_folder.gif",
        CONTEXT + "/copy.gif", CONTEXT + "/save.gif", CONTEXT + "/save_as.gif", CONTEXT + "/save_all.gif" };

    /* *********************************************************************
     * Locators *********************************************************************
     */

    private JQueryLocator separator = jq("div.rf-tb-sep-grid");
    private JQueryLocator searchInput = jq(".barsearch");
    private JQueryLocator searchButton = jq(".barsearchbutton");
    private JQueryLocator toolBar = jq(".rf-tb-cntr td");

    /* *****************************************************************************
     * Fields ******************************************************************** *********
     */

    private Set<String> actualElements = new HashSet<String>();

    /* ********************************************************************************
     * Tests ********************************************************************* ***********
     */

    @Test
    public void testAllExpectedElementsPresention() {

        for (Iterator<JQueryLocator> i = toolBar.iterator(); i.hasNext();) {

            JQueryLocator childrenOfTd = i.next().getChild(jq("img"));

            if (selenium.isElementPresent(childrenOfTd)) {

                actualElements.add(getSrc(selenium.getAttribute(childrenOfTd.getAttribute(Attribute.SRC))));
            }
        }

        checkExpectedSrcs(actualElements);

        assertTrue(selenium.isElementPresent(separator), "The separator element is missing");
        assertTrue(selenium.isElementPresent(searchInput), "The search input is missing");
        assertTrue(selenium.isElementPresent(searchButton), "The search input is missing");

    }

    /* ***********************************************************************************
     * Help methods ************************************************************** *********************
     */

    /**
     * Gets the src part from the line, since there is also some junk information
     */
    private String getSrc(String line) {

        String[] partsOfTheLine = line.split(";");

        return partsOfTheLine[0];
    }

    /**
     * Checks whether particular set contians all expected values
     *
     * @param set
     */
    private void checkExpectedSrcs(Set<String> set) {

        for (int i = 0; i < EXPECTED_ELEMENTS.length; i++) {

            assertTrue(set.contains(EXPECTED_ELEMENTS[i]), "There is missing element " + EXPECTED_ELEMENTS[i]);
        }
    }

}
