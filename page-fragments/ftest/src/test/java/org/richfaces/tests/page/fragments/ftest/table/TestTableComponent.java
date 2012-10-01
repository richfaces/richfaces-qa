package org.richfaces.tests.page.fragments.ftest.table;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.graphene.component.object.api.common.NestedElements;
import org.jboss.arquillian.graphene.component.object.api.table.Cell;
import org.jboss.arquillian.graphene.component.object.api.table.CellFunction;
import org.jboss.arquillian.graphene.component.object.api.table.Column;
import org.jboss.arquillian.graphene.component.object.api.table.Footer;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.ftest.AbstractTest;
import org.richfaces.tests.page.fragments.ftest.bean.Person;
import org.richfaces.tests.page.fragments.ftest.bean.PersonBean;
import org.testng.annotations.Test;

public class TestTableComponent extends AbstractTest {

    @FindBy(xpath = "//foooooooo")
    private TableOfPersons table;

    public class CellFunctionString implements CellFunction<String> {

        @Override
        public boolean accept(Cell<String> cell) {
            List<NestedElements<String>> elements = cell.getContent();

            for (NestedElements<String> i : elements) {
                if (i.getValue().contains("foo")) {
                    return true;
                }
            }
            return false;
        }

    }

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return createDeployment(TestTableComponent.class).addClass(PersonBean.class);
    }

    @Test
    public void testPersonTable() {
        Column<String> column = table.getColumn1();

        List<Cell<String>> cells = table.findCells(new CellFunctionString());

        String cellValue = cells.get(0).getContent().get(0).getValue();

        List<Cell<String>> columnCells = column.getAllCells();

        Person person = table.getPerson(table.getFirstRow());

    }

    @Test
    public void testSumOfAgesIsLessThan100() {

        Integer sum = table.getSumOfAges();
        assertTrue(sum < 100, "The sum of the ages must not be equal to 100 or greater!");

        Footer footer = table.getTableFooter();
        Integer sumFromFooter = footer.getCell(table.getColumn3()).getContent().get(0).getValue();
        assertTrue(sumFromFooter < 100, "The sum of ages from footer is not smaller than 100, and it should be.");
        assertEquals(sum, sumFromFooter, "The sum from footer is wrong!");
    }
}
