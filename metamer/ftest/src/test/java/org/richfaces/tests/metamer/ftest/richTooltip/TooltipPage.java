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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.tooltip.TextualRichFacesTooltip;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class TooltipPage extends MetamerPage {

    @FindBy(css = "div[id$=panel]")
    private TextualRichFacesTooltip tooltipOnPanel;

    @FindBy(css = "div[id$=panel]")
    private WebElement panel;
    @FindBy(css = "div[id$=regular-div]")
    private WebElement panelRegularDiv;
    @FindBy(css = "div[id$=jsf-div]")
    private WebElement panelJSFDiv;

    @FindBy(css = "table[id$='attributes:attributes']")
    private WebElement attributesTable;
    @FindBy(css = "input[id$=showClick]")
    private WebElement jsAPIshowClick;
    @FindBy(css = "input[id$=hideClick]")
    private WebElement jsAPIhideClick;
    @FindBy(css = "input[id$=showMouseOver]")
    private WebElement jsAPIshowMouseOver;
    @FindBy(css = "input[id$=hideMouseOver]")
    private WebElement jsAPIhideMouseOver;

    public WebElement getAttributesTable() {
        return attributesTable;
    }

    public WebElement getJsAPIhideClick() {
        return jsAPIhideClick;
    }

    public WebElement getJsAPIhideMouseOver() {
        return jsAPIhideMouseOver;
    }

    public WebElement getJsAPIshowClick() {
        return jsAPIshowClick;
    }

    public WebElement getJsAPIshowMouseOver() {
        return jsAPIshowMouseOver;
    }

    public WebElement getPanel() {
        return panel;
    }

    public WebElement getPanelRegularDiv() {
        return panelRegularDiv;
    }

    public WebElement getPanelJSFDiv() {
        return panelJSFDiv;
    }

    public TextualRichFacesTooltip getTooltip() {
        return tooltipOnPanel;
    }
//
//    public TextualRichFacesTooltip getTooltipOnPanel1() {
//        return tooltipOnPanel1;
//    }
//
//    public TextualRichFacesTooltip getTooltipOnPanel2() {
//        return tooltipOnPanel2;
//    }
//
//    public TextualRichFacesTooltip getTooltipOnPanel3() {
//        return tooltipOnPanel3;
//    }
}
