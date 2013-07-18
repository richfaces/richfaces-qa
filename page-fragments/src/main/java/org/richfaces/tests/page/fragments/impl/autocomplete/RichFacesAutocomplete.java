package org.richfaces.tests.page.fragments.impl.autocomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.AutocompleteComponent;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.ClearType;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.Suggestion;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.SuggestionParser;
import org.jboss.arquillian.graphene.component.object.api.scrolling.ScrollingType;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class RichFacesAutocomplete<T> implements AutocompleteComponent<T> {

    public static final String CLASS_NAME_SUGG_LIST = "rf-au-lst-cord";
    public static final String CLASS_NAME_SUGG = "rf-au-itm";
    public static final String CLASS_NAME_SUGG_SELECTED = "rf-au-itm-sel";
    public static final String CSS_INPUT = "input[type='text']";

    private static final Logger LOGGER = Logger.getLogger(AutocompleteComponent.class.getName());

    @ArquillianResource
    private Actions actions;

    @Root
    private WebElement root;

    @FindBy(css = CSS_INPUT)
    private WebElement inputToWrite;

    private String separator = " ";
    private SuggestionParser<T> parser;
    private List<Suggestion<T>> selectedSuggestions = new ArrayList<Suggestion<T>>();

    @Override
    public boolean areSuggestionsAvailable() {
        WebElement suggList = getRightSuggestionList();
        return suggList == null ? false : suggList.isDisplayed();
    }

    @Override
    public void clear(ClearType... clearType) {
        if (clearType.length == 0) {
            inputToWrite.clear();
            return;
        }
        if (clearType.length > 1) {
            throw new IllegalArgumentException("The number of clear type method arguments should be one!");
        }

        int valueLength = inputToWrite.getAttribute("value").length();

        switch (clearType[0]) {
            case BACK_SPACE: {
                for (int i = 0; i < valueLength; i++) {
                    actions.sendKeys(inputToWrite, Keys.BACK_SPACE);
                }
                actions.build().perform();
                break;
            }
            case ESCAPE_SQ: {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < valueLength; i++) {
                    sb.append("\b");
                }
                inputToWrite.sendKeys(sb.toString());
                root.click();
                break;
            }
            case DELETE: {
                String ctrlADel = Keys.chord(Keys.CONTROL, "a", Keys.DELETE);
                actions.sendKeys(inputToWrite, ctrlADel).build().perform();
            }
        }
    }

    @Override
    public void finish() {
        actions.sendKeys(Keys.chord(Keys.SPACE, Keys.BACK_SPACE)).build().perform();
        root.findElement(By.xpath("//body")).click();
        waitForSuggestionsNotAvailable(Graphene.waitGui());
    }

    @Override
    public List<Suggestion<T>> getAllSuggestions() {
        checkParser();
        if (!areSuggestionsAvailable()) {
            return null;
        }
        List<Suggestion<T>> allSugg = new ArrayList<Suggestion<T>>();
        WebElement rightSuggList = getRightSuggestionList();
        List<WebElement> suggestions = rightSuggList.findElements(By.className(CLASS_NAME_SUGG));
        for (WebElement suggestion : suggestions) {
            allSugg.add(parser.parse(suggestion));
        }
        return allSugg;
    }

    @Override
    public List<Suggestion<T>> getSelectedSuggestions() {
        return selectedSuggestions;
    }

    @Override
    public List<String> getInputValues() {
        String currentInputValue = inputToWrite.getAttribute("value");
        return currentInputValue != null ? Arrays.asList(currentInputValue.split(separator)) : Collections.EMPTY_LIST;
    }

    @Override
    public void setSeparator(String regex) {
        this.separator = regex;
    }

    @Override
    public List<Suggestion<T>> getFirstNSuggestions(int n) {
        checkParser();
        List<Suggestion<T>> firstNSuggs = new ArrayList<Suggestion<T>>();

        if (!areSuggestionsAvailable()) {
            return null;
        }

        for (int i = 1; i <= n; i++) {
            firstNSuggs.add(getNthSuggestion(i));
        }

        return firstNSuggs;
    }

    @Override
    public Suggestion<T> getFirstSuggestion() {
        List<Suggestion<T>> suggestion = getFirstNSuggestions(1);

        if (suggestion != null) {
            return suggestion.get(0);
        }

        return null;
    }

    @Override
    public Suggestion<T> getNthSuggestion(int order) {
        checkParser();

        if (!areSuggestionsAvailable()) {
            return null;
        }

        WebElement rightSuggList = getRightSuggestionList();
        WebElement nthSuggestion = rightSuggList.findElement(By.cssSelector("." + CLASS_NAME_SUGG + ":nth-of-type(" + order
            + ")"));

        return parser.parse(nthSuggestion);
    }

    @Override
    public void type(String value) {
        inputToWrite.sendKeys(value);
        try {
            waitForSuggestionsAvailable(Graphene.waitGui());
        } catch (TimeoutException e) {
            LOGGER.log(Level.WARNING, "Suggestions aren't visible after typing into the input field.", e);
        }
    }

    @Override
    public List<Suggestion<T>> typeAndReturn(String value) {
        inputToWrite.sendKeys(value);
        try {
            waitForSuggestionsAvailable(Graphene.waitGui());
        } catch (TimeoutException ex) {
            // no suggestions available
            return null;
        }

        return getAllSuggestions();
    }

    public void autocomplete() {
        inputToWrite.sendKeys(Keys.RETURN);
    }

    @Override
    public boolean autocompleteWithSuggestion(Suggestion<T> sugg) {
        return autocomplete(sugg);
    }

    @Override
    public boolean autocompleteWithSuggestion(Suggestion<T> sugg, ScrollingType scrollingType) {
        return autocomplete(sugg, scrollingType);
    }

    @Override
    public void setSuggestionParser(SuggestionParser<T> parser) {
        this.parser = parser;
    }

    @Override
    public String getFirstInputValue() {
        List<String> inputValues = getInputValues();
        return !inputValues.isEmpty() ? inputValues.get(0) : null;
    }

    @Override
    public String getInputValue() {
        return inputToWrite.getAttribute("value");
    }

    protected boolean autocomplete(Suggestion<T> suggToCompleteWith, ScrollingType scrollingType) {
        if (!checkArgumentsAndThatSuggestionsAreAvailable(suggToCompleteWith, scrollingType)) {
            return false;
        }

        WebElement suggList = getRightSuggestionList();
        if (suggList == null) {
            throw new RuntimeException("The suggestions are available, but can not retrieve the right suggestion list!");
        }

        // get all suggestions and find the desired one
        List<WebElement> allSuggestions = suggList.findElements(By.className(CLASS_NAME_SUGG));
        // index for remembering how many times it will need to press key down to select suggestion
        int i = suggList.findElements(By.className(CLASS_NAME_SUGG_SELECTED)).isEmpty() ? 1 : 0;// when @selectFirst=true
        for (WebElement suggestion : allSuggestions) {
            if (suggestion.getText().equals(suggToCompleteWith.getValue())) {
                switch (scrollingType) {
                    case BY_KEYS:
                        // select the suggestion by pressing exact times down key and then enter
                        LOGGER.log(Level.FINE, "Scrolling by keys.");
                        // simulate scrolling by keys
                        for (int j = 0; j < i; j++) {
                            actions.sendKeys(Keys.DOWN);
                        }
                        actions.perform();

                        WebElement selectedSugg = suggList.findElement(By.className(CLASS_NAME_SUGG_SELECTED));
                        if (!selectedSugg.getText().equals(suggToCompleteWith.getValue())) {
                            actions.sendKeys(Keys.DOWN);
                            actions.perform();
                        }

                        actions.sendKeys(suggestion, Keys.NULL).perform();// ENTER | RETURN Key cause HTTP submit

                        // workaround for NoSuchElementException
                        Graphene.waitGui().until().element(root).is().present();
                        break;
                    default:
                    case BY_MOUSE:
                        // move the mouse over the right suggestion and click
                        LOGGER.log(Level.FINE, "Scrolling by mouse.");
                        suggestion.click();
                        break;
                }
                // add suggestion to the list of selected suggestions
                selectedSuggestions.add(suggToCompleteWith);
                return true;
            }
            i++;
        }
        return false;
    }

    protected boolean autocomplete(Suggestion<T> suggToCompleteWith) {
        return autocomplete(suggToCompleteWith, ScrollingType.BY_MOUSE);
    }

    protected void checkParser() {
        if (parser == null) {
            throw new IllegalStateException("The parser need to be set before executing this method!");
        }
    }

    protected boolean checkArgumentsAndThatSuggestionsAreAvailable(Object... arguments) {
        for (Object argument : arguments) {
            if (argument == null) {
                throw new IllegalArgumentException("Argument can not be null!");
            }
        }

        if (!areSuggestionsAvailable()) {
            return false;
        }

        return true;
    }

    /**
     * Returns suggestion list of this autocomplete, null if there is not any.
     *
     * @return
     */
    protected WebElement getRightSuggestionList() {
        // the problem here is that suggestion list object in DOM is moved out of the autocomplete component's form when it is
        // displayed, therefore at first it is neccessary to find correct suggestion list and then check if it is displayed
        List<WebElement> suggestionLists = root.findElements(By.xpath("//*[contains(@class,'" + CLASS_NAME_SUGG_LIST + "')]"));

        for (WebElement suggList : suggestionLists) {
            String idOfSuggLst = suggList.getAttribute("id");
            String idOfInput = inputToWrite.getAttribute("id");

            int index = idOfSuggLst.indexOf("List");
            boolean result = idOfInput.contains(idOfSuggLst.substring(0, index));

            if (result) {
                return suggList;
            }
        }

        return null;
    }

    protected void waitForSuggestionsAvailable(WebDriverWait<Void> wait) {
        wait.until().element(getRightSuggestionList()).is().visible();
    }

    protected void waitForSuggestionsNotAvailable(WebDriverWait<Void> wait) {
        wait.until().element(getRightSuggestionList()).is().not().visible();
    }
}
