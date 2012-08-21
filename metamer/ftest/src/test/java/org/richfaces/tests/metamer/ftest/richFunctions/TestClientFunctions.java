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
package org.richfaces.tests.metamer.ftest.richFunctions;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;


/**
 * Test case for page faces/components/richFunctions/all.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 23010 $
 */
public class TestClientFunctions extends AbstractGrapheneTest {

    private JQueryLocator clientIdOutput = pjq("span[id$=clientIdOutput]");
    private JQueryLocator elementOutput = pjq("span[id$=elementOutput]");
    private JQueryLocator componentOutput = pjq("span[id$=componentOutput]");
    private JQueryLocator findComponentOutput = pjq("span[id$=findComponentOutput]");
    private JQueryLocator jQuerySelectorOutput = pjq("span[id$=jQuerySelectorOutput]");
    private JQueryLocator roleNameInput = pjq("input[id$=roleName]");
    private JQueryLocator applyLink = pjq("a[id$=applyLink]");
    private JQueryLocator userInRoleAUOutput = pjq("span[id$=outputUserInRoleAU]");
    private JQueryLocator userInRoleAOutput = pjq("span[id$=outputUserInRoleA]");
    private JQueryLocator userInRoleUOutput = pjq("span[id$=outputUserInRoleU]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFunctions/all.xhtml");
    }

    @Test
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList", "hDataTable", "uiRepeat" })
    public void testFunctions() {
        String clientId = selenium.getText(clientIdOutput);
        assertNotNull(clientId, "Function clientId() doesn't work.");
        assertNotSame(clientId, "", "Function clientId() doesn't work.");

        String output = selenium.getText(elementOutput);
        assertEquals(output, "document.getElementById('" + clientId + "')", "Function element() doesn't work.");

        output = selenium.getText(componentOutput);
        assertEquals(output, "RichFaces.$('" + clientId + "')", "Function component() doesn't work.");

        output = selenium.getText(findComponentOutput);
        assertEquals(output, "abc", "Function findComponent() doesn't work.");

        output = selenium.getText(jQuerySelectorOutput);
        assertEquals(output, "#form\\\\:subview\\\\:input", "Function jQuerySelector() doesn't work.");
    }

    @Test(groups = { "4.Future" })
    @Templates(value = { "a4jRepeat", "hDataTable", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList", "uiRepeat" })
    @IssueTracking("https://issues.jboss.org/browse/RF-10465")
    public void testFunctionsInIterationComponents() {
        testFunctions();
    }

    @Test
    public void testRoles() {
        String outputAU = selenium.getText(userInRoleAUOutput);
        String outputA = selenium.getText(userInRoleAOutput);
        String outputU = selenium.getText(userInRoleUOutput);
        assertEquals(outputAU, "false", "User is not in role 'admin' nor 'user'.");
        assertEquals(outputA, "false", "User is not in role 'admin'.");
        assertEquals(outputU, "false", "User is not in role 'user'.");

        selenium.type(roleNameInput, "user");
        selenium.click(applyLink);
        selenium.waitForPageToLoad();
        outputAU = selenium.getText(userInRoleAUOutput);
        outputA = selenium.getText(userInRoleAOutput);
        outputU = selenium.getText(userInRoleUOutput);
        assertEquals(outputAU, "true", "User is in role 'admin' or 'user'.");
        assertEquals(outputA, "false", "User is not in role 'admin'.");
        assertEquals(outputU, "true", "User is in role 'user'.");

        selenium.type(roleNameInput, "admin");
        selenium.click(applyLink);
        selenium.waitForPageToLoad();
        outputAU = selenium.getText(userInRoleAUOutput);
        outputA = selenium.getText(userInRoleAOutput);
        outputU = selenium.getText(userInRoleUOutput);
        assertEquals(outputAU, "true", "User is in role 'admin' or 'user'.");
        assertEquals(outputA, "true", "User is in role 'admin'.");
        assertEquals(outputU, "false", "User is not in role 'user'.");

        selenium.type(roleNameInput, "superhero");
        selenium.click(applyLink);
        selenium.waitForPageToLoad();
        outputAU = selenium.getText(userInRoleAUOutput);
        outputA = selenium.getText(userInRoleAOutput);
        outputU = selenium.getText(userInRoleUOutput);
        assertEquals(outputAU, "false", "User is not in role 'admin' nor 'user'.");
        assertEquals(outputA, "false", "User is not in role 'admin'.");
        assertEquals(outputU, "false", "User is not in role 'user'.");
    }
}
