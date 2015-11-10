package org.richfaces.tests.metamer.bean.issues;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.tests.metamer.bean.rich.RichDataTableBean;

@ManagedBean(name = "rf13259")
@SessionScoped
public class RF13259 extends RichDataTableBean {

    private static final long serialVersionUID = 1L;

    @Override
    @PostConstruct
    public void init() {
        super.init();
        getAttributes().setAttribute("columnClasses", "oddCell,evenCell");
        getAttributes().setAttribute("rowClasses", "oddRow,evenRow");
        getAttributes().setAttribute("rows", 10);
    }

}
