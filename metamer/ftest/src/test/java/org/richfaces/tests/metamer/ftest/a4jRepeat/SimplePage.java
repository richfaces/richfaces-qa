/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jRepeat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 5.0.0.Alpha1
 */
public class SimplePage {

    private final Map<Integer, String> cachedTexts = new HashMap<Integer, String>(20);

    @FindBy(css = "[id$='dataScroller']")
    private RichFacesDataScroller dataScroller;
    @FindBy(css = "#list li")
    private List<WebElement> rowsElements;
    @FindBy(css = "span.statuses")
    private List<WebElement> statusesElements;

    public int getBegin(int position) {
        return Integer.valueOf(getValue("begin", position));
    }

    /**
     * @return the texts
     */
    public Map<Integer, String> getCachedTexts() {
        return cachedTexts;
    }

    public int getCount(int position) {
        return Integer.valueOf(getValue("count", position));
    }

    public RichFacesDataScroller getDataScroller() {
        return dataScroller;
    }

    public int getEnd(int position) {
        return Integer.valueOf(getValue("end", position));
    }

    public int getFirstRowFromStateVar(int position) {
        return Integer.valueOf(getValue("firstRowFromStateVar", position));
    }

    public int getIndex(int position) {
        return Integer.valueOf(getValue("index", position));
    }

    public int getRowCount(int position) {
        return Integer.valueOf(getValue("rowCount", position));
    }

    public int getRowKeyVar(int position) {
        return Integer.valueOf(getValue("rowKeyVar", position));
    }

    /**
     * @return the rowsElements
     */
    public List<WebElement> getRowsElements() {
        return rowsElements;
    }

    /**
     * @return the statusesElements
     */
    public List<WebElement> getStatusesElements() {
        return statusesElements;
    }

    private String getValue(String name, int position) {
        String obtained;
        if (getCachedTexts().containsKey(position)) {
            obtained = getCachedTexts().get(position);
        } else {
            obtained = getStatusesElements().get(position).getText();
            getCachedTexts().put(position, obtained);
        }
        return parseValue(name, obtained);
    }

    public boolean isEven(int position) {
        return Boolean.valueOf(getValue("even", position));
    }

    public boolean isFirst(int position) {
        return Boolean.valueOf(getValue("first", position));
    }

    public boolean isLast(int position) {
        return Boolean.valueOf(getValue("last", position));
    }

    private String parseValue(String name, String text) {
        Pattern pattern = Pattern.compile(String.format("(?:^|.* )%s=([^,]+)(?:,.*|$)", name));
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
            throw new IllegalArgumentException(String.format("the text '%s' cannot be parsed", text));
        }
        return matcher.group(1);
    }

    public void resetCachedTexts() {
        getCachedTexts().clear();
    }
}
