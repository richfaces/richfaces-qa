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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jPoll;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.a4jPoll.PollPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jPollSimple extends AbstractWebDriverTest {

    @Page
    private PollPage page;

    @Test(groups = {"RF-11871"})
    public void testStop() throws InterruptedException {
        getPage().getButton().click();
        String before = getPage().getDate().getText();
        Thread.sleep(1500);
        Graphene.waitAjax()
            .withMessage("After clicking on the stop button, there should be not updates.")
            .until(new WebElementConditionFactory(getPage().getDate()).textEquals(before));

    }

    @Test
    public void testUpdates() {
        for (int i=0; i<2; i++) {
            String before = getPage().getDate().getText();
            new WebDriverWait(getWebDriver(), 2)
                .withMessage("The updating doesn't work correctly.")
                .until(new WebElementConditionFactory(getPage().getDate()).not().textEquals(before));
        }
    }

    @Override
    protected PollPage getPage() {
        return page;
    }

}
