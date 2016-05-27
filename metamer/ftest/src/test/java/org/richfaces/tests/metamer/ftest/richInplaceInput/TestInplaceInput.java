package org.richfaces.tests.metamer.ftest.richInplaceInput;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.inplaceInput.RichFacesInplaceInput;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

public class TestInplaceInput extends AbstractWebDriverTest {

    private final Attributes<InplaceInputAttributes> inplaceInputAttributes = getAttributes();

    @FindBy(className = "rf-ii")
    private RichFacesInplaceInput inplaceInput;

    @Override
    public String getComponentTestPagePath() {
        return "richInplaceInput/simple.xhtml";
    }

    @Test(groups = "smoke")
    public void testTypingAndConfirming() {
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
