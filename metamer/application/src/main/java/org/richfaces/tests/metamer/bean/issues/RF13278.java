package org.richfaces.tests.metamer.bean.issues;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "rf13278")
@SessionScoped
public class RF13278 {

    private int clicks = 0;

    public String increaseClicks() {
        clicks++;
        return "";
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }
}