package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.richfaces.application.ServiceTracker;
import org.richfaces.focus.FocusManager;

@ManagedBean(name = "rf13251")
@SessionScoped
public class RF13251 implements Serializable {

    private String actionResult;

    public RF13251() {
        this.actionResult = "No action has been performed yet.";
    }

    public void focusToAutoComplete(ActionEvent event) {
        FocusManager focusManager = ServiceTracker.getService(FocusManager.class);
        focusManager.focus("autocomplete");
        this.actionResult = "Focus to autocomplete was set up";
    }

    public void focusToInputText(ActionEvent event) {
        FocusManager focusManager = ServiceTracker.getService(FocusManager.class);
        focusManager.focus("inputTextSimple");
        this.actionResult = "Focus to inputTextSimple was set up";
    }

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }
}
