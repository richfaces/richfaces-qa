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

    @FindBy(xpath = "//td[contains(@class, 'rf-tab-hdr-act')]")
    private List<WebElement> allActiveHeaders;

    @FindBy(xpath = "//td[contains(@class, 'rf-tab-hdr-inact')]")
    private List<WebElement> allInactiveHeaders;

    @FindBy(xpath = "//td[contains(@class, 'rf-tab-hdr-dis')]")
    private List<WebElement> allDisabledHeaders;

    @FindBy(xpath = "//form/div/div[contains(@id, 'form:tab')]")
    private List<WebElement> allTabContents;

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

        public List<WebElement> getAllActiveHeaders() {
            return allActiveHeaders;
        }

        public List<WebElement> getAllInactiveHeaders() {
            return allInactiveHeaders;
        }

        public List<WebElement> getAllDisabledHeaders() {
            return allDisabledHeaders;
        }

        public List<WebElement> getAllTabContents() {
            return allTabContents;
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
