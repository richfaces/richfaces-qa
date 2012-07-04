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
