package org.richfaces.tests.metamer.bean.issues;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "rf13192")
@SessionScoped
public class RF13192 {
    private static String[][] initdata = { { "test" }, { "foo" }, { "bar" }, { null }, { null } };

    private List<String[]> data = Arrays.asList(initdata);

    public List<String[]> getData() {
        return data;
    }
}