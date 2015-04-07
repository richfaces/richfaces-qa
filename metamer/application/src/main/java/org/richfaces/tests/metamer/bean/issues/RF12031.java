package org.richfaces.tests.metamer.bean.issues;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.validation.constraints.Size;

/**
 * Bean for reproducing of RF-12031
 *
 * @author jhuska
 *
 */
@ManagedBean(name = "rf12031")
@SessionScoped
public class RF12031 {

    private boolean mustRenderThePanel = false;

    @Size(min = 1, max = 4)
    private String firstName;

    public void toggleRender() {
        if (!mustRenderThePanel) {
            mustRenderThePanel = true;
        } else {
            mustRenderThePanel = false;
        }
    }

    public boolean isMustRenderThePanel() {
        return mustRenderThePanel;
    }

    public void setMustRenderThePanel(boolean mustRenderThePanel) {
        this.mustRenderThePanel = mustRenderThePanel;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
