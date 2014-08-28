package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
/**
 * This is bean for reproducer issue rf-13802.
 * @author mtomasek Martin Tomasek
 *
 */

@ManagedBean(name = "rf13802")
@SessionScoped
public class RF13802 implements Serializable {

    private String value;
    private int counter = 0;

    public RF13802() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void action(ActionEvent event) {
        value = "This is value set in action after click number: " + counter;
        counter++;
    }
}
