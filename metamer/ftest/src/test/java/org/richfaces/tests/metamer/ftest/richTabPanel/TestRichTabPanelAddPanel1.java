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
package org.richfaces.tests.metamer.ftest.richTabPanel;

import static org.jboss.arquillian.ajocado.Graphene.countEquals;
import static org.jboss.arquillian.ajocado.Graphene.elementPresent;
import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.textEquals;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richTabPanel/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22999 $
 */
public class TestRichTabPanelAddPanel1 extends AbstractGrapheneTest {

    private static final String SWITCH_TYPE_CLIENT = "client";
    private static final String SWITCH_TYPE_SERVER = "server";
    private static final String SWITCH_TYPE_AJAX = "ajax";

    private static final int MAX_NEW_TAB_COUNT = 3;
    private static final int STATIC_TAB_COUNT = 5;

    private JQueryLocator itemContentsFormat = pjq("div[id$=tab{0}:content]");

    private JQueryLocator inactiveHeadersFormat = pjq("td[id$=tab{0}:header:inactive]");

    private JQueryLocator hCreateTabBtn = pjq("input[id$=hCreateTabButton]");
    private JQueryLocator a4jCreateTabBtn = pjq("input[id$=a4jCreateTabButton]");

    private JQueryLocator tabsCount = pjq("table.rf-tab-hdr-tabs td.rf-tab-hdr-inact");

    private JQueryLocator switchTypeFormat = pjq("input[name$=switchTypeInput][value={0}]");

    private JQueryLocator tabCloseFormat = pjq("td[id$=tab{0}:header:inactive] span.rf-tab-lbl > a");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/addTab2.xhtml");
    }

    /**
     * Simple create new tab (without tab switch)
     *
     * @param addTabBtn
     */
    private void verifyCreateTab(JQueryLocator addTabBtn) {

        int baseTabsCount = selenium.getCount(tabsCount);

        // add 3 new tabs
        for (int i = 1; i <= MAX_NEW_TAB_COUNT; ++i) {
            selenium.click(addTabBtn);
            waitGui.until(countEquals.count(baseTabsCount + i).locator(tabsCount));
        }
    }

    /**
     * Simple test to tab delete. Create 3 new tabs and then delete them. Without tab switch.
     */
    private void verifyDeleteTab() {
        verifyCreateTab(a4jCreateTabBtn);

        int baseTabsCount = selenium.getCount(tabsCount);
        System.out.println(baseTabsCount);
        for (int i = 0; i < MAX_NEW_TAB_COUNT; ++i) {
            waitGui
                .until(elementPresent.locator(inactiveHeadersFormat.format(STATIC_TAB_COUNT + MAX_NEW_TAB_COUNT - i)));
            selenium.fireEvent(tabCloseFormat.format(STATIC_TAB_COUNT + MAX_NEW_TAB_COUNT - i), Event.CLICK);
            System.out.println(selenium.getCount(tabsCount));
            waitGui.until(countEquals.count(baseTabsCount - 1 - i).locator(tabsCount));
        }
    }

    private void verifyContentOfNewTab(JQueryLocator addTabBtn) {

        verifyCreateTab(addTabBtn);

        // tab switch work at least with "client" option
        selenium.click(switchTypeFormat.format(SWITCH_TYPE_CLIENT));
        selenium.waitForPageToLoad();

        System.out.println("testSwitchTypeAjax: tab count: " + selenium.getCount(tabsCount));

        for (int i = STATIC_TAB_COUNT + MAX_NEW_TAB_COUNT; i > STATIC_TAB_COUNT; i--) {
            selenium.click(inactiveHeadersFormat.format(i));
            waitGui.failWith("Tab " + i + " doesn't display correct content.").until(
                textEquals.text("Content of dynamicaly created tab" + i)
                    .locator(itemContentsFormat.format(i)));
        }
    }

    /**
     * Create new tab by clicking on h:commandButton
     */
    @Test
    public void testCreateTabJSF() {
        verifyCreateTab(hCreateTabBtn);
    }

    /**
     * Create new tab by clicking on a4j:commandButton
     */
    @Test
    public void testCreateTabAjax() {
        verifyCreateTab(a4jCreateTabBtn);
    }

    /**
     * Delete newly created tabs
     */
    @Test
    public void testRemoveTab() {
        verifyDeleteTab();
    }

    /**
     * Verify that all tabs displays correct content when switch tab
     */
    @Test
    public void testContentOfDynamicTab() {
        verifyContentOfNewTab(hCreateTabBtn);
    }

    /**
     * Test plan: 1. click on 'create tab' btn 3 time and verify that new tabs appeared 2. verify that switch between
     * newly created tabs still works as in previous tabs (staticaly created) 3. verify a4j ajax btn to create new tabs
     */
    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11081")
    public void testSwitchTypeNull() {

        verifyCreateTab(hCreateTabBtn);

        for (int i = STATIC_TAB_COUNT + MAX_NEW_TAB_COUNT - 1; i >= STATIC_TAB_COUNT; i--) {
            final int index = i;
            guardXhr(selenium).click(inactiveHeadersFormat.format(index + 1));
            waitGui.failWith("Tab " + (index + 1) + " is not present.").until(
                elementPresent.locator(itemContentsFormat.format(index + 1)));
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11081")
    public void testSwitchTypeAjax() {

        verifyCreateTab(hCreateTabBtn);

        selenium.click(switchTypeFormat.format(SWITCH_TYPE_AJAX));
        selenium.waitForPageToLoad();

        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {

        verifyCreateTab(hCreateTabBtn);

        selenium.click(switchTypeFormat.format(SWITCH_TYPE_CLIENT));
        selenium.waitForPageToLoad();

        for (int i = STATIC_TAB_COUNT + MAX_NEW_TAB_COUNT - 1; i >= STATIC_TAB_COUNT; i--) {
            final int index = i;
            guardNoRequest(selenium).click(inactiveHeadersFormat.format(index + 1));
            waitGui.failWith("Tab " + (index + 1) + " is not displayed.").until(
                elementVisible.locator(itemContentsFormat.format(index + 1)));
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11054")
    public void testSwitchTypeServer() {

        verifyCreateTab(hCreateTabBtn);

        selenium.click(switchTypeFormat.format(SWITCH_TYPE_SERVER));
        selenium.waitForPageToLoad();

        for (int i = STATIC_TAB_COUNT + MAX_NEW_TAB_COUNT - 1; i >= STATIC_TAB_COUNT; i--) {
            final int index = i;
            guardHttp(selenium).click(inactiveHeadersFormat.format(index + 1));
            waitGui.failWith("Tab " + (index + 1) + " is not displayed.").until(
                elementVisible.locator(itemContentsFormat.format(index + 1)));
        }
    }

}
