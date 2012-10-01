package org.richfaces.tests.page.fragments.ftest.autocomplete;

import static org.richfaces.tests.page.fragments.impl.autocomplete.AutocompleteComponentImpl.CLASS_NAME_SUGG_LIST;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.ClearType;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.Suggestion;
import org.jboss.arquillian.graphene.component.object.api.scrolling.ScrollingType;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.page.fragments.ftest.AbstractTest;
import org.richfaces.tests.page.fragments.ftest.bean.Person;
import org.richfaces.tests.page.fragments.ftest.bean.PersonBean;
import org.richfaces.tests.page.fragments.impl.autocomplete.AutocompleteComponentImpl;
import org.richfaces.tests.page.fragments.impl.autocomplete.SuggestionImpl;
import org.richfaces.tests.page.fragments.impl.autocomplete.TextSuggestionParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestAutocompleteComponent extends AbstractTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return createDeployment(TestAutocompleteComponent.class).addClass(PersonBean.class).addClass(Person.class);
    }

    @FindBy(xpath = "//*[contains(@id,'autocomplete')]")
    private AutocompleteComponentImpl<String> autocompleteComponent;

    // @FindBy(xpath = "//*[contains(@id,'autocomplete2')]")
    // private AutocompleteComponentImpl<String> autocompleteComponent2;

    @FindBy(className = CLASS_NAME_SUGG_LIST)
    WebElement suggestionList1;

    @FindBy(xpath = "(//input[@type='text'])[1]")
    WebElement inputToWrite;

    // @FindBy(xpath = "(//input[@type='text'])[2]")
    // WebElement inputToWrite2;

    private List<Suggestion<String>> expectedSuggForB;
    private List<Suggestion<String>> expectedSuggForBr;
    private List<Suggestion<String>> expectedSuggForBrn;

    @BeforeMethod
    public void initializeAutocomplete() {
        autocompleteComponent.setSuggestionParser(new TextSuggestionParser());
    }

    // ##################################################################################################################
    // Tests
    // ##################################################################################################################

    @Test
    public void testAreSuggestionsAvailable() {
        boolean result = autocompleteComponent.areSuggestionsAvailable();
        assertFalse(result, "Nothing was written into autocomplete input, therefore no suggestion should be available.");

        inputToWrite.sendKeys("X");
        result = autocompleteComponent.areSuggestionsAvailable();
        assertFalse(result, "No suggestion should available, as for written input no suggestions exist!");
        inputToWrite.clear();

        inputToWrite.sendKeys("B");
        waitForSuggestions(2);
        result = autocompleteComponent.areSuggestionsAvailable();
        assertTrue(result, "Suggestion should be available as there was input for which suggestions do exist");
    }

    @Test
    public void testAreSuggestionsAvailableWhenOnceRendered() {
        inputToWrite.sendKeys("Br");
        waitForSuggestions(2);

        inputToWrite.sendKeys("X");
        boolean result = autocompleteComponent.areSuggestionsAvailable();
        assertFalse(result, "The result of finding out whether suggestions are available should be different!");
    }

    @Test
    public void testClearByBackspace() {
        checkClearInput(ClearType.BACK_SPACE);
    }

    @Test
    public void testClearByDelete() {
        checkClearInput(ClearType.DELETE);
    }

    @Test
    public void testClearByEscapeSequence() {
        checkClearInput(ClearType.ESCAPE_SQ);
    }

    @Test
    public void testClearWithoutPassingClearTypeArgument() {
        checkClearInput();
    }

    @Test
    public void testClearIllegalNumberOfArguments() {
        try {
            checkClearInput(ClearType.BACK_SPACE, ClearType.ESCAPE_SQ);
        } catch (IllegalArgumentException ex) {
            // ok
            return;
        }
        fail("There should be Illegal argument exception thrown, as there was wrong number of arguments!");
    }

    @Test
    public void testFinish() {
        String expected = "Br";
        inputToWrite.sendKeys(expected);
        waitForSuggestions(2);

        autocompleteComponent.finish();

        String actual = inputToWrite.getAttribute("value");
        assertEquals(actual, expected,
            "The value in the input should be different! It should be the one directly written to it, not the suggestion!");

        assertFalse(suggestionList1.isDisplayed(), "The suggestion list should not be displayed!");
    }

    @Test
    public void testGetAllSugestions() {
        initExpectedSuggestionsLists();

        checkGetAllSuggestionsFor("B", expectedSuggForB);

        checkGetAllSuggestionsFor("Br", expectedSuggForBr);

        checkGetAllSuggestionsFor("Brn", expectedSuggForBrn);
    }

    @Test
    public void testType() {
        String expected = "B";
        autocompleteComponent.type(expected);

        String actual = inputToWrite.getAttribute("value");
        assertEquals(actual, expected, "The input value is different than what was type there!");

        expected = "Br";
        autocompleteComponent.type("r");

        actual = inputToWrite.getAttribute("value");
        assertEquals(actual, expected, "The input value is different than what was type there!");
    }

    @Test
    public void testTypeWithParser() {
        initExpectedSuggestionsLists();
        String input = "B";

        List<Suggestion<String>> suggestions = autocompleteComponent.typeAndReturn(input);
        assertEquals(inputToWrite.getAttribute("value"), input, "The value was not written into input correclty!");
        assertTrue(suggestions.containsAll(expectedSuggForB), "Suggestions for input: " + input + " are wrong!");
        assertEquals(suggestions.size(), expectedSuggForB.size(), "Retrieved suggestions has some redundant suggestions.");

        input = "Br";
        suggestions = autocompleteComponent.typeAndReturn("r");
        assertEquals(inputToWrite.getAttribute("value"), input, "The value was not written into input correclty!");
        assertTrue(suggestions.containsAll(expectedSuggForBr), "Suggestions for input: " + input + " are wrong!");
        assertEquals(suggestions.size(), expectedSuggForBr.size(), "Retrieved suggestions has some redundant suggestions.");
    }

    @Test
    public void testAutocompleteWithSuggestion() {
        String expected = "Brno";
        String charToWrite = "B";
        Suggestion<String> brno = new SuggestionImpl<String>(expected);

        inputToWrite.sendKeys(charToWrite);
        waitForSuggestions(2);

        boolean result = autocompleteComponent.autocompleteWithSuggestion(brno);

        String inputValue = inputToWrite.getAttribute("value").trim();
        assertEquals(inputValue, expected, "It was not autocompleted correctly!");
        assertTrue(result, "The return value of method should be true as it should be autocompleted correctly!");

        inputToWrite.clear();
        inputToWrite.sendKeys(charToWrite);

        Suggestion<String> notExisting = new SuggestionImpl<String>("Brn");
        result = autocompleteComponent.autocompleteWithSuggestion(notExisting);

        inputValue = inputToWrite.getAttribute("value").trim();
        assertFalse(result, "The return value of method should be false! It was not autocompleted!");
        assertEquals(inputValue, charToWrite, "The input should not change!");
    }

    @Test
    public void testAutocompleteWithSuggestionNullArguments() {
        String failMsg = "IllegalArgumentException should be thrown!";

        try {
            autocompleteComponent.autocompleteWithSuggestion(null);
            fail(failMsg);
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            autocompleteComponent.autocompleteWithSuggestion(null, null);
            fail(failMsg);
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    @Test
    public void testAutocompleteWithSuggestionWhenNoSuggAvailable() {
        inputToWrite.sendKeys("X");

        SuggestionImpl<String> sugg = new SuggestionImpl<String>("Brno");
        boolean result = autocompleteComponent.autocompleteWithSuggestion(sugg);
        assertFalse(result, "The result of autocomplete method should be false, as there are no suggestion available!");
    }

    @Test
    public void testAutocompleteWithSuggestionAndScrollingTypeByKeys() {
        checkAutocompleteWithDefinedScrollingType(ScrollingType.BY_KEYS);
    }

    @Test
    public void testAutocompleteWithSuggestionAndScrollingTypeByMouse() {
        checkAutocompleteWithDefinedScrollingType(ScrollingType.BY_MOUSE);
    }

    @Test
    public void testGetFirstNSuggestions() {
        List<Suggestion<String>> firstThreeExpected = new ArrayList<Suggestion<String>>();
        firstThreeExpected.add(new SuggestionImpl<String>("Brno"));
        firstThreeExpected.add(new SuggestionImpl<String>("Bratislava"));
        firstThreeExpected.add(new SuggestionImpl<String>("Bruges"));

        inputToWrite.sendKeys("Br");

        int expected = 3;
        List<Suggestion<String>> retrievedSuggestions = autocompleteComponent.getFirstNSuggestions(expected);

        assertEquals(retrievedSuggestions.size(), expected, "The size of retrieved list with suggestions is wrong!");
        assertTrue(retrievedSuggestions.containsAll(firstThreeExpected), "The retrieved " + expected
            + " suggestions are wrong!");
    }

    @Test
    public void testGetFirstSuggestion() {
        Suggestion<String> expected = new SuggestionImpl<String>("Brno");

        inputToWrite.sendKeys("B");

        Suggestion<String> retrievedSuggestion = autocompleteComponent.getFirstSuggestion();

        assertEquals(retrievedSuggestion, expected, "The retrieved first suggestion is wrong!");

    }

    @Test
    public void testGetNthSuggestion() {
        Suggestion<String> expected = new SuggestionImpl<String>("Bratislava");

        inputToWrite.sendKeys("B");

        Suggestion<String> retrievedSuggestion = autocompleteComponent.getNthSuggestion(2);

        assertEquals(retrievedSuggestion, expected, "The retrieved nth suggestion is wrong!");
    }

    @Test
    public void testGetSelectedSuggestions() {
        Suggestion<String> brno = new SuggestionImpl<String>("Brno");
        Suggestion<String> bratislva = new SuggestionImpl<String>("Bratislava");

        inputToWrite.sendKeys("B");

        autocompleteComponent.autocompleteWithSuggestion(brno);
        autocompleteComponent.type(" ");

        inputToWrite.sendKeys("B");

        autocompleteComponent.autocompleteWithSuggestion(bratislva);

        autocompleteComponent.type(" ");
        autocompleteComponent.type("Xlala");
        autocompleteComponent.finish();

        List<Suggestion<String>> selectedSuggestions = autocompleteComponent.getSelectedSuggestions();
        assertEquals(selectedSuggestions.size(), 2, "The returned list with selected suggestions has wrong size!");
        assertTrue(selectedSuggestions.contains(brno), "The returned list should contain suggestion: " + brno);
        assertTrue(selectedSuggestions.contains(bratislva), "The returned list should contain suggestion: " + bratislva);
    }

    // ##################################################################################################################
    // Help Methods
    // ##################################################################################################################

    private void checkAutocompleteWithDefinedScrollingType(ScrollingType scrollingType) {
        inputToWrite.sendKeys("B");
        String expected = "Berlin";

        SuggestionImpl<String> sugg = new SuggestionImpl<String>(expected);
        boolean result = autocompleteComponent.autocompleteWithSuggestion(sugg, scrollingType);
        assertTrue(result, "The result of autocompletition should be true");

        String actual = inputToWrite.getAttribute("value").trim();
        assertEquals(actual, expected, "The value should be autocompleted correctly");
    }

    private void checkGetAllSuggestionsFor(String input, List<Suggestion<String>> expectedSugg) {
        inputToWrite.sendKeys(input);
        waitForSuggestions(2);

        List<Suggestion<String>> actualSuggestions = autocompleteComponent.getAllSuggestions();

        assertTrue(actualSuggestions.containsAll(expectedSugg), "Suggestions for input: " + input + " are wrong!");
        assertEquals(actualSuggestions.size(), expectedSugg.size(), "Retrieved suggestions has some redundant suggestions.");
        inputToWrite.clear();
    }

    private void initExpectedSuggestionsLists() {
        SuggestionImpl<String> brno = new SuggestionImpl<String>("Brno");
        SuggestionImpl<String> bratislava = new SuggestionImpl<String>("Bratislava");
        SuggestionImpl<String> berlin = new SuggestionImpl<String>("Berlin");
        SuggestionImpl<String> bruges = new SuggestionImpl<String>("Bruges");
        SuggestionImpl<String> bradford = new SuggestionImpl<String>("Bradford");
        SuggestionImpl<String> bremen = new SuggestionImpl<String>("Bremen");

        expectedSuggForB = new ArrayList<Suggestion<String>>();
        expectedSuggForB.add(berlin);
        expectedSuggForB.add(bratislava);
        expectedSuggForB.add(brno);
        expectedSuggForB.add(bruges);
        expectedSuggForB.add(bradford);
        expectedSuggForB.add(bremen);

        expectedSuggForBr = new ArrayList<Suggestion<String>>();
        expectedSuggForBr.add(bratislava);
        expectedSuggForBr.add(brno);
        expectedSuggForBr.add(bruges);
        expectedSuggForBr.add(bradford);
        expectedSuggForBr.add(bremen);

        expectedSuggForBrn = new ArrayList<Suggestion<String>>();
        expectedSuggForBrn.add(brno);
    }

    private void checkClearInput(ClearType... clearType) {
        String testString = "test";
        inputToWrite.sendKeys(testString);

        String inputValue = inputToWrite.getAttribute("value");
        assertEquals(inputValue, testString, "Nothing was written to input!");

        if (clearType.length == 0) {
            autocompleteComponent.clear();
        } else {
            autocompleteComponent.clear(clearType);
        }
        inputValue = inputToWrite.getAttribute("value");
        assertTrue(inputValue.isEmpty(), "The input should be empty but was: " + inputValue);

        inputToWrite.clear();
    }

    private void waitForSuggestions(int timeout) {
        (new WebDriverWait(webDriver, timeout)).until(new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver d) {
                return suggestionList1.isDisplayed();
            }
        });
    }
}
