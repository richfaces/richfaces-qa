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
package org.richfaces.tests.metamer.ftest.richTabPanel;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.tabPanel.RichFacesTabPanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.richTab.TabAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF14043 extends AbstractWebDriverTest {

    private final Attributes<TabAttributes> attributes = getAttributes();

    @FindBy(css = "div[id$='tabPanel']")
    private RichFacesTabPanel tabPanel;

    @Override
    public String getComponentTestPagePath() {
        return "richTab/simple.xhtml";
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-14043")
    public void testTabCanBeSwitchedToEvenIfNameIsANumber() {
        // check switching between tabs is possible
        Graphene.guardAjax(tabPanel).switchTo(1);
        Graphene.guardAjax(tabPanel).switchTo(0);

        // now set the name of the first tab to a number
        attributes.set(TabAttributes.name, 1);
        // check switching to a static tab is possible
        Graphene.guardAjax(tabPanel).switchTo(1);
        // now switch to the tested tab (with the number as name)
        // if the tab is not switched to, the TimeoutException will be thrown from the page fragment
        Graphene.guardAjax(tabPanel).switchTo(0);
    }
}
