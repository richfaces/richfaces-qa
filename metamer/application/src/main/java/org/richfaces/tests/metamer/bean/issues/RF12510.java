package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "rf12510")
// should not be view-scoped (see https://jira.jboss.org/browse/RF-9287)
@ViewScoped
public class RF12510 implements Serializable {

    private static final long serialVersionUID = 54887878L;
    private int counter = 0;
    private int year;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        year = 1001;
    }

    public String action() {
        return year + "";
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getYear() {
        counter++;
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
