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
package org.richfaces.tests.metamer.ftest.richTreeModelAdaptor;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.Uses;
import org.richfaces.tests.metamer.ftest.richTree.AbstractTreeSelectionTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class TestTreeModelAdaptorSelection extends AbstractTreeSelectionTest {

    @FindByJQuery(":radio[id*=recursiveModelRepresentation]")
    private List<WebElement> recursiveModelRepresentations;
    @FindByJQuery(":checkbox[id$=recursiveLeafChildrenNullable]")
    private WebElement recursiveLeafChildrenNullableElement;

    private PathsCrate path;
    private final PathsCrate[] paths = new PathsCrate[]{
        new PathsCrate("listModel", new Integer[][]{ { 1, 0, 2 }, { 2, 2, 1, 1 } }),
        new PathsCrate("mapModel", new Integer[][]{ { 1, 0, 4 }, { 2, 2, 1, 5 } }),
        new PathsCrate("recursiveModel", new Integer[][]{ { 3, 0, 9, 1 }, { 0, 3, 2, 10, 3 } }) };

    @UseForAllTests(valuesFrom = FROM_ENUM, value = "")
    private RecursiveModelRepresentation representation;

    @UseForAllTests(valuesFrom = FROM_FIELD, value = "booleans")
    private Boolean recursiveLeafChildrenNullable;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTree/treeAdaptors.xhtml");
    }

    @BeforeMethod
    public void initPathsAndModelRepresentation() {
        if (path != null) {
            selectionPaths = path.paths;
        }
        if (representation == RecursiveModelRepresentation.MAP) {
            MetamerPage.requestTimeChangesWaiting(recursiveModelRepresentations.get(1)).click();
        }
        if (recursiveLeafChildrenNullable) {
            MetamerPage.requestTimeChangesWaiting(recursiveLeafChildrenNullableElement).click();
        }
    }

    @Test
    @UseWithField(field = "selectionType", valuesFrom = FROM_FIELD, value = "selectionTypes")
    @Override
    public void testTopLevelSelection() {
        super.testTopLevelSelection();
    }

    @Test
    @Uses({
        @UseWithField(field = "selectionType", valuesFrom = FROM_FIELD, value = "selectionTypes"),
        @UseWithField(field = "path", valuesFrom = FROM_FIELD, value = "paths"), })
    @Override
    public void testSubNodesSelection() {
        super.testSubNodesSelection();
    }

    @Test
    @Uses({
        @UseWithField(field = "selectionType", valuesFrom = FROM_FIELD, value = "selectionTypes"),
        @UseWithField(field = "path", valuesFrom = FROM_FIELD, value = "paths"),
        @UseWithField(field = "selectionType", valuesFrom = FROM_FIELD, value = "eventEnabledSelectionTypes") })
    @Override
    public void testSubNodesSelectionEvents() {
        super.testSubNodesSelectionEvents();
    }

    @Override
    protected Integer[] getIntsFromString(String string) {
        Pattern pattern = Pattern.compile("(?:\\{[^}]+modelKey=(\\d+)\\})");
        Matcher matcher = pattern.matcher(string);
        List<Integer> list = new LinkedList<Integer>();
        while (matcher.find()) {
            int integer = Integer.valueOf(matcher.group(1));
            integer = fixShiftWhenModelPresent(list, integer);
            list.add(integer);
        }
        if (list.isEmpty()) {
            throw new IllegalStateException("selection string does not match pattern: " + string);
        }
        return list.toArray(new Integer[list.size()]);
    }

    private Integer fixShiftWhenModelPresent(List<Integer> list, int integer) {
        if (!list.isEmpty()) {
            if (list.get(0) % 2 == 1) {
                if (list.size() == 2) {
                    if (path.toString().equals(paths[2].toString())) {
                        return integer + 7;
                    } else if (path.toString().equals(paths[1].toString())) {
                        return integer + 3;
                    }
                }
            } else {
                if (list.size() == 3) {
                    if (path.toString().equals(paths[2].toString())) {
                        return integer + 7;
                    } else if (path.toString().equals(paths[1].toString())) {
                        return integer + 3;
                    }
                }
            }
        }
        return integer;
    }

    private class PathsCrate {

        String name;
        Integer[][] paths;

        public PathsCrate(String name, Integer[][] paths) {
            this.name = name;
            this.paths = paths;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
