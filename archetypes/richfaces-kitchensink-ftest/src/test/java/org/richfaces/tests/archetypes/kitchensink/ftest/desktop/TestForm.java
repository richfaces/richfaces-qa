/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.archetypes.kitchensink.ftest.desktop;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.AbstractKitchensinkTest;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.MemberDetails;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.MembersTable;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.RegisterForm;
import org.richfaces.tests.configurator.unstable.annotation.Unstable;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Unstable
public class TestForm extends AbstractKitchensinkTest {

    @Page
    private MemberDetails memberDetails;
    @Page
    private MembersTable membersTable;
    @Page
    private RegisterForm registerForm;

    private int id = 0;

    private void registerNewMember(String email) {
        id++;// because test is unstable, make the inserted emails unique
        String uniqueEmail = email + id;
        final int numberOfRowsBefore = membersTable.getNumberOfRows();
        registerForm.setCorrectName();
        registerForm.setEmail(uniqueEmail);
        registerForm.setCorrectPhone();

        registerForm.clickOnRegisterButton();
        membersTable.waitUntilNumberOfRowsChanged(numberOfRowsBefore);
    }

    private void switchToSecondWindow(String originalWindow) {
        for (String window : webDriver.getWindowHandles()) {
            if (!window.equals(originalWindow)) {
                webDriver.switchTo().window(window);
                break;
            }
        }
    }

    @Test
    public void testAddCorrectMember() {
        String nameSet = registerForm.setCorrectName();
        registerForm.setCorrectPhone();
        registerForm.setCorrectEmail();

        final int numberOfRowsBefore = membersTable.getNumberOfRows();

        registerForm.clickOnRegisterButton();
        membersTable.waitUntilNumberOfRowsChanged(numberOfRowsBefore);

        Graphene.waitAjax()
            .withMessage("The new member was not added correctly!")
            .until().element(membersTable.getTable()).text().contains(nameSet);
        assertEquals(membersTable.getNumberOfRows(), numberOfRowsBefore + 1, "The new member was not added corractly!");
    }

    @Test
    public void testAddMemberCSVEmailPatternViolation() {
        registerForm.setIncorrectEmailPatternViolation();

        registerForm.blur();
        registerForm.waitForErrorMessages(CSV_ERROR_MSG, 1);
    }

    @Test
    public void testAddMemberCSVNamePatternViolation() {
        registerForm.setIncorrectNamePatternViolation();

        registerForm.blur();
        registerForm.waitForErrorMessages(CSV_ERROR_MSG, 1);
    }

    @Test
    public void testAddMemberCSVPhonePatternViolation() {
        registerForm.setIncorrectPhonePatternViolation();

        registerForm.blur();
        registerForm.waitForErrorMessages(CSV_ERROR_MSG, 1);
    }

    @Test
    public void testAddMemberSSVEverythingWrong() {
        registerForm.setIncorrectEmailPatternViolation();
        registerForm.setIncorrectPhonePatternViolation();
        registerForm.setIncorrectNameTooShort();

        registerForm.clickOnRegisterButton();
        registerForm.waitForErrorMessages("The number of error messages after server side validation is wrong.", 3);
    }

    @Test
    public void testRestAPI() {
        String originalWindow = webDriver.getWindowHandle();
        try {
            WebElement firstRow = membersTable.getTableRow();

            String idFromFstRow = firstRow.findElement(ByJQuery.selector("td:eq(1)")).getText();
            String nameFromFstRow = firstRow.findElement(ByJQuery.selector("td:eq(2)")).getText();
            String mailFromFstRow = firstRow.findElement(ByJQuery.selector("td:eq(3)")).getText();
            String phoneFromFstRow = firstRow.findElement(ByJQuery.selector("td:eq(4)")).getText();

            // open REST URL
            firstRow.findElement(ByJQuery.selector("td:eq(5) a")).click();

            switchToSecondWindow(originalWindow);

            WebElement body = webDriver.findElement(By.xpath("//pre"));

            Graphene.waitAjax()
                .withMessage("The REST api should provide json data with correct EMAIL of the member from first row!")
                .until().element(body).text().contains(mailFromFstRow);
            String bodyText = body.getText();
            assertTrue(bodyText.contains(idFromFstRow), "The REST api should provide json data with correct ID of the member from first row!");
            assertTrue(bodyText.contains(nameFromFstRow), "The REST api should provide json data with correct NAME of the member from first row!");
            assertTrue(bodyText.contains(phoneFromFstRow), "The REST api should provide json data with correct PHONE of the member from first row!");

        } finally {
            if (!webDriver.getWindowHandle().equals(originalWindow)) {
                webDriver.close();
            }
            webDriver.switchTo().window(originalWindow);
        }
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
            memberDetails.waitMemberDetailsAreAvailable();

            String emailFromPopup = memberDetails.getEmail().getAttribute("value");

            assertEquals(emailFromPopup, emailFromTable, "The email of the member is different in table than in popup!");
            memberDetails.getBackToForm().click();
        }
    }
}
