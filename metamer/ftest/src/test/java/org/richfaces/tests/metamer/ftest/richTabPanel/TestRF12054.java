/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richTabPanel;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.tabPanel.RichFacesTabPanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12054 extends AbstractWebDriverTest {

    @FindBy(css = "[id$='innerIvoked']")
    private WebElement innerIvoked;
    @FindBy(css = "[id$='innerTabPanel']")
    private RichFacesTabPanel innerTabPanel;
    @FindBy(css = "[id$='outerIvoked']")
    private WebElement outerIvoked;
    @FindBy(css = "[id$='outerTabPanel']")
    private RichFacesTabPanel outerTabPanel;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/rf-12054.xhtml");
    }

    @Test
    @Templates("plain")
    @RegressionTest("https://issues.jboss.org/browse/RF-12054")
    public void testOnitemchangeInInnerTabPanelIsNotInvokedInOuterTabPanel() {
        assertInvocations(0, 0);
        Graphene.guardAjax(outerTabPanel).switchTo(0);
        assertInvocations(1, 0);
        Graphene.guardAjax(outerTabPanel).switchTo(0);
        assertInvocations(2, 0);
        Graphene.guardAjax(outerTabPanel).switchTo(1);
        assertInvocations(3, 0);
        Graphene.guardAjax(innerTabPanel).switchTo(1);
        assertInvocations(3, 1);
        Graphene.guardAjax(innerTabPanel).switchTo(1);
        assertInvocations(3, 2);
        Graphene.guardAjax(innerTabPanel).switchTo(0);
        assertInvocations(3, 3);
        Graphene.guardAjax(outerTabPanel).switchTo(1);
        assertInvocations(4, 3);
    }

    private void assertInvocations(int outer, int inner) {
        assertEquals(Integer.parseInt(outerIvoked.getAttribute("value")), outer, "Invocations of outer tab panel @onitemchange event");
        assertEquals(Integer.parseInt(innerIvoked.getAttribute("value")), inner, "Invocations of inner tab panel @onitemchange event");
    }

}
