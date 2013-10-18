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
package org.richfaces.tests.metamer.ftest.a4jPush;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.google.common.collect.Lists;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 * Test for simple push example faces/components/a4jPush/simple.xhtml
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestSimplePush extends AbstractWebDriverTest {

    private static final int NUMBER_OF_VALUES = 5;
    private static final int TOLERANCE = 2;// s
    private static final int UPDATE_INTERVAL = 5;// s
    private static final DateTimeFormatter TIME_PARSER = DateTimeFormat.forPattern("'['MMM d, yyyy HH:mm:ss a']'");

    @FindBy(css = "div[id$=messagePanel] span.timestamp")
    private WebElement timestamp;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jPush/simple.xhtml");
    }

    @Test
    public void testPushComponentExists() {
        List<String> timeStampsString = Lists.newArrayList();
        String previousTimeStampText;
        Graphene.waitModel().until().element(timestamp).is().present();
        for (int i = 0; i < NUMBER_OF_VALUES; ++i) {
            previousTimeStampText = timestamp.getText();
            timeStampsString.add(previousTimeStampText);
            Graphene.waitModel().until().element(timestamp).text().not().equalTo(previousTimeStampText);
        }

        List<DateTime> times = Lists.newArrayList();
        for (String string : timeStampsString) {
            times.add(TIME_PARSER.parseDateTime(string));
        }
        DateTime prevTime = null;
        for (DateTime dateTime : times) {
            if (prevTime != null) {
                assertTrue(prevTime.isBefore(dateTime));
                long millis = dateTime.getMillis() - prevTime.getMillis();
                assertEquals(millis / 1000.0, UPDATE_INTERVAL, TOLERANCE, "The update interval is not in tolerance.");
            }
            prevTime = dateTime;
        }
    }
}
