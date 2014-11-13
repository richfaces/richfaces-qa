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
package org.richfaces.tests.metamer.ftest.richColumn;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.log.Log;
import org.richfaces.fragment.log.RichFacesLog;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12654 extends AbstractWebDriverTest {

    private final String[] pages = new String[]{ "rf-12654-CST", "rf-12654-DT", "rf-12654-EDT" };
    private String page;

    @FindBy(css = "[id$='testButton']")
    private List<WebElement> testButtonsElements;
    @FindBy(css = ".rf-log")
    private RichFacesLog log;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, MessageFormat.format("faces/components/richColumn/{0}.xhtml", page));
    }

    @Test
    @UseWithField(field = "page", valuesFrom = ValuesFrom.FROM_FIELD, value = "pages")
    public void testRF12654() {
        log.changeLevel(Log.LogEntryLevel.ERROR);
        Assert.assertFalse(testButtonsElements.isEmpty());
        for (WebElement testButtonsElement : testButtonsElements) {
            log.clear();
            Graphene.guardAjax(testButtonsElement).click();
            Assert.assertTrue(log.getLogEntries().isEmpty(), "There should be no error in the log.");
        }
    }
}
