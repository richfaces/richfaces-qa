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

import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.BasicTestChain;

/**
 * Test chain for checking order of multiple events triggered by single action in Metamer application. Useful for testing order
 * of ajax events like onbegin, oncomblete, etc. This chain main responsibility is to setup events attributes in Metamer,
 * trigger the custom action and check events order.
 * <br/>
 * Examples:<br/>
 * Test the order of onbeforeitemchange and onitemchange events:<br/>
 * <code>testOrderOfEvents("onbeforeitemchange", "onitemchange").triggeredByAction(customAction).test();</code>
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface EventsOrderTestChain extends BasicTestChain<EventsOrderTestChain> {

    /**
     * Setup events in right order of their execution.
     */
    EventTesterBasicConfig testOrderOfEvents(String... attributeNames);

    /**
     * Setup events in right order of their execution.
     */
    EventTesterBasicConfig testOrderOfEvents(Object... attributeNames);

    public interface EventTesterBasicConfig {

        /**
         * Setup the action, which will trigger all tested events.
         */
        EventsOrderTestChain triggeredByAction(Action customAction);
    }
}
