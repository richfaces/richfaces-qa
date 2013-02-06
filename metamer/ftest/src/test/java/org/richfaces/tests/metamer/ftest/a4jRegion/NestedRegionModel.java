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

import static org.jboss.arquillian.ajocado.locator.option.OptionLocatorFactory.optionLabel;

import static org.jboss.arquillian.ajocado.guard.RequestGuardFactory.guard;

import static org.richfaces.tests.metamer.ftest.AbstractMetamerTest.pjq;

import static org.testng.Assert.assertTrue;

import org.apache.commons.lang.WordUtils;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.locator.option.OptionLocator;
import org.jboss.arquillian.ajocado.request.RequestType;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22500 $
 */
public class NestedRegionModel {

    private static GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

    private static ThreadLocal<Integer> sequence = new ThreadLocal<Integer>() {
        protected Integer initialValue() {
            return 0;
        };
    };

    private JQueryLocator selectDefaults = pjq("select[id$=defaultsSelect]");

    public void setDefaultExecute(Execute execute) {
        selectOrFireChange(selectDefaults, execute.option);
    }

    public void setExecute(Component component, Execute execute) {
        selectOrFireChange(component.select, execute.option);
    }

    private void selectOrFireChange(ElementLocator<?> selectLocator, OptionLocator<?> optionLocator) {
        if (optionLocator.getRawLocator().equals(selenium.getSelectedLabel(selectLocator))) {
             guard(selenium, RequestType.XHR).fireEvent(selectLocator, Event.CHANGE);
        } else {
             guard(selenium, RequestType.XHR).select(selectLocator, optionLocator);
        }
    }

    public void changeInputs() {
        sequence.set(sequence.get() + 1);
        for (Component component : Component.values()) {
            selenium.type(component.input, Integer.toString(sequence.get()));
        }
    }

    public enum Component {
        OUTER("Outer"), REGION("Region"), NESTED("Nested region"), DECORATION("Decoration"), INSERTION("Insertion");

        private final JQueryLocator select;
        private final JQueryLocator output;
        private final JQueryLocator input;
        private final JQueryLocator link;
        private final OptionLocator<?> executeOption;

        private Component(String name) {
            String id = name.substring(0, 1).toLowerCase() + WordUtils.capitalize(name).replace(" ", "").substring(1);

            this.select = pjq("select[id$={0}Select]").format(id);
            this.output = pjq("span[id$={0}ValueOutput]").format(id);
            this.input = pjq("input:text[id$={0}ValueInput]").format(id);
            this.link = pjq("input:submit[id$={0}ValueButton]").format(id);
            this.executeOption = optionLabel(name);
        }

        public void fireAction() {
             guard(selenium, RequestType.XHR).click(link);
        }

        public boolean isChanged() {
            final String out = selenium.getText(output);
            assertTrue("".equals(out) || Integer.toString(sequence.get()).equals(out));
            return Integer.toString(sequence.get()).equals(out);
        }
    }

    public enum Execute {
        DEFAULT("default"), ALL("@all"), REGION("@region"), FORM("@form"), THIS("@this"), COMPONENT_OUTER(
            Component.OUTER), COMPONENT_REGION(Component.REGION), COMPONENT_NESTED(Component.NESTED), COMPONENT_DECORATION(
            Component.DECORATION), COMPONENT_INSERTION(Component.INSERTION);

        private final OptionLocator<?> option;
        private final Component componentBase;

        private Execute(String label) {
            this.option = optionLabel(label);
            this.componentBase = null;
        }

        private Execute(Component component) {
            this.option = component.executeOption;
            this.componentBase = component;
        }

        public boolean isComponentBased() {
            return componentBase != null;
        }

        public Component getComponentBase() {
            return componentBase;
        }
    }
}
