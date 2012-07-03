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
    
    private final int PAGE_TRANSITION_WAIT = 3;
    
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
        waitFor(PAGE_TRANSITION_WAIT);

        //set twice as workaround for first time filling in input
        String nameSet = registerForm.setCorrectName();
        registerForm.setCorrectName();
        registerForm.setCorrectEmail();
        registerForm.setCorrectPhone();

        registerForm.getRegisterButton().click();

        waitFor(PAGE_TRANSITION_WAIT);
        
        String table = membersTable.getTable().getText();
        assertTrue(table.contains(nameSet), "The new member was not added correctly!");
    }
    
    private void waitFor(int howLong) {
        
        long timeout = System.currentTimeMillis() + (howLong * 1000);
        
        while(System.currentTimeMillis() < timeout) {
            
        }
    }
}
