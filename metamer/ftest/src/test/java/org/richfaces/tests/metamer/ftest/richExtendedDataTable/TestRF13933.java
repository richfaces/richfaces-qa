/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.NullFragment;
import org.richfaces.fragment.extendedDataTable.RichFacesExtendedDataTable;
import org.richfaces.fragment.panel.TextualFragmentPart;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Templates(value = { "plain" })
public class TestRF13933 extends AbstractWebDriverTest {

    @FindBy(css = "[id='form:repeat:0:edt']")
    private SimpleEDT edt1;
    @FindBy(css = "[id='form:repeat:1:edt']")
    private SimpleEDT edt2;
    @FindBy(css = "[id='form:repeat:2:edt']")
    private SimpleEDT edt3;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richExtendedDataTable/rf-13933.xhtml");
    }

    @Test
    public void testTablesHaveDifferentItems() {
        assertEquals(edt1.getRow(0).getText(), "firstA");
        assertEquals(edt2.getRow(0).getText(), "firstB");
        assertEquals(edt3.getRow(0).getText(), "firstC");

        assertEquals(edt1.getRow(1).getText(), "secondA");
        assertEquals(edt2.getRow(1).getText(), "secondB");
        assertEquals(edt3.getRow(1).getText(), "secondC");

        assertEquals(edt1.getRow(2).getText(), "thirdA");
        assertEquals(edt2.getRow(2).getText(), "thirdB");
        assertEquals(edt3.getRow(2).getText(), "thirdC");
    }

    public static class SimpleEDT extends RichFacesExtendedDataTable<NullFragment, TextualFragmentPart, NullFragment> {
    }
}
