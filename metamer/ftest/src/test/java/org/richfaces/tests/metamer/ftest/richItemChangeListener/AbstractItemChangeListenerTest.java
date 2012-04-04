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
package org.richfaces.tests.metamer.ftest.richItemChangeListener;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.fail;

import java.net.URL;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractItemChangeListenerTest extends AbstractWebDriverTest {

    private final String testedComponent;
    //ICL = ItemChangeListener
    IICLPage page;

    public AbstractItemChangeListenerTest(String testedComponent) {
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
        return buildUrl(contextPath, "faces/components/richItemChangeListener/" + testedComponent + ".xhtml");
    }

    private void testICL(String expectedText, String failMessage) {
        page.getInactivePanel().click();
        List<WebElement> list = page.getPhases();
        for (WebElement webElement : list) {
            if (webElement.getText().equals(expectedText)) {
                return;//Text found
            }
        }
        fail(failMessage);
    }

    public void testICLAsAttributeOfComponent(String expectedMSG) {
        testICL(expectedMSG,
                "ItemChangeListener as attribute of component " + testedComponent + " does not work.");
    }

    public void testICLInComponentWithType(String expectedMSG) {
        testICL(expectedMSG,
                "ItemChangeListener set in attribute @type in component "
                + testedComponent + " does not work.");
    }

    public void testICLInComponentWithListener(String expectedMSG) {
        testICL(expectedMSG,
                "ItemChangeListener set in attribute @listener in component "
                + testedComponent + " does not work.");
    }

    public void testICLAsForAttributeWithType(String expectedMSG) {
        testICL(expectedMSG,
                "ItemChangeListener set in attribute @type and using attribute @for outside component "
                + testedComponent + " does not work.");
    }
}
