package org.richfaces.tests.archetypes.kitchensink.ftest.mobile.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MenuPage {
    
    @FindBy(xpath="//*[contains(.,'Application Menu')]")
    private WebElement applicationMenu;
    
    @FindBy(xpath="//*[contains(.,'Add Member')]")
    private WebElement addMember;
    
    @FindBy(xpath="//*[contains(.,'List Members')]")
    private WebElement listMembers;

    public WebElement getApplicationMenu() {
        return applicationMenu;
    }

    public void setApplicationMenu(WebElement applicationMenuCollapsiblePanel) {
        this.applicationMenu = applicationMenuCollapsiblePanel;
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
}
