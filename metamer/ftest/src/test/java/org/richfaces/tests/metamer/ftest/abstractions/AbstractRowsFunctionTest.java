/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AbstractRowsFunctionTest extends AbstractWebDriverTest {

    private final String componentName;

    @FindBy(css = "[id$=incrementFirstLast] ")
    private WebElement incrementFirstLast;
    @FindBy(css = "[id$=incrementExceptFirstLast] ")
    private WebElement incrementExceptFirstLast;
    @FindBy(css = "[id$=resetValues] ")
    private WebElement resetValues;
    @FindBy(css = "[id$=updateValues] ")
    private WebElement updateValues;

    @FindBy(css = "[id$=output]")
    private List<WebElement> outputs;

    /**
     * @param componentName used for building the URL.
     */
    public AbstractRowsFunctionTest(String componentName) {
        this.componentName = componentName;
    }

    private void checkOutputValues(Integer... values) {
        assertEquals(getOutputValues(), Lists.newArrayList(values));
    }

    public void checkRowsFunction() {
        checkOutputValues(0, 0, 0, 0);
        incrementFirstLast();
        checkOutputValues(1, 0, 0, 1);
        incrementFirstLast();
        checkOutputValues(2, 0, 0, 2);
        incrementExceptFirstLast();
        checkOutputValues(2, 3, 3, 2);
        incrementExceptFirstLast();
        checkOutputValues(2, 4, 4, 2);
        incrementFirstLast();
        checkOutputValues(5, 4, 4, 5);

        resetValues();
        checkOutputValues(0, 0, 0, 0);
        incrementFirstLast();
        checkOutputValues(1, 0, 0, 1);
        updateValues();
        checkOutputValues(1, 1, 1, 1);
    }

    private void clickGuardedButton(WebElement button) {
        Graphene.guardAjax(button).click();
    }

    private List<Integer> getOutputValues() {
        List<Integer> outputsValues = Lists.newArrayList();
        for (WebElement webElement : outputs) {
            outputsValues.add(Integer.parseInt(webElement.getText().replace("value = ", "").trim()));
        }
        return outputsValues;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/" + componentName + "/rows.xhtml");
    }

    private void incrementExceptFirstLast() {
        clickGuardedButton(incrementExceptFirstLast);
    }

    private void incrementFirstLast() {
        clickGuardedButton(incrementFirstLast);
    }

    private void resetValues() {
        clickGuardedButton(resetValues);
    }

    private void updateValues() {
        clickGuardedButton(updateValues);
    }
}
