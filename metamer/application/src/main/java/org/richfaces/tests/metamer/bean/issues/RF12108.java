package org.richfaces.tests.metamer.bean.issues;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.richfaces.event.ItemChangeEvent;

@ManagedBean(name = "tabPanelAjaxBean")
@RequestScoped
public class RF12108 {

    private String name;
    private String selectedTab = "second";

    public void updateCurrent(ItemChangeEvent event) {
    }

    public RF12108() {
        super();
    }

    public String getName() {
        return name;
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    public void setName(String name) {
        this.name = name;
    }

}
