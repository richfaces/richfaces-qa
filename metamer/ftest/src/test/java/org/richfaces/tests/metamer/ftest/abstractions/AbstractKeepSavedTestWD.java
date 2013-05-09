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
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;

/**
 * @author jstefek
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 5.0.0.Alpha1
 */
public class AbstractKeepSavedTestWD extends AbstractWebDriverTest {

    private static final String TEXT = "a";

    @FindBy(jquery = ".content input[type=text]:first")
    public WebElement inputFirst;
    @FindBy(css = "input[id$=submitButton]")
    public WebElement submit;
    @FindBy(css = "div.messages li")
    public WebElement errorMessage;
    @FindBy(css = "input[type=radio][value=true]")
    public WebElement keepSavedInput;

    private final String componentSpecifier;

    public AbstractKeepSavedTestWD(String componentSpecifier) {
        this.componentSpecifier = componentSpecifier;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/" + componentSpecifier + "/keepSaved.xhtml");
    }

    public String getComponentSpecifier() {
        return componentSpecifier;
    }

    public void testKeepSavedFalse() {
        inputFirst.sendKeys(TEXT);
        Graphene.guardHttp(submit).click();
        assertPresent(errorMessage, "Error message should be present.");
        Graphene.guardHttp(submit).click();
        assertNotPresent(errorMessage, "Error message should not be present.");
    }

    public void testKeepSavedTrue() {
        Graphene.guardHttp(keepSavedInput).click();
        inputFirst.sendKeys(TEXT);
        Graphene.guardHttp(submit).click();
        assertPresent(errorMessage, "Error message should be present.");
        Graphene.guardHttp(submit).click();
        assertPresent(errorMessage, "Error message should be present.");
    }
}
