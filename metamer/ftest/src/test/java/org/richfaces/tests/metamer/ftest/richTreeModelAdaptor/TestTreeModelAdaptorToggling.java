/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.richTreeModelAdaptor;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.richTree.TestTreeToggling;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class TestTreeModelAdaptorToggling extends TestTreeToggling {

    @FindByJQuery(":radio[id*=recursiveModelRepresentation]")
    private List<WebElement> recursiveModelRepresentations;
    @FindByJQuery(":checkbox[id$=recursiveLeafChildrenNullable]")
    private WebElement recursiveLeafChildrenNullableElement;

    @UseForAllTests(valuesFrom = FROM_ENUM, value = "")
    private RecursiveModelRepresentation representation;

    @UseForAllTests(valuesFrom = FROM_FIELD, value = "booleans")
    private Boolean recursiveLeafChildrenNullable;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTree/treeAdaptors.xhtml");
    }

    @BeforeMethod
    public void initPathsAndModelRepresentation() {
        if (representation == RecursiveModelRepresentation.MAP) {
            MetamerPage.requestTimeChangesWaiting(recursiveModelRepresentations.get(1)).click();
        }
        if (recursiveLeafChildrenNullable) {
            MetamerPage.requestTimeChangesWaiting(recursiveLeafChildrenNullableElement).click();
        }
    }

    @BeforeClass
    public void setupTreeModelTesting() {
        paths = new Integer[][]{ { 2, 1, 0, 1 }, { 1, 3, 5 } };
    }

    @Test
    @Override
    public void testDeepCollapsion() {
        super.testDeepCollapsion();
    }

    @Test
    @Override
    public void testDeepExpansion() {
        super.testDeepExpansion();
    }

    @Test
    @Override
    public void testTopLevelNodesCollapsion() {
        super.testTopLevelNodesCollapsion();
    }

    @Test
    @Override
    public void testTopLevelNodesExpansion() {
        super.testTopLevelNodesExpansion();
    }
}
