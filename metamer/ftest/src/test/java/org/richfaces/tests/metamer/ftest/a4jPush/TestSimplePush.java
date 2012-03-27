/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.tests.metamer.ftest.a4jPush;

import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitAjax;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test for simple push example faces/components/a4jPush/simple.xhtml
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 */
public class TestSimplePush extends AbstractAjocadoTest {

    protected JQueryLocator messagePanel = pjq("div[id$=messagePanel]");
    protected JQueryLocator timestamp = messagePanel.getDescendant(jq(" div > span.timestamp"));

    @Override
    public URL getTestUrl() {

        return buildUrl(contextPath, "faces/components/a4jPush/simple.xhtml");
    }

    @Test
    public void testPushComponentExists() {
        TextRetriever messageRetriever = retrieveText.locator(messagePanel);
        TextRetriever timestampRetreiver = retrieveText.locator(timestamp);
        messageRetriever.initializeValue();

        // before receive first push update the message is "waiting for data from server"
        waitAjax.waitForChangeAndReturn(messageRetriever);
        timestampRetreiver.initializeValue();

        List<String> times = new ArrayList<String>();
        for (int i = 0; i < 6; ++i) {
            waitAjax.waitForChangeAndReturn(timestampRetreiver);
            String timestamp = timestampRetreiver.getValue();
            if (timestamp != null && timestamp.startsWith("[")) {
                timestamp = timestamp.substring(1, timestamp.length() - 1);
            }
            times.add(timestamp);
        }
        Collections.sort(times);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        try {
            Date first = sdf.parse(times.get(0));
            Date last = sdf.parse(times.get(times.size() - 1));
            // Assert that last message is dated 5 * 5s (received 5 updates, with 5s interval)
            Assert.assertEquals(first.getTime(), last.getTime() - 5 * 5 * 1000);
        } catch (ParseException e) {
            Assert.fail("Get wrong date format");
        }
    }

}
