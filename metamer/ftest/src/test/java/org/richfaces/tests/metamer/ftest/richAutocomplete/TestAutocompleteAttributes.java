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
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import static java.text.MessageFormat.format;
import static org.jboss.arquillian.ajocado.Ajocado.textEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteAttributes extends AbstractAutocompleteTest {

    private static final JQueryLocator PHASE_FORMAT = jq("div#phasesPanel li:eq({0})");
    private static final String PHASE_LISTENER_LOG_FORMAT = "*1 value changed: {0} -> {1}";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/autocomplete.xhtml");
    }

    @Test
    @Templates(exclude = { "richPopupPanel" })
    public void testValueChangeListener() {
        getAutocomplete().typeKeys("something");
        getAutocomplete().confirmByKeys();
        // valueChangeListener output as 4th record
        waitFor(2000);
        waitGui.failWith(
            "Expected <" + format(PHASE_LISTENER_LOG_FORMAT, "null", "something") + ">, found <"
                + selenium.getText(PHASE_FORMAT.format(3)) + ">").until(
            textEquals.locator(PHASE_FORMAT.format(3)).text(format(PHASE_LISTENER_LOG_FORMAT, "null", "something")));

        getAutocomplete().clearInputValue();
        getAutocomplete().typeKeys("something else");
        getAutocomplete().confirmByKeys();
        // valueChangeListener output as 4th record
        waitFor(2000);
        waitGui.failWith(
            "Expected <" + format(PHASE_LISTENER_LOG_FORMAT, "something", "something else") + ">, found <"
                + selenium.getText(PHASE_FORMAT.format(3)) + ">").until(
            textEquals.locator(PHASE_FORMAT.format(3)).text(
                format(PHASE_LISTENER_LOG_FORMAT, "something", "something else")));
    }

    @Test(groups = { "4.Future" })
    @Templates(value = { "richPopupPanel" })
    public void testValueChangeListenerInPopupPanel() {
        testValueChangeListener();
    }

}
