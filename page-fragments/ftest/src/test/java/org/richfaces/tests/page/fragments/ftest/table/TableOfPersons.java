package org.richfaces.tests.page.fragments.ftest.table;

import java.util.List;

import org.jboss.arquillian.graphene.component.object.api.table.Row;
import org.richfaces.tests.page.fragments.ftest.bean.Person;
import org.richfaces.tests.page.fragments.impl.table.TableComponent6Impl;

public class TableOfPersons extends TableComponent6Impl<String, String, Integer, String, String, String> {

    public Person getPerson(Row row) {
        Person person = new Person();
        person.setFirstName(row.getCell(getColumn1()).getContent().get(0).getValue());
        person.setLastName(row.getCell(getColumn2()).getContent().get(0).getValue());
        person.setAge(row.getCell(getColumn3()).getContent().get(0).getValue());
        person.setAddress(row.getCell(getColumn4()).getContent().get(0).getValue());
        person.setPhone(row.getCell(getColumn5()).getContent().get(0).getValue());
        person.setJob(row.getCell(getColumn6()).getContent().get(0).getValue());

        return person;
    }

    public Integer getSumOfAges() {

        Integer sum = new Integer(0);

        List<Row> rows = getAllRows();
        for (Row i : rows) {
            Integer age = i.getCell(getColumn3()).getContent().get(0).getValue();
            sum += age;
        }

        return sum;
    }
}
