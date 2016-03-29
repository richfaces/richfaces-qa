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
package org.richfaces.tests.metamer.ftest.richTogglePanel;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestTogglePanelRF12977 extends AbstractWebDriverTest {

    @FindBy(css = "div[id$=item1]")
    private GrapheneElement item1;
    @FindBy(css = "div[id$=item2]")
    private GrapheneElement item2;
    @FindBy(css = "div[id$=item3]")
    private GrapheneElement item3;

    @FindBy(css = "a[id$=tcLink1]")
    private GrapheneElement tc1;
    @FindBy(css = "a[id$=tcLink2]")
    private GrapheneElement tc2;
    @FindBy(css = "a[id$=tcLink3]")
    private GrapheneElement tc3;

    @Override
    public String getComponentTestPagePath() {
        return "richTogglePanel/rf-12977.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12977")
    public void testComponentCanBeSwitchedWhenUsingGlobalQueue() {
        Graphene.guardAjax(tc2).click();
        Graphene.waitAjax().until().element(item2).is().visible();

        Graphene.guardAjax(tc1).click();
        Graphene.waitAjax().until().element(item1).is().visible();

        Graphene.guardAjax(tc3).click();
        Graphene.waitAjax().until().element(item3).is().visible();
    }
}
