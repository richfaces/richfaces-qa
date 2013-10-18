/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.common.ClearType;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richFunctions/all.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestFunctions extends AbstractWebDriverTest {

    @FindBy(css = "span[id$=clientIdOutput]")
    private WebElement clientIdOutput;
    @FindBy(css = "span[id$=elementOutput]")
    private WebElement elementOutput;
    @FindBy(css = "span[id$=componentOutput]")
    private WebElement componentOutput;
    @FindBy(css = "span[id$=findComponentOutput]")
    private WebElement findComponentOutput;
    @FindBy(css = "span[id$=jQuerySelectorOutput]")
    private WebElement jQuerySelectorOutput;
    @FindBy(css = "span[id$=jQueryOutput]")
    private WebElement jQueryOutput;
    @FindBy(css = "span[id$=outputUserInRoleAU]")
    private WebElement userInRoleAUOutput;
    @FindBy(css = "span[id$=outputUserInRoleA]")
    private WebElement userInRoleAOutput;
    @FindBy(css = "span[id$=outputUserInRoleU]")
    private WebElement userInRoleUOutput;
    @FindBy(css = "a[id$=applyLink]")
    private WebElement applyLink;
    @FindBy(css = "input[id$=jQuerySelectorOutputFadeOut]")
    private WebElement fadeOut;
    @FindBy(css = "input[id$=jQuerySelectorOutputFadeIn]")
    private WebElement fadeIn;
    @FindBy(css = "input[id$=input]")
    private TextInputComponentImpl input;
    @FindBy(css = "input[id$=roleName]")
    private TextInputComponentImpl roleNameInput;

    private enum Role {

        ADMIN, USER, NOBODY
    }

    private void assertUserIs(Role role) {
        switch (role) {
            case ADMIN:
                assertTrue(isAdmin(), "Current user should have 'admin' rights");
                assertFalse(isUser(), "Current user should not have 'user' rights");
                assertTrue(isAdminOrUser(), "Current user should have 'admin' or 'user' rights");
                break;
            case USER:
                assertFalse(isAdmin(), "Current user should not have 'admin' rights");
                assertTrue(isUser(), "Current user should have 'user' rights");
                assertTrue(isAdminOrUser(), "Current user should have 'admin' or 'user' rights");
                break;
            case NOBODY:
                assertFalse(isAdmin(), "Current user should not have 'admin' rights");
                assertFalse(isUser(), "Current user should not have 'user' rights");
                assertFalse(isAdminOrUser(), "Current user should not have 'admin' nor 'user' rights");
                break;
        }
    }

    private void fillInRoleAndApply(String role) {
        roleNameInput.advanced().clear(ClearType.JS).sendKeys(role);
        MetamerPage.waitRequest(applyLink, WaitRequestType.HTTP).click();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFunctions/all.xhtml");
    }

    private boolean isAdmin() {
        return Boolean.parseBoolean(userInRoleAOutput.getText());
    }

    private boolean isAdminOrUser() {
        return Boolean.parseBoolean(userInRoleAUOutput.getText());
    }

    private boolean isUser() {
        return Boolean.parseBoolean(userInRoleUOutput.getText());
    }

    @Test
    public void testFadeOutFadeIn() {
        assertVisible(input.advanced().getInputElement(), "Input should be visible");
        new Actions(driver).moveToElement(fadeOut).perform();
        Graphene.waitGui().until().element(input.advanced().getInputElement()).is().not().visible();
        assertNotVisible(input.advanced().getInputElement(), "Input should not be visible.");
        new Actions(driver).moveToElement(fadeIn).perform();
        Graphene.waitGui().until().element(input.advanced().getInputElement()).is().visible();
        assertVisible(input.advanced().getInputElement(), "Input should be visible");
    }

    @Test
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList", "hDataTable", "uiRepeat" })
    public void testFunctions() {
        String id = input.advanced().getInputElement().getAttribute("id");
        String clientId = clientIdOutput.getText();
        assertNotNull(clientId, "Function clientId() doesn't work.");
        assertNotSame(clientId, "", "Function clientId() doesn't work.");
        assertEquals(clientId, id, "Function clientId() doesn't work.");

        String output = elementOutput.getText();
        assertEquals(output, "document.getElementById('" + clientId + "')", "Function element() doesn't work.");

        output = componentOutput.getText();
        assertEquals(output, "RichFaces.$('" + clientId + "')", "Function component() doesn't work.");

        output = findComponentOutput.getText();
        assertEquals(output, "abc", "Function findComponent() doesn't work.");
        input.advanced().clear(ClearType.JS).sendKeys("RichFaces");
        MetamerPage.waitRequest(applyLink, WaitRequestType.HTTP).click();
        output = findComponentOutput.getText();
        assertEquals(output, "RichFaces", "Function findComponent() doesn't work.");

        output = jQuerySelectorOutput.getText();
        assertEquals(output, "#form\\\\:subview\\\\:input", "Function jQuerySelector() doesn't work.");

        output = jQueryOutput.getText();
        assertEquals(output, "jQuery(document.getElementById('" + clientId + "'))", "Function jQuerySelector() doesn't work.");
    }

    @Test(groups = { "Future" })
    @Templates(value = { "a4jRepeat", "hDataTable", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList", "uiRepeat" })
    @IssueTracking("https://issues.jboss.org/browse/RF-10465")
    public void testFunctionsInIterationComponents() {
        testFunctions();
    }

    @Test
    public void testRoles() {
        assertUserIs(Role.NOBODY);

        fillInRoleAndApply("user");
        assertUserIs(Role.USER);

        fillInRoleAndApply("admin");
        assertUserIs(Role.ADMIN);

        fillInRoleAndApply("superhero");
        assertUserIs(Role.NOBODY);
    }
}
