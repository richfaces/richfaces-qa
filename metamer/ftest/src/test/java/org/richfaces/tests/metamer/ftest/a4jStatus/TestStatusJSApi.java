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
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.status.Status.StatusState;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for a4jStatus JS API. Tests are done via testFireEvent
 *
 * @author <a href="mailto:manovotn@redhat.com"> Matej Novotny </a>
 *
 */
public class TestStatusJSApi extends AbstractStatusTest {

    private final Attributes<StatusAttributes> statusAttributes = getAttributes();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jStatus/withoutFacets.xhtml");
    }

    @BeforeMethod
    public void assertInitialState() {
        assertEquals(getStatus().getStatusState(), StatusState.STOP);
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsError() {
        testFireEvent(statusAttributes, StatusAttributes.onerror, new Action() {
            @Override
            public void perform() {
                Utils.invokeRichFacesJSAPIFunction(getStatus().advanced().getRootElement(), "error()");
            }
        });
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsStart() {
        testFireEvent(statusAttributes, StatusAttributes.onstart, new Action() {
            @Override
            public void perform() {
                Utils.invokeRichFacesJSAPIFunction(getStatus().advanced().getRootElement(), "start()");
            }
        });
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsStop() {
        testFireEvent(statusAttributes, StatusAttributes.onstop, new Action() {
            @Override
            public void perform() {
                Utils.invokeRichFacesJSAPIFunction(getStatus().advanced().getRootElement(), "stop()");
            }
        });
    }
}
