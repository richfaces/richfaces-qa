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
package org.richfaces.tests.metamer.ftest.a4jRegion;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.EnumSet;

import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.a4jRegion.NestedRegionModel.Component;
import org.richfaces.tests.metamer.ftest.a4jRegion.NestedRegionModel.Execute;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22614 $
 */
public class TestNestedRegion extends AbstractGrapheneTest {

    NestedRegionModel model = new NestedRegionModel();

    @Inject
    @Use("components")
    Component component;
    Component[] components = Component.values();

    @Inject
    @Use("executes")
    Execute execute;
    Execute[] executes = Execute.values();

    EnumSet<Component> expectedChanges;
    EnumSet<Component> actualChanges;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jRegion/nested.xhtml");
    }

    @Test
    @Templates(exclude = { "a4jRegion" })
    public void test() {
        model.setExecute(component, execute);
        model.changeInputs();
        component.fireAction();

        countExpectedChanges();
        obtainActualChanges();
        checkChanged();
    }

    private void countExpectedChanges() {
        expectedChanges = EnumSet.noneOf(Component.class);

        if (execute == Execute.DEFAULT) {
            if (component == Component.OUTER) {
                execute = Execute.THIS;
            } else {
                execute = Execute.REGION;
            }
        }

        if (execute == Execute.ALL || execute == Execute.FORM) {
            expectedChanges = EnumSet.allOf(Component.class);
        }

        if (execute == Execute.REGION) {
            if (EnumSet.of(Component.REGION, Component.INSERTION, Component.DECORATION).contains(component)) {
                expectedChanges = EnumSet.complementOf(EnumSet.of(Component.OUTER));
            } else if (component == Component.NESTED) {
                expectedChanges.add(Component.NESTED);
            }
        }

        if (execute.isComponentBased()) {
            expectedChanges.add(execute.getComponentBase());
        }
    }

    private void obtainActualChanges() {
        actualChanges = EnumSet.noneOf(Component.class);
        for (Component currentComponent : Component.values()) {
            if (currentComponent.isChanged()) {
                actualChanges.add(currentComponent);
            }
        }
    }

    private void checkChanged() {
        EnumSet<Component> missing = EnumSet.copyOf(expectedChanges);
        missing.removeAll(actualChanges);

        EnumSet<Component> redundant = EnumSet.complementOf(expectedChanges);
        redundant.retainAll(actualChanges);

        StringBuilder message = new StringBuilder(format("Component: {0}, Execute: {1}; ", component, execute));

        if (!missing.isEmpty()) {
            message.append("Expected to change: " + missing + "; ");
        }

        if (!redundant.isEmpty()) {
            message.append("Expected to not change: " + redundant + "; ");
        }

        assertEquals(actualChanges, expectedChanges, message.toString());
    }
}
