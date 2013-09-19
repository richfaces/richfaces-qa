package org.richfaces.tests.page.fragments.test.choicePicker;

import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.testng.Arquillian;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.richfaces.tests.page.fragments.impl.utils.picker.MultipleChoicePicker;
import org.richfaces.tests.page.fragments.test.choicePicker.fragments.MyPageFragment;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

public class TestChoicePickerByWebElement extends Arquillian {

    @Drone
    private WebDriver browser;

    @FindBy(tagName = "body")
    private MyPageFragment myFragment;

    private List<String> getStringsFromElements(List<WebElement> list) {
        List<String> result = Lists.newArrayList();
        for (WebElement webElement : list) {
            result.add(webElement.getText());
        }
        return result;
    }

    @Test
    public void testPickByStyleClassEvenANDOddElements() {
        browser.get(TestChoicePickerByWebElement.class.getResource("choicePickerByWebElement.html").toExternalForm());
        MultipleChoicePicker picker = ChoicePickerHelper.byWebElement()
            .attribute("class").contains("even")
            .and()
            .attribute("class").contains("odd");
        List<WebElement> elements = picker.pickMultiple(myFragment.getDivs());
        assertEquals(getStringsFromElements(elements), Collections.EMPTY_LIST);
    }

    @Test
    public void testPickByStyleClassEvenElements() {
        browser.get(TestChoicePickerByWebElement.class.getResource("choicePickerByWebElement.html").toExternalForm());
        MultipleChoicePicker evenPicker = ChoicePickerHelper.byWebElement()
            .attribute("class").contains("even");
        List<WebElement> elements = evenPicker.pickMultiple(myFragment.getDivs());
        assertEquals(getStringsFromElements(elements), Lists.newArrayList("2", "4", "6"));
    }

    @Test
    public void testPickByStyleClassEvenElementsIsEqualToPickNotOdd() {
        browser.get(TestChoicePickerByWebElement.class.getResource("choicePickerByWebElement.html").toExternalForm());
        MultipleChoicePicker evenPicker = ChoicePickerHelper.byWebElement()
            .attribute("class").contains("even");
        MultipleChoicePicker notOddPicker = ChoicePickerHelper.byWebElement()
            .attribute("class").contains("odd").not();
        assertEquals(getStringsFromElements(evenPicker.pickMultiple(myFragment.getDivs())), Lists.newArrayList("2", "4", "6"));
        assertEquals(getStringsFromElements(notOddPicker.pickMultiple(myFragment.getDivs())), Lists.newArrayList("2", "4", "6"));
        assertEquals(getStringsFromElements(notOddPicker.pickMultiple(myFragment.getDivs())), getStringsFromElements(evenPicker.pickMultiple(myFragment.getDivs())));
    }

    @Test
    public void testPickByStyleClassFirstAndLastElements() {
        browser.get(TestChoicePickerByWebElement.class.getResource("choicePickerByWebElement.html").toExternalForm());
        MultipleChoicePicker oddPicker = ChoicePickerHelper.byWebElement()
            .attribute("class").contains("first")
            .and()
            .attribute("class").contains("last");
        List<WebElement> elements = oddPicker.pickMultiple(myFragment.getDivs());
        assertEquals(getStringsFromElements(elements), Lists.<String>newArrayList());
    }

    @Test
    public void testPickByStyleClassOddElements() {
        browser.get(TestChoicePickerByWebElement.class.getResource("choicePickerByWebElement.html").toExternalForm());
        MultipleChoicePicker oddPicker = ChoicePickerHelper.byWebElement()
            .attribute("class").contains("odd");
        List<WebElement> elements = oddPicker.pickMultiple(myFragment.getDivs());
        assertEquals(getStringsFromElements(elements), Lists.newArrayList("1", "3", "5"));
    }

    @Test
    public void testPickByStyleClassOddOrEvenElements() {
        browser.get(TestChoicePickerByWebElement.class.getResource("choicePickerByWebElement.html").toExternalForm());
        MultipleChoicePicker oddPicker = ChoicePickerHelper.byWebElement()
            .attribute("class").contains("odd")
            .or()
            .attribute("class").contains("even");
        List<WebElement> elements = oddPicker.pickMultiple(myFragment.getDivs());
        assertEquals(getStringsFromElements(elements), Lists.newArrayList("1", "2", "3", "4", "5", "6"));
    }

    @Test
    public void testPickByStyleClassOddOrFirstOrSecondOrLastElements() {
        browser.get(TestChoicePickerByWebElement.class.getResource("choicePickerByWebElement.html").toExternalForm());
        MultipleChoicePicker picker = ChoicePickerHelper.byWebElement()
            .attribute("class").contains("odd")
            .or()
            .attribute("class").contains("first")
            .or()
            .attribute("class").contains("second")
            .or()
            .attribute("class").contains("last");
        List<WebElement> elements = picker.pickMultiple(myFragment.getDivs());
        assertEquals(getStringsFromElements(elements), Lists.newArrayList("1", "2", "3", "5", "6"));
    }

    @Test
    public void testPickByStyleClassOddWithoutFirstElements() {
        browser.get(TestChoicePickerByWebElement.class.getResource("choicePickerByWebElement.html").toExternalForm());
        MultipleChoicePicker oddPicker = ChoicePickerHelper.byWebElement()
            .attribute("class").contains("odd")
            .and()
            .attribute("class").contains("first").not();
        List<WebElement> elements = oddPicker.pickMultiple(myFragment.getDivs());
        assertEquals(getStringsFromElements(elements), Lists.newArrayList("3", "5"));
    }

}
