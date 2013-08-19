/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.abstractions.message;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;

/**
 * Common page of all message(s) components (rich:message(s), rich:notifyMessage(s)).
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class MessageComponentCommonPage {

    @FindBy(css = "input[id$=a4jButton]")
    public WebElement a4jCommandButton;
    @FindBy(css = "input[id$=setCorrectValuesButton]")
    public WebElement correctValuesButton;
    @FindBy(css = "input[id$=hButton]")
    public WebElement hCommandButton;
    @FindBy(css = "span[id$=newSpan]")
    public WebElement newSpan;
    @FindBy(css = "input[id$=simpleInput1]")
    public TextInputComponentImpl simpleInput1;
    @FindBy(css = "input[id$=simpleInput2]")
    public TextInputComponentImpl simpleInput2;
    @FindBy(css = "input[id$=setWrongValuesButton]")
    public WebElement wrongValuesButton;
    @FindBy(css = "input[id$=generateFatalMsgBtn]")
    public WebElement generateFatalMessageButton;
    @FindBy(css = "input[id$=generateErrorMsgBtn]")
    public WebElement generateErrorMessageButton;
    @FindBy(css = "input[id$=generateWarnMsgBtn]")
    public WebElement generateWarnMessageButton;
    @FindBy(css = "input[id$=generateInfoMsgBtn]")
    public WebElement generateInfoMessageButton;
}
