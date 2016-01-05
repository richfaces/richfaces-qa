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
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractResetValuesTest extends AbstractWebDriverTest {

    @FindBy(css = "[id$='increaseElement']")
    private WebElement increaseElement;
    @FindBy(css = "[id$=v1]")
    private TextInputComponentImpl input1;
    @FindBy(css = "[id$=submitButton]")
    private WebElement submitButton;

    public void checkResetValues() {
        setAttribute("resetValues", false);

        int val = input1.getIntValue();
        increaseValue();
        assertEquals(input1.getIntValue(), ++val);
        increaseValue();
        assertEquals(input1.getIntValue(), ++val);

        // submit, first value should stay the same with every increase,, because we are not using @resetValues
        Graphene.guardAjax(submitButton).click();
        val = input1.getIntValue();
        increaseValue();
        assertEquals(input1.getIntValue(), val);
        increaseValue();
        assertEquals(input1.getIntValue(), val);

        setAttribute("resetValues", true);
        val = input1.getIntValue() + 2;// 2 previous requests increased the value, but value was not rendered
        increaseValue();
        assertEquals(input1.getIntValue(), ++val);
        increaseValue();
        assertEquals(input1.getIntValue(), ++val);
        // submit, first value should increase normally, because we are using @resetValues=true
        Graphene.guardAjax(submitButton).click();
        val = input1.getIntValue();
        increaseValue();
        assertEquals(input1.getIntValue(), ++val);
        increaseValue();
        assertEquals(input1.getIntValue(), ++val);
    }

    public WebElement getIncreaseElement() {
        return increaseElement;
    }

    protected void increaseValue() {
        Graphene.guardAjax(getIncreaseElement()).click();
    }
}
