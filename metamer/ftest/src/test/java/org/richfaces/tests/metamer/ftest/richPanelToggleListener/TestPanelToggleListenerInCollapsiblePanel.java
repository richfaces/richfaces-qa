/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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

import static java.text.MessageFormat.format;

import static org.richfaces.tests.metamer.bean.rich.PanelToggleListenerBeanUsingListener.COLLAPSED;
import static org.richfaces.tests.metamer.bean.rich.PanelToggleListenerBeanUsingListener.EXPANDED;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.collapsiblePanel.TextualRichFacesCollapsiblePanel;
import org.richfaces.tests.metamer.bean.rich.PanelToggleListenerBeanUsingFor;
import org.richfaces.tests.metamer.bean.rich.PanelToggleListenerBeanUsingListener;
import org.richfaces.tests.metamer.bean.rich.PanelToggleListenerBeanUsingType;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPanelToggleListenerInCollapsiblePanel extends AbstractWebDriverTest {

    @FindBy(css = ".rf-cp[id$='collapsiblePanel']")
    private TextualRichFacesCollapsiblePanel panel;

    @Override
    public String getComponentTestPagePath() {
        return "richPanelToggleListener/collapsiblePanel.xhtml";
    }

    private void testPTL(final String expectedTextTemplate) {
        //first test collapsing of panel
        Graphene.guardAjax(panel).collapse();
        //checks if phases contains the correct listener message
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, format(expectedTextTemplate, COLLAPSED));
        //then test expanding of panel
        Graphene.guardAjax(panel).expand();
        //checks if phases contains the correct listener message
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, format(expectedTextTemplate, EXPANDED));
    }

    @Test(groups = "smoke")
    @RegressionTest({ "https://issues.jboss.org/browse/RF-11568" })
    public void testPTLAsAttribute() {
        testPTL("panel {0}");
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-12105")
    public void testPTLInsideComponentUsingListener() {
        testPTL(PanelToggleListenerBeanUsingListener.MSG_TEMPLATE);
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-12280" })
    public void testPTLInsideComponentUsingType() {
        testPTL(PanelToggleListenerBeanUsingType.MSG_TEMPLATE);
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-12106")
    public void testPTLOutsideComponentUsingFor() {
        testPTL(PanelToggleListenerBeanUsingFor.MSG_TEMPLATE);
    }
}
