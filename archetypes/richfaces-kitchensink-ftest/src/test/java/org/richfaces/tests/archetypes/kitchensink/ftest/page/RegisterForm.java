package org.richfaces.tests.archetypes.kitchensink.ftest.page;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegisterForm {

    private final String NAME_LOC = "//input[contains(@id,'name')]";
    private final String EMAIL_LOC = "//input[contains(@id,'email')]";
    private final String PHONE_LOC = "//input[contains(@id,'phoneNumber')]";
    private final String REGISTER_BUTTON_LOC = "//input[@type='submit']";
    private final String ERROR_MSGS_LOC = "rf-msg-err";
    
    @FindBy(xpath = NAME_LOC)
    private WebElement nameInput;

    @FindBy(xpath = EMAIL_LOC)
    private WebElement emailInput;

    @FindBy(xpath = PHONE_LOC)
    private WebElement phoneInput;

    @FindBy(xpath = REGISTER_BUTTON_LOC)
    private WebElement registerButton;

    @FindBy(className = ERROR_MSGS_LOC)
    private List<WebElement> errorMessages;

    private String CORRECT_NAME = "Andrasi Oliver";
    private String CORRECT_EMAIL = "ado@gmaul.xor";
    private String CORRECT_PHONE = "12345678910";

    private String INCORRECT_NAME_PATTERN = "1234";
    private String INCORRECT_EMAIL_PATTERN = "wrong";
    private String INCORRECT_PHONE_PATTERN = "wrong";
    
    private String INCORRECT_NAME_TOO_SHORT = "";

    public void setIncorrectNameTooShort() {
        nameInput.sendKeys(INCORRECT_NAME_TOO_SHORT);
    }
    
    public String setCorrectName() {
        nameInput.sendKeys(CORRECT_NAME);
        return CORRECT_NAME;
    }

    public String setCorrectEmail() {
        emailInput.sendKeys(CORRECT_EMAIL);
        return CORRECT_EMAIL;
    }

    public String setCorrectPhone() {
        phoneInput.sendKeys(CORRECT_PHONE);
        return CORRECT_PHONE;
    }

    public String setIncorrectNamePatternViolation() {
        nameInput.sendKeys(INCORRECT_NAME_PATTERN);
        return INCORRECT_NAME_PATTERN;
    }

    public String setIncorrectPhonePatternViolation() {
        phoneInput.sendKeys(INCORRECT_PHONE_PATTERN);
        return INCORRECT_PHONE_PATTERN;
    }

    public String setIncorrectEmailPatternViolation() {
        emailInput.sendKeys(INCORRECT_EMAIL_PATTERN);
        return INCORRECT_EMAIL_PATTERN;
    }

    public WebElement getRegisterButton() {
        return registerButton;
    }

    public List<WebElement> getErrorMessages() {
        return errorMessages;
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
    
    public String getNAME_LOC() {
        return NAME_LOC;
    }

    public String getEMAIL_LOC() {
        return EMAIL_LOC;
    }

    public String getPHONE_LOC() {
        return PHONE_LOC;
    }

    public String getREGISTER_BUTTON_LOC() {
        return REGISTER_BUTTON_LOC;
    }

    public String getERROR_MSGS_LOC() {
        return ERROR_MSGS_LOC;
    }

}
