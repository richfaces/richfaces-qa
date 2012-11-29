/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richMessage;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichMessageJSFValidator extends AbstractRichMessageTest {

    @Override
    protected void waitingForValidationMessages() {
        MetamerPage.waitRequest(Graphene.guardXhr(page.a4jCommandButton), WaitRequestType.XHR).click();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richMessage/jsfValidator.xhtml");
    }

    @Test
    public void testAjaxRendered() {
        super.testAjaxRendered();
    }

    @Test
    public void testDir() {
        super.testDir();
    }

    @Test
    public void testEscape() {
        super.testEscape();
    }

    @Test
    public void testFor() {
        super.testFor();
    }

    @Test
    public void testLang() {
        super.testLang();
    }

    @Test
    public void testNoShowDetailNoShowSummary() {
        super.testNoShowDetailNoShowSummary();
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
    @Override
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
    @Override
    public void testOnMouseOver() {
        super.testOnMouseOver();
    }

    @Test
    public void testOnMouseUp() {
        super.testOnMouseUp();
    }

    @Test
    public void testRendered() {
        super.testRendered();
    }

    @Test
    public void testShowDetail() {
        super.testShowDetail();
    }

    @Test
    public void testShowSummary() {
        super.testShowSummary();
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
    public void testTitle() {
        super.testTitle();
    }
}
