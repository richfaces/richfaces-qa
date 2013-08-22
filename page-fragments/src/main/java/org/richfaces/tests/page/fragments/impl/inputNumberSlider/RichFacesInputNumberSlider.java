package org.richfaces.tests.page.fragments.impl.inputNumberSlider;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;

import com.google.common.base.Preconditions;

public class RichFacesInputNumberSlider extends AbstractNumberInput implements InputNumberSlider {

    @FindBy(className = "rf-insl-inc")
    private WebElement arrowIncrease;

    @FindBy(className = "rf-insl-dec")
    private WebElement arrowDecrease;

    @FindBy(css = "span.rf-insl-inp-cntr > input.rf-insl-inp")
    private TextInputComponentImpl input;

    @FindBy(className = "rf-insl-hnd-cntr")
    private WebElement handleContainer;

    @FindBy(className = "rf-insl-hnd")
    private WebElement handle;

    @FindBy(className = "rf-insl-hnd-dis")
    private WebElement disabledHandle;

    @FindBy(className = "rf-insl-mn")
    private WebElement min;

    @FindBy(className = "rf-insl-mx")
    private WebElement max;

    @FindBy(css = "span.rf-insl-trc")
    private WebElement trackComponent;

    @FindBy(className = "rf-insl-tt")
    private WebElement tooltip;

    @FindBy(css = "span.rf-insl-trc")
    private WebElement sliderElement;

    @Drone
    private WebDriver browser;

    @Root
    private WebElement root;

    private AdvancedInteractions advancedInteractons;

    @Override
    public void slideToValue(double n) {
        advanced().dragHandleToPointInTrace((int) (n * advanced().getWidth()));
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractons == null) {
            advancedInteractons = new AdvancedInteractions();
        }
        return advancedInteractons;
    }

    public class AdvancedInteractions extends AbstractNumberInput.AdvancedInteractions {

        public void dragHandleToPointInTrace(int pixelInTrace) {
            Preconditions.checkArgument(pixelInTrace >= 0 && pixelInTrace <= getWidth(), "Cannot slide outside the trace.");
            if (!new WebElementConditionFactory(root).isVisible().apply(browser)) {
                throw new RuntimeException("Trace is not visible.");
            }
            scrollToView();
            Actions actions = new Actions(browser).clickAndHold(handle);
            actions.moveToElement(root, pixelInTrace, 0);
            actions.release(handle).build().perform();
        }

        public int getWidth() {
            return Utils.getLocations(handleContainer).getWidth();
        }

        public WebElement getRootElement() {
            return root;
        }

        public WebElement getDisabledHandleElement() {
            return disabledHandle;
        }

        public WebElement getHandleElement() {
            return handle;
        }

        public WebElement getMinimumElement() {
            return min;
        }

        public WebElement getMaximumElement() {
            return max;
        }

        public WebElement getTrackElement() {
            return trackComponent;
        }

        public WebElement getTooltipElement() {
            return tooltip;
        }

        public WebElement getSliderElement() {
            return sliderElement;
        }
    }

    private void scrollToView() {
        new Actions(browser).moveToElement(root).perform();
    }

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
}