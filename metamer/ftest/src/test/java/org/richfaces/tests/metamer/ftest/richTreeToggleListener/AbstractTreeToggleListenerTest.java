/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.richfaces.tests.metamer.ftest.richTreeToggleListener;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractTreeToggleListenerTest extends AbstractWebDriverTest {

    private final String collapsedNodeString = " collapsed";
    private final String expandedNodeString = " expanded";
    private final String testedComponent;
    //TTL = TreeToggleListener
    ITTLPage page;

    public AbstractTreeToggleListenerTest(String testedComponent) {
        this.testedComponent = testedComponent;
    }

    @BeforeMethod
    public void pageLoad() {
        loadPage();
        injectWebElementsToPage(page);
    }

    //this should be used to set a @page parameter to actual one
    public abstract void loadPage();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTreeToggleListener/" + testedComponent + ".xhtml");
    }

    /**
     * Gets list of WebElements and check if some of them has same text as
     * expected in attribute @expectedText.
     *
     * @param expectedText value of the text to be find
     * @return true if text was found or false
     */
    private boolean subTest(String expectedText) {
        List<WebElement> list = page.getPhases();
        for (WebElement webElement : list) {
            if (webElement.getText().equals(expectedText)) {
                return true;//Text found
            }
        }
        return false;
    }

    private void testTTL(String expectedText, String failMessage) {
        //test expanding of node
        page.getExpandButton().click();
        //checks if phases contains the correct listener message
        assertTrue(subTest(expectedText + expandedNodeString), failMessage);
        //then test collapsing of node
        page.getCollapseButton().click();
        assertTrue(subTest(expectedText + collapsedNodeString), failMessage);
    }

    private void testTTLWithoutAdditionalStateStrings(String expectedText, String failMessage) {
        //test expanding of node
        page.getExpandButton().click();
        //checks if phases contains the correct listener message
        assertTrue(subTest(expectedText), failMessage);
        //then test collapsing of node
        page.getCollapseButton().click();
        assertTrue(subTest(expectedText), failMessage);
    }

    public void testTTLAsAttributeOfComponent(String expectedMSG) {
        testTTLWithoutAdditionalStateStrings(expectedMSG,
                "TreeToggleListener as attribute of component " + testedComponent + " does not work.");
    }

    public void testTTLInComponentWithType(String expectedMSG) {
        testTTL(expectedMSG,
                "TreeToggleListener set in attribute @type in component "
                + testedComponent + " does not work.");
    }

    public void testTTLInComponentWithListener(String expectedMSG) {
        testTTL(expectedMSG,
                "TreeToggleListener set in attribute @listener in component "
                + testedComponent + " does not work.");
    }

    public void testTTLAsForAttributeWithType(String expectedMSG) {
        testTTL(expectedMSG,
                "TreeToggleListener set in attribute @type and using attribute @for outside component "
                + testedComponent + " does not work.");
    }
}
