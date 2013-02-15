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

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.panelMenu.RichPanelMenu;
import org.richfaces.tests.page.fragments.impl.panelMenuGroup.PanelMenuGroup;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class PanelMenuGroupPage extends MetamerPage {

    @FindBy(css = "div.rf-pm[id$=panelMenu]")
    public RichPanelMenu menu;

    @FindBy(css = "div[id$=group2]")
    public PanelMenuGroup topGroup;

    @FindBy(css = "div[id$=group1]")
    public PanelMenuGroup group1;

    @FindBy(css = "div[id$=group23]")
    public PanelMenuGroup subGroup;

    @FindBy(css = "table[id$='attributes:attributes']")
    public WebElement attributesTable;

}
