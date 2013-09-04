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
package org.richfaces.tests.metamer.ftest.richTooltip;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.tooltip.RichFacesTooltip;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class TooltipPage extends MetamerPage {

    @FindBy(css = "div[id$=panel]")
    private WebElement panel;

    @FindBy(css = "div[id$=regular-div]")
    private WebElement panel1;

    @FindBy(css = "div[id$=jsf-div]")
    private WebElement panel2;

    @FindBy(css = "div[id$=panel_body]")
    private WebElement panel3;

    @FindBy(className = "rf-tt")
    private RichFacesTooltip tooltip;

    @FindBy(css = "table[id$='attributes:attributes']")
    private WebElement attributesTable;

    public WebElement getAttributesTable() {
        return attributesTable;
    }

    public WebElement getPanel() {
        return panel;
    }

    public WebElement getPanel1() {
        return panel1;
    }

    public WebElement getPanel2() {
        return panel2;
    }

    public WebElement getPanel3() {
        return panel3;
    }

    public RichFacesTooltip getTooltip() {
        return tooltip;
    }

    @FindBy(css = "input[id$=showClick]")
    public WebElement jsAPIshowClick;

    @FindBy(css = "input[id$=hideClick]")
    public WebElement jsAPIhideClick;

    @FindBy(css = "input[id$=showMouseOver]")
    public WebElement jsAPIshowMouseOver;

    @FindBy(css = "input[id$=hideMouseOver]")
    public WebElement jsAPIhideMouseOver;
}
