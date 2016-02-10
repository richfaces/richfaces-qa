/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richDragSource;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator;
import org.richfaces.tests.metamer.ftest.richSelect.TestRF14018.JSErrorStorage;
import org.testng.annotations.Test;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.0.CR1
 *
 */
public class TestDragSource extends AbstractDragSourceTest {

    @JavaScript
    private JSErrorStorage jsErrorStorage;

    @Override
    public String getComponentTestPagePath() {
        return "richDragSource/simple.xhtml";
    }

    @Test
    public void testCustomIndicator() {
        super.testCustomIndicator();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12441")
    public void testDefaultIndicator() {
        super.testDefaultIndicator();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14081")
    @CoversAttributes("dragOptions")
    public void testDragOptions() {
        indicator = new Indicator(getPage().getDefaultIndicatorElement());
        indicator.setDefaultIndicator(true);
        dragSourceAttributes.set(DragSourceAttributes.dragOptions, "predefined1");
        Actions actionQueue = new Actions(driver);

        actionQueue.clickAndHold(getPage().getDrag1Element()).perform();
        assertFalse(getPage().getDefaultIndicatorElement().isPresent());

        actionQueue.moveByOffset(1, 1).perform();
        assertTrue(getPage().getDefaultIndicatorElement().isPresent());

        // check indicator has predefined properties from @dragOptions (from JavaScript object 'predefined1')
        assertEquals(getPage().getDefaultIndicatorElement().getCssValue("opacity"), "0.5");
        assertEquals(getPage().getDefaultIndicatorElement().getCssValue("cursor"), "crosshair");

        // check it is working
        testMovingOverDifferentStates();

        actionQueue.release(getPage().getDrop1Element()).perform();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14229")
    @CoversAttributes("dragOptions")
    public void testDragOptionsWithoutHelper() {
        indicator = new Indicator(getPage().getDefaultIndicatorElement());
        indicator.setDefaultIndicator(true);
        dragSourceAttributes.set(DragSourceAttributes.dragOptions, "predefinedWithoutHelper");

        // check no errors are present in browser's console
        assertEquals(jsErrorStorage.getMessages().size(), 0);

        Actions actionQueue = new Actions(driver);

        actionQueue.clickAndHold(getPage().getDrag1Element()).perform();
        try {
            assertFalse(getPage().getDefaultIndicatorElement().isPresent());

            actionQueue.moveByOffset(1, 1).perform();
            assertTrue(getPage().getDefaultIndicatorElement().isPresent());

            // check indicator has predefined properties from @dragOptions (from JavaScript object 'predefinedWithoutHelper')
            assertEquals(getPage().getDefaultIndicatorElement().getCssValue("opacity"), "0.5");
            assertEquals(getPage().getDefaultIndicatorElement().getCssValue("cursor"), "move");

            // check it is working
            testMovingOverDifferentStates();
        } finally {
            actionQueue.release(getPage().getDrop1Element()).perform();
        }
        // check no errors are present in browser's console
        assertEquals(jsErrorStorage.getMessages().size(), 0);
    }

    @Test
    public void testDragValue() {
        super.testDragValue();
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        super.testRendered();
    }

    @Test
    public void testType() {
        super.testType();
    }
}
