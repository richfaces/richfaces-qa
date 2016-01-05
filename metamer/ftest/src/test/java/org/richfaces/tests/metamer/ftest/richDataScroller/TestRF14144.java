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
package org.richfaces.tests.metamer.ftest.richDataScroller;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF14144 extends AbstractWebDriverTest {

    @FindBy(css = "[id$=renderTree] label")
    private WebElement renderTreeTrue;
    @FindBy(css = "[id$=scroller]")
    private WebElement scroller;
    @FindBy(css = "[id$=table]")
    private WebElement table;
    @FindBy(css = "[id$=tree]")
    private WebElement tree;

    @Override
    public String getComponentTestPagePath() {
        return "richDataScroller/rf-14144.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14144")
    public void testDataScrollerCanBeOnTheSamePageAsTree() {
        // check all components except tree are visible
        assertNotVisible(tree, "Tree should not be visible.");
        assertVisible(table, "Table should be visible.");
        assertVisible(scroller, "Scroller should be visible.");
        // enable/render the tree >>> this should cause an exception on the server side (UnsupportedOperationException)
        // and display an error page (Mojarra) or leave the tree and all following components not rendered (MyFaces)
        Graphene.guardHttp(renderTreeTrue).click();
        // check all components are visible (will fail when an error was detected)
        assertVisible(tree, "Tree should be visible.");
        assertVisible(table, "Table should be visible.");
        assertVisible(scroller, "Scroller should be visible.");
    }
}
