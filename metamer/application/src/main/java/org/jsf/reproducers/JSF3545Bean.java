package org.jsf.reproducers;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

@ManagedBean(name = "JSF3545Bean")
@ViewScoped
public class JSF3545Bean implements Serializable {

    private JSF3545ChildBean bean0 = new JSF3545ChildBean("Tab 0 value");
    private JSF3545ChildBean bean1 = new JSF3545ChildBean("Tab 1 value");
    private Boolean tab0Expanded = Boolean.TRUE;
    private Boolean tab1Expanded = Boolean.FALSE;

    public JSF3545ChildBean getBean0() {
        return this.bean0;
    }

    public JSF3545ChildBean getBean1() {
        return this.bean1;
    }

    public Boolean getTab0Expanded() {
        return this.tab0Expanded;
    }

    public Boolean getTab1Expanded() {
        return this.tab1Expanded;
    }

    public void tab0Click(AjaxBehaviorEvent event) {
        this.tab0Expanded = Boolean.TRUE;
        this.tab1Expanded = Boolean.FALSE;
    }

    public void tab1Click(AjaxBehaviorEvent event) {
        this.tab1Expanded = Boolean.TRUE;
        this.tab0Expanded = Boolean.FALSE;
    }
}
