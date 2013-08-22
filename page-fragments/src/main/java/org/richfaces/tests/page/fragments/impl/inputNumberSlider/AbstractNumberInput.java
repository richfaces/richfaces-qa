package org.richfaces.tests.page.fragments.impl.inputNumberSlider;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;

public abstract class AbstractNumberInput implements NumberInput {

    @Override
    public void increase() {
        if (!new WebElementConditionFactory(getArrowIncrease()).isVisible().apply(getBrowser())) {
            throw new RuntimeException("Arrow for increasing value is not visible.");
        }
        getArrowIncrease().click();
    }

    @Override
    public void decrease() {
        if (!new WebElementConditionFactory(getArrowDecrease()).isVisible().apply(getBrowser())) {
            throw new RuntimeException("arrow for decreasing value is not visible.");
        }
        getArrowDecrease().click();
    }

    @Override
    public void increase(int n) {
        for (int i = 0; i < n; i++) {
            increase();
        }
    }

    @Override
    public void decrease(int n) {
        for (int i = 0; i < n; i++) {
            decrease();
        }
    }

    @Override
    public void setValue(double value) {
        getInput().sendKeys(String.valueOf(value));
    }

    @Override
    public double getValue() {
        return Double.valueOf(getInput().getStringValue());
    }

    protected abstract WebElement getArrowIncrease();

    protected abstract WebDriver getBrowser();

    protected abstract WebElement getArrowDecrease();

    protected abstract TextInputComponentImpl getInput();

    public class AdvancedInteractions {
        public TextInputComponentImpl getInput() {
            return AbstractNumberInput.this.getInput();
        }

        public WebElement getArrowIncrease() {
            return AbstractNumberInput.this.getArrowIncrease();
        }

        public WebElement getArrowDecrease() {
            return AbstractNumberInput.this.getArrowDecrease();
        }
    }
}
