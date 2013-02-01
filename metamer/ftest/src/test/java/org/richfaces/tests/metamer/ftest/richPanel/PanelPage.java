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
package org.richfaces.tests.metamer.ftest.richPanel;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class PanelPage extends MetamerPage {

    @FindBy(css = "div[id$=panelWithHeader]")
    public WebElement panelWithHeader;

    @FindBy(css = "div[id$=panelWithoutHeader]")
    public WebElement panelWithoutHeader;

    @FindBy(css = "div[id$=panelWithHeader] div.rf-p-hdr")
    public WebElement headersWithHeader;

    @FindBy(css = "div[id$=panelWithoutHeader] div.rf-p-hdr")
    public WebElement headersWithoutHeader;

    @FindBy(css = "div[id$=panelWithHeader] div.rf-p-b")
    public WebElement bodiesWithHeader;

    @FindBy(css = "div[id$=panelWithoutHeader] div.rf-p-b")
    public WebElement bodiesWithoutHeader;

    public WebElement getPanelWithHeader() {
        return panelWithHeader;
    }

}
