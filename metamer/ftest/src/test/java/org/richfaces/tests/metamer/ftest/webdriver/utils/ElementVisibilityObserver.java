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
package org.richfaces.tests.metamer.ftest.webdriver.utils;

import static java.text.MessageFormat.format;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.joda.time.DateTime;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Lists;

public class ElementVisibilityObserver {

    public static ElementVisibilityObserver getInstance() {
        return new ElementVisibilityObserver();
    }

    public List<Record> getRecords() {
        List<Record> result = Lists.newArrayList();
        for (String record : JSInstanceHolder.instance.getRecords()) {
            result.add(new Record(record));
        }
        return result;
    }

    public void watchForVisibilityChangeOfElement(WebElement e) {
        JSInstanceHolder.instance.watchForVisibilityChangeOfElement(e);
    }

    private static class JSInstanceHolder {

        public static ElementVisibilityObserverJS instance = JSInterfaceFactory.create(context(), ElementVisibilityObserverJS.class);

        private static GrapheneContext context() {
            GrapheneContext context = GrapheneContext.lastContext();
            if (context == null) {
                throw new IllegalStateException("The last used Graphene context is not available.");
            }
            return context;
        }
    }

    public static class Record {

        private static final Pattern pattern = Pattern.compile("at <(\\d*)> element was <((visible|not visible))>");

        private final DateTime time;
        private final boolean visible;

        public Record(String record) {
            Matcher matcher = pattern.matcher(record);
            if (!matcher.matches()) {
                throw new IllegalStateException(format("The record \"{0}\" is corrupted!", record));
            }
            time = new DateTime(Long.parseLong(matcher.group(1)));
            visible = matcher.group(2).equals("visible");
        }

        public Record(boolean visible, DateTime time) {
            this.visible = visible;
            this.time = time;
        }

        public DateTime getTime() {
            return time;
        }

        public boolean isVisible() {
            return visible;
        }

        @Override
        public String toString() {
            return "Record{" + (visible ? "visible at " : "not visible at ") + time + '}';
        }

    }

    @JavaScript("Element.Visibility.Observer")
    @Dependency(sources = "javascript/Element.Visibility.Observer.js")
    public interface ElementVisibilityObserverJS {

        List<String> getRecords();

        void watchForVisibilityChangeOfElement(WebElement element);
    }
}
