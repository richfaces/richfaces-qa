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
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.jboss.arquillian.ajocado.Graphene.alertPresent;
import static org.jboss.arquillian.ajocado.Graphene.waitAjax;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;

import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.tests.metamer.ftest.a4jStatus.StatusAttributes.onerror;
import static org.richfaces.tests.metamer.ftest.a4jStatus.StatusAttributes.onstart;
import static org.richfaces.tests.metamer.ftest.a4jStatus.StatusAttributes.onstop;
import static org.richfaces.tests.metamer.ftest.a4jStatus.StatusAttributes.onsuccess;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.statusAttributes;

import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.jboss.cheiron.halt.XHRHalter;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22972 $
 */
public class TestInstantAttributes extends AbstractStatusTest {

    JavaScript alert = js("alert('{0}')");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jStatus/instantAttributes.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("A4J", "A4J Status", "Instant Attributes");
    }

    @BeforeClass
    public void setupAttributes() {
        statusAttributes.setRequestType(RequestType.XHR);
    }

    @AfterClass
    public void teardownAttributes() {
        statusAttributes.setRequestType(RequestType.HTTP);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RFPL-1516")
    public void testOnStart() {
        statusAttributes.setRequestType(null);

        XHRHalter.enable();
        for (int i = 0; i < 2; i++) {
            final int ii = i;
            retrieveRequestTime.initializeValue();
            statusAttributes.set(onstart, js("window.metamer = " + ii));
            getCurrentXHRHalter().complete();
            waitGui.waitForChange(retrieveRequestTime);

            selenium.click(button1);
            selenium.waitForCondition(js("window.metamer == " + i));
            getCurrentXHRHalter().complete();

            // FIXME workaround
            waitFor(1000);
        }
        XHRHalter.disable();
    }

    @Test(groups = { "4.Future" })
    @IssueTracking({ "https://issues.jboss.org/browse/RF-9118",
        "http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-1024" })
    public void testOnError() {
        for (int i = 0; i < 2; i++) {
            statusAttributes.set(onerror, alert.parametrize("error" + i));

            selenium.click(buttonError);
            waitAjax.until(alertPresent);
            assertEquals(selenium.getAlert(), "error" + i);
        }
    }

    @Test
    public void testOnSuccess() {
        for (int i = 0; i < 2; i++) {
            statusAttributes.set(onsuccess, alert.parametrize("success" + 1));

            selenium.click(button1);
            waitAjax.until(alertPresent);
            assertEquals(selenium.getAlert(), "success" + 1);
        }
    }

    @Test
    public void testOnStop() {
        for (int i = 0; i < 2; i++) {
            statusAttributes.set(onstop, alert.parametrize("stop" + i));

            selenium.click(button1);
            waitAjax.until(alertPresent);
            assertEquals(selenium.getAlert(), "stop" + i);

            selenium.click(buttonError);
            waitAjax.until(alertPresent);
            assertEquals(selenium.getAlert(), "stop" + i);
        }
    }
}
