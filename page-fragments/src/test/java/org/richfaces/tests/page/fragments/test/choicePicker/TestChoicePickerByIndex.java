package org.richfaces.tests.page.fragments.test.choicePicker;

import static org.testng.Assert.fail;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.testng.Arquillian;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByIndexChoicePicker;
import org.richfaces.tests.page.fragments.test.choicePicker.fragments.MyPageFragment;
import org.testng.annotations.Test;

public class TestChoicePickerByIndex extends Arquillian {

    @Drone
    private WebDriver browser;

    @FindBy(tagName = "body")
    private MyPageFragment myFragment;

    @Test
    public void testPickNonExistingElement() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().index(15);
        try {
            WebElement element = picker.pick(myFragment.getDivs());
            fail("IndexOutOfBounds should be thrown!");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
    }
}