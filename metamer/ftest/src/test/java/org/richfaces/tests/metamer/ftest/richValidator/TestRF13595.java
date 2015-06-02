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
package org.richfaces.tests.metamer.ftest.richValidator;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.calendar.RichFacesCalendar;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.metamer.bean.issues.RF13595;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * This is a reproducer test for RF-13595
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 *
 */
public class TestRF13595 extends AbstractWebDriverTest {

    @FindBy(className = "rf-cal")
    private RichFacesCalendar calendar;
    @FindBy(className = "rf-msg")
    private RichFacesMessage message;
    @FindBy(css = "input[id$=setInputVisibleButton]")
    private WebElement toggleButton;

    @Override
    public String getComponentTestPagePath() {
        return "richValidator/rf-13595.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13595")
    public void testToggledInputValidator() {
        // toggle calendar and wait until it is present
        Graphene.guardAjax(toggleButton).click();
        Graphene.waitGui().until().element(calendar.advanced().getRootElement()).is().present();

        // set today date
        Graphene.guardAjax(calendar).setDate(new DateTime());

        // expect validation error msg
        assertEquals(message.getDetail(), RF13595.VALIDATION_MESSAGE);
    }
}
