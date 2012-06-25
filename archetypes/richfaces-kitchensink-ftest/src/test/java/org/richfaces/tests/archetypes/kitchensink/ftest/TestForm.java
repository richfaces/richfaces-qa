package org.richfaces.tests.archetypes.kitchensink.ftest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.archetypes.kitchensink.ftest.page.MembersTable;
import org.richfaces.tests.archetypes.kitchensink.ftest.page.RegisterForm;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestForm extends AbstractKitchensinkTest {

    private RegisterForm registerForm;
    private MembersTable membersTable;
    
    private final String ERROR_MSG_CSV = "The number of error messages after client side validation is wrong!";

    @BeforeMethod(groups = "arquillian")
    public void initialiseWebElements() {
        registerForm = PageFactory.initElements(webDriver, RegisterForm.class);
        membersTable = PageFactory.initElements(webDriver, MembersTable.class);
    }

    @Test
    public void testAddCorrectMember() {

        String nameSet = registerForm.setCorrectName();
        registerForm.setCorrectPhone();
        registerForm.setCorrectEmail();

        registerForm.getRegisterButton().click();

        final int numberOfRowsBefore = membersTable.getNumberOfRows();
        (new WebDriverWait(webDriver, 4)).until(new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver d) {

                int numberOfRowsAfter = membersTable.getNumberOfRows();
                return (numberOfRowsBefore < numberOfRowsAfter);
            }
        });

        String table = membersTable.getTable().getText();
        assertTrue(table.contains(nameSet), "The new member was not added correctly!");
    }

    @Test(groups = "4.Future")
    public void testAddMemberCSVNamePatternViolation() {

        // workaround to be sure that the event was fired
        webDriverBackedSelenium.type(registerForm.getEMAIL_LOC(), "blah");
        webDriverBackedSelenium.fireEvent(registerForm.getEMAIL_LOC(), "click");

        webDriverBackedSelenium.type(registerForm.getNAME_LOC(), "122345");
        webDriverBackedSelenium.fireEvent(registerForm.getNAME_LOC(), "click");

        registerForm.setIncorrectNamePatternViolation();
        registerForm.setIncorrectEmailPatternViolation();

        assertEquals(registerForm.getErrorMessages().size(), 2, ERROR_MSG_CSV);
    }

    @Test
    public void testAddMemberCSVEmailPatternViolation() {

        // workaround to be sure that the event was fired
        webDriverBackedSelenium.type(registerForm.getEMAIL_LOC(), "blah");
        webDriverBackedSelenium.fireEvent(registerForm.getEMAIL_LOC(), "click");

        registerForm.setIncorrectEmailPatternViolation();

        assertEquals(registerForm.getErrorMessages().size(), 1, ERROR_MSG_CSV);
    }

    @Test
    public void testAddMemberCSVPhonePatternViolation() {

        // workaround to be sure that the event was fired
        webDriverBackedSelenium.type(registerForm.getPHONE_LOC(), "blah");
        webDriverBackedSelenium.fireEvent(registerForm.getPHONE_LOC(), "click");

        registerForm.setCorrectPhone();

        assertEquals(registerForm.getErrorMessages().size(), 1, ERROR_MSG_CSV);
    }

    @Test
    public void testAddMemberSSVEverythingWrong() {
        registerForm.setIncorrectEmailPatternViolation();
        registerForm.setIncorrectPhonePatternViolation();
        registerForm.setIncorrectNameTooShort();

        registerForm.getRegisterButton().click();

        assertEquals(registerForm.getErrorMessages().size(), 3,
            "The number of error messages after server side validation is wrong.");
    }
}
