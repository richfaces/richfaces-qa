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
package org.jboss.test.selenium.waiting;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;

/**
 * Condition that verifies that the given condition doesn't hold.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class NegationCondition implements SeleniumCondition, JavaScriptCondition{

    private SeleniumCondition condition;

    /**
     * Factory method.
     *
     * @return single instance of NegationCondition
     */
    public static NegationCondition getInstance() {
        return new NegationCondition();
    }

    public NegationCondition condition(SeleniumCondition condition) {
        Validate.notNull(condition);
        NegationCondition copy = new NegationCondition();
        copy.condition = condition;
        return copy;
    }

    @Override
    public JavaScript getJavaScriptCondition() {
        if (!(condition instanceof JavaScriptCondition)) {
            throw new IllegalStateException("The given condition doesn't implement JavaScriptCondition so its negation can't be converted to the JavaScript.");
        }
        return new JavaScript("!(" + ((JavaScriptCondition) condition).getJavaScriptCondition().getAsString() + ")");
    }

    @Override
    public boolean isTrue() {
        return !condition.isTrue();
    }
}
