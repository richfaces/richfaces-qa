package org.richfaces.tests.archetypes.kitchensink.ftest.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegisterForm {

    @FindBy(xpath = "//input[contains(@id,'name')]")
    private WebElement nameInput;

    @FindBy(xpath="//input[contains(@id,'email')]")
    private WebElement emailInput;

    @FindBy(xpath="//input[contains(@id,'phoneNumber')]")
    private WebElement phoneInput;

    @FindBy(xpath="//input[@type='submit']")
    private WebElement registerButton;

    public WebElement getRegisterButton() {
        return registerButton;
    }

    public void setRegisterButton(WebElement registerButton) {
        this.registerButton = registerButton;
    }

    public WebElement getEmailInput() {
        return emailInput;
    }

    public WebElement getPhoneInput() {
        return phoneInput;
    }

    public void setEmailInput(WebElement emailInput) {
        this.emailInput = emailInput;
    }

    public void setPhoneInput(WebElement phoneInput) {
        this.phoneInput = phoneInput;
    }

    public WebElement getNameInput() {
        return nameInput;
    }

    public void setNameInput(WebElement nameInput) {
        this.nameInput = nameInput;
    }


}
