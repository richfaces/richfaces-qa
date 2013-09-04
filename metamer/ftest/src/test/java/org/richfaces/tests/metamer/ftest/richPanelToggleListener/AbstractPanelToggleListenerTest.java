/**
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
 */
package org.richfaces.tests.metamer.ftest.richPanelToggleListener;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;
import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractPanelToggleListenerTest extends AbstractWebDriverTest {

    @Page
    private PTLCollapsiblePanelPage page;

    private final String collapsedPanelString = " collapsed";
    private final String expandedPanelString = " expanded";
    private final String testedComponent;

    public AbstractPanelToggleListenerTest(String testedComponent) {
        this.testedComponent = testedComponent;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelToggleListener/" + testedComponent + ".xhtml");
    }

    private void testPTL(final String expectedText) {
        //first test collapsing of panel
        MetamerPage.waitRequest(page.getCollapseButton(), WaitRequestType.XHR).click();
        //checks if phases contains the correct listener message
        page.assertListener(PhaseId.INVOKE_APPLICATION, expectedText + collapsedPanelString);
        //then test expanding of panel
        MetamerPage.waitRequest(page.getExpandButton(), WaitRequestType.XHR).click();
        //checks if phases contains the correct listener message
        page.assertListener(PhaseId.INVOKE_APPLICATION, expectedText + expandedPanelString);
    }

    public void testPTLAsAttributeOfComponent(String expectedMSG) {
        testPTL(expectedMSG);
    }

    public void testPTLInComponentWithType(String expectedMSG) {
        testPTL(expectedMSG);
    }

    public void testPTLInComponentWithListener(String expectedMSG) {
        testPTL(expectedMSG);
    }

    public void testPTLAsForAttributeWithType(String expectedMSG) {
        testPTL(expectedMSG);
    }
}
