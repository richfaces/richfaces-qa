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
package org.richfaces.tests.metamer.ftest.richSelect;

import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.textEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.Test;


/**
 * Test case for page faces/components/richSelect/fAjax.xhtml.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22493 $
 */
public class TestRichSelectFAjax extends AbstractAjocadoTest {

    private JQueryLocator input = pjq("input.rf-sel-inp[id$=selectInput]");
    private JQueryLocator popup = jq("div.rf-sel-lst-cord");
    private JQueryLocator options = jq("div.rf-sel-opt:eq({0})"); // 00..49
    private JQueryLocator button = pjq("span.rf-sel-btn");
    private JQueryLocator output = pjq("span[id$=output]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richSelect/fAjax.xhtml");
    }

    @Test
    public void testSelectWithMouse() {
        guardNoRequest(selenium).mouseDown(button);
        selenium.mouseUp(button);
        assertTrue(selenium.isVisible(popup), "Popup should be displayed.");

        for (int i = 0; i < 50; i++) {
            assertTrue(selenium.isVisible(options.format(i)), "Select option " + i + " should be displayed.");
        }

        String[] selectOptions = {"Alabama", "Hawaii", "Massachusetts", "New Mexico", "South Dakota"};
        for (int i = 0; i < 50; i += 10) {
            assertEquals(selenium.getText(options.format(i)), selectOptions[i / 10], "Select option nr. " + i);
        }

        guardNoRequest(selenium).click(options.format(10));
        guardXhr(selenium).fireEvent(input, Event.BLUR);
        assertTrue(selenium.belongsClass(options.format(10), "rf-sel-sel"));

        waitGui.failWith("Bean was not updated").until(textEquals.locator(output).text("Hawaii"));
        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }
}
