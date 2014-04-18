/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.fragment.dataTable;

import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.TypeResolver;
import org.richfaces.fragment.common.Utils;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class AbstractTable<HEADER, ROW, FOOTER> implements DataTable<HEADER,ROW,FOOTER> {

    @Root
    private WebElement root;

    @SuppressWarnings("unchecked")
    private final Class<ROW> rowClass = (Class<ROW>) TypeResolver.resolveRawArguments(DataTable.class, getClass())[1];
    @SuppressWarnings("unchecked")
    private final Class<HEADER> headerClass = (Class<HEADER>) TypeResolver.resolveRawArguments(DataTable.class, getClass())[0];
    @SuppressWarnings("unchecked")
    private final Class<FOOTER> footerClass = (Class<FOOTER>) TypeResolver.resolveRawArguments(DataTable.class, getClass())[2];

    private final AdvancedTableInteractions advancedInteractions = new AdvancedTableInteractions();

    @Override
    public ROW getRow(int n) {
        return Graphene.createPageFragment(rowClass, getTableRows().get(n));
    }

    @Override
    public ROW getFirstRow() {
        return getRow(0);
    }

    @Override
    public ROW getLastRow() {
        return getRow(advanced().getNumberOfRows() - 1);
    }

    @Override
    public List<ROW> getAllRows() {
        List<ROW> result = new ArrayList<ROW>();
        for (int i = 0; i < advanced().getNumberOfRows(); i++) {
            result.add(getRow(i));
        }
        return result;
    }

    public HEADER getHeader() {
        return Graphene.createPageFragment(headerClass, getWholeTableHeader());
    }

    public FOOTER getFooter() {
        return Graphene.createPageFragment(footerClass, getWholeTableFooter());
    }

    public AdvancedTableInteractions advanced() {
        return advancedInteractions;
    }

    protected abstract List<WebElement> getTableRows();

    protected abstract List<WebElement> getFirstRowCells();

    protected abstract ByJQuery getSelectorForCell(int column);

    protected abstract WebElement protectedGetNoData();

    protected abstract WebElement getWholeTableHeader();

    protected abstract WebElement getWholeTableFooter();

    protected abstract WebElement getHeaderElement();

    protected abstract WebElement getFooterElement();

    protected abstract List<WebElement> getColumnHeaderElements();

    protected abstract List<WebElement> getColumnFooterElements();

    public class AdvancedTableInteractions {

        public int getNumberOfColumns() {
            if (!isVisible()) {
                return 0;
            } else {
                return getFirstRowCells().size();
            }
        }

        public int getNumberOfRows() {
            if (!isVisible()) {
                return 0;
            } else {
                return getTableRows().size();
            }
        }

        public boolean isVisible() {
            return Utils.isVisible(root);
        }

        public boolean isNoData() {
            return Utils.isVisible(protectedGetNoData());
        }

        public WebElement getNoDataElement() {
            return protectedGetNoData();
        }

        public WebElement getCellElement(int column, int row) {
            return getTableRows().get(row).findElement(getSelectorForCell(column));
        }

        public WebElement getHeaderElement() {
            return getHeaderElement();
        }

        public WebElement getFooterElement() {
            return getFooterElement();
        }

        public WebElement getColumnHeaderElement(int column) {
            return getColumnHeaderElements().get(column);
        }

        public WebElement getColumnFooterElement(int column) {
            return getColumnFooterElements().get(column);
        }
    }
}
