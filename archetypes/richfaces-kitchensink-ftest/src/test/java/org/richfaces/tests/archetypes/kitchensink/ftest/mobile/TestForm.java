/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.archetypes.kitchensink.ftest.mobile;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.test.selenium.support.pagefactory.StaleReferenceAwareFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
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

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestForm extends AbstractKitchensinkTest {

    private RegisterForm registerForm = new RegisterForm();
    private MembersTable membersTable = new MembersTable();
    private MemberDetails memberDetails = new MemberDetails();
    private MenuPage menuPage = new MenuPage();

    private final String DESKTOP_MOBILE_NEW_MEMEBER = "/mobile/index.jsf#new";
    private final String DESKTOP_MOBILE_LIST_MEMBERS = "/mobile/index.jsf#list";

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
        menuPage.gotoAddMemberPage();
        // registerForm.switchOffAutocompleteOnInputs(webDriver);

        // set twice as workaround for first time filling in input
        String nameSet = registerForm.setCorrectName();
        if (registerForm.getNameInput().getAttribute("value").isEmpty()) {
            registerForm.setCorrectName();
        }
        registerForm.setCorrectEmail();
        registerForm.setCorrectPhone();

        registerForm.clickOnRegisterButton();
        menuPage.waitFor(MenuPage.PAGE_TRANSITION_WAIT);

        String table = membersTable.getTable().getText();
        assertTrue(table.contains(nameSet), "The new member was not added correctly!");
    }

    @Test
    public void testCSVEmailPattern() {
        menuPage.gotoAddMemberPage();
        // registerForm.switchOffAutocompleteOnInputs(webDriver);

        final int numberOfErrorMessagesBefore = registerForm.getErrorMessages().size();

        registerForm.setIncorrectEmailPatternViolation();
        registerForm.getPhoneInput().click();

        registerForm.waitForErrorMessages(3, webDriver, numberOfErrorMessagesBefore);
        assertEquals(registerForm.getErrorMessages().size(), 1, ERROR_MSG_CSV);

        registerForm.areAllErrorMessagesRendered(CSV_EMAIL);
    }

    @Test(groups = "4.Future")
    public void testCSVNamePattern() {
        menuPage.gotoAddMemberPage();
        // registerForm.switchOffAutocompleteOnInputs(webDriver);

        final int numberOfErrorMessagesBefore = registerForm.getErrorMessages().size();

        registerForm.setIncorrectNamePatternViolation();
        registerForm.getEmailInput().click();

        registerForm.waitForErrorMessages(WAIT_FOR_ERR_MSG_RENDER, webDriver, numberOfErrorMessagesBefore);
        assertEquals(registerForm.getErrorMessages().size(), 1, ERROR_MSG_CSV);
    }

    @Test
    public void testCSVPhonePattern() {
        menuPage.gotoAddMemberPage();
        // registerForm.switchOffAutocompleteOnInputs(webDriver);

        final int numberOfErrorMessagesBefore = registerForm.getErrorMessages().size();

        registerForm.setIncorrectPhonePatternViolation();
        registerForm.getEmailInput().click();

        registerForm.waitForErrorMessages(WAIT_FOR_ERR_MSG_RENDER, webDriver, numberOfErrorMessagesBefore);
        assertEquals(registerForm.getErrorMessages().size(), 1, ERROR_MSG_CSV);

        registerForm.areAllErrorMessagesRendered(CSV_PHONE);
    }

    @Test
    public void testSSVInputsEmpty() {
        menuPage.gotoAddMemberPage();
        // registerForm.switchOffAutocompleteOnInputs(webDriver);

        final int numberOfErrorMessagesBefore = registerForm.getErrorMessages().size();
        registerForm.clickOnRegisterButton();

        registerForm.waitForErrorMessages(WAIT_FOR_ERR_MSG_RENDER, webDriver, numberOfErrorMessagesBefore);
        assertEquals(registerForm.getErrorMessages().size(), 3, ERROR_MSG_CSV);
    }

    @Test
    public void testSSVWrongInputs() {
        menuPage.gotoAddMemberPage();
        // registerForm.switchOffAutocompleteOnInputs(webDriver);

        final int numberOfErrorMessagesBefore = registerForm.getErrorMessages().size();

        registerForm.setIncorrectNameTooShort();
        registerForm.setIncorrectEmailPatternViolation();
        registerForm.setIncorrectPhonePatternViolation();

        registerForm.clickOnRegisterButton();

        registerForm.waitForErrorMessages(WAIT_FOR_ERR_MSG_RENDER, webDriver, numberOfErrorMessagesBefore);
        assertEquals(registerForm.getErrorMessages().size(), 3, ERROR_MSG_CSV);

        registerForm.areAllErrorMessagesRendered(SSV_NAME_SIZE, CSV_EMAIL);
        registerForm.isAnyErrorMessageRendered(CSV_PHONE, SSV_PHONE_SIZE);
    }

    @Test
    public void testViewMemberDetails() {
        menuPage.gotoAddMemberPage();

        registerForm.setCorrectName();
        registerForm.setEmail("wonderland@provider.org");
        registerForm.setCorrectPhone();

        registerForm.clickOnRegisterButton();
        menuPage.waitFor(MenuPage.PAGE_TRANSITION_WAIT);

        int numberOfRows = membersTable.getNumberOfRows();
        for (int i = 1; i <= numberOfRows; i++) {

            String expectedEmail = membersTable.getTable().findElement(By.xpath("(//*[@class='rf-dt-b']/tr)[" + i + "]/td[4]"))
                .getText();

            membersTable.getTable().findElement(By.xpath("(//*[@class='rf-dt-b']/tr)[" + i + "]/td/a")).click();
            menuPage.waitFor(MenuPage.PAGE_TRANSITION_WAIT);

            String actualEmail = memberDetails.getEmailOnMobile().getText().trim();
            assertEquals(actualEmail, expectedEmail, "Email from the table is not equal to email from the user details!");

            if (i == numberOfRows)
                break;
            memberDetails.getBackToMenuMobile().click();
            menuPage.gotoListMembersPage();
        }
    }

    @Test
    public void testPushFromDesktopToMobile() {
        FirefoxDriver firefoxDriver = new FirefoxDriver();
        String url = getDeployedURL().toExternalForm().replace(ANDORID_LOOPBACK, "localhost")
            .concat(DESKTOP_MOBILE_NEW_MEMEBER);

        // opening mobile version of kitchensink on desktop browser as it is not possible to open two browsers on mobile device
        // or open two emulators
        firefoxDriver.get(url);

        FieldDecorator fd = new StaleReferenceAwareFieldDecorator(new DefaultElementLocatorFactory(firefoxDriver), 2);
        RegisterForm registerFormDesktop = new RegisterForm();
        PageFactory.initElements(fd, registerFormDesktop);

        menuPage.gotoListMembersPage();

        registerFormDesktop.setCorrectName();
        String emailToSet = "pushtomobile@ff.sf";
        registerFormDesktop.setEmail(emailToSet);
        registerFormDesktop.setCorrectPhone();
        registerFormDesktop.clickOnRegisterButton();
        menuPage.waitFor(MenuPage.PAGE_TRANSITION_WAIT);

        String tableText = membersTable.getTable().getText();
        assertTrue(tableText.contains(emailToSet),
            "Registration of new member desktop mobile version was not pushed to the mobile device!");

        firefoxDriver.close();
    }

    @Test
    public void testPushFromMobileToDesktop() {
        FirefoxDriver firefoxDriver = new FirefoxDriver();
        String url = getDeployedURL().toExternalForm().replace(ANDORID_LOOPBACK, "localhost")
            .concat(DESKTOP_MOBILE_LIST_MEMBERS);

        // opening mobile version of kitchensink on desktop browser as it is not possible to open two browsers on mobile device
        // or open two emulators
        firefoxDriver.get(url);

        FieldDecorator fd = new StaleReferenceAwareFieldDecorator(new DefaultElementLocatorFactory(firefoxDriver), 2);
        MembersTable membersTableDesktop = new MembersTable();
        PageFactory.initElements(fd, membersTableDesktop);

        menuPage.gotoAddMemberPage();

        registerForm.setCorrectName();
        String emailToSet = "pushfromdesktop@pushik.cz";
        registerForm.setEmail(emailToSet);
        registerForm.setCorrectPhone();
        registerForm.clickOnRegisterButton();
        menuPage.waitFor(MenuPage.PAGE_TRANSITION_WAIT);

        String tableText = membersTableDesktop.getTable().getText();
        assertTrue(tableText.contains(emailToSet),
            "Registration of new member on mobile device was not pushed to the desktop mobile version!");
        
        firefoxDriver.close();
    }
}
