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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 */
public class ToggleControlPage {

    @FindBy(css = "input[id$=tc11]")
    private WebElement tcPanel1Item1;
    @FindBy(css = "input[id$=tc12]")
    private WebElement tcPanel1Item2;
    @FindBy(css = "input[id$=tc13]")
    private WebElement tcPanel1Item3;
    @FindBy(css = "input[id$=tc21]")
    private WebElement tcPanel2Item1;
    @FindBy(css = "input[id$=tc22]")
    private WebElement tcPanel2Item2;
    @FindBy(css = "input[id$=tc23]")
    private WebElement tcPanel2Item3;
    @FindBy(css = "input[id$=tcCustom]")
    private WebElement tcCustom;

    @FindBy(css = "div[id$='item11:content']")
    private WebElement panel1Item1Content;
    @FindBy(css = "div[id$='item12:content']")
    private WebElement panel1Item2Content;
    @FindBy(css = "div[id$='item13:content']")
    private WebElement panel1Item3Content;
    @FindBy(css = "div[id$='item21:content']")
    private WebElement panel2Item1Content;
    @FindBy(css = "div[id$='item22:content']")
    private WebElement panel2Item2Content;
    @FindBy(css = "div[id$='item23:content']")
    private WebElement panel2Item3Content;
    @FindBy(css = "div[id$=item11]")
    private WebElement panel1Item1;
    @FindBy(css = "div[id$=item12]")
    private WebElement panel1Item2;
    @FindBy(css = "div[id$=item13]")
    private WebElement panel1Item3;
    @FindBy(css = "div[id$=item21]")
    private WebElement panel2Item1;
    @FindBy(css = "div[id$=item22]")
    private WebElement panel2Item2;
    @FindBy(css = "div[id$=item23]")
    private WebElement panel2Item3;

    public WebElement getPanel1Item1() {
        return panel1Item1;
    }

    public WebElement getPanel1Item1Content() {
        return panel1Item1Content;
    }

    public WebElement getPanel1Item2() {
        return panel1Item2;
    }

    public WebElement getPanel1Item2Content() {
        return panel1Item2Content;
    }

    public WebElement getPanel1Item3() {
        return panel1Item3;
    }

    public WebElement getPanel1Item3Content() {
        return panel1Item3Content;
    }

    public WebElement getPanel2Item1() {
        return panel2Item1;
    }

    public WebElement getPanel2Item1Content() {
        return panel2Item1Content;
    }

    public WebElement getPanel2Item2() {
        return panel2Item2;
    }

    public WebElement getPanel2Item2Content() {
        return panel2Item2Content;
    }

    public WebElement getPanel2Item3() {
        return panel2Item3;
    }

    public WebElement getPanel2Item3Content() {
        return panel2Item3Content;
    }

    public WebElement getTcCustom() {
        return tcCustom;
    }

    public WebElement getTcPanel1Item1() {
        return tcPanel1Item1;
    }

    public WebElement getTcPanel1Item2() {
        return tcPanel1Item2;
    }

    public WebElement getTcPanel1Item3() {
        return tcPanel1Item3;
    }

    public WebElement getTcPanel2Item1() {
        return tcPanel2Item1;
    }

    public WebElement getTcPanel2Item2() {
        return tcPanel2Item2;
    }

    public WebElement getTcPanel2Item3() {
        return tcPanel2Item3;
    }
}
