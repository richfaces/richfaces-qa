/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.ScrollingType;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.select.RichFacesSelect;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestSelectFragment extends AbstractWebDriverTest {

    @FindBy(css = "div[id$=select]")
    private RichFacesSelect select;
    @FindBy(css = "span[id$=output]")
    private WebElement output;

    private final Attributes<SelectAttributes> attributes = getAttributes();

    @UseForAllTests(valuesFrom = FROM_ENUM, value = "")
    private ScrollingType scrollingType;
    @UseForAllTests(valuesFrom = FROM_FIELD, value = "booleans")
    private Boolean selectFirst;

    private String getOutputText() {
        return output.getText();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richSelect/simple.xhtml");
    }

    @Test
    @Templates("plain")
    public void testOpenAndSelect() {
        attributes.set(SelectAttributes.selectFirst, selectFirst);
        select.advanced().setupScrollingType(scrollingType);

        Graphene.guardAjax(select.openSelect()).select(0);
        assertEquals(getOutputText(), "Alabama");
        select.advanced().getInput().clear();

        Graphene.guardAjax(select.openSelect()).select("Michigan");
        assertEquals(getOutputText(), "Michigan");
        select.advanced().getInput().clear();

        Graphene.guardAjax(select.openSelect()).select(ChoicePickerHelper.byIndex().last());
        assertEquals(getOutputText(), "Wyoming");
    }

    @Test
    @Templates("plain")
    public void testTypeAndSelect() {
        attributes.set(SelectAttributes.selectFirst, selectFirst);
        select.advanced().setupScrollingType(scrollingType);

        Graphene.guardAjax(select.type("ala")).select(0);
        assertEquals(getOutputText(), "Alabama");

        Graphene.guardAjax(select.type("m")).select("Michigan");
        assertEquals(getOutputText(), "Michigan");

        Graphene.guardAjax(select.type("c")).select(ChoicePickerHelper.byIndex().last());
        assertEquals(getOutputText(), "Connecticut");
    }
}
