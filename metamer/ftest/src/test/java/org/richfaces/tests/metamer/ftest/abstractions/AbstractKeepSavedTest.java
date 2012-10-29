/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;

/**
 *
 * @author jstefek
 */
public class AbstractKeepSavedTest extends AbstractGrapheneTest {

    private static final JQueryLocator inputFirst = pjq("input[type=text]:first");
    private static final JQueryLocator submit = pjq("input[id$=submitButton]");
    private static final JQueryLocator errorMessage = pjq("div.messages li");
    private static final JQueryLocator keepSavedInput = pjq("input[type=radio][value=true]");
    private static final String TEXT = "a";
    private final String componentSpecifier;

    public AbstractKeepSavedTest(String componentSpecifier) {
        this.componentSpecifier = componentSpecifier;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/" + componentSpecifier + "/keepSaved.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
         String[] componentNameParts = componentSpecifier.substring(4).split("(?<!^)(?=[A-Z])");
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < componentNameParts.length; ++i) {
             sb.append(" ").append(componentNameParts[i]);
             System.out.println(" ### componentNameParts[" + i + "] " + componentNameParts[i]);
         }
         return new MetamerNavigation("Rich", "Rich" + sb.toString(), "Keep Saved");
    }

    public String getComponentSpecifier() {
        return componentSpecifier;
    }

    public void testKeepSavedFalse() {
        selenium.type(inputFirst, TEXT);
        guardHttp(selenium).click(submit);
        assertTrue(selenium.isElementPresent(errorMessage), "No error message shown.");
        guardHttp(selenium).click(submit);
        assertFalse(selenium.isElementPresent(errorMessage), "Error message should be shown.");
    }

    public void testKeepSavedTrue() {
        guardHttp(selenium).click(keepSavedInput);
        selenium.type(inputFirst, TEXT);
        guardHttp(selenium).click(submit);
        assertTrue(selenium.isElementPresent(errorMessage), "No error message shown.");
        guardHttp(selenium).click(submit);
        assertTrue(selenium.isElementPresent(errorMessage), "No error message shown.");
    }
}
