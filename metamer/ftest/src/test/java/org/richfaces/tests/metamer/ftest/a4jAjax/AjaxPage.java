/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jAjax;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public class AjaxPage extends MetamerPage {

    @FindBy(css = "input[type=submit][id$=commandButton]")
    WebElement button;
    @FindBy(css = "a[id$=commandLink]")
    WebElement link;
    @FindBy(css = "img[id$=image]")
    WebElement image;
    @FindBy(css = "input[type=text][id$=input]")
    WebElement input;
    @FindBy(css = "select[id$=selectManyListbox]")
    WebElement selectManyListbox;
    @FindBy(css = "select[id$=selectOneListbox]")
    WebElement selectOneListbox;
    @FindBy(css = "select[id$=selectManyMenu]")
    WebElement selectManyMenu;
    @FindBy(css = "select[id$=selectOneMenu]")
    WebElement selectOneMenu;
    @FindBy(css = "[id$=output1]")
    WebElement output1;
    @FindBy(css = "[id$=output2]")
    WebElement output2;

}
