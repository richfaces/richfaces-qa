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

import org.apache.commons.lang.WordUtils;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.WebElementUtils;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import static org.testng.Assert.assertTrue;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22500 $
 */
public class NestedRegionModel {

    private static ThreadLocal<Integer> sequence = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        };
    };

    @FindBy(css="select[id$=defaultsSelect]")
    private Select selectDefaults;

    public void setDefaultExecute(Execute execute) {
        selectOrFireChange(selectDefaults, execute.option);
    }

    public void setExecute(Component component, Execute execute) {
        selectOrFireChange(component.select, execute.option);
    }

    private void selectOrFireChange(Select select, String visibleText) {
        if (!visibleText.equals(select.getFirstSelectedOption().getText())) {
            Graphene.guardAjax(select).selectByVisibleText(visibleText);
        }
    }

    public void changeInputs() {
        sequence.set(sequence.get() + 1);
        for (Component component : Component.values()) {
            component.input.click();
            component.input.clear();
            component.input.sendKeys(Integer.toString(sequence.get()));
        }
    }

    public enum Component {
        OUTER("Outer"), REGION("Region"), NESTED("Nested region"), DECORATION("Decoration"), INSERTION("Insertion");

        private final Select select;
        private final WebElement output;
        private final WebElement input;
        private final WebElement link;
        private final String executeOption;

        private Component(String name) {
            final String id = name.substring(0, 1).toLowerCase() + WordUtils.capitalize(name).replace(" ", "").substring(1);
            GrapheneContext context = GrapheneContext.getContextFor(Default.class);
            final WebDriver browser = context.getWebDriver();
            this.select = GrapheneProxy.getProxyForHandler(GrapheneProxyHandler.forFuture(context, new GrapheneProxy.FutureTarget() {
                @Override
                public Object getTarget() {
                    return new Select(browser.findElement(By.cssSelector("select[id$="+id+"Select]")));
                }
            }), Select.class);
            this.output = WebElementUtils.findElementLazily(By.cssSelector("span[id$="+id+"ValueOutput]"), browser);
            this.input = WebElementUtils.findElementLazily(ByJQuery.jquerySelector("input:text[id$="+id+"ValueInput]"), browser);
            this.link = WebElementUtils.findElementLazily(ByJQuery.jquerySelector("input:submit[id$="+id+"ValueButton]"), browser);
            this.executeOption = name;
        }

        public void fireAction() {
            Graphene.guardAjax(link).click();
        }

        public boolean isChanged() {
            final String out = output.getText();
            assertTrue("".equals(out) || Integer.toString(sequence.get()).equals(out));
            return Integer.toString(sequence.get()).equals(out);
        }
    }

    public enum Execute {
        DEFAULT("default"), ALL("@all"), REGION("@region"), FORM("@form"), THIS("@this"), COMPONENT_OUTER(
            Component.OUTER), COMPONENT_REGION(Component.REGION), COMPONENT_NESTED(Component.NESTED), COMPONENT_DECORATION(
            Component.DECORATION), COMPONENT_INSERTION(Component.INSERTION);

        private final String option;
        private final Component componentBase;

        private Execute(String label) {
            this.option = label;
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
