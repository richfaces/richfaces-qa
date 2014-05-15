/**
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
 */
package org.richfaces.tests.metamer.ftest.richDropDownMenu;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 */
public class TestDropDownMenuTopMenu extends AbstractDropDownMenuTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDropDownMenu/topMenu.xhtml");
    }

    private Integer delay;

    @Test
    public void testInit() {
        super.testInit();
    }

    @Test(groups = "smoke")
    public void testShowEvent() {
        super.testShowEvent();
    }

    @Test
    @Templates("plain")
    public void testStyle() {
        super.testStyle();
    }

    @Test
    @Templates("plain")
    public void testStyleClass() {
        super.testStyleClass();
    }

    @Test
    @Templates("plain")
    public void testDir() {
        super.testDir();
    }

    @Test
    @Templates("plain")
    @UseWithField(field = "positioning", valuesFrom = FROM_ENUM, value = "")
    public void testDirection() {
        super.testDirection();
    }

    @Test
    @Templates("plain")
    @UseWithField(field = "positioning", valuesFrom = FROM_ENUM, value = "")
    public void testJointPoint() {
        super.testJointPoint();
    }

    @Test
    @Templates("plain")
    public void testLang() {
        super.testLang();
    }

    @Test(groups = "smoke")
    public void testMode() {
        super.testMode();
    }

    @Test
    public void testDisabled() {
        super.testDisabled();
    }

    @Test
    @UseWithField(field = "delay", valuesFrom = FROM_FIELD, value = "delays")
    public void testHideDelay() {
        super.testHideDelay(delay);
    }

    @Test
    public void testPopupWidth() {
        super.testPopupWidth();
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        super.testRendered();
    }

    @Test
    @UseWithField(field = "delay", valuesFrom = FROM_FIELD, value = "delays")
    public void testShowDelay() {
        super.testShowDelay(delay);
    }

    @Test
    @Templates("plain")
    public void testTitle() {
        super.testTitle();
    }

    @Test
    @Templates("plain")
    public void testOnclick() {
        super.testOnclick();
    }

    @Test
    @Templates("plain")
    public void testOndblclick() {
        super.testOndblclick();
    }

    @Test
    public void testOngrouphide() {
        super.testOngrouphide();
    }

    @Test
    public void testOngroupshow() {
        super.testOngroupshow();
    }

    @Test
    public void testOnhide() {
        super.testOnhide();
    }

    @Test
    @Templates(value = "plain")
    public void testOnitemclick() {
        super.testOnitemclick();
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12792")
    public void testOnkeydown() {
        super.testOnkeydown();
    }

    @Test
    @Templates("plain")
    public void testOnkeypress() {
        super.testOnkeypress();
    }

    @Test
    @Templates("plain")
    public void testOnkeyup() {
        super.testOnkeyup();
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        super.testOnmousedown();
    }

    @Test
    @Templates("plain")
    public void testOnmousemove() {
        super.testOnmousemove();
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12854")
    @Templates(value = "plain")
    public void testOnmouseout() {
        super.testOnmouseout();
    }

    @Test
    @Templates("plain")
    public void testOnmouseover() {
        super.testOnmouseover();
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        super.testOnmouseup();
    }

    @Test
    public void testOnshow() {
        super.testOnshow();
    }

    @Test
    @Templates("plain")
    public void testHorizontalOffset() {
        super.testHorizontalOffset();
    }

    @Test
    @Templates("plain")
    public void testVerticalOffset() {
        super.testVerticalOffset();
    }
}
