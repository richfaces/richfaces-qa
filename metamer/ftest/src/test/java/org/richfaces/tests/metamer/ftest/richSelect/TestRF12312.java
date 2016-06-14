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
package org.richfaces.tests.metamer.ftest.richSelect;

import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.select.RichFacesSelect;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12312 extends AbstractWebDriverTest {

    private static final String NULL_STRING = "null";
    private static final List<String> VALUES = Lists.newArrayList("value1", "value2", "value3");

    private String actualSelectedValue = NULL_STRING;

    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindBy(css = "div[id$=select]")
    private RichFacesSelect select;

    private void checkListenerValue(String value) {
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, String.format("value changed: %s -> %s", actualSelectedValue, value));
    }

    private void checkOutputChangesTo(String value) {
        Graphene.waitAjax().until().element(output).text().equalTo(value);
    }

    private void checkSelectedValue(String expectedValue) {
        checkOutputChangesTo(expectedValue);
        checkListenerValue(expectedValue);
        actualSelectedValue = expectedValue;
    }

    @Override
    public String getComponentTestPagePath() {
        return "richSelect/rf-12312.xhtml";
    }

    @BeforeMethod
    private void resetActualSelectedValue() {
        actualSelectedValue = NULL_STRING;
        select.advanced().setOpenByInputClick(false);
    }

    private void selectValuesAtIndexesWithCheck(int... indexes) {
        for (int index : indexes) {
            Graphene.guardAjax(select.openSelect()).select(index);
            checkSelectedValue(VALUES.get(index));
            waitUtilNoTimeoutsArePresent();
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-12312")
    public void testRF12312() {
        selectValuesAtIndexesWithCheck(1, 0, 2, 1, 0);
    }
}
