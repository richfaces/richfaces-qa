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
package org.richfaces.tests.metamer.ftest.richAccordionItem;

import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * Test rich:accordion keeping visual state (KVS) on page faces/components/richAccordion/simple.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision$
 */
public class TestAccordionItemKVS extends AbstractGrapheneTest {

    AccordionReloadTester reloadTester = new AccordionReloadTester();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAccordionItem/simple.xhtml");
    }

    @Test(groups = { "keepVisualStateTesting" })
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testRefreshFullPage() {
        reloadTester.testFullPageRefresh();
    }

    @Test(groups = { "keepVisualStateTesting", "4.Future" })
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    @IssueTracking("https://issues.jboss.org/browse/RF-12131")
    public void testRefreshFullPageInIterationComponents() {
        testRefreshFullPage();
    }

    @Test(groups = { "keepVisualStateTesting", "4.3" })
    @IssueTracking("https://issues.jboss.org/browse/RF-12035")
    public void testRenderAll() {
        reloadTester.testRerenderAll();
    }

    private class AccordionReloadTester extends ReloadTester<String> {
        @Override
        public void doRequest(String accordionIndex) {
            guardXhr(selenium).click(pjq("div[id$=item{0}:header]").format(accordionIndex));
        }

        @Override
        public void verifyResponse(String accordionIndex) {
            waitGui.until(elementVisible.locator(pjq("div[id$=item{0}:content]").format(accordionIndex)));
        }

        @Override
        public String[] getInputValues() {
            return new String[] { "1", "2", "3" };
        }
    }

}
