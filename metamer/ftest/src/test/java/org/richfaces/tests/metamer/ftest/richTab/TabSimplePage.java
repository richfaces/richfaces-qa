package org.richfaces.tests.metamer.ftest.richTab;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.tabPanel.RichFacesTabPanel;

public class TabSimplePage extends MetamerPage {

    @FindByJQuery(value = "div[id$=tabPanel]")
    private RichFacesTabPanel tab;

    @FindByJQuery(value="div[id$=tabPanel]")
    private WebElement tabAsWebElement;

    @FindByJQuery(value = "div[id*=tab] > div.rf-tab-cnt")
    private List<WebElement> itemContents;

    @FindByJQuery(value = "td[id*='tab'].[id$=':header:active']")
    private List<WebElement> activeHeaders;

    @FindByJQuery(value = "td[id*='tab'].[id$=':header:inactive']")
    private List<WebElement> inactiveHeaders;

    @FindByJQuery(value = "td[id*='tab'].[id$=':header:disabled']")
    private List<WebElement> disabledHeaders;

    @FindByJQuery(value = "div[id$=tab1]")
    private WebElement firstTabContentParentElement;

    public RichFacesTabPanel getTab() {
        return tab;
    }

    public List<WebElement> getItemContents() {
        return itemContents;
    }

    public List<WebElement> getActiveHeaders() {
        return activeHeaders;
    }

    public List<WebElement> getInactiveHeaders() {
        return inactiveHeaders;
    }

    public List<WebElement> getDisabledHeaders() {
        return disabledHeaders;
    }

    public WebElement getFirstTabContentParentElement() {
        return firstTabContentParentElement;
    }

    public WebElement getTabAsWebElement () {
        return tabAsWebElement;
    }
}
