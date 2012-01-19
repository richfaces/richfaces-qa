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
package org.richfaces.tests.metamer.ftest.richJQuery;

import static org.jboss.arquillian.ajocado.Ajocado.alertEquals;
import static org.jboss.arquillian.ajocado.Ajocado.countEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitAjax;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.arquillian.ajocado.Ajocado.waitModel;

import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.jQueryAttributes;
import static org.richfaces.tests.metamer.ftest.richJQuery.RichJQueryAttributes.attachType;
import static org.richfaces.tests.metamer.ftest.richJQuery.RichJQueryAttributes.event;
import static org.richfaces.tests.metamer.ftest.richJQuery.RichJQueryAttributes.query;
import static org.richfaces.tests.metamer.ftest.richJQuery.RichJQueryAttributes.selector;
import static org.richfaces.tests.metamer.ftest.richJQuery.RichJQueryAttributes.timing;

import static org.testng.Assert.assertEquals;

import java.awt.Color;
import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.cheiron.retriever.ColorRetriever;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22746 $
 */
public class TestSimple extends AbstractAjocadoTest {

    JQueryLocator button = jq("#jQueryTestButton");
    JQueryLocator rebind = jq("#rebindOneClickButton");
    JQueryLocator addLiveComponent = jq("input[id$=addComponentButton]");
    JQueryLocator liveTestComponent = jq("div.liveTestComponent");

    ColorRetriever buttonColorRetriver = new ColorRetriever().locator(button);

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richJQuery/simple.xhtml");
    }

    @Test
    public void testDefaultTiming() {
        setupDomReadyTypeAttributes();
        jQueryAttributes.reset(timing);
        assertEquals(buttonColorRetriver.retrieve(), Color.RED);
    }

    @Test
    public void testTimingImmediate() {
        setupImmediateTypeAttributes();
        buttonColorRetriver.initializeValue();
        selenium.click(button);
        assertEquals(waitModel.waitForChangeAndReturn(buttonColorRetriver), Color.RED);
    }

    @Test
    public void testTimingDomReady() {
        setupDomReadyTypeAttributes();
        assertEquals(buttonColorRetriver.retrieve(), Color.RED);
    }

    @Test
    public void testAttachTypeOne() {
        setupImmediateTypeAttributes();
        jQueryAttributes.set(attachType, "one");
        jQueryAttributes.set(query, js("alert('first')"));

        selenium.click(button);
        waitGui.until(alertEquals.message("first"));
        selenium.click(button);
        selenium.click(button);

        for (int i = 0; i < 3; i++) {
            selenium.click(rebind);
            selenium.click(button);
            waitGui.until(alertEquals.message("one attachType rebound event"));
        }
    }

    @Test
    public void testAttachTypeLive() {
        for (int count = 1; count <= 4; count++) {
            if (count > 1) {
                selenium.click(addLiveComponent);
                waitAjax.until(countEquals.locator(liveTestComponent).count(count));
            }

            for (int i = 1; i <= count; i++) {
                JQueryLocator component = liveTestComponent.get(i);

                String message = selenium.getText(component);
                selenium.click(component);
                waitGui.until(alertEquals.message(message));
            }

        }
    }

    private void setupImmediateTypeAttributes() {
        jQueryAttributes.set(event, "click");
        jQueryAttributes.set(query, js("$(this).css('color', 'red')"));
        jQueryAttributes.set(selector, button);
        jQueryAttributes.set(timing, "immediate");
    }

    private void setupDomReadyTypeAttributes() {
        jQueryAttributes.reset(event);
        jQueryAttributes.set(query, js("css('color', 'red')"));
        jQueryAttributes.set(selector, button);
        jQueryAttributes.set(timing, "domready");
    }

}
