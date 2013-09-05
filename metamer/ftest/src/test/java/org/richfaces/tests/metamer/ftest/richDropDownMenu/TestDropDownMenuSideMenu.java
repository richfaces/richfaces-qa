/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richDropDownMenu;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;
/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 *
 */
public class TestDropDownMenuSideMenu extends AbstractDropDownMenuTest {
    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDropDownMenu/sideMenu.xhtml");
    }

    @Inject
    @Use(empty = false)
    private Integer delay;

    @Test
    public void testInit() {
        super.testInit();
    }

    @Test
    public void testShowEvent() {
        super.testShowEvent();
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
    public void testDir() {
        super.testDir();
    }

    @Test
    public void testLang() {
        super.testLang();
    }

    @Test
    public void testMode() {
        super.testMode();
    }

    @Test
    public void testDisabled() {
        super.testDisabled();
    }

    @Test
    @Use(field = "delay", ints = { 1000, 1500, 2500 })
    public void testHideDelay() {
        super.testHideDelay(delay);
    }

    @Test
    public void testPopupWidth() {
        super.testPopupWidth();
    }

    @Test
    public void testRendered() {
        super.testRendered();
    }

    @Test
    @Use(field = "delay", ints = { 1000, 1500, 2500 })
    public void testShowDelay() {
        super.testShowDelay(delay);
    }

    @Test
    public void testTitle() {
        super.testTitle();
    }

    @Test
    public void testOnclick() {
        super.testOnclick();
    }

    @Test
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
    public void testOnitemclick() {
        super.testOnitemclick();
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12792")
    public void testOnkeydown() {
        super.testOnkeydown();
    }

    @Test
    public void testOnkeypress() {
        super.testOnkeypress();
    }

    @Test
    public void testOnkeyup() {
        super.testOnkeyup();
    }

    @Test
    public void testOnmousedown() {
        super.testOnmousedown();
    }

    @Test
    public void testOnmousemove() {
        super.testOnmousemove();
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12854")
    public void testOnmouseout() {
        super.testOnmouseout();
    }

    @Test
    public void testOnmouseover() {
        super.testOnmouseover();
    }

    @Test
    public void testOnmouseup() {
        super.testOnmouseup();
    }

    @Test
    public void testOnshow() {
        super.testOnshow();
    }

    @Test
    public void testHorizontalOffset() {
        super.testHorizontalOffset();
    }

    @Test
    public void testVerticalOffset() {
        super.testVerticalOffset();
    }
}
