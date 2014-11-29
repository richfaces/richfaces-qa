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

import org.richfaces.tests.metamer.ftest.extension.tester.attributes.AttributesHandler;
import org.richfaces.tests.metamer.ftest.extension.tester.attributes.MultipleAttributesSetter;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class BasicTestChainImpl<T extends BasicTestChain> implements BasicTestChain<T> {

    private final ActionsSetupImpl<T> afterTriggeringActions;
    private final AttributesHandler attributesSetter;
    private final ActionsSetupImpl<T> beforeTrigerringActions;
    private final ActionsSetupImpl<T> checkingActions;
    private final TestResourcesProvider testResources;
    private final ActionsSetupImpl<T> trigerringActions;

    @SuppressWarnings("unchecked")
    public BasicTestChainImpl(TestResourcesProvider provider) {
        this.testResources = provider;
        this.attributesSetter = new AttributesHandler(provider);
        this.afterTriggeringActions = new ActionsSetupImpl<T>((T) this);
        this.beforeTrigerringActions = new ActionsSetupImpl<T>((T) this);
        this.checkingActions = new ActionsSetupImpl<T>((T) this);
        this.trigerringActions = new ActionsSetupImpl<T>((T) this);
    }

    @Override
    public TestResourcesProvider getTestResources() {
        return testResources;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ActionsSetup<T> setupActionsAfterTriggering() {
        return afterTriggeringActions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ActionsSetup<T> setupActionsBeforeTriggering() {
        return beforeTrigerringActions;
    }

    @Override
    public MultipleAttributesSetter setupAttributes() {
        return attributesSetter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ActionsSetup<T> setupCheckingAction() {
        return checkingActions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ActionsSetup<T> setupTriggeringAction() {
        return trigerringActions;
    }

    @Override
    public void test() {
        attributesSetter.perform();
        beforeTrigerringActions.perform();
        trigerringActions.perform();
        afterTriggeringActions.perform();
        checkingActions.perform();
    }

    @Override
    public String toString() {
        return "BasicTestChainImpl";
    }
}
