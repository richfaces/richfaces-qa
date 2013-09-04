package org.richfaces.tests.page.fragments.impl.inplaceInput;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.utils.Event;

public class RichFacesInplaceInput implements InplaceInput {

    @FindBy(className = "rf-ii-fld")
    private TextInputComponentImpl textInput;

    @FindByJQuery(".rf-ii-btn:eq(0)")
    private WebElement confirmButton;

    @FindByJQuery(".rf-ii-btn:eq(1)")
    private WebElement cancelButton;

    @FindBy(css = "span[id$=Label]")
    private WebElement label;

    @FindBy(css = "span[id$=Edit] span[id$=Btn]")
    private WebElement controls;

    @FindBy(css = "span[id$=Edit] > input[id$=Input]")
    private WebElement editInputElement;

    private Event editByEvent = Event.CLICK;

    @Root
    private WebElement root;

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private JavascriptExecutor executor;

    private AdvancedInteractions advancedInteractions;

    @Override
    public TextInputComponentImpl getTextInput() {
        return textInput;
    }

    @Override
    public ConfirmOrCancel type(String text) {
        Utils.triggerJQ(executor, editByEvent.getEventName(), root);
        if (!advanced().isInState(InplaceComponentState.ACTIVE)) {
            throw new IllegalStateException("You should set correct editBy event. Current: " + editByEvent
                + " did not changed the inplace input for editing!");
        }
        textInput.clear();
        textInput.sendKeys(text);
        return new ConfirmOrCancelImpl();
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
            return editInputElement;
        }

        @Override
        public WebElement getCancelButton() {
            return cancelButton;
        }

        @Override
        public void waitAfterConfirmOrCancel() {

        }
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions {

        private static final String RF_II_CHNG_CLASS = "rf-ii-chng";
        private static final String RF_II_ACT_CLASS = "rf-ii-act";

        public void setEditByEvent(Event event) {
            editByEvent = event;
        }

        public WebElement getRootElement() {
            return root;
        }

        public boolean isInState(InplaceComponentState state) {
            return root.getAttribute("class").contains(getClassForState(state));
        }

        public String getClassForState(InplaceComponentState state) {
            switch (state) {
                case ACTIVE:
                    return RF_II_ACT_CLASS;
                case CHANGED:
                    return RF_II_CHNG_CLASS;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public String getLabelValue() {
            return label.getText().trim();
        }

        public WebElement getCancelButtonElement() {
            return cancelButton;
        }

        public WebElement getConfirmButtonElement() {
            return confirmButton;
        }

        public WebElement getEditInputElement() {
            return editInputElement;
        }

        public WebElement getLabelInputElement() {
            return label;
        }
    }
}
