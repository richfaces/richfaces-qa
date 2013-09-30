/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.tests.metamer.ftest.richAccordion;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.BooleanConditionWrapper;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAccordionKVS extends AbstractWebDriverTest {

    @Page
    private AccordionPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAccordion/simple.xhtml");
    }

    @Test(groups = {"keepVisualStateTesting"})
    public void testRefreshFullPage() {
        new AccordionReloadTester().testFullPageRefresh();
    }

    @Test(groups = {"keepVisualStateTesting", "Future"})
    @IssueTracking("https://issues.jboss.org/browse/RF-12035")
    public void testRenderAll() {
        new AccordionReloadTester().testRerenderAll();
    }

    private class AccordionReloadTester extends ReloadTester<Integer> {

        public AccordionReloadTester() {
            super(page);
        }

        @Override
        public void doRequest(Integer accordionIndex) {
            page.getAccordion().switchTo(accordionIndex.intValue());
        }

        @Override
        public void verifyResponse(final Integer accordionIndex) {
            Graphene.waitAjax()
                    .withMessage("Test failed with accordion item " + (accordionIndex+1))
                    .until(new BooleanConditionWrapper(new ExpectedCondition<Boolean>() {
                        @Override
                        public Boolean apply(WebDriver input) {
                            return page.getAccordion().advanced().getAccordionItems().get(accordionIndex).advanced().isActive();
                        }
                    }, NoSuchElementException.class, StaleElementReferenceException.class));
        }

        @Override
        public Integer[] getInputValues() {
            return new Integer[]{4, 2, 1, 0};
        }
    }
}
