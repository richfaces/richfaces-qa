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
package org.richfaces.tests.metamer.ftest.richColumn;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;
import org.richfaces.tests.metamer.ftest.model.AbstractModel;
import org.richfaces.tests.metamer.model.Capital;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public class ColumnModel extends AbstractModel<JQueryLocator> {

    AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    private ReferencedLocator<JQueryLocator> tableHeader = ref(root, "> thead.rf-dt-thd");
    private ReferencedLocator<JQueryLocator> tableBody = ref(root, "> tbody.rf-dt-b");
    private ReferencedLocator<JQueryLocator> tableFooter = ref(root, "> tfoot.rf-dt-tft");

    private ReferencedLocator<JQueryLocator> headerRow = ref(tableHeader, "> tr.rf-dt-hdr");
    private ReferencedLocator<JQueryLocator> bodyRow = ref(tableBody, "> tr.rf-dt-r");
    private ReferencedLocator<JQueryLocator> footerRow = ref(tableFooter, "> tr.rf-dt-ftr");

    private JQueryLocator headerCell = jq("th.rf-dt-hdr-c");
    private JQueryLocator bodyCell = jq("td.rf-dt-c");
    private JQueryLocator footerCell = jq("td.rf-dt-ftr-c");

    public ColumnModel(String name, JQueryLocator root) {
        super(name, root);
    }

    public int getBodyRowCount() {
        return selenium.getCount(getNthChildElement(bodyRow.getChild(bodyCell), 1));
        // return
        // selenium.getCount(bodyRow.getChild(bodyCell).getNthChildElement(1));
    }

    public int getHeaderRowCount() {
        return selenium.getCount(getNthChildElement(headerRow.getChild(headerCell), 1));
        // return
        // selenium.getCount(headerRow.getChild(headerCell).getNthChildElement(1));
    }

    public int getFooterRowCount() {
        return selenium.getCount(getNthChildElement(footerRow.getChild(footerCell), 1));
        // return selenium.getCount(footerRow.getChild(footerCell)
        // .getNthChildElement(1));
    }

    public int getBodyRowCellCount(int row) {
        return selenium.getCount(getNthChildElement(bodyRow, row).getChild(bodyCell));
        // return selenium.getCount(bodyRow.getNthChildElement(row).getChild(
        // bodyCell));
    }

    public int getHeaderRowCellCount(int row) {
        return selenium.getCount(getNthChildElement(headerRow, row).getChild(headerCell));
        // return selenium.getCount(headerRow.getNthChildElement(row).getChild(
        // headerCell));
    }

    public int getFooterRowCellCount(int row) {
        return selenium.getCount(getNthChildElement(footerRow, row).getChild(footerCell));
        // return selenium.getCount(footerRow.getNthChildElement(row).getChild(
        // footerCell));
    }

    public JQueryLocator getBodyCell(int row, int column) {
        return getNthChildElement(getNthChildElement(bodyRow, row).getChild(bodyCell), column);
        // return bodyRow.getNthChildElement(row).getChild(bodyCell)
        // .getNthChildElement(column);
    }

    public JQueryLocator getHeaderCell(int row, int column) {
        return getNthChildElement(getNthChildElement(headerRow, row).getChild(headerCell), column);
        // return headerRow.getNthChildElement(row).getChild(headerCell)
        // .getNthChildElement(column);
    }

    public JQueryLocator getFooterCell(int row, int column) {
        return getNthChildElement(getNthChildElement(footerRow, row).getChild(footerCell), column);
        // return footerRow.getNthChildElement(row).getChild(footerCell)
        // .getNthChildElement(column);
    }

    public static JQueryLocator getNthChildElement(JQueryLocator parent, int index) {

        return new JQueryLocator(SimplifiedFormat.format("{0}:nth-child({1})", parent.getRawLocator(), index));
    }

    public JQueryLocator getNthChildElement(ReferencedLocator<JQueryLocator> parent, int index) {

        return new JQueryLocator(SimplifiedFormat.format("{0}:nth-child({1})", parent.getRawLocator(), index));
    }

    public Capital getCapital(int index) {
        String state = selenium.getText(getBodyCell(index + 1, 1));
        String name = selenium.getText(getBodyCell(index + 1, 2));
        Capital result = new Capital();
        result.setName(name);
        result.setState(state);
        return result;
    }

    public Collection<Capital> getCapitals() {
        int count = getBodyRowCount();
        List<Capital> capitals = new LinkedList<Capital>();
        for (int i = 0; i < count; i++) {
            capitals.add(getCapital(i));
        }
        return capitals;
    }
}
