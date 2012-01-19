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
package org.richfaces.tests.metamer.ftest.richValidator;

import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.Ajocado.textEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.arquillian.ajocado.Ajocado.waitModel;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;


/**
 * Selenium tests for page faces/components/richValidator/single.xhtml
 * 
 * Test no-request handling client side validation. Validation for min is done by javax.validation.constraints.Min, and
 * this should completely handle validation on client side.
 * 
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision$
 */
public class TestWrappingValidatorSingle extends AbstractValidatorsTest {

    private JQueryLocator input = pjq("input[id$=:min]");
    private JQueryLocator setCorrectValBtn = pjq("input[id$=setCorrectValuesButton]");
    private JQueryLocator a4jSubmitBtn = pjq("input[id$=a4jButton]");
    private JQueryLocator msg = pjq("span[id$=minMsg] span.rf-msg-err");

    /** component page locator */
    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richValidator/single.xhtml");
    }

    @Test(groups = { "4.Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11576")
    public void testIntegerMin() {
        selenium.click(setCorrectValBtn);

        // integer input min
        selenium.type(input, "1");
        guardNoRequest(selenium).click(a4jSubmitBtn);

        waitGui.until(textEquals.locator(msg).text("must be greater than or equal to 2"));
    }

    /**
     * Check validation on input after blur event fired and no XHR request as well
     * 
     */
    @Test
    public void testValidateOnBlur() {
        selenium.click(setCorrectValBtn);

        // integer input min
        selenium.type(input, "1");

        // no request (HTTP neither XHR) should be sent if validation fails
        guardNoRequest(selenium).fireEvent(input, Event.BLUR);

        waitModel.until(textEquals.locator(msg).text("must be greater than or equal to 2"));
    }

}
