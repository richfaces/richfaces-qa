package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.tests.metamer.model.Employee;

@ManagedBean(name="rf13889")
@ViewScoped
public class RF13889 implements Serializable{

    private static final long serialVersionUID = 1L;
    private List<Employee> employees;

    @PostConstruct
    public void init() {
        employees = new ArrayList<Employee>();
        employees.add(new Employee("Martin Tomasek", "Mr."));
        employees.add(new Employee("John Doe", "Mr."));
        employees.add(new Employee("Richard Pierc", "Mr."));
        employees.add(new Employee("Natalia Small", "Mrs."));
        employees.add(new Employee("Andrea Thorn", "Mrs."));
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
