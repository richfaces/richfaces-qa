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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jQueue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.a4jQueue.QueuePage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jQueueSimple extends AbstractWebDriverTest {

    @Page
    private QueuePage page;

    @Test(groups = { "broken" })
    public void testType() {
        StringBuilder inputAll = new StringBuilder();
        for(int n=1; n<=3; n++) {
            StringBuilder input = new StringBuilder();
            for(int i=0; i<n; i++) {
                input.append("a");
            }
            inputAll.append(input);
            getPage().getInput().click();
            getPage().getInput().sendKeys(input.toString());
            Graphene.waitAjax()
                .until(new WebElementConditionFactory(getPage().getOutput()).textEquals(inputAll.toString()));
            assertEquals(getPage().getEvents().getText(), String.valueOf(inputAll.toString().length()));
            assertEquals(getPage().getUpdates().getText(), String.valueOf(n));
            assertEquals(getPage().getRequests().getText(), String.valueOf(n));
        }
    }

    @Override
    protected QueuePage getPage() {
        return page;
    }

}
