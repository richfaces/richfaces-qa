/**
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
    private WebElement actualProgressElement;
    @FindBy(id = "isEnabled")
    private WebElement isEnabledElement;
    @FindBy(css = "div[id$=completeOutput]")
    private WebElement completeOutputElement;
    @FindBy(css = "div[id$=progressBar]")
    private WebElement progressBarElement;
    @FindBy(css = "div.rf-pb-init")
    private WebElement initElement;
    @FindBy(css = "div.rf-pb-init > span")
    private WebElement initialOutputElement;
    @FindBy(css = "div.rf-pb-fin")
    private WebElement finishElement;
    @FindBy(css = "div.rf-pb-fin > span")
    private WebElement finishOutputElement;
    @FindBy(css = "div.rf-pb-rmng")
    private WebElement remainElement;
    @FindBy(css = "div.rf-pb-prgs")
    private WebElement progressElement;
    @FindBy(css = "div[id$=complete]")
    private WebElement completeElement;
    @FindBy(css = "div.rf-pb-lbl")
    private WebElement labelElement;
    @FindBy(css = "input[id$=childrenRendered]")
    private WebElement childrenRenderedCheckboxElement;
    @FindBy(css = "input[id$=initialFacetRendered]")
    private WebElement initialFacetRenderedCheckboxElement;
    @FindBy(css = "input[id$=finishFacetRendered]")
    private WebElement finishFacetRenderedCheckboxElement;
    @FindBy(css = "div.rf-pb-init > input")
    private WebElement startButtonElement;
    @FindBy(css = "input[id$=startButton]")
    private WebElement startButtonClientElement;
    @FindBy(css = "div.rf-pb-fin > input")
    private WebElement restartButtonElement;
    @FindBy(css = "input[id$=pauseButton]")
    private WebElement pauseButtonElement;
    @FindBy(css = "input[id$='stopPolling']")
    private WebElement stopPollingButtonElement;

    /**
     * @return the actualProgressElement
     */
    public WebElement getActualProgressElement() {
        return actualProgressElement;
    }

    /**
     * @return the childrenRenderedCheckboxElement
     */
    public WebElement getChildrenRenderedCheckboxElement() {
        return childrenRenderedCheckboxElement;
    }

    /**
     * @return the completeElement
     */
    public WebElement getCompleteElement() {
        return completeElement;
    }

    /**
     * @return the completeOutputElement
     */
    public WebElement getCompleteOutputElement() {
        return completeOutputElement;
    }

    /**
     * @return the finishElement
     */
    public WebElement getFinishElement() {
        return finishElement;
    }

    /**
     * @return the finishFacetRenderedCheckboxElement
     */
    public WebElement getFinishFacetRenderedCheckboxElement() {
        return finishFacetRenderedCheckboxElement;
    }

    /**
     * @return the finishOutputElement
     */
    public WebElement getFinishOutputElement() {
        return finishOutputElement;
    }

    /**
     * @return the initElement
     */
    public WebElement getInitElement() {
        return initElement;
    }

    /**
     * @return the initialFacetRenderedCheckboxElement
     */
    public WebElement getInitialFacetRenderedCheckboxElement() {
        return initialFacetRenderedCheckboxElement;
    }

    /**
     * @return the initialOutputElement
     */
    public WebElement getInitialOutputElement() {
        return initialOutputElement;
    }

    /**
     * @return the isEnabledElement
     */
    public WebElement getIsEnabledElement() {
        return isEnabledElement;
    }

    /**
     * @return the labelElement
     */
    public WebElement getLabelElement() {
        return labelElement;
    }

    /**
     * @return the pauseButtonElement
     */
    public WebElement getPauseButtonElement() {
        return pauseButtonElement;
    }

    /**
     * @return the progressBarElement
     */
    public WebElement getProgressBarElement() {
        return progressBarElement;
    }

    /**
     * @return the progressElement
     */
    public WebElement getProgressElement() {
        return progressElement;
    }

    /**
     * @return the remainElement
     */
    public WebElement getRemainElement() {
        return remainElement;
    }

    /**
     * @return the restartButtonElement
     */
    public WebElement getRestartButtonElement() {
        return restartButtonElement;
    }

    /**
     * @return the startButtonClientElement
     */
    public WebElement getStartButtonClientElement() {
        return startButtonClientElement;
    }

    /**
     * @return the startButtonElement
     */
    public WebElement getStartButtonElement() {
        return startButtonElement;
    }

    /**
     * @return the stopPollingButtonElement
     */
    public WebElement getStopPollingButtonElement() {
        return stopPollingButtonElement;
    }
}
