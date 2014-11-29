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

import java.util.LinkedList;

import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.extension.tester.basic.BasicTestChain.ActionsSetup;

import com.google.common.collect.Lists;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ActionsSetupImpl<T extends BasicTestChain> implements ActionsSetup, Action {

    private final LinkedList<Action> actions = Lists.newLinkedList();

    private final T testChain;

    public ActionsSetupImpl(T testChain) {
        this.testChain = testChain;
    }

    @Override
    public ActionsSetup addFirst(Action a) {
        actions.addFirst(a);
        return this;
    }

    @Override
    public ActionsSetup addLast(Action a) {
        actions.addLast(a);
        return this;
    }

    @Override
    public Action asSingleAction() {
        return this;
    }

    @Override
    public T back() {
        return testChain;
    }

    @Override
    public void perform() {
        for (Action action : actions) {
            action.perform();
        }
    }

    @Override
    public BasicTestChain.ActionsSetup replaceAllActionsByAction(Action a) {
        actions.clear();
        actions.add(a);
        return this;
    }
}
