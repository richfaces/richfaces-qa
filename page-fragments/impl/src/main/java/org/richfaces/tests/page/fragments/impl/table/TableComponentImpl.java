package org.richfaces.tests.page.fragments.impl.table;

import java.util.List;

import org.jboss.arquillian.graphene.component.object.api.scrolling.DataScrollerComponent;
import org.jboss.arquillian.graphene.component.object.api.table.Cell;
import org.jboss.arquillian.graphene.component.object.api.table.CellFunction;
import org.jboss.arquillian.graphene.component.object.api.table.Column;
import org.jboss.arquillian.graphene.component.object.api.table.ColumnFunction;
import org.jboss.arquillian.graphene.component.object.api.table.Footer;
import org.jboss.arquillian.graphene.component.object.api.table.Header;
import org.jboss.arquillian.graphene.component.object.api.table.Row;
import org.jboss.arquillian.graphene.component.object.api.table.RowFunction;
import org.jboss.arquillian.graphene.component.object.api.table.TableComponent;
import org.openqa.selenium.WebElement;

public class TableComponentImpl implements TableComponent {

    @Override
    public void setDateScroller(DataScrollerComponent scroller) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getNumberOfRows() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNumberOfCells() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNumberOfColumns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Row getFirstRow() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Row getLastRow() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Row getRow(int order) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<Cell<T>> findCells(CellFunction<T> function) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Row> findRow(RowFunction function) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<Column<T>> findColumns(ColumnFunction<T> function) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> Cell<T> getCell(Row row, Column<T> column) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Row> getAllRows() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Footer getTableFooter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Header getTableHeader() {
        // TODO Auto-generated method stub
        return null;
    }
}
