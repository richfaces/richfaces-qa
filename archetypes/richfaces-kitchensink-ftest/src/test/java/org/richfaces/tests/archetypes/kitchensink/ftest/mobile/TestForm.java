package org.richfaces.tests.archetypes.kitchensink.ftest.mobile;

import static org.testng.Assert.assertTrue;

import org.jboss.test.selenium.support.pagefactory.StaleReferenceAwareFieldDecorator;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.AbstractKitchensinkTest;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.MemberDetails;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.MembersTable;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.RegisterForm;
import org.richfaces.tests.archetypes.kitchensink.ftest.mobile.page.MenuPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestForm extends AbstractKitchensinkTest {
    
    private RegisterForm registerForm = new RegisterForm();
    private MembersTable membersTable = new MembersTable();
    private MemberDetails memberDetails = new MemberDetails();
    private MenuPage menuPage = new MenuPage();
    
    @BeforeMethod(groups = "arquillian")
    public void initialiseWebElements() {
        FieldDecorator fd = new StaleReferenceAwareFieldDecorator(new DefaultElementLocatorFactory(webDriver), 2);
        PageFactory.initElements(fd, registerForm);
        PageFactory.initElements(fd, membersTable);
        PageFactory.initElements(fd, memberDetails);
        PageFactory.initElements(fd, menuPage);
    }
    
    @Test
    public void testAddCorrectMember() {
        menuPage.getAddMember().click();
        implicitWait(3);
        
        //workaround for delay when first typing into input
        registerForm.getNameInput().sendKeys("foo");

        String nameSet = registerForm.setCorrectName();
        registerForm.setCorrectEmail();
        registerForm.setCorrectPhone();

        final int numberOfRowsBefore = membersTable.getNumberOfRows();

        registerForm.getRegisterButton().click();

        membersTable.waitUntilNumberOfRowsChanged(4, webDriver, numberOfRowsBefore);

        String table = membersTable.getTable().getText();
        assertTrue(table.contains(nameSet), "The new member was not added correctly!");
    }
    
    private void implicitWait(int howLong) {
        
        long timeout = System.currentTimeMillis() + (howLong * 1000);
        
        while(System.currentTimeMillis() < timeout) {
            
        }
    }
}
