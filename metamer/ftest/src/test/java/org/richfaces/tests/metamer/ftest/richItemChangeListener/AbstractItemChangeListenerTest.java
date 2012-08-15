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
package org.richfaces.tests.metamer.ftest.richItemChangeListener;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractItemChangeListenerTest extends AbstractWebDriverTest<ICLPage> {

    private final String testedComponent;

    public AbstractItemChangeListenerTest(String testedComponent) {
        this.testedComponent = testedComponent;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richItemChangeListener/" + testedComponent + ".xhtml");
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

    private void testICL(final String expectedText, String failMessage) {
        waitRequest(page.getInactivePanel(), WaitRequestType.XHR).click();
        assertTrue(subTest(expectedText), failMessage);
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
