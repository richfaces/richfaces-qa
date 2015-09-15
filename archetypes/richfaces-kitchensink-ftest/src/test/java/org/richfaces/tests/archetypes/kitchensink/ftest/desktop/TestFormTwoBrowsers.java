/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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

import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebDriver;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.AbstractKitchensinkTest;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.annotations.SecondWindow;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.MembersTable;
import org.richfaces.tests.archetypes.kitchensink.ftest.common.page.RegisterForm;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestFormTwoBrowsers extends AbstractKitchensinkTest {

    @Page
    private MembersTable membersTable1;
    @Page
    @SecondWindow
    private MembersTable membersTable2;
    @Page
    private RegisterForm registerForm1;
    @Page
    @SecondWindow
    private RegisterForm registerForm2;
    @Drone
    @SecondWindow
    private WebDriver secondWindow;

    private void registerNewMember(String email, MembersTable membersTable, RegisterForm registerForm) {
        final int numberOfRowsBefore = membersTable.getNumberOfRows();
        registerForm.setCorrectName();
        registerForm.setEmail(email);
        registerForm.setCorrectPhone();

        registerForm.clickOnRegisterButton();
        membersTable.waitUntilNumberOfRowsChanged(numberOfRowsBefore);
    }

    private void registerNewMemberAndCheckPush(String email, MembersTable membersTable, RegisterForm registerForm) {
        registerNewMember(email, membersTable, registerForm);

        assertTrue(membersTable.getTable().getText().contains(email),
            "The table from first window should contain the added member!");
        assertTrue((membersTable == membersTable1 ? membersTable2 : membersTable1).getTable().getText().contains(email),
            "The table from second window should contain the added member!");
    }

    @Test
    public void testPushFunctionality() {
        secondWindow.get(getDeployedURL().toExternalForm());

        registerNewMemberAndCheckPush("robino@ba.sk", membersTable1, registerForm1);
        registerNewMemberAndCheckPush("juraj@gmaul.for", membersTable2, registerForm1);
        registerNewMemberAndCheckPush("michal@lala.ru", membersTable1, registerForm2);
        registerNewMemberAndCheckPush("miroslav@de.du", membersTable2, registerForm2);
    }
}
