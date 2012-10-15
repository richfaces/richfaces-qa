/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import java.net.URL;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteWithJSR303 extends TestComponentWithJSR303 {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/jsr303.xhtml");
    }

    @Test
    public void testNotEmpty() {
        verifyNotEmpty();
    }

    @Test
    public void testRegExpPattern() {
        verifyRegExpPattern();
    }

    @Test
    public void testStringSize() {
        verifyStringSize();
    }

    @Test
    public void testCustomString() {
        verifyCustomString();
    }

    @Test
    public void testAllInputsWrong() {
        verifyAllInputs();
    }

}
