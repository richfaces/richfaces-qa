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
package org.richfaces.tests.metamer.ftest.extension.tester.basic;

import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.extension.tester.Tester;
import org.richfaces.tests.metamer.ftest.extension.tester.attributes.MultipleAttributesSetter;

/**
 * Basic test chain for setting up attributes in Metamer, adding action check and triggering action.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface BasicTestChain<T extends BasicTestChain> extends Tester {

    /**
     * Returns resources used for testing.
     */
    TestResourcesProvider getTestResources();

    /**
     * Returns a setup for adding/removing actions inside 'after triggering action' step.
     * The action steps are: setup attributes, before triggering action, triggering action, after triggering action, checking action.
     */
    ActionsSetup<T> setupActionsAfterTriggering();

    /**
     * Returns a setup for adding/removing actions inside 'before triggering action' step.
     * The action steps are: setup attributes, before triggering action, triggering action, after triggering action, checking action.
     */
    ActionsSetup<T> setupActionsBeforeTriggering();

    /**
     * Returns a setup for setting of component's attributes.
     * The action steps are: setup attributes, before triggering action, triggering action, after triggering action, checking action.
     */
    MultipleAttributesSetter setupAttributes();

    /**
     * Returns a setup for adding/removing actions inside 'checking action' step.
     * The action steps are: setup attributes, before triggering action, triggering action, after triggering action, checking action.
     */
    ActionsSetup<T> setupCheckingAction();

    /**
     * Returns a setup for adding/removing actions inside 'triggering action' step.
     * The action steps are: setup attributes, before triggering action, triggering action, after triggering action, checking action.
     */
    ActionsSetup<T> setupTriggeringAction();

    public interface ActionsSetup<T extends BasicTestChain> extends ToSingleActionBuilder {

        /**
         * Adds action as a first step in actual chain.
         */
        ActionsSetup<T> addFirst(Action a);

        /**
         * Adds action as a last step in actual chain.
         */
        ActionsSetup<T> addLast(Action a);

        /**
         * Return back to parent test chain.
         */
        T back();

        /**
         * Remove all actions in actual chain and add an action to actual chain.
         */
        ActionsSetup<T> replaceAllActionsByAction(Action a);

    }
}
