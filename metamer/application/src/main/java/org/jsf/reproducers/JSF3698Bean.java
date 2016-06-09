package org.jsf.reproducers;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name = "JSF3698Bean")
public class JSF3698Bean implements Serializable {

    public void buttonSubmit() {
    }

    public String getInputTextValue() {
        return "test test test test";
    }

    public String getRandomNumber() {
        Double x = Math.random() * 100;
        String retVal = x.toString();
        return retVal;
    }

    public void setInputTextValue(String textVal) {
    }
}
