package org.richfaces.tests.page.fragments.impl.accordion;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.switchable.ComponentContainer;

public class RichFacesAccordionItem implements ComponentContainer {

    @Root
    private WebElement root;

    private static final String ACTIVE_HEADER_CLASS = "rf-ac-itm-lbl-act";
    private static final String DISABLED_HEADER_CLASS = "rf-ac-itm-lbl-dis";
    private static final String INACTIVE_HEADER_CLASS = "rf-ac-itm-lbl-inact";
    private static final String CONTENT_CLASS = "rf-ac-itm-cnt";
    private static final String TO_ACTIVATE_CLASS = "rf-ac-itm-hdr";

    @FindBy(className = ACTIVE_HEADER_CLASS)
    private GrapheneElement activeHeader;

    @FindBy(className = DISABLED_HEADER_CLASS)
    private GrapheneElement disabledHeader;

    @FindBy(className = INACTIVE_HEADER_CLASS)
    private GrapheneElement inactiveHeader;

    @FindBy(className = CONTENT_CLASS)
    private GrapheneElement content;

    @FindBy(className = TO_ACTIVATE_CLASS)
    private GrapheneElement toActivate;

    private AdvancedInteractions advancedInteractions;

    @Override
    public <T> T getContent(Class<T> clazz) {
        return Graphene.createPageFragment(clazz, root);
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions {
        public String getHeader() {
            if (isActive()) {
                return getActiveHeaderElement().getText();
            } else if (isEnabled()) {
                return getInactiveHeaderElement().getText();
            } else {
                return getDisabledHeaderElement().getText();
            }
        }

        public final WebElement getHeaderElement() {
            if (isActive()) {
                return getActiveHeaderElement();
            } else if (isEnabled()) {
                return getInactiveHeaderElement();
            } else {
                return getDisabledHeaderElement();
            }
        }

        public boolean isActive() {
            return getActiveHeaderElement().isPresent() && getActiveHeaderElement().isDisplayed() && getContentElement().isDisplayed();
        }

        public boolean isEnabled() {
            return !getDisabledHeaderElement().isPresent();
        }

        public final WebElement getContentElement() {
            return content;
        }

        public final WebElement getToActivateElement() {
            return toActivate;
        }
    }

    protected final GrapheneElement getActiveHeaderElement() {
        return activeHeader;
    }

    protected final GrapheneElement getDisabledHeaderElement() {
        return disabledHeader;
    }

    protected final WebElement getInactiveHeaderElement() {
        return inactiveHeader;
    }
}
