package org.richfaces.tests.archetypes.kitchensink.ftest.common.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MemberDetails {

    @FindBy(xpath="//*[@class='rf-pp-cnt']/descendant::input[contains(@id,'email')]")
    private WebElement emailOnDesktop;
    
    @FindBy(xpath="//span[contains(@id, 'email')]")
    private WebElement emailOnMobile;
    
    @FindBy(xpath="//*[@class='rf-pp-hdr-cntrls ']/a")
    private WebElement backToFormDesktop;

    public void waitMemberDetailsAreAvailableOnDesktop(int timeoutInSeconds, WebDriver webDriver) {
        (new WebDriverWait(webDriver, timeoutInSeconds)).until(new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver d) {
                return backToFormDesktop.isDisplayed();
            }
        });
    }
    
    public WebElement getBackToFormDesktop() {
        return backToFormDesktop;
    }

    public void setBackToFormDesktop(WebElement backToFormDesktop) {
        this.backToFormDesktop = backToFormDesktop;
    }

    public WebElement getEmailOnDesktop() {
        return emailOnDesktop;
    }

    public void setEmailOnDesktop(WebElement emailOnDesktop) {
        this.emailOnDesktop = emailOnDesktop;
    }

    public WebElement getEmailOnMobile() {
        return emailOnMobile;
    }

    public void setEmailOnMobile(WebElement emailOnMobile) {
        this.emailOnMobile = emailOnMobile;
    }
    
    
}
