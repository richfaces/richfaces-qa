/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richTreeSelectionChangeListener;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
// FIXME AbstractTreeSelectionChangeListenerTest should not be generic (bug in Graphene)
public abstract class AbstractTreeSelectionChangeListenerTest<P extends TSCLPage> extends AbstractWebDriverTest<P> {

    private final String nullToFirst = " []->[org.richfaces.model.SequenceRowKey[1]]";
    private final String firstToSecond = " [org.richfaces.model.SequenceRowKey[1]]->[org.richfaces.model.SequenceRowKey[2]]";
    private final String testedComponent;

    public AbstractTreeSelectionChangeListenerTest(String testedComponent) {
        this.testedComponent = testedComponent;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTreeSelectionChangeListener/" + testedComponent + ".xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Tree Selection Change Listener", testedComponent);
    }

    /**
     * Gets list of WebElements and check if some of them has same text as
     * expected in attribute @expectedText.
     *
     * @param expectedText value of the text to be find
     * @return true if text was found or false
     */
    private boolean subTest(String expectedText) {
        return page.checkPhasesContainAllOf(expectedText);
    }

    private void testTSCL(String expectedText, String failMessage) {
        //selects a first node
        MetamerPage.waitRequest(page.getItem(1), WaitRequestType.XHR).click();
        //checks if phases contains the correct listener message
        assertTrue(subTest(expectedText + nullToFirst), failMessage);
        //then selectis a second node
        MetamerPage.waitRequest(page.getItem(2), WaitRequestType.XHR).click();
        //checks if phases contains the correct listener message
        assertTrue(subTest(expectedText + firstToSecond), failMessage);
    }

    private void testTSCLWithoutAdditionalStrings(String expectedText, String failMessage) {
        //selects a first node
        MetamerPage.waitRequest(page.getItem(1), WaitRequestType.XHR).click();
        //checks if phases contains the correct listener message
        assertTrue(subTest(expectedText), failMessage);
        //then test selecting a second node
        MetamerPage.waitRequest(page.getItem(2), WaitRequestType.XHR).click();
        //checks if phases contains the correct listener message
        assertTrue(subTest(expectedText), failMessage);
    }

    public void testTSCLAsAttributeOfComponent(String expectedMSG) {
        testTSCLWithoutAdditionalStrings(expectedMSG,
                "TreeToggleListener as attribute of component " + testedComponent + " does not work.");
    }

    public void testTSCLInComponentWithType(String expectedMSG) {
        testTSCL(expectedMSG,
                "TreeToggleListener set in attribute @type in component "
                + testedComponent + " does not work.");
    }

    public void testTSCLInComponentWithListener(String expectedMSG) {
        testTSCL(expectedMSG,
                "TreeToggleListener set in attribute @listener in component "
                + testedComponent + " does not work.");
    }

    public void testTSCLAsForAttributeWithType(String expectedMSG) {
        testTSCL(expectedMSG,
                "TreeToggleListener set in attribute @type and using attribute @for outside component "
                + testedComponent + " does not work.");
    }
}
