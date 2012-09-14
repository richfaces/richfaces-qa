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
package org.richfaces.tests.metamer.ftest.a4jLog;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public class LogPage extends MetamerPage {

    @FindBy(css = "input[id$=nameInput]")
    WebElement input;
    @FindBy(css = "input[id$=submitButton]")
    WebElement submitButton;
    @FindBy(css = "span[id$=out]")
    WebElement output;
    @FindBy(css = "div.rf-log")
    WebElement log;
    @FindBy(css = "div.rf-log select")
    WebElement levelSelect;

    @FindBy(css = "div.rf-log div.rf-log-contents div")
    List<WebElement> logMsg;
    @FindBy(css = "div.rf-log div.rf-log-contents div > span.rf-log-entry-lbl")
    List<WebElement> msgType;
    @FindBy(css = "div.rf-log div.rf-log-contents div > span.rf-log-entry-msg")
    List<WebElement> msgContent;

    @FindBy(css = "div.rf-log button")
    WebElement clearButton;
    @FindBy(css = "input[id$=debugButton]")
    WebElement debugButton;
    @FindBy(css = "input[id$=infoButton]")
    WebElement infoButton;
    @FindBy(css = "input[id$=warnButton]")
    WebElement warnButton;
    @FindBy(css = "input[id$=errorButton]")
    WebElement errorButton;

}
