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
package org.richfaces.tests.page.fragments.impl.calendar.popup.popup;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.calendar.common.RichFacesFooterControls;

/**
 * Component for footer controls of calendar.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesPopupFooterControls extends RichFacesFooterControls implements PopupFooterControls {

    @FindByJQuery("div.rf-cal-tl-btn:contains('Apply')")
    private WebElement applyButtonElement;

    @Override
    public void applyDate() {
        if (!isVisible()) {
            throw new RuntimeException("Footer controls are not displayed, cannot interact with apply button");
        }
        if (new WebElementConditionFactory(applyButtonElement).not().isVisible().apply(driver)) {
            throw new RuntimeException("Apply button is not displayed.");
        }
        applyButtonElement.click();
    }

    @Override
    public WebElement getApplyButtonElement() {
        return applyButtonElement;
    }

    @Override
    public void setTodaysDate() {
        todayDate();
        if (new WebElementConditionFactory(applyButtonElement).isVisible().apply(driver)) {
            applyDate();
        }
    }

    @Override
    public void todayDate() {
        if (!isVisible()) {
            throw new RuntimeException("Footer controls are not displayed, cannot interact with today button");
        }
        if (new WebElementConditionFactory(todayButtonElement).not().isVisible().apply(driver)) {
            throw new RuntimeException("Today button is not displayed.");
        }
        todayButtonElement.click();
    }
}
