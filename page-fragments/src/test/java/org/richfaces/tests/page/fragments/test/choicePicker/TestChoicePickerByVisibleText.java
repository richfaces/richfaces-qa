package org.richfaces.tests.page.fragments.test.choicePicker;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.testng.Arquillian;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByVisibleTextChoicePicker;
import org.richfaces.tests.page.fragments.test.choicePicker.fragments.MyPageFragment;
import org.testng.annotations.Test;

public class TestChoicePickerByVisibleText extends Arquillian {

    @Drone
    private WebDriver browser;

    @FindBy(tagName = "body")
    private MyPageFragment myFragment;

    @Test
    public void testMultipleChoicePickerNonExistingElement() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByVisibleTextChoicePicker picker = ChoicePickerHelper.byVisibleText().match("non existing");
        List<WebElement> elements = picker.pickMultiple(myFragment.getDivs());
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testPickExistingElement() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByVisibleTextChoicePicker picker = ChoicePickerHelper.byVisibleText().match("1");
        WebElement element = picker.pick(myFragment.getDivs());
        assertNotNull(element);
        assertEquals(element.getText(), "1");
    }

    @Test
    public void testPickNotExistingElement() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByVisibleTextChoicePicker picker = ChoicePickerHelper.byVisibleText().match("non existing");
        WebElement element = picker.pick(myFragment.getDivs());
        assertNull(element);
    }
}
