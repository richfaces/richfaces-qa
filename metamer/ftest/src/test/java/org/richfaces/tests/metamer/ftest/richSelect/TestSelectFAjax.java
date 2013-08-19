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
package org.richfaces.tests.metamer.ftest.richSelect;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.select.internal.Selection;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richSelect/fAjax.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestSelectFAjax extends AbstractWebDriverTest {

    @FindBy(css = "div[id$=select]")
    private RichFacesSelectEnhanced select;
    @FindBy(css = "span[id$=output]")
    private WebElement output;
    @FindBy(css = "div[id$=selectItem10]")
    private WebElement item10;
    @Page
    private MetamerPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richSelect/fAjax.xhtml");
    }

    @Test
    public void testSelectWithKeyboard() {
        Graphene.guardAjax(select.callPopup()).selectByIndex(10, Selection.BY_KEYS);
        assertTrue(item10.getAttribute("class").contains("rf-sel-sel"), "Selected item should contain class for selected option.");
        assertEquals(select.getSelectedOption().getVisibleText(), "Hawaii");
        assertEquals(output.getText(), "Hawaii");
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }

    @Test
    public void testSelectWithMouse() {
        Graphene.guardAjax(select.callPopup()).selectByIndex(10, Selection.BY_MOUSE);
        assertTrue(item10.getAttribute("class").contains("rf-sel-sel"), "Selected item should contain class for selected option.");
        assertEquals(select.getSelectedOption().getVisibleText(), "Hawaii");
        assertEquals(output.getText(), "Hawaii");
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }
}
