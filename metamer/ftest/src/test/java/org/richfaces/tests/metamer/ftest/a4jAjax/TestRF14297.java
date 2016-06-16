/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jAjax;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.CheckboxInputComponentImpl;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF14297 extends AbstractWebDriverTest {

    private final Attributes<AjaxAttributes> atts = getAttributes();

    @FindBy(css = "[id$='selectBooleanCheckbox']")
    private CheckboxInputComponentImpl checkbox;
    @FindBy(css = "[id$='output']")
    private WebElement output;

    @Override
    public String getComponentTestPagePath() {
        return "a4jAjax/rf-14297.xhtml";
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-14297")
    public void testListenerIsInvoked() {
        atts.set(AjaxAttributes.listener, "toUpperCaseListener");
        Boolean actualValue = Boolean.valueOf(output.getText());

        CheckboxInputComponentImpl guardedCheckbox = Graphene.guardAjax(checkbox);
        for (int i = 0; i < 3; i++) {
            if (actualValue) {
                guardedCheckbox.uncheck();
            } else {
                guardedCheckbox.check();
            }
            actualValue = !actualValue;
            Graphene.waitAjax().until().element(output).text().equalTo(actualValue.toString().toUpperCase());
            getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "listener invoked");
        }
    }
}
