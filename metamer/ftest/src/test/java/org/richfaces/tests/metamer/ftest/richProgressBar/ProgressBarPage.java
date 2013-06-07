/**
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
 */
package org.richfaces.tests.metamer.ftest.richProgressBar;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M3
 */
public class ProgressBarPage extends MetamerPage {

    @FindBy(id = "progress")
    WebElement actualProgress;
    @FindBy(id = "isEnabled")
    WebElement isEnabled;
    @FindBy(css = "div[id$=completeOutput]")
    WebElement completeOutput;
    @FindBy(css = "div[id$=progressBar]")
    WebElement progressBar;
    @FindBy(css = "div.rf-pb-init")
    WebElement init;
    @FindBy(css = "div.rf-pb-init > span")
    WebElement initialOutput;
    @FindBy(css = "div.rf-pb-fin")
    WebElement finish;
    @FindBy(css = "div.rf-pb-fin > span")
    WebElement finishOutput;
    @FindBy(css = "div.rf-pb-rmng")
    WebElement remain;
    @FindBy(css = "div.rf-pb-prgs")
    WebElement progress;
    @FindBy(css = "div[id$=complete]")
    WebElement complete;
    @FindBy(css = "div.rf-pb-lbl")
    WebElement label;
    @FindBy(css = "input[id$=childrenRendered]")
    WebElement childrenRenderedCheckbox;
    @FindBy(css = "input[id$=initialFacetRendered]")
    WebElement initialFacetRenderedCheckbox;
    @FindBy(css = "input[id$=finishFacetRendered]")
    WebElement finishFacetRenderedCheckbox;
    @FindBy(css = "div.rf-pb-init > input")
    WebElement startButton;
    @FindBy(css = "input[id$=startButton]")
    WebElement startButtonClient;
    @FindBy(css = "div.rf-pb-fin > input")
    WebElement restartButton;
    @FindBy(css = "input[id$=pauseButton]")
    WebElement pauseButton;
    @FindBy(css = "input[id$='stopPolling']")
    WebElement stopPollingButton;
}
