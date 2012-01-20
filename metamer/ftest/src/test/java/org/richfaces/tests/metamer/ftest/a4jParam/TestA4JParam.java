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
package org.richfaces.tests.metamer.ftest.a4jParam;

import static org.jboss.arquillian.ajocado.Ajocado.guardHttp;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/a4jParam/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 20962 $
 */
public class TestA4JParam extends AbstractAjocadoTest {

    private JQueryLocator button1 = pjq("input[id$=button1]");
    private JQueryLocator button2 = pjq("input[id$=button2]");
    private JQueryLocator resetButton = pjq("input[id$=resetButton]");
    private JQueryLocator output1 = pjq("span[id$=output1]");
    private JQueryLocator output2 = pjq("span[id$=output2]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jParam/simple.xhtml");
    }

    @Test
    public void testParameter() {
        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(button1);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String screenWidth = selenium.getEval(new JavaScript("window.screen.width"));

        assertEquals(selenium.getText(output1), screenWidth, "Output 1 after clicking on first button.");
        assertEquals(selenium.getText(output2), screenWidth, "Output 2 after clicking on first button.");

        reqTime = selenium.getText(time);
        guardXhr(selenium).click(resetButton);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "", "Output 1 after clicking on reset button.");
        assertEquals(selenium.getText(output2), "", "Output 2 after clicking on reset button.");

        reqTime = selenium.getText(time);
        guardHttp(selenium).click(button2);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "screen.width", "Output 1 after clicking on second button.");
        assertEquals(selenium.getText(output2), "screen.width", "Output 2 after clicking on second button.");
    }

    @Test
    public void testName() {
        selenium.type(pjq("input[id$=nameInput]"), "metamer");
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(button1);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String screenWidth = selenium.getEval(new JavaScript("window.screen.width"));

        assertEquals(selenium.getText(output1), screenWidth, "Output 1 after clicking on first button.");
        assertEquals(selenium.getText(output2), screenWidth, "Output 2 after clicking on first button.");
    }

    @Test
    public void testNoEscape() {
        JQueryLocator renderedInput = pjq("input[type=radio][name$=noEscapeInput][value=false]");
        selenium.click(renderedInput);
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(button1);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "screen.width", "Output 1 after clicking on first button.");
        assertEquals(selenium.getText(output2), "screen.width", "Output 2 after clicking on first button.");
    }

    @Test
    public void testValue() {
        selenium.type(pjq("input[id$=valueInput]"), "4+5");
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(button1);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "9", "Output 1 after clicking on first button.");
        assertEquals(selenium.getText(output2), "9", "Output 2 after clicking on first button.");

        reqTime = selenium.getText(time);
        guardXhr(selenium).click(resetButton);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "", "Output 1 after clicking on reset button.");
        assertEquals(selenium.getText(output2), "", "Output 2 after clicking on reset button.");

        reqTime = selenium.getText(time);
        guardHttp(selenium).click(button2);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "4+5", "Output 1 after clicking on second button.");
        assertEquals(selenium.getText(output2), "4+5", "Output 2 after clicking on second button.");
    }
}
