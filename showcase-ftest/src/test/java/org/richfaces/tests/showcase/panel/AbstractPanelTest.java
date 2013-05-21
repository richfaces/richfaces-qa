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
package org.richfaces.tests.showcase.panel;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class AbstractPanelTest extends AbstractGrapheneTest {

    /* *********************************************************************************
     * Constants ***************************************************************** *****************
     */

    protected final String RICH_FACES_INFO = "RichFaces is a component library for "
        + "JSF and an advanced framework for easily integrating AJAX capabilities into "
        + "business applications. 100+ AJAX enabled components in two libraries r: page "
        + "centric AJAX controls r: self contained, ready to use components Whole "
        + "set of JSF benefits while working with AJAX Skinnability mechanism "
        + "Component Development Kit (CDK) Dynamic resources handling Testing "
        + "facilities for components, actions, listeners, and pages Broad cross-browser "
        + "support Large and active community";

    protected final String RICH_FACES_JSF_INFO = "We are working hard on RichFaces 4.0 which will have full JSF 2 integration. "
        + "That is not all though, here is a summary of updates and features:";

    protected final String HEADER = "div[id*=header]";
    protected final String BODY = "div[id*=body]";

    /* **********************************************************************************
     * Help methods ************************************************************** ********************
     */

    /**
     * Checks whether the particular part of panel is not empty and whether contains particular piece of text
     *
     * @param whichPart
     *            the string representation of the JQLocator for panel's body
     * @param expectedContent
     *            the content which should be in the particular part of output panel
     */
    protected void checkContentOfPanel(String whichPart, String expectedContent) {

        JQueryLocator panel = jq(whichPart);

        String actualContent = selenium.getText(panel);

        assertFalse(actualContent.isEmpty(), "The content of " + panel.getRawLocator() + "should not be empty");

        assertTrue(actualContent.contains(expectedContent), "The content is different");
    }

    /**
     * Checks whether the particular part of panel is not empty and whether contains particular piece of text
     *
     * @param whichPart
     *            panel which content will be checked
     * @param expectedContent
     *            the content which should be in the particular part of output panel
     */
    protected void checkContentOfPanel(JQueryLocator whichPart, String expectedContent) {

        String actualContent = selenium.getText(whichPart);

        assertFalse(actualContent.isEmpty(), "The content of " + whichPart.getRawLocator() + "should not be empty");

        assertTrue(actualContent.contains(expectedContent), "The content is different");
    }

}
