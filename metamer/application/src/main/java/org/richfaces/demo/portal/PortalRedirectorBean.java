/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.demo.portal;

import java.io.IOException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author pmensik
 */
@ManagedBean
@ApplicationScoped
public class PortalRedirectorBean {

    private String linkToTest;

    public void redirectToTestPage() throws IOException {
        String newUrl = linkToTest + "?javax.portlet.faces.ViewLink=true&template=plain";
        FacesContext.getCurrentInstance().getExternalContext().redirect(newUrl);
    }

    public String getLinkToTest() {
        return linkToTest;
    }

    public void setLinkToTest(String linkToTest) {
        this.linkToTest = linkToTest;
    }
}
