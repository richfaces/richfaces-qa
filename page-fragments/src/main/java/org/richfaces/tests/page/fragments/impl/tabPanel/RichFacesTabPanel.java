package org.richfaces.tests.page.fragments.impl.tabPanel;

import java.util.List;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.switchable.AbstractSwitchableComponent;

public class RichFacesTabPanel extends AbstractSwitchableComponent<RichFacesTab> {

    @FindBy(jquery = ".rf-tab-hdr:visible")
    private List<WebElement> switcherControllerElements;

    @FindBy(jquery = ".rf-tab:visible")
    private WebElement rootOfContainerElement;

    @FindBy(jquery = ".rf-tab-hdr-act:visible")
    private WebElement activeHeader;

    private AdvancedInteractions advancedInteractions;

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions extends AbstractSwitchableComponent<RichFacesTab>.AdvancedInteractions {
        public WebElement getActiveHeader() {
            return activeHeader;
        }
    }

    public int getNumberOfTabs() {
        return switcherControllerElements.size();
    }

    @Override
    protected List<WebElement> getSwitcherControllerElements() {
        return switcherControllerElements;
    }

    @Override
    protected WebElement getRootOfContainerElement() {
        return rootOfContainerElement;
    }

}
