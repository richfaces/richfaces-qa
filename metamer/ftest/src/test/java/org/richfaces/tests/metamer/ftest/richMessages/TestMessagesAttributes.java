/*******************************************************************************
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
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.richMessages;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.tests.metamer.Template;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.page.fragments.impl.message.Message;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestMessagesAttributes extends AbstractMessagesTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richMessages/jsr303.xhtml");
    }

    @Test
    public void testAjaxRendered() {
        checkAjaxRendered();
    }

    @Test
    public void testDir() {
        checkDir();
    }

    @Test
    public void testEscape() {
        checkEscape();
    }

    @Test
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat", "hDataTable", "uiRepeat" })
    public void testFor() {
        checkFor(2);//2 messages
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11298")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat", "hDataTable", "uiRepeat" })
    public void testForInIterationComponents() {
        testFor();
    }

    @Test
    @Templates(exclude = { "richAccordion", "richCollapsiblePanel" })
    public void testGlobalOnly() {
        checkGlobalOnly(2);//2 messages
    }

    @Test
    @Templates(value = { "richAccordion", "richCollapsiblePanel" })
    @RegressionTest("https://issues.jboss.org/browse/RF-11415")
    public void testGlobalOnlyInAccordionCollapsiblePanel() {
        testGlobalOnly();
    }

    @Test
    public void testLang() {
        checkLang();
    }

    @Test
    public void testMessagesTypes() {
        checkMessagesTypes();
    }

    @Test
    public void testNoShowDetailNoShowSummary() {
        checkNoShowDetailNoShowSummary();
    }

    @Test
    public void testOnClick() {
        checkOnClick();
    }

    @Test
    public void testOnDblClick() {
        checkOnDblClick();
    }

    @Test
    public void testOnKeyDown() {
        checkOnKeyDown();
    }

    @Test
    public void testOnKeyPress() {
        checkOnKeyPress();
    }

    @Test
    public void testOnKeyUp() {
        checkOnKeyUp();
    }

    @Test
    public void testOnMouseDown() {
        checkOnMouseDown();
    }

    @Test
    public void testOnMouseMove() {
        checkOnMouseMove();
    }

    @Test
    public void testOnMouseOut() {
        checkOnMouseOut();
    }

    @Test
    public void testOnMouseOver() {
        checkOnMouseOver();
    }

    @Test
    public void testOnMouseUp() {
        checkOnMouseUp();
    }

    @Test
    public void testRendered() {
        checkRendered();
    }

    @Test
    public void testShowDetail() {
        checkShowDetail();
    }

    @Test
    public void testShowSummary() {
        checkShowSummary();
    }

    @Test
    public void testStyle() {
        checkStyle();
    }

    @Test
    public void testStyleClass() {
        checkStyleClass();
    }

    @Test
    public void testTitle() {
        checkTitle();
    }

    @Override
    protected void waitingForValidationMessagesToShow() {
        submitWithA4jBtn();
    }
}
