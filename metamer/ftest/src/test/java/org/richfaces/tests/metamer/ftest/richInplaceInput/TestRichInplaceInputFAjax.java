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
package org.richfaces.tests.metamer.ftest.richInplaceInput;

import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.Ajocado.textEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.Test;


/**
 * Test case for page faces/components/richInplaceInput/fAjax.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22636 $
 */
public class TestRichInplaceInputFAjax extends AbstractAjocadoTest {

    private JQueryLocator inplaceInput = pjq("span[id$=inplaceInput]");
    private JQueryLocator label = pjq("span.rf-ii-lbl");
    private JQueryLocator input = pjq("input[id$=inplaceInputInput]");
    private JQueryLocator edit = pjq("span.rf-ii-fld-cntr");
    private JQueryLocator output = pjq("span[id$=output]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceInput/fAjax.xhtml");
    }

    @Test
    public void testClick() {
        guardNoRequest(selenium).click(inplaceInput);
        assertFalse(selenium.belongsClass(edit, "rf-ii-none"),
            "Edit should not contain class rf-ii-none when popup is open.");
        assertTrue(selenium.isVisible(input), "Input should be displayed.");

        selenium.type(input, "new value");
        selenium.fireEvent(input, Event.BLUR);
        assertTrue(selenium.belongsClass(inplaceInput, "rf-ii-chng"), "New class should be added to inplace input.");
        assertTrue(selenium.belongsClass(edit, "rf-ii-none"),
            "Edit should contain class rf-ii-none when popup is closed.");

        assertEquals(selenium.getText(label), "new value", "Label should contain selected value.");
        waitGui.failWith("Output did not change.").until(textEquals.locator(output).text("new value"));

        String listenerText = selenium.getText(jq("div#phasesPanel li:eq(3)"));
        assertEquals(listenerText, "*1 value changed: RichFaces 4 -> new value",
            "Value change listener was not invoked.");

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: RichFaces 4 -> new value");
    }
}
