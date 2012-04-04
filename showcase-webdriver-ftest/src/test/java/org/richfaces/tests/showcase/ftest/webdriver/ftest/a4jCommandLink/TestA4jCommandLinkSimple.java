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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jCommandLink;

import static org.testng.Assert.assertEquals;

import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.a4jCommandLink.CommandLinkPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jCommandLinkSimple extends AbstractWebDriverTest<CommandLinkPage> {

    @Test
    public void testTypeAndSend() {
        sendKeysToInputCarefully(getPage().getInput(), "something");

        getPage().getLink().click();

        new WebDriverWait(getWebDriver())
            .until(ElementPresent.getInstance().element(getPage().getOutput()));

        assertEquals(getPage().getOutput().getText(), "Hello something !", "After typing something into the input and clicking on the command link, the text should appear in the output area.");
    }

    @Override
    protected CommandLinkPage createPage() {
        return new CommandLinkPage();
    }

}
