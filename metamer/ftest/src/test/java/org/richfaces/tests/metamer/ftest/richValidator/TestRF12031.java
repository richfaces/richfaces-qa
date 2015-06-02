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

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.testng.annotations.Test;

/**
 * Test demonstrating RF-12031
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestRF12031 extends AbstractWebDriverTest {

    @FindBy(css = "input[id$='inputRF12031']")
    private WebElement inputRF12031;
    @FindBy(css = "span[id$='msgRF12031']")
    private RichFacesMessage msgRF12031;
    @FindByJQuery(value = "input[id$='toggleButton']")
    private WebElement toggleButton;

    @Override
    public String getComponentTestPagePath() {
        return "richValidator/rf-12031.xhtml";
    }

    @Test
    @Skip
    @IssueTracking({ "https://issues.jboss.org/browse/RF-12031", "https://issues.jboss.org/browse/RF-12536" })
    public void testCSVOnConditionallyRenderedInput() {
        toggleButton.click();
        Graphene.waitGui().until().element(inputRF12031).is().present();

        inputRF12031.sendKeys("RichFaces 4");
        fireEvent(inputRF12031, Event.BLUR);

        msgRF12031.advanced().waitUntilMessageIsVisible();
    }
}
