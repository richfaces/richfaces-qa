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
package org.richfaces.tests.metamer.ftest.richMessage;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class MessagePage extends MetamerPage {

    @FindBy(css = "span[id$=simpleInputMsg]")
    WebElement messageForInputX;
    @FindBy(css = "span[id$=simpleInputMsg] > span > span.rf-msg-sum")
    WebElement messageForInputXSummary;
    @FindBy(css = "span[id$=simpleInputMsg] > span > span.rf-msg-det")
    WebElement messageForInputXDetail;
    @FindBy(css = "span[id$=simpleInputMsg1]")
    WebElement messageForInput1;
    @FindBy(css = "span[id$=simpleInputMsg1] > span > span.rf-msg-sum")
    WebElement messageForInput1Summary;
    @FindBy(css = "span[id$=simpleInputMsg1] > span > span.rf-msg-det")
    WebElement messageForInput1Detail;
    @FindBy(css = "span[id$=simpleInputMsg2]")
    WebElement messageForInput2;
    @FindBy(css = "span[id$=simpleInputMsg2] > span > span")
    WebElement messageForInput2Summary;
    @FindBy(css = "span[id$=simpleInputMsg2] > span > span.rf-msg-det")
    WebElement messageForInput2Detail;
    @FindBy(css = "span[id$=msgs]")
    WebElement messages;
    // controls
    @FindBy(css = "input[type=button][id$=setWrongValuesButton]")
    WebElement wrongValuesButton;
    @FindBy(css = "input[type=button][id$=setCorrectValuesButton]")
    WebElement correctValuesButton;
    @FindBy(css = "input[id$=hButton]")
    WebElement hCommandButton;
    @FindBy(css = "input[id$=a4jButton]")
    WebElement a4jCommandButton;
}
