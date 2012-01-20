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
package org.richfaces.tests.metamer.ftest.a4jPush;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.SeleniumGetter.getText;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.BeforeClass;

/**
 * Abstract test case for testing a4j:push component.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23061 $
 */
public abstract class AbstractPushTest extends AbstractAjocadoTest {

    private static final int STATUS_CODE_OK = 200;

    private JQueryLocator outputCounter = pjq("span[id$=outputCounter]");
    protected JQueryLocator outputList = pjq("ul.push-list");

    private HttpClient httpClient;
    private HttpMethod pushMethod;

    @Override
    public URL getTestUrl() {

        return buildUrl(contextPath, "faces/components/a4jPush/simple.xhtml");
    }

    /**
     * Initializes the HttpClient which triggers events in {@link #generatePushEvent()}.
     */
    @BeforeClass
    public void initializeHttpClient() {
        httpClient = new HttpClient();
        URL eventProducerUrl = buildUrl(getTestUrl(), "event-producer.xhtml");
        pushMethod = new GetMethod(eventProducerUrl.toString());
    }

    /**
     * Returns the value of counter as pushed value
     *
     * @return the value of counter as pushed value
     */
    protected int getCounter() {
        return getText(outputCounter).asInteger();
    }

    /**
     * Push the event specified number times and then waits for observation of event by client.
     *
     * @param numberOfPushes
     *            the number of events should be generated
     */
    protected void pushAndWait(int numberOfPushes) throws IOException {
        selenium.getPageExtensions().install();
        selenium.getRequestGuard().clearRequestDone();

        for (int i = 0; i < numberOfPushes; i++) {
            generatePushEvent();
        }

        selenium.getRequestGuard().waitForRequest();
        RequestType requestDone = selenium.getRequestGuard().clearRequestDone();

        assertEquals(requestDone, RequestType.XHR);
    }

    /**
     * <p>
     * Generates the push event for all registered listeners from a4j:push test Simple page.
     * </p>
     *
     * <p>
     * Internally use HttpClient to ping URL triggering push event in preRenderView phase.
     * </p>
     */
    private void generatePushEvent() throws IOException {
        httpClient.executeMethod(pushMethod);
        assertEquals(pushMethod.getStatusCode(), STATUS_CODE_OK);
    }
}
