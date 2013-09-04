package org.richfaces.tests.metamer.ftest.richTabPanel;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.tabPanel.RichFacesTabPanel;

;

public class TabPanelSimplePage extends MetamerPage {

    @FindByJQuery("div[id*=tabPanel]")
    private RichFacesTabPanel panelTab;

    @FindByJQuery("div[id*=tabPanel]")
    private WebElement panelTabAsWebElement;

    @FindByJQuery("input[id$=hCreateTabButton]")
    private WebElement createTabHButton;

    @FindByJQuery("input[id$=a4jCreateTabButton]")
    private WebElement createTabA4jButton;

    public WebElement getCreateTabButtonA4j() {
        return createTabA4jButton;
    }

    public WebElement getCreateTabButtonHButton() {
        return createTabHButton;
    }

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
