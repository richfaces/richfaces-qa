package org.richfaces.tests.showcase.editor.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

public class AdvancedConfigurationPage {

    public static final String NEW_PAGE_ENG = "New Page";
    public static final String NEW_PAGE_FR = "Nouvelle page";
    public static final String NEW_PAGE_DE = "Neue Seite";

    @FindByJQuery("input[type=radio]:eq(0)")
    public WebElement englishRadio;

    @FindByJQuery("input[type=radio]:eq(1)")
    public WebElement frenchRadio;

    @FindByJQuery("input[type=radio]:eq(2)")
    public WebElement germanRadio;

    @FindByJQuery(".cke_button_newpage")
    public WebElement newPageButton;
}
