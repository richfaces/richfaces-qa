package org.richfaces.tests.metamer.ftest.richFocus;

import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

public class AbstractFocusPage extends MetamerPage {

    protected static final String EXPECTED_STRING = "RichFaces";

    protected void typeStringAndDoNotCareAboutFocus() {
        Actions builder = new Actions(driver);

        builder.sendKeys(EXPECTED_STRING);
        builder.build().perform();
    }
}
