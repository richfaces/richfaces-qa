/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.model;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;

import java.util.Iterator;

import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;
import org.richfaces.tests.metamer.ftest.richColumn.ColumnModel;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22499 $
 */
public class DataGrid extends AbstractModel<JQueryLocator> {

    GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

    ReferencedLocator<JQueryLocator> rows = ref(root, "> tbody.rf-dg-body > tr.rf-dg-r");
    JQueryLocator rowToNonEmptyElement = jq("td.rf-dg-c:not(:empty)");
    JQueryLocator rowToElement = jq("td.rf-dg-c");
    ReferencedLocator<JQueryLocator> noDataElement = ref(root, "td.rf-dg-nd-c");

    public DataGrid(JQueryLocator root) {
        super(root);
    }

    public DataGrid(String name, JQueryLocator root) {
        super(name, root);
    }

    public int getElementCount() {
        return selenium.getCount(rows.getChild(rowToNonEmptyElement));
    }

    public int getColumnCount() {
        return selenium.getCount(rows.get(1).getChild(rowToElement));
    }

    public int getRowCount() {
        return selenium.getCount(ColumnModel.getNthChildElement(rows.getChild(rowToElement), 1));
        // return selenium.getCount(rows.getChild(rowToElement).getNthChildElement(1));
    }

    public JQueryLocator getElementOnCoordinates(int row, int column) {
        return ColumnModel.getNthChildElement(rows.get(row).getChild(rowToNonEmptyElement), column);
        // return rows.get(row).getChild(rowToNonEmptyElement).getNthChildElement(column);
    }

    public JQueryLocator getElementOnIndex(int index) {
        return rows.getChild(rowToNonEmptyElement).get(index);
    }

    public Iterator<JQueryLocator> iterateElements() {
        return rows.getDescendant(rowToNonEmptyElement).iterator();
    }

    public boolean isNoData() {
        return selenium.isElementPresent(noDataElement);
    }
}
