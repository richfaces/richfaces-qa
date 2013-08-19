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
package org.richfaces.tests.metamer.ftest.richInplaceSelect;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.inplaceSelectAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.metamer.model.Capital;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.InplaceComponent;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.OptionsList;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.RichFacesInplaceSelect;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.EditingState.FinishEditingBy;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.InplaceComponent.OpenBy;
import org.richfaces.tests.page.fragments.impl.inplaceSelect.InplaceComponent.State;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richInplaceSelect/fAjax.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInplaceSelectFAjax extends AbstractWebDriverTest {

    @Page
    private MetamerPage page;
    @FindBy(css = "[id$=inplaceSelect]")
    private RichFacesInplaceSelect select;
    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=inplaceSelect] span.rf-is-lst-cord")
    private WebElement popup;
    @FindBy(css = "body > span.rf-is-lst-cord")
    private WebElement globalPopup;

    private String getOutputText() {
        return output.getText().trim();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceSelect/fAjax.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11227")
    public void testClick() {
        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, Boolean.TRUE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnBlur, Boolean.FALSE);
        inplaceSelectAttributes.set(InplaceSelectAttributes.saveOnSelect, Boolean.FALSE);

        OptionsList options = MetamerPage.waitRequest(select, WaitRequestType.NONE, 1000).editBy(OpenBy.CLICK).getOptions();
        assertTrue(select.is(InplaceComponent.State.ACTIVE), "Select should be active.");
        assertFalse(select.is(InplaceComponent.State.CHANGED), "Select should not have changed value.");
        assertVisible(globalPopup, "Popup should be displayed.");

        assertEquals(options.size(), 50, "50 options should be displayed.");

        List<Capital> capitals = Model.unmarshallCapitals();
        for (int i = 0; i < options.size(); i++) {
            assertEquals(options.get(i).getText(), capitals.get(i).getState());
        }

        select.editBy(OpenBy.CLICK).changeToValueAtIndex(10).confirm(FinishEditingBy.CONTROLS);
        Graphene.waitModel().until().element(output).text().equalTo("Hawaii");
        assertEquals(getOutputText(), "Hawaii", "Output should contain selected value.");
        assertEquals(select.getLabelValue(), "Hawaii", "Label should contain selected value.");
        assertTrue(select.is(State.CHANGED), "Select should have changed value.");
        assertFalse(select.is(State.ACTIVE), "Select should not be active.");

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: -> Hawaii");
    }
}
