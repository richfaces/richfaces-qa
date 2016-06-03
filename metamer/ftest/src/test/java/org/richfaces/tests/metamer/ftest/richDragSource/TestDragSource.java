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
import static org.testng.Assert.assertTrue;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.as.cli.CommandLineException;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.AndExpression;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.Uses;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.0.CR1
 *
 */
@SuppressWarnings("unchecked")
public class TestDragSource extends AbstractDragSourceTest {

    private String dragOptionsValue;
    private Map<String, List<? extends Entry<String, String>>> dragOptionsValueMap;
    private String indicatorValue;

    {
        dragOptionsValueMap = new HashMap<String, List<? extends Entry<String, String>>>(3);

        dragOptionsValueMap.put("", Collections.EMPTY_LIST);
        dragOptionsValueMap.put("predefinedWithHelper", Lists.newArrayList(
            new SimpleEntry<String, String>("opacity", "0.75"),
            new SimpleEntry<String, String>("cursor", "crosshair")));
        dragOptionsValueMap.put("predefinedWithoutHelper", Lists.newArrayList(
            new SimpleEntry<String, String>("opacity", "0.85"),
            new SimpleEntry<String, String>("cursor", "move")));
    }

    @Override
    public String getComponentTestPagePath() {
        return "richDragSource/simple.xhtml";
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-12441", "https://issues.jboss.org/browse/RF-14081", "https://issues.jboss.org/browse/RF-14229" })
    @Uses({
        @UseWithField(field = "indicatorValue", valuesFrom = ValuesFrom.STRINGS, value = { "", "indicator", "indicator2" }),
        @UseWithField(field = "dragOptionsValue", valuesFrom = ValuesFrom.STRINGS, value = { "", "predefinedWithHelper", "predefinedWithoutHelper" })
    })
    @CoversAttributes({ "dragIndicator", "dragOptions" })
    public void testDragIndicatorAndOptions() {
        // setup attributes
        attsSetter()
            .setAttribute("dragIndicator").toValue(indicatorValue)
            .setAttribute("dragOptions").toValue(dragOptionsValue)
            .asSingleAction().perform();
        // setup indicator
        boolean isDragOptionsWithHelper = dragOptionsValue.equals("predefinedWithHelper");
        boolean isDefaultIndicatorUsed = indicatorValue.isEmpty() || isDragOptionsWithHelper;
        GrapheneElement indicatorElement = isDefaultIndicatorUsed
            ? getPage().getDefaultIndicatorElement()
            : indicatorValue.equals("indicator")
                ? getPage().getIndicatorElement()
                : getPage().getIndicator2Element();
        indicator = new Indicator(indicatorElement);
        indicator.setDefaultIndicator(isDefaultIndicatorUsed);
        // check no errors are present in browser's console
        assertEquals(jsErrorStorage.getMessages().size(), 0);

        Actions actionQueue = new Actions(driver);
        try {
            actionQueue.clickAndHold(getPage().getDrag1Element()).moveByOffset(1, 1).perform();
            assertTrue(indicatorElement.isPresent());

            if (!dragOptionsValue.isEmpty()) {
                // check indicator has predefined properties from @dragOptions
                for (Entry<String, String> e : dragOptionsValueMap.get(dragOptionsValue)) {
                    // when using DragOptionsWithoutHelper, the opacity is used from the indicator
                    if (!e.getKey().equals("opacity") || isDragOptionsWithHelper) {
                        assertEquals(indicatorElement.getCssValue(e.getKey()), e.getValue());
                    }
                }
            }
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
    @Skip(expressions = {
        @AndExpression(On.Container.Tomcat8.class),// not tested, needs a JBoss container due to war redeployment through CLI
        @AndExpression(On.Container.Tomcat7.class),// not tested, needs a JBoss container due to war redeployment through CLI
        @AndExpression(On.Container.EAP62x.class),
        @AndExpression(On.Container.EAP63x.class),
        @AndExpression(On.Container.EAP64x.class)
    })
    @IssueTracking("https://issues.jboss.org/browse/RF-14251")
    @RegressionTest("https://issues.jboss.org/browse/RF-10967")
    public void testDragValueWithPartialStateSavingOff() {
        disablePartialStateSavingAndRedeploy();
        openPageWithCurrentConfiguration();
        try {
            testDragValue();
        } finally {
            try {
                undeployMetamerWars();
                deployMetamerWar();
            } catch (CommandLineException ex) {
                System.err.println(ex);
                System.err.println("Was not able to deploy the original WAR. Exiting.");
                System.exit(1);
            }
        }
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
