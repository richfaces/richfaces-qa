/**g
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the
 *
 * @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.richfaces.tests.page.fragments.impl.accordion;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RichFacesAccordionItem implements AccordionItem {

    public static final String ACTIVE_HEADER_CLASS = "rf-ac-itm-lbl-act";
    public static final String DISABLED_HEADER_CLASS = "rf-ac-itm-lbl-dis";
    public static final String INACTIVE_HEADER_CLASS = "rf-ac-itm-lbl-inact";
    public static final String CONTENT_CLASS = "rf-ac-itm-cnt";
    public static final String TO_ACTIVATE_CLASS = "rf-ac-itm-hdr";

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    @FindBy(className = ACTIVE_HEADER_CLASS)
    private WebElement activeHeader;
    @FindBy(className = DISABLED_HEADER_CLASS)
    private WebElement disabledHeader;
    @FindBy(className = INACTIVE_HEADER_CLASS)
    private WebElement inactiveHeader;
    @FindBy(className = CONTENT_CLASS)
    private WebElement content;
    @FindBy(className = TO_ACTIVATE_CLASS)
    private WebElement toActivate;

    private String id;

    @Override
    public void activate() {
        if (isActive()) {
            return;
        } else {
            getToActivateElement().click();
            Graphene.waitAjax().until(getIsActiveCondition());
        }
    }

    @Override
    public String getHeader() {
        if (isActive()) {
            return getActiveHeaderElement().getText();
        } else if (isEnabled()) {
            return getInactiveHeaderElement().getText();
        } else {
            return getDisabledHeaderElement().getText();
        }
    }

    public final WebElement getHeaderElement() {
        if (isActive()) {
            return getActiveHeaderElement();
        } else if (isEnabled()) {
            return getInactiveHeaderElement();
        } else {
            return getDisabledHeaderElement();
        }
    }

    @Override
    public String getContent() {
        if (isActive()) {
            return getContentElement().getText();
        } else {
            return null;
        }
    }

    public final WebElement getContentElement() {
        return content;
    }

    public final WebElement getToActivateElement() {
        return toActivate;
    }

    @Override
    public boolean isActive() {
        return getIsActiveCondition().apply(browser) && getContentElement().isDisplayed();
    }

    @Override
    public boolean isEnabled() {
        return getIsEnabledCondition().apply(browser);
    }

    @Override
    public boolean isInactive() {
        return !getIsActiveCondition().apply(browser) && !getContentElement().isDisplayed();
    }

    protected final ExpectedCondition<Boolean> getIsActiveCondition() {
        return Graphene.element(getActiveHeaderElement()).isVisible();
    }

    protected final ExpectedCondition<Boolean> getIsEnabledCondition() {
        return Graphene.element(getDisabledHeaderElement()).not().isPresent();
    }

    protected final WebElement getActiveHeaderElement() {
        return activeHeader;
    }

    protected final WebElement getDisabledHeaderElement() {
        return disabledHeader;
    }

    protected final WebElement getInactiveHeaderElement() {
        return inactiveHeader;
    }

}
