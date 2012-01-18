package org.richfaces.tests.metamer.ftest.model;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;
import org.richfaces.tests.metamer.ftest.richColumn.ColumnModel;

public class CollapsibleSubTable extends AbstractModel<JQueryLocator> {

    AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    JQueryLocator subtableRow = jq("tr[class*=rf-cst-][class*=-r]");
    JQueryLocator subtableCell = jq("td.rf-cst-c");

    ReferencedLocator<JQueryLocator> noData = ref(root, "> tr.rf-cst-nd > td.rf-cst-nd-c");
    ReferencedLocator<JQueryLocator> header = ref(root, "> tr.rf-cst-hdr > td.rf-cst-hdr-c");
    ReferencedLocator<JQueryLocator> footer = ref(root, "> tr.rf-cst-ftr > td.rf-cst-ftr-c");

    JQueryLocator visible = jq("{0}:visible");

    public CollapsibleSubTable(JQueryLocator root) {
        super(root);
    }
    
    public JQueryLocator getAnyRow() {
        return root.getLocator().getChild(subtableRow);
    }
    
    public JQueryLocator getAnyCellInColumn(int column) {
    	return ColumnModel.getNthChildElement(root.getLocator().getChild(subtableRow).getChild(subtableCell), column);
    }

    public JQueryLocator getRows() {
        //return jq(root.getLocator().getRawLocator() + " > " + subtableRow);
        return root.getLocator().getChild(subtableRow);
    	//return root.getLocator().getChildren(subtableRow);
    }

    public JQueryLocator getRow(int rowIndex) {
        return root.getLocator().getChild(subtableRow).get(rowIndex);
    }

    public int getRowCount() {
        return selenium.getCount(root.getLocator().getChild(subtableRow));
    }

    public boolean hasVisibleRows() {
        JQueryLocator locator = root.getLocator().getChild(subtableRow);
        locator = visible.format(locator.getRawLocator());
        return selenium.isElementPresent(locator);
    }

    public JQueryLocator getCell(int column, int row) {
        return getRow(row).getChild(subtableCell).get(column);
    }

    public ExtendedLocator<JQueryLocator> getNoData() {
        return noData;
    }

    public boolean isNoData() {
        if (!selenium.isElementPresent(noData)) {
            return false;
        }
        if (selenium.isVisible(noData)) {
            return !selenium.getText(noData).isEmpty();
        } else {
            return selenium.getText(noData).isEmpty();
        }
    }
    
    public boolean isVisible() {
        return selenium.isElementPresent(root.getLocator()) && selenium.isVisible(root.getLocator());
    }

    public ExtendedLocator<JQueryLocator> getHeader() {
        return header;
    }

    public ExtendedLocator<JQueryLocator> getFooter() {
        return footer;
    }
}