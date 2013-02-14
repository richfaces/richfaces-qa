/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.metamer;

import java.io.IOException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.portlet.ActionResponse;

/**
 *
 * @author pmensik
 */
@ManagedBean
@ApplicationScoped
public class PortalUrlBean {

    private String linkToTest;

    public void redirect() throws IOException {
        Object response = FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ((ActionResponse) response).sendRedirect(linkToTest);
    }

    public String getLinkToTest() {
        return linkToTest;
    }

    public void setLinkToTest(String linkToTest) {
        this.linkToTest = linkToTest;
    }
}
