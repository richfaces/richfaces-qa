/**
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
package org.richfaces.tests.metamer.ftest.richPanelMenuGroup;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.panelMenu.RichFacesPanelMenu;
import org.richfaces.fragment.panelMenu.RichFacesPanelMenuGroup;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 *
 */
public class PanelMenuGroupPage extends MetamerPage {

    @FindBy(css = "div.rf-pm[id$=panelMenu]")
    private RichFacesPanelMenu menu;
    @FindBy(css = "div[id$=group2]")
    private RichFacesPanelMenuGroup topGroup;
    @FindBy(css = "div[id$=group1]")
    private RichFacesPanelMenuGroup group1;
    @FindBy(css = "div[id$=group23]")
    private RichFacesPanelMenuGroup subGroup;

    public RichFacesPanelMenu getMenu() {
        return menu;
    }

    public RichFacesPanelMenuGroup getTopGroup() {
        return topGroup;
    }

    public RichFacesPanelMenuGroup getGroup1() {
        return group1;
    }

    public RichFacesPanelMenuGroup getSubGroup() {
        return subGroup;
    }
}
