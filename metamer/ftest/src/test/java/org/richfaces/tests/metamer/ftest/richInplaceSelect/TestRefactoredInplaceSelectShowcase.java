/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richInplaceSelect;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.inplaceSelect.RichFacesInplaceSelect;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

public class TestRefactoredInplaceSelectShowcase extends AbstractWebDriverTest {

    private final Attributes<InplaceSelectAttributes> inplaceSelectAttributes = getAttributes();

    @FindBy(css = ".rf-is")
    private RichFacesInplaceSelect inplaceSelect;

    @Override
    public String getComponentTestPagePath() {
        return "richInplaceSelect/simple.xhtml";
    }

    @Test
    public void testInplaceSelectCancelConfirm() {
        Graphene.guardAjax(inplaceSelect).select(2);

        String selectedValue = inplaceSelect.getTextInput().getStringValue();
        assertEquals(selectedValue, "Arizona");

        attsSetter()
            .setAttribute(InplaceSelectAttributes.saveOnBlur).toValue(false)
            .setAttribute(InplaceSelectAttributes.saveOnSelect).toValue(false)
            .asSingleAction().perform();

        Graphene.guardAjax(inplaceSelect.select(4)).confirm();
        inplaceSelect.select(3).cancel();
        selectedValue = inplaceSelect.getTextInput().getStringValue();
        assertEquals(selectedValue, "California");

        inplaceSelectAttributes.set(InplaceSelectAttributes.showControls, true);
        Graphene.guardAjax(inplaceSelect.select(3)).confirmByControlls();
        selectedValue = inplaceSelect.getTextInput().getStringValue();
        assertEquals(selectedValue, "Arkansas");
    }
}
