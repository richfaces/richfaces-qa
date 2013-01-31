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
package org.richfaces.tests.metamer.ftest.a4jRepeat;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public class SimpleModel {

    Map<Integer, String> texts = new HashMap<Integer, String>();

    JQueryLocator row = jq("#list li");
    JQueryLocator statuses = jq("span.statuses");

    private GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

    public boolean isRendered() {
        return selenium.isElementPresent(row);
    }

    public int getTotalRowCount() {
        return selenium.getCount(row);
    }

    public int getBegin(int position) {
        return Integer.valueOf(getValue("begin", position));
    }

    public int getEnd(int position) {
        return Integer.valueOf(getValue("end", position));
    }

    public int getIndex(int position) {
        return Integer.valueOf(getValue("index", position));
    }

    public int getCount(int position) {
        return Integer.valueOf(getValue("count", position));
    }

    public boolean isFirst(int position) {
        return Boolean.valueOf(getValue("first", position));
    }

    public boolean isLast(int position) {
        return Boolean.valueOf(getValue("last", position));
    }

    public boolean isEven(int position) {
        return Boolean.valueOf(getValue("even", position));
    }

    public int getRowCount(int position) {
        return Integer.valueOf(getValue("rowCount", position));
    }

    private String getValue(String name, int position) {
        String obtained;
        if (texts.containsKey(position)) {
            obtained = texts.get(position);
        } else {
            JQueryLocator locator = getRowOnPosition(position).getDescendant(statuses);
            obtained = selenium.getText(locator);
            texts.put(position, obtained);
        }
        return parseValue(name, obtained);
    }

    private JQueryLocator getRowOnPosition(int position) {
        return row.get(position);
    }

    private String parseValue(String name, String text) {
        Pattern pattern = Pattern.compile(format("(?:^|.* ){0}=([^,]+)(?:,.*|$)", name));
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
            throw new IllegalArgumentException(format("the text '{0}' cannot be parsed", text));
        }
        return matcher.group(1);
    }
}
