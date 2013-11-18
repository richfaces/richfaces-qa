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
package org.richfaces.tests.metamer.ftest.richSelect;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.selectAttributes;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.fragment.select.RichFacesSelect;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.validator.CaliforniaFirstValidator;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richSelect/validator.xhtml.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestSelectValidator extends AbstractWebDriverTest {

    @FindBy(css = "div[id$=select]")
    private RichFacesSelect select;
    @FindBy(css = "[id$=message]")
    private RichFacesMessage message;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richSelect/validator.xhtml");
    }

    @Test
    public void testValidatorMessage() {
        String customMessage = "Custom message!";
        Graphene.guardAjax(select.openSelect()).select(10);
        Assert.assertTrue(message.advanced().isVisible(), "Validator message should be visible");
        Assert.assertEquals(message.getDetail(), CaliforniaFirstValidator.VALIDATOR_ERROR_MSG);

        selectAttributes.set(SelectAttributes.validatorMessage, customMessage);
        Graphene.guardAjax(select.openSelect()).select(10);
        Assert.assertTrue(message.advanced().isVisible(), "Validator message should be visible");
        Assert.assertEquals(message.getDetail(), customMessage);

        Graphene.guardAjax(select.openSelect()).select("California");
        Assert.assertFalse(message.advanced().isVisible(), "Validator message should not be visible");
    }
}
