/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.AbstractKitchensinkTest;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.MemberDetails;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.MembersTable;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.RegisterForm;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestForm extends AbstractKitchensinkTest {

    @Page
    private RegisterForm registerForm;
    @Page
    private MembersTable membersTable;
    @Page
    private MemberDetails memberDetails;

    @Test
    public void testAddCorrectMember() {
        String nameSet = registerForm.setCorrectName();
        registerForm.setCorrectEmail();
        registerForm.setCorrectPhone();

        registerForm.clickOnRegisterButton();
        Graphene.waitAjax()
                .until()
                .element(membersTable.getTable())
                .is()
                .present();

        String table = membersTable.getTable().getText();
        assertTrue(table.contains(nameSet), "The new member was not added correctly!");
    }

    @Test
    public void testCSVEmailPattern() {
        registerForm.setIncorrectEmailPatternViolation();

        registerForm.blur();
        registerForm.waitForErrorMessages(ERROR_MSG_CSV, 1);

        assertEquals(registerForm.getErrorMessages().size(), 1, ERROR_MSG_CSV);

        registerForm.areAllErrorMessagesRendered(CSV_EMAIL);
    }

    @Test
    public void testCSVNamePattern() {
        registerForm.setIncorrectNamePatternViolation();

        registerForm.blur();
        registerForm.waitForErrorMessages(ERROR_MSG_CSV, 1);

        assertEquals(registerForm.getErrorMessages().size(), 1, ERROR_MSG_CSV);
    }

    @Test
    public void testCSVPhonePattern() {
        registerForm.setIncorrectPhonePatternViolation();

        registerForm.blur();
        registerForm.waitForErrorMessages(ERROR_MSG_CSV, 1);

        assertEquals(registerForm.getErrorMessages().size(), 1, ERROR_MSG_CSV);

        registerForm.areAllErrorMessagesRendered(CSV_PHONE);
    }

    @Test
    public void testSSVInputsEmpty() {
        registerForm.clickOnRegisterButton();
        registerForm.waitForErrorMessages(ERROR_MSG_CSV, 3);

        assertEquals(registerForm.getErrorMessages().size(), 3, ERROR_MSG_CSV);
    }

    @Test
    public void testSSVWrongInputs() {
        registerForm.setIncorrectNameTooShort();
        registerForm.setIncorrectEmailPatternViolation();
        registerForm.setIncorrectPhonePatternViolation();

        registerForm.clickOnRegisterButton();
        registerForm.waitForErrorMessages(ERROR_MSG_CSV, 3);

        assertEquals(registerForm.getErrorMessages().size(), 3, ERROR_MSG_CSV);

        registerForm.areAllErrorMessagesRendered(SSV_NAME_SIZE, CSV_EMAIL);
        registerForm.isAnyErrorMessageRendered(CSV_PHONE, SSV_PHONE_SIZE);
    }

    @Test
    public void testViewMemberDetails() {
        final String expectedEmail = "wonderland@provider.org";
        registerForm.setCorrectName();
        registerForm.setEmail(expectedEmail);
        registerForm.setCorrectPhone();

        registerForm.clickOnRegisterButton();
        Graphene.waitAjax()
                .until()
                .element(membersTable.getTable())
                .is()
                .present();


        membersTable.getTableRow().findElement(By.cssSelector("td a")).click();
        Graphene.waitAjax()
                    .until()
                    .element(memberDetails.getEmailOnMobile())
                    .is()
                    .present();

        String actualEmail = memberDetails.getEmailOnMobile().getText().trim();
        assertEquals(actualEmail, expectedEmail, "Email from the table is not equal to email from the user details!");
    }

    // skip since Graphene doesn't support more webdrivers
    @Test(enabled=false)
    public void testPushFromDesktopToMobile() {
//        FirefoxDriver firefoxDriver = new FirefoxDriver();
//        String url = getDeployedURL().toExternalForm().replace(ANDORID_LOOPBACK, "localhost")
//            .concat(DESKTOP_MOBILE_NEW_MEMEBER);
//
//        // opening mobile version of kitchensink on desktop browser as it is not possible to open two browsers on mobile device
//        // or open two emulators
//        firefoxDriver.get(url);
//
//        FieldDecorator fd = new StaleReferenceAwareFieldDecorator(new DefaultElementLocatorFactory(firefoxDriver), 2);
//        RegisterForm registerFormDesktop = new RegisterForm();
//        PageFactory.initElements(fd, registerFormDesktop);
//
//        menuPage.gotoListMembersPage();
//
//        registerFormDesktop.setCorrectName();
//        String emailToSet = "pushtomobile@ff.sf";
//        registerFormDesktop.setEmail(emailToSet);
//        registerFormDesktop.setCorrectPhone();
//        registerFormDesktop.clickOnRegisterButton();
//        menuPage.waitFor(MenuPage.PAGE_TRANSITION_WAIT);
//
//        String tableText = membersTable.getTable().getText();
//        assertTrue(tableText.contains(emailToSet),
//            "Registration of new member desktop mobile version was not pushed to the mobile device!");
//
//        firefoxDriver.close();
    }

    // skip since Graphene doesn't support more webdrivers
    @Test(enabled=false)
    public void testPushFromMobileToDesktop() {
//        FirefoxDriver firefoxDriver = new FirefoxDriver();
//        String url = getDeployedURL().toExternalForm().replace(ANDORID_LOOPBACK, "localhost")
//            .concat(DESKTOP_MOBILE_LIST_MEMBERS);
//
//        // opening mobile version of kitchensink on desktop browser as it is not possible to open two browsers on mobile device
//        // or open two emulators
//        firefoxDriver.get(url);
//
//        FieldDecorator fd = new StaleReferenceAwareFieldDecorator(new DefaultElementLocatorFactory(firefoxDriver), 2);
//        MembersTable membersTableDesktop = new MembersTable();
//        PageFactory.initElements(fd, membersTableDesktop);
//
//        menuPage.gotoAddMemberPage();
//
//        registerForm.setCorrectName();
//        String emailToSet = "pushfromdesktop@pushik.cz";
//        registerForm.setEmail(emailToSet);
//        registerForm.setCorrectPhone();
//        registerForm.clickOnRegisterButton();
//        menuPage.waitFor(MenuPage.PAGE_TRANSITION_WAIT);
//
//        String tableText = membersTableDesktop.getTable().getText();
//        assertTrue(tableText.contains(emailToSet),
//            "Registration of new member on mobile device was not pushed to the desktop mobile version!");
//
//        firefoxDriver.close();
    }

    @Override
    protected String getUrlSuffix() {
        return "mobile/#new";
    }
}
