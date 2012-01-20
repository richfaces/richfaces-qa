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
package org.richfaces.tests.metamer.ftest.richHotKey;

import static org.jboss.arquillian.ajocado.Ajocado.countEquals;
import static org.jboss.arquillian.ajocado.Ajocado.elementPresent;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.waiting.NegationCondition;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.attributes.Attributes;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractRichHotKeyTest extends AbstractAjocadoTest {

    protected static final String INPUT_1_LOCATOR = "input.first-input";
    protected static final String INPUT_2_LOCATOR = "input.second-input";

    protected static final JQueryLocator HOTKEY_1 = pjq("span[id$=richHotKey1]");
    protected static final JQueryLocator HOTKEY_2 = pjq("span[id$=richHotKey2]");

    protected static final JQueryLocator INPUT_1 = pjq(INPUT_1_LOCATOR);
    protected static final JQueryLocator INPUT_2 = pjq(INPUT_2_LOCATOR);

    private static final JQueryLocator CLEAR_BUTTON = pjq("button.rf-log-element");
    private static final String HOT_KEY_EVENT_LOCATOR_WITH_CONTENT = "span.rf-log-entry-msg-info:contains({0})";
    private static final String HOT_KEY_EVENT_LOCATOR_WITH_ORDER = "span.rf-log-entry-msg-info:eq({0})";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richHotKey/simple.xhtml");
    }

    protected void checkEvent(String text, int number) {
        waitGui
            .failWith("The number of hotkey events doesn't match. Found <" + selenium.getCount(pjq(format(HOT_KEY_EVENT_LOCATOR_WITH_CONTENT, text))) + ">, expected <" + number + ">.")
            .until(countEquals.locator(pjq(format(HOT_KEY_EVENT_LOCATOR_WITH_CONTENT, text))).count(number));
    }

    protected void clearHotKeyEvents() {
        selenium.click(CLEAR_BUTTON);
        waitGui.until(NegationCondition.getInstance().condition(elementPresent.locator(pjq(format(HOT_KEY_EVENT_LOCATOR_WITH_CONTENT, "''")))));
    }

    protected List<String> getEvents() {
        List<String> events = new ArrayList<String>();
        int eventsCount = selenium.getCount(pjq(format(HOT_KEY_EVENT_LOCATOR_WITH_CONTENT, "''")));
        for (int i=0; i<eventsCount; i++) {
            events.add(selenium.getText(pjq(format(HOT_KEY_EVENT_LOCATOR_WITH_ORDER, String.valueOf(i)))));
        }
        return events;
    }

    protected static final Attributes<HotKeyAttributes> ATTRIBUTES_FIRST = new Attributes<HotKeyAttributes>(pjq("table.attributes[id$=attributes1]"));

    protected static final Attributes<HotKeyAttributes> ATTRIBUTES_SECOND = new Attributes<HotKeyAttributes>(pjq("table.attributes[id$=attributes2]"));

}
