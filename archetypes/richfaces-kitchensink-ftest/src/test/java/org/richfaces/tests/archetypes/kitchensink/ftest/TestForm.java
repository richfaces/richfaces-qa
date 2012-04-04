package org.richfaces.tests.archetypes.kitchensink.ftest;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
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

    @BeforeMethod
    public void initialiseWebElements() {
        registerForm = PageFactory.initElements(webDriver, RegisterForm.class);
        membersTable = PageFactory.initElements(webDriver, MembersTable.class);
    }

    @Test
    public void testAddMemberIncorrectName() {

        String name = "Andrasi Oliver";
        registerForm.getNameInput().sendKeys(name);
        registerForm.getEmailInput().sendKeys("ado@gmaul.xor");
        registerForm.getPhoneInput().sendKeys("12345678910");

        registerForm.getRegisterButton().click();

        final int numberOfRowsBefore = webDriver.findElements(By.xpath("//*[@class='rf-dt-b']/tr")).size();
        (new WebDriverWait(webDriver, 4)).until(new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver d) {

                int numberOfRowsAfter = webDriver.findElements(By.xpath("//*[@class='rf-dt-b']/tr")).size();
                return (numberOfRowsBefore < numberOfRowsAfter);
            }
        });

        String table = membersTable.getTable().getText();
        assertTrue(table.contains(name), "The new member was not added correctly!");
    }

}
