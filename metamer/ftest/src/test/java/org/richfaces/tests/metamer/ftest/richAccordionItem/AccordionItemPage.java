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
package org.richfaces.tests.metamer.ftest.richAccordionItem;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.accordion.RichFacesAccordion;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * Page object for rich accordion item
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class AccordionItemPage extends MetamerPage {

    @FindBy(css = "div[id$=accordion]")
    private RichFacesAccordion accordion;

    @FindByJQuery(value = "div[id$='header'].[id*='item']")
    private List<WebElement> itemHeaders;

    @FindByJQuery(value = "div[id$='content'].[id*='item']")
    private List<WebElement> itemContents;

    @FindByJQuery(value = "div[id$='header'].[id*='item'] div.rf-ac-itm-lbl-act")
    private List<WebElement> activeHeaders;

    @FindByJQuery(value = "div[id$='header'].[id*='item'] div.rf-ac-itm-lbl-inact")
    private List<WebElement> inactiveHeaders;

    @FindByJQuery(value = "div[id$='header'].[id*='item'] div.rf-ac-itm-lbl-dis")
    private List<WebElement> disabledHeaders;

    @FindByJQuery(value = "div[id$=item1] td.rf-ac-itm-ico div")
    private WebElement leftActiveIcon;

    @FindByJQuery(value = "div[id$=item1] td.rf-ac-itm-ico div.rf-ac-itm-ico-act")
    private WebElement leftDisabledIcon;

    @FindByJQuery(value = "div[id$=item1] td.rf-ac-itm-ico div.rf-ac-itm-ico-inact")
    private WebElement leftInactiveIcon;

    @FindByJQuery(value = "div[id$=item1] td.rf-ac-itm-exp-ico div")
    private WebElement rightActiveIcon;

    @FindByJQuery(value = "div[id$=item1] td.rf-ac-itm-exp-ico div.rf-ac-itm-ico-act")
    private WebElement rightDisabledIcon;

    @FindByJQuery(value = "div[id$=item1] td.rf-ac-itm-exp-ico div.rf-ac-itm-ico-inact")
    private WebElement rightInactiveIcon;

    private String leftIconSelector = "div[id$=item1] td.rf-ac-itm-ico";
    private String rightIconSelector = "div[id$=item1] td.rf-ac-itm-exp-ico";

    public String getLeftIconSelector() {
        return leftIconSelector;
    }

    public String getRightIconSelector() {
        return rightIconSelector;
    }

    public RichFacesAccordion getAccordion() {
        return accordion;
    }

    public WebElement getAccordionRootElement() {
        return accordion.advanced().getRootElement();
    }

    public List<WebElement> getItemHeaders() {
        return itemHeaders;
    }

    public List<WebElement> getItemContents() {
        return itemContents;
    }

    public List<WebElement> getActiveHeaders() {
        return activeHeaders;
    }

    public List<WebElement> getInactiveHeaders() {
        return inactiveHeaders;
    }

    public List<WebElement> getDisabledHeaders() {
        return disabledHeaders;
    }

    public WebElement getLeftActiveIcon() {
        return leftActiveIcon;
    }

    public WebElement getLeftDisabledIcon() {
        return leftDisabledIcon;
    }

    public WebElement getLeftInactiveIcon() {
        return leftInactiveIcon;
    }

    public WebElement getRightActiveIcon() {
        return rightActiveIcon;
    }

    public WebElement getRightDisabledIcon() {
        return rightDisabledIcon;
    }

    public WebElement getRightInactiveIcon() {
        return rightInactiveIcon;
    }
}
