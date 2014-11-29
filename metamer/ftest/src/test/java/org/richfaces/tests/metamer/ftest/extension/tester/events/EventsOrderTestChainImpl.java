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
package org.richfaces.tests.metamer.ftest.extension.tester.events;

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.BasicTestChainImpl;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.TestResourcesProvider;
import org.richfaces.tests.metamer.ftest.extension.tester.events.EventsOrderTestChain.EventTesterBasicConfig;
import org.richfaces.tests.metamer.ftest.extension.tester.events.TriggerSingleEventTestChainImpl.ObjectWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class EventsOrderTestChainImpl extends BasicTestChainImpl<EventsOrderTestChain> implements EventsOrderTestChain, EventTesterBasicConfig {

    private static final Logger LOG = LoggerFactory.getLogger("EventsOrderTestChain");
    private static final String PREFIX_ON = "on";
    private static final String SPACE = " ";

    private String attributeNamesTogether;
    private final Action checkAction = new Action() {
        private final ObjectWrapper<String> wrappedString = new ObjectWrapper<String>();

        @Override
        public void perform() {
            try {
                Graphene.waitModel().until(new Predicate<WebDriver>() {
                    Object executedScriptResult;

                    @Override
                    public boolean apply(WebDriver t) {
                        executedScriptResult = getTestResources().getJSExecutor().executeScript(TriggerSingleEventTestChainImpl.RETURN_METAMER_EVENTS);
                        if (executedScriptResult != null) {
                            wrappedString.setObject(((String) executedScriptResult).trim());
                            LOG.debug(format("This was triggered: <{0}>.", wrappedString.getObject()));
                            return wrappedString.getObject().equals(attributeNamesTogether);
                        }
                        return Boolean.FALSE;
                    }
                });
            } catch (TimeoutException e) {
                assertEquals(wrappedString.getObject(), attributeNamesTogether, "Events order does not match.");
            }
        }
    };
    private final Action cleanEventsAction = new Action() {

        @Override
        public void perform() {
            getTestResources().getJSExecutor().executeScript(TriggerSingleEventTestChainImpl.CLEAN_METAMER_EVENTS);
        }
    };

    public EventsOrderTestChainImpl(TestResourcesProvider provider) {
        super(provider);
    }

    private String apendPrefixIfMissing(String attributeName) {
        return attributeName.startsWith(PREFIX_ON) ? attributeName : format("on{0}", attributeName);
    }

    private String concatStrings(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string);
            sb.append(SPACE);
        }
        return sb.substring(0, sb.length() - 1);
    }

    @Override
    public EventTesterBasicConfig testOrderOfEvents(String... attributeNames) {
        this.attributeNamesTogether = concatStrings(attributeNames);
        for (String attributeName : attributeNames) {
            setupAttributes()
                .setAttribute(apendPrefixIfMissing(attributeName)).toValue(format(TriggerSingleEventTestChainImpl.TESTED_ATTRIBUTE_VALUE_TEMPLATE, attributeName));
        }
        setupActionsBeforeTriggering().addLast(cleanEventsAction);
        setupCheckingAction().addLast(checkAction);
        return this;
    }

    @Override
    public EventTesterBasicConfig testOrderOfEvents(Object... attributeNames) {
        String[] names = null;
        if (attributeNames != null && attributeNames.length > 0) {
            names = new String[attributeNames.length];
            for (int i = 0; i < attributeNames.length; i++) {
                names[i] = String.valueOf(attributeNames[i]);
            }
        }
        return testOrderOfEvents(names);
    }

    @Override
    public String toString() {
        return "EventsOrderTestChainImpl{" + "attributeNamesTogether=" + attributeNamesTogether + '}';
    }

    @Override
    public EventsOrderTestChain triggeredByAction(Action customAction) {
        setupTriggeringAction().replaceAllActionsByAction(customAction);
        return this;
    }
}
