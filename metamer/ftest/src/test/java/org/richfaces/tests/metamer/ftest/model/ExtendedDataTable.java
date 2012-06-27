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

import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;
import org.richfaces.tests.metamer.ftest.abstractions.DataTable;
import org.richfaces.tests.metamer.ftest.richColumn.ColumnModel;

/**
 * Provides methods to control DataTable
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public class ExtendedDataTable extends AbstractModel<JQueryLocator> implements DataTable {

    GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

    ReferencedLocator<JQueryLocator> tbody = ref(root, "> div.rf-edt-b > div > table > tbody > tr > td > div.rf-edt-cnt > table > tbody");
    ReferencedLocator<JQueryLocator> firstColumnRows = ref(tbody, "> tr > td:nth-child(1) > div.rf-edt-c > div.rf-edt-c-cnt");
    ReferencedLocator<JQueryLocator> firstRowColumns = ref(tbody, "> tr:eq(0) > td > div.rf-edt-c > div.rf-edt-c-cnt");
    ReferencedLocator<JQueryLocator> body = ref(root, "> div.rf-edt-b");
    ReferencedLocator<JQueryLocator> columns = ref(tbody, "> tr");
    JQueryLocator columnToElement = jq("td");
    JQueryLocator elementToContent = jq("div.rf-edt-c > div.rf-edt-c-cnt");

    ReferencedLocator<JQueryLocator> tableHeader = ref(root, "> div.rf-edt-tbl-hdr");
    ReferencedLocator<JQueryLocator> columnHeader = ref(root, "> div.rf-edt-hdr > table > tbody > tr > td > div.rf-edt-cnt > table > tbody > tr > td");
    JQueryLocator columnHeaderToContent = jq("div.rf-edt-hdr-c > div.rf-edt-hdr-c-cnt");
    ReferencedLocator<JQueryLocator> columnFooter = ref(root, "> div.rf-edt-ftr > table > tbody > tr > td > div.rf-edt-cnt > table > tbody > tr > td");
    JQueryLocator columnFooterToContent = jq("div.rf-edt-ftr-c > div.rf-edt-ftr-c-cnt");

    public ExtendedDataTable(JQueryLocator root) {
        super(root);
    }

    public ExtendedDataTable(String name, JQueryLocator root) {
        super(name, root);
    }

    public String getTableText() {
        return selenium.getText(root.getLocator());
    }

    @Override
    public int getRows() {
        return selenium.getCount(firstColumnRows);
    }

    @Override
    public int getColumns() {
        return selenium.getCount(firstRowColumns);
    }

    @Override
    public boolean isVisible() {
        return selenium.isElementPresent(root.getLocator()) && selenium.isVisible(root.getLocator());
    }

    @Override
    public boolean isNoData() {
        return selenium.isElementPresent(body) && !selenium.isElementPresent(tbody);
    }

    @Override
    public JQueryLocator getNoData() {
        return body.getReferenced();
    }

    @Override
    public JQueryLocator getElement(int column, int row) {
        return ColumnModel.getNthChildElement(columns.get(row).getChild(columnToElement), column).getChild(elementToContent);
    }

    @Override
    public JQueryLocator getColumnHeader(int column) {
        return columnHeader.get(column).getChild(columnHeaderToContent);
    }

    @Override
    public JQueryLocator getColumnFooter(int column) {
        return columnFooter.get(column).getChild(columnFooterToContent);
    }

    @Override
    public JQueryLocator getHeader() {
        return tableHeader.getReferenced();
    }
}
