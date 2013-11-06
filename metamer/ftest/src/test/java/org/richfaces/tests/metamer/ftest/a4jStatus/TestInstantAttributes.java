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
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.requestTimeChangesWaiting;

import java.net.URL;

import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInstantAttributes extends AbstractStatusTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jStatus/instantAttributes.xhtml");
    }

    @Test(groups = { "Future" })
    @IssueTracking({ "https://issues.jboss.org/browse/RF-9118",
        "http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-1024" })
    public void testOnError() {
        for (int i = 0; i < 2; i++) {
            testFireEvent(getStatusAttributes(), StatusAttributes.onerror, new Action() {
                @Override
                public void perform() {
                    requestTimeChangesWaiting(getButtonError()).click();
                }
            });
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RFPL-1516")
    public void testOnStart() {
        for (int i = 0; i < 2; i++) {
            testFireEvent(getStatusAttributes(), StatusAttributes.onstart, new Action() {
                @Override
                public void perform() {
                    requestTimeChangesWaiting(getButton1()).click();
                }
            });
        }
    }

    @Test
    public void testOnStop() {
        for (int i = 0; i < 2; i++) {
            testFireEvent(getStatusAttributes(), StatusAttributes.onstop, new Action() {
                @Override
                public void perform() {
                    requestTimeChangesWaiting(getButton1()).click();
                }
            });
        }
    }

    @Test
    public void testOnSuccess() {
        for (int i = 0; i < 2; i++) {
            testFireEvent(getStatusAttributes(), StatusAttributes.onsuccess, new Action() {
                @Override
                public void perform() {
                    requestTimeChangesWaiting(getButton1()).click();
                }
            });
        }
    }
}
