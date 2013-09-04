package org.richfaces.tests.page.fragments.impl.inputNumberSpinner;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.inputNumberSlider.AbstractNumberInput;

public class RichFacesInputNumberSpinner extends AbstractNumberInput {

    @FindBy(css = "input.rf-insp-inp")
    private TextInputComponentImpl input;

    @FindBy(css = "span.rf-insp-btns > span.rf-insp-inc")
    private WebElement arrowIncrease;

    @FindBy(css = "span.rf-insp-btns > span.rf-insp-dec")
    private WebElement arrowDecrease;

    @Drone
    private WebDriver browser;
    @Root
    private WebElement root;
    private AdvancedInteractions advancedInteractions;

    @Override
    protected WebElement getArrowIncrease() {
        return arrowIncrease;
    }

    @Override
    protected WebDriver getBrowser() {
        return browser;
    }

    @Override
    protected WebElement getArrowDecrease() {
        return arrowDecrease;
    }

    @Override
    protected TextInputComponentImpl getInput() {
        return input;
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions extends AbstractNumberInput.AdvancedInteractions {

        public WebElement getRootElement() {
            return root;
        }
    }
}
