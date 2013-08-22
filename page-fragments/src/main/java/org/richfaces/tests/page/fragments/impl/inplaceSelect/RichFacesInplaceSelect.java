package org.richfaces.tests.page.fragments.impl.inplaceSelect;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.inplaceInput.AbstractConfirmOrCancel;
import org.richfaces.tests.page.fragments.impl.inplaceInput.ConfirmOrCancel;
import org.richfaces.tests.page.fragments.impl.inplaceInput.InplaceComponentState;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

public class RichFacesInplaceSelect implements InplaceSelect {

    @FindBy(css = "input[id$=Okbtn]")
    private WebElement confirmButton;

    @FindBy(css = "input[id$=Cancelbtn]")
    private WebElement cancelButton;

    @FindBy(className = "rf-is-fld")
    private TextInputComponentImpl textInput;

    @FindBy(css = ".rf-is-lbl")
    private WebElement label;

    @FindBy(css = "span[id$=Edit] > input[id$=Input]")
    private WebElement editInputElement;

    @FindBy(tagName = "script")
    private WebElement script;

    @FindBy(css = "span.rf-is-lst-cord")
    private WebElement localList;

    @FindBy(xpath = "//body/span[contains(@class, rf-is-lst-cord)]")
    // whole page search
    private WebElement globalList;

    private static final String OPTIONS_CLASS = "rf-is-opt";

    @Root
    private WebElement root;

    private Event editByEvent = Event.CLICK;

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private JavascriptExecutor executor;

    private AdvancedInteractions advancedInteractions;

    @Override
    public ConfirmOrCancel select(ChoicePicker picker) {
        advanced().switchToEditingState();
        WebElement optionToBeSelected = picker.pick(advanced().getOptions());
        optionToBeSelected.click();
        if (isSaveOnSelect() && !isShowControlls()) {
            textInput.advanced().trigger("blur");
            waitForPopupHide();
        }
        return new ConfirmOrCancelImpl();
    }

    @Override
    public ConfirmOrCancel select(int index) {
        return select(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public ConfirmOrCancel select(String text) {
        return select(ChoicePickerHelper.byVisibleText().match(text));
    }

    @Override
    public TextInputComponentImpl getTextInput() {
        return textInput;
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class ConfirmOrCancelImpl extends AbstractConfirmOrCancel {

        @Override
        public WebDriver getBrowser() {
            return browser;
        }

        @Override
        public WebElement getConfirmButton() {
            return confirmButton;
        }

        @Override
        public WebElement getInput() {
            return textInput.advanced().getInput();
        }

        @Override
        public WebElement getCancelButton() {
            return cancelButton;
        }

        @Override
        public void waitAfterConfirmOrCancel() {
            waitForPopupHide();
        }
    }

    public class AdvancedInteractions {

        private static final String RF_IS_CHNG_CLASS = "rf-is-chng";
        private static final String RF_IS_ACT_CLASS = "rf-is-act";

        public void setEditByEvent(Event event) {
            editByEvent = event;
        }

        public WebElement getRootElement() {
            return root;
        }

        public void switchToEditingState() {
            Utils.triggerJQ(executor, editByEvent.getEventName(), root);
        }

        public List<WebElement> getOptions() {
            return browser.findElements(By.className(OPTIONS_CLASS));
        }

        public boolean isInState(InplaceComponentState state) {
            return root.getAttribute("class").contains(getClassForState(state));
        }

        public String getClassForState(InplaceComponentState state) {
            switch (state) {
                case ACTIVE:
                    return RF_IS_ACT_CLASS;
                case CHANGED:
                    return RF_IS_CHNG_CLASS;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public String getLabelValue() {
            return label.getText().trim();
        }

        public WebElement getEditInputElement() {
            return editInputElement;
        }

        public WebElement getLabelInputElement() {
            return label;
        }

        public WebElement getCancelButtonElement() {
            return cancelButton;
        }

        public WebElement getConfirmButtonElement() {
            return confirmButton;
        }

        public WebElement getSelectedOption() {
            for (WebElement element : getOptions()) {
                if (element.getAttribute("class").contains("rf-is-sel")) {
                    return element;
                }
            }
            return null;
        }
    }

    private boolean isSaveOnSelect() {
        String text = Utils.returningJQ(executor, "text()", script);// getting text from hidden element
        if (text.contains("\"saveOnSelect\":false")) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean isShowControlls() {
        return new WebElementConditionFactory(cancelButton).isPresent().apply(browser);
    }

    private void waitForPopupHide() {
        Graphene.waitModel().until().element(localList).is().present();
        Graphene.waitModel().until().element(globalList).is().not().visible();
    }
}