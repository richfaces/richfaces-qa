package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.tests.metamer.bean.RichBean;

@ViewScoped
@ManagedBean(name = "rf13018")
public class RF13018 implements Serializable {

    private static final long serialVersionUID = -62122827644L;

    public String function1() {
        RichBean.logToPage("Function 1 called");
        return null;
    }

    public String function2() {
        RichBean.logToPage("Function 2 called");
        return null;
    }
}