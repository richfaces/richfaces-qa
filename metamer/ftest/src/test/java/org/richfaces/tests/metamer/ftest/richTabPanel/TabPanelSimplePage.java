package org.richfaces.tests.metamer.ftest.richTabPanel;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.tabPanel.RichFacesTabPanel;

;

public class TabPanelSimplePage extends MetamerPage {

    @FindBy(className = "rf-tbp")
    private RichFacesTabPanel panelTab;

    @FindBy(className = "rf-tbp")
    private WebElement panelTabAsWebElement;

    @FindBy()
    private WebElement nextTabButton;

    @FindBy()
    private WebElement prevTabButton;

    public RichFacesTabPanel getPanelTab() {
        return panelTab;
    }

    /**
     * Return RichFacesTabPanel as WebElement to be used for visibility assertion in tests.
     *
     * @return WebElement
     */
    public WebElement getPanelTabAsWebElement() {
        return panelTabAsWebElement;
    }
}
