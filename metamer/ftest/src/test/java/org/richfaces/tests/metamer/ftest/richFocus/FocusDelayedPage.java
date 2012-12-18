package org.richfaces.tests.metamer.ftest.richFocus;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;

public class FocusDelayedPage extends AbstractFocusPage {

    @FindBy(jquery = "[value*='Next']")
    private WebElement nextButton;

    @FindBy(jquery = ".input")
    private TextInputComponentImpl nameInput;

    public WebElement getNextButton() {
        return nextButton;
    }

    public void setNextButton(WebElement nextButton) {
        this.nextButton = nextButton;
    }

    public TextInputComponentImpl getNameInput() {
        return nameInput;
    }

    public void setNameInput(TextInputComponentImpl nameInput) {
        this.nameInput = nameInput;
    }
}
