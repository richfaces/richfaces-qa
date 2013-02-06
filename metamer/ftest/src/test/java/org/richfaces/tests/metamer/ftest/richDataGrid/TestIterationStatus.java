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
package org.richfaces.tests.metamer.ftest.richDataGrid;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;

import java.net.URL;

import javax.xml.bind.JAXBException;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestIterationStatus extends AbstractDataGridTest {

    public TestIterationStatus() throws JAXBException {
        super();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataGrid/iterationStatus.xhtml");
    }

    @Test
    public void testBegin() {
        for(int i=0; i<dataGrid.getElementCount(); i++) {
            JQueryLocator beginLocator = jq("span[id$=statusBegin]:eq(" + i + ")");
            assertEquals(selenium.getText(beginLocator), "begin=0", "The iteration status property <begin> doesn't match (item: " + i + ").");
        }
    }

    @Test
    public void testCount() {
        for(int i=0; i<dataGrid.getElementCount(); i++) {
            JQueryLocator propertyLocator = jq("span[id$=statusCount]:eq(" + i + ")");
            String expected = "count=" + (i + 1);
            assertEquals(selenium.getText(propertyLocator), expected, "The iteration status property <count> doesn't match (item: " + i + ").");
        }
    }

    @Test
    public void testEnd() {
        String expected = "end=" + (dataGrid.getElementCount() - 1);
        for(int i=0; i<dataGrid.getElementCount(); i++) {
            JQueryLocator propertyLocator = jq("span[id$=statusEnd]:eq(" + i + ")");
            assertEquals(selenium.getText(propertyLocator), expected, "The iteration status property <end> doesn't match (item: " + i + ").");
        }
    }

    @Test
    public void testEven() {
        for(int i=0; i<dataGrid.getElementCount(); i++) {
            boolean expected = false;
            if (i%2 == 1) {
                expected = true;
            }
            JQueryLocator propertyLocator = jq("span[id$=statusEven]:eq(" + i + ")");
            assertEquals(selenium.getText(propertyLocator), "even=" + Boolean.toString(expected), "The iteration status property <even> doesn't match (item: " + i + ").");
        }
    }

    @Test
    public void testFirst() {
        for(int i=0; i<dataGrid.getElementCount(); i++) {
            boolean expected = false;
            if (i == 0) {
                expected = true;
            }
            JQueryLocator propertyLocator = jq("span[id$=statusFirst]:eq(" + i + ")");
            assertEquals(selenium.getText(propertyLocator), "first=" + Boolean.toString(expected), "The iteration status property <first> doesn't match (item: " + i + ").");
        }
    }

    @Test
    public void testIndex() {
        for(int i=0; i<dataGrid.getElementCount(); i++) {
            JQueryLocator propertyLocator = jq("span[id$=statusIndex]:eq(" + i + ")");
            String expected = "index=" + i;
            assertEquals(selenium.getText(propertyLocator), expected, "The iteration status property <index> doesn't match (item: " + i + ").");
        }
    }

    @Test
    public void testLast() {
        for(int i=0; i<dataGrid.getElementCount(); i++) {
            boolean expected = false;
            if (i == dataGrid.getElementCount() - 1) {
                expected = true;
            }
            JQueryLocator propertyLocator = jq("span[id$=statusLast]:eq(" + i + ")");
            assertEquals(selenium.getText(propertyLocator), "last=" + Boolean.toString(expected), "The iteration status property <last> doesn't match (item: " + i + ").");
        }
    }

    @Test
    public void testRowCount() {
        String expected = "rowCount=" + dataGrid.getElementCount();
        for(int i=0; i<dataGrid.getElementCount(); i++) {
            JQueryLocator propertyLocator = jq("span[id$=statusRowCount]:eq(" + i + ")");
            assertEquals(selenium.getText(propertyLocator), expected, "The iteration status property <rowCount> doesn't match (item: " + i + ").");
        }
    }
}
