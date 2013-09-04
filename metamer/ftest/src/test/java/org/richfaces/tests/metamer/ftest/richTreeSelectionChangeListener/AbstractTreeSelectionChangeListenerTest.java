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
package org.richfaces.tests.metamer.ftest.richTreeSelectionChangeListener;

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
public abstract class AbstractTreeSelectionChangeListenerTest extends AbstractWebDriverTest {

    @Page
    private TSCLPage page;

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

    private void testTSCL(String expectedText) {
        //selects a first node
        MetamerPage.waitRequest(page.getItem(1), WaitRequestType.XHR).click();
        //checks if phases contains the correct listener message
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, expectedText + nullToFirst);
        //then selectis a second node
        MetamerPage.waitRequest(page.getItem(2), WaitRequestType.XHR).click();
        //checks if phases contains the correct listener message
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, expectedText + firstToSecond);
    }

    private void testTSCLWithoutAdditionalStrings(String expectedText) {
        //selects a first node
        MetamerPage.waitRequest(page.getItem(1), WaitRequestType.XHR).click();
        //checks if phases contains the correct listener message
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, expectedText);
        //then test selecting a second node
        MetamerPage.waitRequest(page.getItem(2), WaitRequestType.XHR).click();
        //checks if phases contains the correct listener message
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, expectedText);
    }

    public void testTSCLAsAttributeOfComponent(String expectedMSG) {
        testTSCLWithoutAdditionalStrings(expectedMSG);
    }

    public void testTSCLInComponentWithType(String expectedMSG) {
        testTSCLWithoutAdditionalStrings(expectedMSG);
    }

    public void testTSCLInComponentWithListener(String expectedMSG) {
        testTSCLWithoutAdditionalStrings(expectedMSG);
    }

    public void testTSCLAsForAttributeWithType(String expectedMSG) {
        testTSCLWithoutAdditionalStrings(expectedMSG);
    }
}
