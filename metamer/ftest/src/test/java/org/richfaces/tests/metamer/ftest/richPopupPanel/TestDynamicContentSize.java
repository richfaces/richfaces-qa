/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richPopupPanel;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.NullFragment;
import org.richfaces.fragment.extendedDataTable.RichFacesExtendedDataTable;
import org.richfaces.fragment.popupPanel.RichFacesPopupPanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestDynamicContentSize extends AbstractWebDriverTest {

    @FindBy(css = "input[id$=openPanelButton]")
    private WebElement callPopupButton;

    @FindBy(css = "div.rf-pp-cntr[id$=editWindow_container]")
    private TestedPopupPanel popup;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPopupPanel/rf-13655.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13655")
    public void testAutosize() {
        callPopupButton.click();
        Graphene.waitGui().until().element(popup.advanced().getRootElement()).is().visible();

        Dimension sizeBefore = popup.advanced().getRootElement().getSize();
        for (int i = 0; i < 3; i++) {
            popup.getBodyContent().addRow();
        }
        Dimension sizeAfter = popup.advanced().getRootElement().getSize();

        assertTrue(sizeAfter.getHeight() > sizeBefore.getHeight(),
            "The popupPanel width and height should be autosized when content gets bigger!");
    }

    public static class TestedPopupPanel extends RichFacesPopupPanel<NullFragment, NullFragment, Body> {
    }

    public static class Body {

        @FindBy(css = "input[type=submit]")
        private WebElement addRowButton;

        @FindBy
        private TableInPopup table;

        public void addRow() {
            Graphene.guardAjax(addRowButton).click();
        }
    }

    public static class TableInPopup extends RichFacesExtendedDataTable<NullFragment, TableRow, NullFragment> {

    }

    public static class TableRow {
    }

}
