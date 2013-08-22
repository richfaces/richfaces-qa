package org.richfaces.tests.page.fragments.impl.inplaceInput;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public abstract class AbstractConfirmOrCancel implements ConfirmOrCancel {

    @Override
    public void confirm() {
        new Actions(getBrowser()).sendKeys(Keys.chord(Keys.CONTROL, Keys.RETURN)).perform();
        waitAfterConfirmOrCancel();
    }

    @Override
    public void confirmByControlls() {
        checkControllsAreAvailable();
        getConfirmButton().click();
        waitAfterConfirmOrCancel();
    }

    @Override
    public void cancel() {
        getInput().sendKeys(Keys.chord(Keys.CONTROL, Keys.ESCAPE));
        waitAfterConfirmOrCancel();
    }

    @Override
    public void cancelByControlls() {
        checkControllsAreAvailable();
        getCancelButton().click();
        waitAfterConfirmOrCancel();
    }

    private void checkControllsAreAvailable() {
        boolean condition = new WebElementConditionFactory(getConfirmButton()).isPresent().apply(getBrowser());
        if (!condition) {
            throw new IllegalStateException(
                "You are trying to use cotrolls to confirm/cancel the input, however, there are no controlls!");
        }
    }

    public abstract WebDriver getBrowser();
    public abstract WebElement getConfirmButton();
    public abstract WebElement getInput();
    public abstract WebElement getCancelButton();
    public abstract void waitAfterConfirmOrCancel();
}
