/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richPanelMenuGroup;

import static java.text.MessageFormat.format;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public abstract class AbstractPanelMenuCommonTest extends AbstractWebDriverTest {

    private static final String ATTR_INPUT_LOC_FORMAT = "input[id$=on{0}Input]";

    public abstract MetamerPage getPage();

    public void testRequestEventsBefore(String... events) {
        for (String event : events) {
            String inputExp = format(ATTR_INPUT_LOC_FORMAT, event);
            WebElement input = getPage().attributesTable.findElement(By.cssSelector(inputExp));
            // Note: To avoid lost metamerEvents variable when @mode=server, use sessionStorage
            String inputVal = format("metamerEvents += \"{0} \"", event);
            String inputValFull = "sessionStorage.setItem('metamerEvents', " + inputVal + ")";
            // even there would be some events (in params) twice, don't expect handle routine to be executed twice
            input.clear();
            waiting(1000);
            input = getPage().attributesTable.findElement(By.cssSelector(inputExp));
            input.sendKeys(inputValFull);
            // sendKeys triggers page reload automatically
            waiting(300);
            Graphene.waitAjax().until(ElementPresent.getInstance().element(getPage().attributesTable));
            input = getPage().attributesTable.findElement(By.cssSelector(inputExp));
            MetamerPage.waitRequest(input, WaitRequestType.HTTP).submit();
        }
        cleanMetamerEventsVariable();
    }

    public void testRequestEventsAfter(String... events) {
        String[] actualEvents = ((String)executeJS("return sessionStorage.getItem('metamerEvents')")).split(" ");
        assertEquals(actualEvents, events, format("The events ({0}) don't came in right order ({1})",
            Arrays.deepToString(actualEvents), Arrays.deepToString(events)));
    }

    public void cleanMetamerEventsVariable() {
        // since metamerEvents variable stored on session too, make sure that cleaned both of them
        executeJS("window.metamerEvents = \"\";");
        executeJS("sessionStorage.removeItem('metamerEvents')");
    }
}
