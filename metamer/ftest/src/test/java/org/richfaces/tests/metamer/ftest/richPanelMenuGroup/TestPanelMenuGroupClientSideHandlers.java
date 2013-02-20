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
package org.richfaces.tests.metamer.ftest.richPanelMenuGroup;

import static java.text.MessageFormat.format;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.component.Mode.ajax;
import static org.richfaces.component.Mode.client;
import static org.richfaces.component.Mode.server;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.mode;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.panelMenuGroupAttributes;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
public class TestPanelMenuGroupClientSideHandlers extends AbstractPanelMenuGroupTest {

    private static final String ATTR_INPUT_LOC_FORMAT = "input[id$=on{0}Input]";

    @Inject
    @Use(empty = true)
    String event;
    String[] ajaxExpansionEvents = new String[] { "beforeselect", "beforeswitch", "beforeexpand", "begin",
        "beforedomupdate", "select", "expand", "switch", "complete" };
    String[] ajaxCollapsionEvents = new String[] { "beforeselect", "beforeswitch", "beforecollapse", "begin",
        "beforedomupdate", "select", "collapse", "switch", "complete" };
    String[] clientExpansionEvents = new String[] { "beforeselect", "beforeswitch", "beforeexpand", "select", "expand",
        "switch" };
    String[] clientCollapsionEvents = new String[] { "beforeselect", "beforeswitch", "beforecollapse", "select",
        "collapse", "collapse", "switch" };
    String[] serverExpansionEvents1 = new String[] { "beforeswitch" };
    String[] serverExpansionEvents2 = new String[] { "beforeexpand" };
    String[] serverCollapsionEvents = new String[] { "beforeswitch", "beforecollapse" };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenuGroup/simple.xhtml");
    }

    @Test
    @Use(field = "event", value = "ajaxCollapsionEvents")
    public void testClientSideCollapsionEvent() {
        panelMenuGroupAttributes.set(mode, ajax);
        page.topGroup.setMode(ajax);
        testRequestEventsBefore(event);
        page.topGroup.toggle();
        testRequestEventsAfter(event);
    }

    @Test
    @Use(field = "event", value = "ajaxExpansionEvents")
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEvent() {
        panelMenuGroupAttributes.set(mode, ajax);
        page.topGroup.setMode(ajax);
        page.topGroup.toggle();
        testRequestEventsBefore(event);
        page.topGroup.toggle();
        testRequestEventsAfter(event);
    }

    @Test
    @Use(field = "event", strings = { "beforeselect", "beforeswitch", "begin", "beforedomupdate", "select", "switch",
        "complete" })
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEventInIterationComponents() {
        testClientSideExpansionEvent();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11547")
    @Use(field = "event", strings = { "beforeexpand", "expand" })
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEventInIterationComponentsExpand() {
        testClientSideExpansionEvent();
    }

    @Test
    public void testClientSideExpansionEventsOrderClient() {
        panelMenuGroupAttributes.set(mode, client);
        page.topGroup.setMode(client);
        page.group1.setMode(client);
        testRequestEventsBefore(clientExpansionEvents);
        page.topGroup.toggle();
        page.group1.toggle();
        executeJS("window.metamerEvents = \"\";");
        page.topGroup.toggle();
        testRequestEventsAfter(clientExpansionEvents);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10564")
    public void testClientSideCollapsionEventsOrderClient() {
        panelMenuGroupAttributes.set(mode, client);
        page.topGroup.setMode(client);
        page.topGroup.toggle();
        testRequestEventsBefore(clientCollapsionEvents);
        page.topGroup.toggle();
        testRequestEventsAfter(clientCollapsionEvents);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12549")
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEventsOrderAjax() {
        panelMenuGroupAttributes.set(mode, ajax);
        page.topGroup.setMode(ajax);
        page.topGroup.toggle();
        testRequestEventsBefore(ajaxExpansionEvents);
        executeJS("window.metamerEvents = \"\";");
        page.topGroup.toggle();
        testRequestEventsAfter(ajaxExpansionEvents);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11547")
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEventsOrderAjaxInIterationComponents() {
        testClientSideExpansionEventsOrderAjax();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12549")
    public void testClientSideCollapsionEventsOrderAjax() {
        panelMenuGroupAttributes.set(mode, ajax);
        page.topGroup.setMode(ajax);
        testRequestEventsBefore(ajaxCollapsionEvents);
        page.topGroup.toggle();
        testRequestEventsAfter(ajaxCollapsionEvents);
    }

    @Test
    @Use(field = "event", value = "serverExpansionEvents1")
    public void testClientSideExpansionEventsServerBeforeSwitch() {
        panelMenuGroupAttributes.set(mode, server);
        page.topGroup.setMode(server);
        page.topGroup.toggle();
        page.topGroup.setMode(null);
        testRequestEventsBeforeByAlert(event);
        page.topGroup.toggle();
        testRequestEventsAfterByAlert(event);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11547")
    @Use(field = "event", value = "serverExpansionEvents2")
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEventsServerBeforeExpand() {
        panelMenuGroupAttributes.set(mode, server);
        page.topGroup.setMode(server);
        page.topGroup.toggle();
        page.topGroup.setMode(null);
        testRequestEventsBeforeByAlert(event);
        page.topGroup.toggle();
        testRequestEventsAfterByAlert(event);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11547")
    @Use(field = "event", value = "serverExpansionEvents2")
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEventsServerBeforeExpandIterationComponents() {
        testClientSideExpansionEventsServerBeforeExpand();
    }

    @Test
    @Use(field = "event", value = "serverCollapsionEvents")
    public void testClientSideCollapsionEventsServer() {
        panelMenuGroupAttributes.set(mode, server);
        page.topGroup.setMode(null);
        testRequestEventsBeforeByAlert(event);
        page.topGroup.toggle();
        testRequestEventsAfterByAlert(event);
    }

    public void testRequestEventsBefore(String... events) {
        for (String event : events) {
            String inputExp = format(ATTR_INPUT_LOC_FORMAT, event);
            WebElement input = page.attributesTable.findElement(By.cssSelector(inputExp));
            String inputVal = format("metamerEvents += \"{0} \"", event);
            // even there would be some events (in params) twice, don't expect handle routine to be executed twice
            input.clear();
            waiting(1000);
            input = page.attributesTable.findElement(By.cssSelector(inputExp));
            input.sendKeys(inputVal);
            // sendKeys triggers page reload automatically
            waiting(300);
            Graphene.waitAjax().until(ElementPresent.getInstance().element(page.attributesTable));
            input = page.attributesTable.findElement(By.cssSelector(inputExp));
            MetamerPage.waitRequest(input, WaitRequestType.HTTP).submit();
        }
        executeJS("window.metamerEvents = \"\";");
    }

    public void testRequestEventsAfter(String... events) {
        String[] actualEvents = ((String)executeJS("return window.metamerEvents")).split(" ");
        assertEquals(actualEvents, events, format("The events ({0}) don't came in right order ({1})",
            Arrays.deepToString(actualEvents), Arrays.deepToString(events)));
    }

    public void testRequestEventsBeforeByAlert(String... events) {
        for (String event : events) {
            String inputExp = format(ATTR_INPUT_LOC_FORMAT, event);
            WebElement input = page.attributesTable.findElement(By.cssSelector(inputExp));

            input.sendKeys(format("alert(\"{0}\")", event));
            // sendKeys triggers page reload automatically
            waiting(300);
            Graphene.waitAjax().until(ElementPresent.getInstance().element(page.attributesTable));
            input = page.attributesTable.findElement(By.cssSelector(inputExp));
            MetamerPage.waitRequest(input, WaitRequestType.HTTP).submit();
        }
    }

    public void testRequestEventsAfterByAlert(String... events) {
        List<String> list = new LinkedList<String>();

        for (int i = 0; i < events.length; i++) {
            AlertPresent ap = new AlertPresent();
            Graphene.waitGui().until(ap);
            if (ap.apply(driver)) {
                list.add(ap.getAlert().getText());
                ap.getAlert().accept();
            }
        }

        String[] actualEvents = list.toArray(new String[list.size()]);
        assertEquals(actualEvents, events, format("The events ({0}) don't came in right order ({1})",
            Arrays.deepToString(actualEvents), Arrays.deepToString(events)));
    }

    private class AlertPresent implements Predicate<WebDriver> {
        private Alert alert;

        @Override
        public boolean apply(@Nullable WebDriver driver) {
            alert = driver.switchTo().alert();
            return  alert != null;
        }

        public Alert getAlert() { return alert; }
    }
}

