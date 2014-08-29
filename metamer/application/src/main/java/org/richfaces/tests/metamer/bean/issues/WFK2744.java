package org.richfaces.tests.metamer.bean.issues;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "wfk2744")
@SessionScoped
public class WFK2744 {

    private List<String> names;
    private String name;

    public WFK2744(){
        names = new ArrayList<String>();
        names.add("Aaron");
        names.add("Josh");
        names.add("Steve");
        names.add("Jason");
        names.add("Robert");
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
