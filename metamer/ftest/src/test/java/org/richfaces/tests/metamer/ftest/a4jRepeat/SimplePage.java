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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 5.0.0.Alpha1
 */
public class SimplePage {

    Map<Integer, String> texts = new HashMap<Integer, String>();

    @FindBy(css = "#list li")
    public List<WebElement> rows;
    @FindBy(css = "span.statuses")
    public List<WebElement> statuses;

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
            obtained = statuses.get(position).getText();
            texts.put(position, obtained);
        }
        return parseValue(name, obtained);
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
