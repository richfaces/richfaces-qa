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
package org.richfaces.tests.metamer.ftest.richPickList;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.fragment.pickList.RichFacesPickList;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12098 extends AbstractWebDriverTest {

    @FindBy(css = "[id$=list]")
    private RichFacesPickList list;
    @FindBy(css = "[id$=submitButton]")
    private WebElement submitButton;
    @FindBy(className = "rf-msg")
    RichFacesMessage msg;

    @Override
    public String getComponentTestPagePath() {
        return "richPickList/rf-12098.xhtml";
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-12098")
    public void testListCanHandleItemsWithCommas() {
        String sourceListElementsBefore = list.advanced().getSourceListContentAreaElement().getText();
        String targetListElementsBefore = list.advanced().getTargetListContentAreaElement().getText();

        // pick every second element
        list.addMultiple(ChoicePickerHelper.byIndex().everyNth(2));

        String sourceListElementsBeforeSubmit = list.advanced().getSourceListContentAreaElement().getText();
        String targetListElementsBeforeSubmit = list.advanced().getTargetListContentAreaElement().getText();

        // check some elements were picked
        assertNotEquals(sourceListElementsBeforeSubmit, sourceListElementsBefore);
        assertNotEquals(targetListElementsBeforeSubmit, targetListElementsBefore);

        // submit
        Graphene.guardHttp(submitButton).click();

        // check no error message is displayed
        assertFalse(msg.advanced().isVisible());

        String sourceListElementsAfterSubmit = list.advanced().getSourceListContentAreaElement().getText();
        String targetListElementsAfterSubmit = list.advanced().getTargetListContentAreaElement().getText();
        // check elements stay picked
        assertEquals(sourceListElementsAfterSubmit, sourceListElementsBeforeSubmit);
        assertEquals(targetListElementsAfterSubmit, targetListElementsBeforeSubmit);
    }
}
