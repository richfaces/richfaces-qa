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
package org.richfaces.tests.metamer.ftest.attributes;

import static org.jboss.arquillian.ajocado.Graphene.waitAjax;

import static org.jboss.arquillian.ajocado.dom.Attribute.VALUE;

import static org.jboss.arquillian.ajocado.guard.RequestGuardFactory.guard;

import static org.jboss.test.selenium.locator.reference.ReferencedLocator.referenceInferred;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.AbstractMetamerTest.pjq;

import org.jboss.arquillian.ajocado.command.CommandContext;
import org.jboss.arquillian.ajocado.command.CommandInterceptor;
import org.jboss.arquillian.ajocado.command.CommandInterceptorException;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.LocatorFactory;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;
import org.jboss.arquillian.ajocado.locator.option.OptionLabelLocator;
import org.jboss.arquillian.ajocado.locator.option.OptionValueLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.jboss.test.selenium.locator.reference.LocatorReference;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;

import com.thoughtworks.selenium.SeleniumException;


public class Attributes<T extends AttributeEnum>  {

    protected GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();
    LocatorReference<ExtendedLocator<JQueryLocator>> root =
        new LocatorReference<ExtendedLocator<JQueryLocator>>(pjq(""));
    ReferencedLocator<JQueryLocator> propertyLocator = referenceInferred(root, ":input[id*={0}Input]{1}");

    RequestType requestType = RequestType.HTTP;

    /**
     * Ctor for create Attributes instance with different root locator
     * @param <LT> - ExtentedLocator type definition
     * @param root - ExtendedLocator
     */
    public <LT extends ExtendedLocator<JQueryLocator>> Attributes(LT root) {
        this.root.setLocator(root);
    }

    public Attributes() { }

    public Attributes(RequestType reqType) {
        this.requestType = reqType;
    }

    public void set(T attribute, String string) {
        setProperty(attribute.toString(), string);
    }

    /**
     * Setter for special cases. For example, 'for' is reserved java word, but valid richfaces attribute as well.
     * So we use this attribute name in upper case in enum, and then set by special method to avoid overload by
     * toLowerCase-in over the whole world of richfaces attributes
     * @param attribute
     * @param string
     */
    public void setLower(T attribute, String string) {
        setProperty(attribute.toString().toLowerCase(), string);
    }

    // TODO jjamrich 2011-09-02: make sure that this resolve to correct string representation of number given as attr
    public void set(T attribute, Number no) {
        setProperty(attribute.toString(), no);
    }

    public void set(T attribute, Boolean bool) {
        setProperty(attribute.toString(), bool);
    }

    public void set(T attribute, JavaScript js) {
        setProperty(attribute.toString(), js);
    }

    public void set(T attribute, JQueryLocator locator) {
        setProperty(attribute.toString(), locator.getRawLocator());
    }

    public void set(T attribute, Enum<?> item) {
        setProperty(attribute.toString(), item.toString());
    }

    public void set(T attribute, Event event) {
        setProperty(attribute.toString(), event.getEventName());
    }

    public void reset(T attribute) {
        setProperty(attribute.toString(), "");
    }

    /**
     * Retrieve current attribute value
     * @param attribute
     * @return current attribute value
     */
    public String get(T attribute) {
        return getProperty(attribute.toString());
    }

    /*protected void set(String propertyName, Object value) {

    }*/

    protected void setProperty(String propertyName, Object value) {
        selenium.getCommandInterceptionProxy().registerInterceptor(new RepeatForElementNotFound());

        ExtendedLocator<JQueryLocator> locator = propertyLocator.format(propertyName, "");
        final AttributeLocator<?> typeLocator = locator.getAttribute(Attribute.TYPE);
        final ExtendedLocator<JQueryLocator> optionLocator = locator.getChild(jq("option"));

        String inputType = null;
        if (selenium.getCount(propertyLocator.format(propertyName)) > 1) {
            inputType = "radio";
        } else if (selenium.getCount(optionLocator) > 1) {
            inputType = "select";
        } else {
            inputType = selenium.getAttribute(typeLocator);
        }

        if (value == null) {
            value = "";
        }

        String valueAsString = value.toString();
        // System.out.println("\n ###setting following value as string: '" + valueAsString + "'");

        if ("text".equals(inputType)) {
            applyText(locator, valueAsString);
        } else if ("checkbox".equals(inputType)) {
            boolean checked = Boolean.valueOf(valueAsString);
            applyCheckbox(locator, checked);
        } else if ("radio".equals(inputType)) {
            // experimental: radio-button doesn't have label the same as value. But value should be more complicated
            // such as path to image. But it is possible that there are some values differing just in appendix.
            // This should be collected and verified with this solution, to make tests stable
            if ("".equals(valueAsString)) {
                locator = pjq("td:has(label:contains(null)) > input[name$=" + propertyName + "Input]");
            } else {
                locator = propertyLocator.format(propertyName, "[value*=" + valueAsString + "]");
            }

            if (!selenium.isChecked(locator)) {
                applyRadio(locator);
            }
        } else if ("select".equals(inputType)) {
            String curValue = selenium.getValue(locator);
            if (valueAsString.equals(curValue)) {
                return;
            }
            applySelect(locator, valueAsString);
        }

        selenium.getCommandInterceptionProxy().unregisterInterceptorType(RepeatForElementNotFound.class);
    }

    protected String getProperty(String propertyName) {
        final ReferencedLocator<JQueryLocator> locator = propertyLocator.format(propertyName, "");
        if (selenium.getCount(locator) > 1) {
            return selenium.getAttribute(propertyLocator.format(propertyName, "[checked]").getAttribute(VALUE));
        }
        return selenium.getValue(locator);
    }

    protected void applyText(ElementLocator<?> locator, String value) {
        guard(selenium, requestType).type(locator, value);
    }

    protected void applyCheckbox(ElementLocator<?> locator, boolean checked) {
        selenium.check(locator, checked);
        guard(selenium, requestType).fireEvent(locator, Event.CHANGE);
    }

    protected void applyRadio(ElementLocator<?> locator) {
        guard(selenium, requestType).click(locator);
    }

    protected void applySelect(ElementLocator<?> locator, String value) {
        OptionValueLocator optionLocator = new OptionValueLocator(value);

        LocatorFactory.jq(locator.getRawLocator());
        JQueryLocator fullLocator = jq(locator.getRawLocator()).getChild(jq(optionLocator.getRawLocator()));
        if (selenium.isElementPresent(fullLocator)) {
            guard(selenium, requestType).select(locator, optionLocator);
        } else {
            OptionLabelLocator optionLabelLocator = new OptionLabelLocator(value);
            guard(selenium, requestType).select(locator, optionLabelLocator);
        }
    }

    private class RepeatForElementNotFound implements CommandInterceptor {
        @Override
        public void intercept(CommandContext ctx) throws CommandInterceptorException {
            for (int i = 1; i <= 3; i++) {
                try {
                    ctx.invoke();
                    break;
                } catch (SeleniumException e) {
                    if (i == 3) {
                        throw e;
                    }
                    if (e.getMessage().matches("ERROR: Element .* not found")) {
                        waitAjax.timeout(500).interval(100).waitForTimeout();
                        continue;
                    }
                    throw e;
                }
            }
        }
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public ExtendedLocator<JQueryLocator> getRoot() {
        return root.getLocator();
    }

    public void setRoot(ExtendedLocator<JQueryLocator> root) {
        this.root.setLocator(root);
    }


}
