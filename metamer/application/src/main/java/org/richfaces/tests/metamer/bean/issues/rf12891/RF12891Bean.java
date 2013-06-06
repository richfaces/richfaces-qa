package org.richfaces.tests.metamer.bean.issues.rf12891;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.ArrayDataModel;

import org.richfaces.tests.metamer.model.Employee;

@ManagedBean
@SessionScoped
public class RF12891Bean implements Serializable {

    private static final long serialVersionUID = 1L;

    public Object getDataModel() {
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(new Employee("Karol", "manager"));
        employees.add(new Employee("Kamil", "manager"));
        employees.add(new Employee("Kebab", "manager"));
        employees.add(new Employee("Edo", "manager"));
        employees.add(new Employee("Peter", "manager"));
        employees.add(new Employee("Oliver", "manager"));
        employees.add(new Employee("Filip", "manager"));
        employees.add(new Employee("Emil", "manager"));
        employees.add(new Employee("Andrej", "manager"));
        employees.add(new Employee("Nunez", "manager"));

        Employee[] employeeArray = new Employee[10];
        ArrayDataModel<Employee> employeeDataModel = new ArrayDataModel<Employee>(employees.toArray(employeeArray));

        return new SequenceDataModel<Employee>(employeeDataModel);
    }

    public Object someAction(Object obj) {
        System.out.println("someAction called!");
        return "foo";
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
