package org.richfaces.tests.metamer.ftest.richInplaceInput;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.inplaceInput.RichFacesInplaceInput;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

public class TestRefactoredInplaceInputShowcase extends AbstractWebDriverTest {

    private final Attributes<InplaceInputAttributes> inplaceInputAttributes = getAttributes();

    @FindBy(className = "rf-ii")
    private RichFacesInplaceInput inplaceInput;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceInput/simple.xhtml");
    }

    @Test
    public void showcase1() {
        String expected = "RichFaces 4";
        inplaceInputAttributes.set(InplaceInputAttributes.showControls, true);
        inplaceInput.type(expected).cancelByControlls();
        assertEquals(inplaceInput.getTextInput().getStringValue(), expected);

        inplaceInput.type(expected).cancel();
        assertEquals(inplaceInput.getTextInput().getStringValue(), expected);

        expected = "foo bar";
        inplaceInput.type(expected).confirmByControlls();
        assertEquals(inplaceInput.getTextInput().getStringValue(), expected);

        inplaceInput.getTextInput().clear();

        inplaceInput.type(expected).confirm();
        assertEquals(inplaceInput.getTextInput().getStringValue(), expected);
    }
}
