package org.richfaces.tests.metamer.ftest.richFocus;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.test.selenium.support.ui.ElementIsFocused;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.AttributeList;
import org.testng.annotations.Test;

public class TestPreserve extends AbstractWebDriverTest {

    @Page
    private FocusSimplePage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFocus/preserve.xhtml");
    }

    @Test(groups = "Future")
    // false negative - should be fixed
    public void testPreserveTrueValidationAwareTrue() {
        testPreserveTrue();
    }

    @Test(groups = "Future")
    // false negative - should be fixed
    public void testPreserveTrueValidationAwareFalse() {
        AttributeList.focusAttributes.set(FocusAttributes.validationAware, false);

        testPreserveTrue();
    }

    private void testPreserveTrue() {
        page.getAgeInput().focus();
        page.getAgeInput().getInput().click();

        page.ajaxValidateInputs();
        waitModel().until(new ElementIsFocused(page.getAgeInput().getInput()));

        page.typeStringAndDoNotCareAboutFocus();

        String actual = page.getAgeInput().getStringValue();
        assertTrue(actual.contains(AbstractFocusPage.EXPECTED_STRING),
            "The age input should be focused, since the preserve is true and before form submission that input was focused!");
    }

}
