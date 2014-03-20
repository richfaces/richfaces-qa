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
package org.richfaces.tests.metamer.ftest.richTree;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRowKeyConverter extends AbstractTreeTest {

    @FindBy(className = "rf-tr-nd-exp")
    private List<WebElement> expanded;
    @FindBy(className = "rf-trn-hnd")
    private WebElement toggle;

    public int getNumberOfExpandedNodes() {
        return expanded.size();
    }

    @Test(groups = "smoke")
    @UseWithField(field = "sample", valuesFrom = FROM_FIELD, value = "richFacesTreeNodes")
    public void testToggle() {
        toggle();
        Assert.assertEquals(getNumberOfExpandedNodes(), 1, "Number of expanded nodes after expanding one node doesn't match.");
        toggle();
        Assert.assertEquals(getNumberOfExpandedNodes(), 0, "Number of expanded nodes after collapsing one node doesn't match.");
    }

    public void toggle() {
        final int before = expanded.size();
        toggle.click();
        Graphene.waitAjax().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return expanded.size() != before;
            }
        });
    }
}
