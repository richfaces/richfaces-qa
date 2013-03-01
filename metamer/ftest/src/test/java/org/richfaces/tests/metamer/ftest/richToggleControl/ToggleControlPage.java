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
package org.richfaces.tests.metamer.ftest.richToggleControl;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.toggleControl.RichFacesToggleControl;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class ToggleControlPage {
    @FindBy(css = "input[id$=tc11]")
    public RichFacesToggleControl tcPanel1Item1;

    @FindBy(css = "input[id$=tc12]")
    public RichFacesToggleControl tcPanel1Item2;

    @FindBy(css = "input[id$=tc13]")
    public RichFacesToggleControl tcPanel1Item3;

    @FindBy(css = "input[id$=tc21]")
    public RichFacesToggleControl tcPanel2Item1;

    @FindBy(css = "input[id$=tc22]")
    public RichFacesToggleControl tcPanel2Item2;

    @FindBy(css = "input[id$=tc23]")
    public RichFacesToggleControl tcPanel2Item3;

    @FindBy(css = "input[id$=tcCustom]")
    public RichFacesToggleControl tcCustom;

    @FindBy(css = "div[id$='item11:content']")
    public WebElement panel1Item1Content;

    @FindBy(css = "div[id$='item12:content']")
    public WebElement panel1Item2Content;

    @FindBy(css = "div[id$='item13:content']")
    public WebElement panel1Item3Content;

    @FindBy(css = "div[id$='item21:content']")
    public WebElement panel2Item1Content;

    @FindBy(css = "div[id$='item22:content']")
    public WebElement panel2Item2Content;

    @FindBy(css = "div[id$='item23:content']")
    public WebElement panel2Item3Content;

    @FindBy(css = "div[id$=item11]")
    public WebElement panel1Item1;

    @FindBy(css = "div[id$=item12]")
    public WebElement panel1Item2;

    @FindBy(css = "div[id$=item13]")
    public WebElement panel1Item3;

    @FindBy(css = "div[id$=item21]")
    public WebElement panel2Item1;

    @FindBy(css = "div[id$=item22]")
    public WebElement panel2Item2;

    @FindBy(css = "div[id$=item23]")
    public WebElement panel2Item3;
}
