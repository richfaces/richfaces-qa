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
package org.richfaces.tests.metamer.ftest.extension.multipleEventFiring;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class MultipleEventFirererImpl implements MultipleEventFirerer {

    private static final String TRIGGER_EVENT_END = "');";
    private static final String WRAP_ELEMENTS_IN_JQUERY_END_TRIGGER_EVENT_START = "]).trigger('";
    private static final String WRAP_ELEMENTS_IN_JQUERY_START = "jQuery(arguments[";

    private final List<Integer> counts = Lists.newArrayList();
    private final ArrayList<WebElement> elements = Lists.newArrayList();
    private final ArrayList<String> events = Lists.newArrayList();
    private final JavascriptExecutor executor;

    public MultipleEventFirererImpl(JavascriptExecutor executor) {
        this.executor = executor;
    }

    @Override
    public MultipleEventFirerer addEvent(WebElement element, int count, String eventName) {
        elements.add(element);
        counts.add(count);
        events.add(eventName);
        return this;
    }

    @Override
    public void perform() {
        executor.executeScript(prepareScript(), elements);
    }

    private String prepareScript() {
        StringBuilder sb = new StringBuilder();
        int countsSize = counts.size();
        for (int i = 0; i < countsSize; i++) {
            int count = counts.get(i);
            for (int j = 0; j < count; j++) {
                // wrap element in JQuery and trigger an event on it
                // jQuery(arguments[<i>]).trigger('<events.get(i)>');
                sb.append(WRAP_ELEMENTS_IN_JQUERY_START).append(i).append(WRAP_ELEMENTS_IN_JQUERY_END_TRIGGER_EVENT_START)
                    .append(events.get(i)).append(TRIGGER_EVENT_END);
            }
        }
        return sb.toString();
    }
}
