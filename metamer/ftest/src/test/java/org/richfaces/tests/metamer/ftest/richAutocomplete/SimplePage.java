package org.richfaces.tests.metamer.ftest.richAutocomplete;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class SimplePage extends MetamerPage {

    @FindBy(id="form:output")
    private WebElement output;
    @FindBy(id="locale")
    private WebElement locale;

    public void blur() {
        locale.click();
    }

    public WebElement getOutput() {
        return output;
    }

}
