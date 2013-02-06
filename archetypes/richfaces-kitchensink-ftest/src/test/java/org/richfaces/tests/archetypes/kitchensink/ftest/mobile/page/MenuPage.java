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
package org.richfaces.tests.archetypes.kitchensink.ftest.mobile.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MenuPage {

    @FindBy(xpath = "(//*[@class='rf-pm-itm-lbl'])[1]")
    private WebElement addMember;

    @FindBy(xpath = "(//*[@class='rf-pm-itm-lbl'])[2]")
    private WebElement listMembers;

    public static final int PAGE_TRANSITION_WAIT = 2;
    
    public void gotoAddMemberPage() {
        addMember.click();
        waitFor(PAGE_TRANSITION_WAIT);
    }

    public void gotoListMembersPage() {
        listMembers.click();
        waitFor(PAGE_TRANSITION_WAIT);
    }
    
    public WebElement getAddMember() {
        return addMember;
    }

    public void setAddMember(WebElement addMember) {
        this.addMember = addMember;
    }

    public WebElement getListMembers() {
        return listMembers;
    }

    public void setListMembers(WebElement listMembers) {
        this.listMembers = listMembers;
    }

    public void waitFor(int howLong) {

        long timeout = System.currentTimeMillis() + (howLong * 1000);

        while (System.currentTimeMillis() < timeout) {

        }
    }
}
