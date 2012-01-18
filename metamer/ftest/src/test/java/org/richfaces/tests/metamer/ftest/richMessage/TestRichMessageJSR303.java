/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richMessage;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richMessage/jsr303.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 22505 $
 */
public class TestRichMessageJSR303 extends RichMessageTest {

    /* (non-Javadoc)
     * @see org.richfaces.tests.metamer.ftest.AbstractMetamerTest#getTestUrl()
     */
    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richMessage/jsr303.xhtml");
    }

    @Override
    public JQueryLocator getTestElemLocator() {
        return mainMessage;
    }

    @Override
    public JQueryLocator getSummaryElemLocator() {
        return summary;
    }

    @Override
    public JQueryLocator getDetailElemLocator() {
        return detail;
    }

    // list of called test methods
    @Test
    public void testFor() {
        super.testFor();
    }

    @Test
    public void testAjaxRendered() {
        super.testAjaxRendered();
    }

    @Test
    public void testRendered() {
        super.testRendered();
    }

    @Test
    public void testShowSummary() {
        super.testShowSummary();
    }

    @Test
    public void testShowDetail() {
        super.testShowDetail();
    }

    @Test
    public void testTitle() {
        super.testTitle();
    }

    @Test
    public void testDir() {
        super.testDir();
    }

    @Test
    public void testLang() {
        super.testLang();
    }

    @Test
    public void testStyle() {
        super.testStyle();
    }

    @Test
    public void testStyleClass() {
        super.testStyleClass();
    }

    @Test
    public void testOnClick() {
        super.testOnClick();
    }

    @Test
    public void testOnDblClick() {
        super.testOnDblClick();
    }

    @Test
    public void testOnKeyDown() {
        super.testOnKeyDown();
    }

    @Test
    public void testOnKeyPress() {
        super.testOnKeyPress();
    }

    @Test
    public void testOnKeyUp() {
        super.testOnKeyUp();
    }

    @Test
    public void testOnMouseDown() {
        super.testOnMouseDown();
    }

    @Test
    public void testOnMouseMove() {
        super.testOnMouseMove();
    }

    @Test
    public void testOnMouseOut() {
        super.testOnMouseOut();
    }

    @Test
    public void testOnMouseOver() {
        super.testOnMouseOver();
    }

    @Test
    public void testOnMouseUp() {
        super.testOnMouseUp();
    }
}
