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
package org.richfaces.tests.metamer.ftest.extension.attributes.collector.vdl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.richfaces.tests.metamer.ftest.extension.attributes.collector.AttributesCollector;
import org.testng.collections.Maps;

import com.google.common.collect.Lists;

/**
 * Collects attributes for each rich/a4j component from VDL doc.
 * Slower implementation than {@link org.richfaces.tests.metamer.ftest.extension.attributes.collector.taglib.AttributesCollectorFromTaglib AttributesCollectorFromTaglib}.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AttributesCollectorFromVDL implements AttributesCollector {

    private static final String ATTRIBUTES = "attributes";
    private static final String CAPTION_SPAN = "caption span";
    private static final String EMPTY_STRING = "";
    private static final String H2 = "h2";
    private static final String HREF = "href";
    private static final String ID = "id";
    private static final String TABLE_OVERVIEW_SUMMARY = "table.overviewSummary";
    private static final String TAG = "Tag";
    private static final String TBODY_TR = "tbody tr";
    private static final String VDL_DOC_URL = "http://docs.jboss.org/richfaces/nightly_4_5_X/vdldoc/";
    private static final String VDL_DOC_URL_ALLTAGS_NOFRAMES = VDL_DOC_URL + "alltags-noframe.html";

    private static String getComponentNameFromActualPage(WebDriver wd) {
        return WordUtils.capitalize(wd.findElement(By.tagName(H2)).getText().replaceAll(TAG, EMPTY_STRING).trim());
    }

    private static List<String> getLinksURLFromElements(List<WebElement> linksElements) {
        List<String> linksURL = new ArrayList<String>(linksElements.size());
        for (WebElement element : linksElements) {
            linksURL.add(element.getAttribute(HREF));
        }
        return linksURL;
    }

    @Override
    public Map<String, List<String>> collectAttributes() {
        WebDriver browser = new FirefoxDriver();
        try {
            browser.get(VDL_DOC_URL_ALLTAGS_NOFRAMES);
            List<WebElement> linksElements = browser.findElements(By.cssSelector("li a"));
            Map<String, List<String>> result = Maps.newHashMap();
            List<String> attributes;
            List<WebElement> tables;
            WebElement correctAttributesTable;
            for (String link : getLinksURLFromElements(linksElements)) {
                browser.get(link);
                attributes = Lists.newArrayList();
                tables = browser.findElements(By.cssSelector(TABLE_OVERVIEW_SUMMARY));
                correctAttributesTable = null;
                for (WebElement table : tables) {
                    if (table.findElement(By.cssSelector(CAPTION_SPAN)).getText().toLowerCase().contains(ATTRIBUTES)) {
                        correctAttributesTable = table;
                        break;
                    }
                }
                if (correctAttributesTable != null) {
                    for (WebElement attributeElement : correctAttributesTable.findElements(By.cssSelector(TBODY_TR))) {
                        attributes.add(attributeElement.getAttribute(ID));
                    }
                    result.put(getComponentNameFromActualPage(browser), attributes);
                }
            }
            return result;
        } finally {
            browser.quit();
        }
    }
}
