/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richInplaceInput;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import javax.faces.event.PhaseId;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.input.inplaceInput.EditingState;
import org.richfaces.tests.page.fragments.impl.input.inplaceInput.InplaceInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.input.inplaceInput.InplaceInputComponent.OpenBy;
import org.richfaces.tests.page.fragments.impl.input.inplaceInput.InplaceInputComponent.State;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richInplaceInput/fAjax.xhtml.
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichInplaceInputFAjax extends AbstractWebDriverTest<MetamerPage> {

    @FindBy(css = "span[id$=inplaceInput]")
    private InplaceInputComponentImpl inplaceInput;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceInput/fAjax.xhtml");
    }

    @Test
    public void testClick() {
        assertTrue(inplaceInput.isVisible(), "Inplace input should be visible.");

        EditingState editingState = MetamerPage.waitRequest(inplaceInput, WaitRequestType.NONE).editBy(OpenBy.CLICK);
        assertTrue(inplaceInput.is(State.active), "Input should be active.");

        String testedValue = "new value";
        MetamerPage.waitRequest(editingState.type(testedValue), WaitRequestType.XHR).confirm();

        assertTrue(inplaceInput.is(State.changed), "Input should contain class indicating a change.");
        assertEquals(inplaceInput.getEditValue(), testedValue, "Input should contain typed text.");
        assertEquals(inplaceInput.getLabelValue(), testedValue, "Label should contain typed text.");

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: RichFaces 4 -> " + testedValue);
        page.assertPhases(PhaseId.ANY_PHASE);
    }
}
