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
package org.richfaces.tests.archetypes.kitchensink.ftest.desktop;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.test.selenium.support.pagefactory.StaleReferenceAwareFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.AbstractKitchensinkTest;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.annotations.SecondWindow;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.MemberDetails;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.MembersTable;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.RegisterForm;
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

    @BeforeMethod(groups = "arquillian")
    public void initialiseWebElements() {
        FieldDecorator fd = new StaleReferenceAwareFieldDecorator(new DefaultElementLocatorFactory(webDriver), 2);
        PageFactory.initElements(fd, registerForm);
        PageFactory.initElements(fd, membersTable);
        PageFactory.initElements(fd, memberDetails);
    }

    @Test
    public void testAddCorrectMember() {
        String nameSet = registerForm.setCorrectName();
        registerForm.setCorrectPhone();
        registerForm.setCorrectEmail();

        final int numberOfRowsBefore = membersTable.getNumberOfRows();

        registerForm.clickOnRegisterButton();

        membersTable.waitUntilNumberOfRowsChanged(4, webDriver, numberOfRowsBefore);

        String table = membersTable.getTable().getText();
        assertTrue(table.contains(nameSet), "The new member was not added correctly!");
    }

    @Test(groups = "4.Future")
    public void testAddMemberCSVNamePatternViolation() {
        // workaround to be sure that the event was fired
        webDriverBackedSelenium.type(registerForm.getNAME_LOC(), "122345");
        webDriverBackedSelenium.fireEvent(registerForm.getNAME_LOC(), "click");

        assertEquals(registerForm.getErrorMessages().size(), 1, ERROR_MSG_CSV);
    }

    @Test
    public void testAddMemberCSVEmailPatternViolation() {
        final int numberOfErrorMessagesBefore = registerForm.getErrorMessages().size();

        // workaround to be sure that the event was fired
        webDriverBackedSelenium.type(registerForm.getEMAIL_LOC(), "blah");
        webDriverBackedSelenium.fireEvent(registerForm.getEMAIL_LOC(), "click");

        registerForm.waitForErrorMessages(WAIT_FOR_ERR_MSG_RENDER, webDriver, numberOfErrorMessagesBefore);

        assertEquals(registerForm.getErrorMessages().size(), 1, ERROR_MSG_CSV);
    }

    @Test
    public void testAddMemberCSVPhonePatternViolation() {
        // workaround to be sure that the event was fired
        webDriverBackedSelenium.type(registerForm.getPHONE_LOC(), "blah");
        webDriverBackedSelenium.fireEvent(registerForm.getPHONE_LOC(), "click");

        assertEquals(registerForm.getErrorMessages().size(), 1, ERROR_MSG_CSV);
    }

    @Test
    public void testAddMemberSSVEverythingWrong() {
        registerForm.setIncorrectEmailPatternViolation();
        registerForm.setIncorrectPhonePatternViolation();
        registerForm.setIncorrectNameTooShort();

        final int numberOfErrorMessagesBefore = registerForm.getErrorMessages().size();
        registerForm.clickOnRegisterButton();

        registerForm.waitForErrorMessages(WAIT_FOR_ERR_MSG_RENDER, webDriver, numberOfErrorMessagesBefore);

        assertEquals(registerForm.getErrorMessages().size(), 3,
            "The number of error messages after server side validation is wrong.");
    }

    @Test(dataProvider = Arquillian.ARQUILLIAN_DATA_PROVIDER)
    public void testPushFunctionality(@Drone @SecondWindow WebDriver secondWindow) {
        FieldDecorator fd = new StaleReferenceAwareFieldDecorator(new DefaultElementLocatorFactory(webDriver), 3);
        MembersTable membersTable2 = new MembersTable();
        PageFactory.initElements(secondWindow, membersTable2);

        secondWindow.get(getDeployedURL().toExternalForm());

        registerNewMemberAndCheckPush("juraj@gmaul.for", membersTable2);
        registerNewMemberAndCheckPush("michal@lala.ru", membersTable2);
        registerNewMemberAndCheckPush("miroslav@de.du", membersTable2);
        registerNewMemberAndCheckPush("robino@ba.sk", membersTable2);
    }

    @Test
    public void testViewRowsData() {
        registerNewMember("foo@gmaul.for");
        registerNewMember("bar@lala.ru");
        registerNewMember("other@de.du");
        registerNewMember("absolutelydifferent@ba.sk");

        for (WebElement row : membersTable.getTableRows()) {
            String emailFromTable = row.findElement(By.xpath("./td[4]")).getText();

            row.findElement(By.xpath("./td/a")).click();
            memberDetails.waitMemberDetailsAreAvailableOnDesktop(2, webDriver);

            String emailFromPopup = memberDetails.getEmailOnDesktop().getAttribute("value");

            assertEquals(emailFromPopup, emailFromTable, "The email of the member is different in table than in popup!");
            memberDetails.getBackToFormDesktop().click();
        }
    }

    @Test
    public void testRestAPI() {
        registerNewMember("diff@de.du");
        registerNewMember("definitelydifferent@ba.sk");

        WebElement fstRow = membersTable.getTableRow();
        String mailFromFstRow = fstRow.findElement(By.xpath("./td[4]")).getText();

        fstRow.findElement(By.xpath("./td[6]/a")).click();

        String jsonData = webDriver.findElement(By.xpath("//pre")).getText();
        assertTrue(jsonData.contains(mailFromFstRow),
            "The REST api should provide json data with all details about member from first row!");
    }

    private void registerNewMemberAndCheckPush(String email, MembersTable membersTable2) {
        registerNewMember(email);

        assertTrue(membersTable.getTable().getText().contains(email),
            "The table from first window should contain the added member!");
        assertTrue(membersTable2.getTable().getText().contains(email),
            "The table from second window should contain the added member!");
    }

    private void registerNewMember(String email) {
        registerForm.setCorrectName();
        registerForm.setEmail(email);
        registerForm.setCorrectPhone();

        final int numberOfRowsBefore = membersTable.getNumberOfRows();

        registerForm.getRegisterButton().click();

        membersTable.waitUntilNumberOfRowsChanged(4, webDriver, numberOfRowsBefore);
    }
}
