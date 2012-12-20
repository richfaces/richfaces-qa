/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.richfaces.component.UIRichMessages;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * Simple bean for rich:messages component example.
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "richMessagesBean")
@ViewScoped
public class RichMessagesBean implements Serializable {

    /** Generated UID */
    private static final long serialVersionUID = 4893769498631480379L;
    // id for input element to bound some FacesMessage to it
    private static final String INPUT1_ID = "form:simpleInput1";
    private static final String INPUT2_ID = "form:simpleInput2";
    private static final Logger logger = LoggerFactory.getLogger(RichMessagesBean.class);
    private Attributes attributes;
    private String simpleInput1;
    private String simpleInput2;
    private static final List<FacesMessage.Severity> msgsTypes = Lists
            .newArrayList(FacesMessage.SEVERITY_FATAL,
            FacesMessage.SEVERITY_ERROR, FacesMessage.SEVERITY_WARN,
            FacesMessage.SEVERITY_INFO);

    @PostConstruct
    public void init() {
        logger.debug("initializing bean " + getClass().getName());
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIRichMessages.class, getClass());

        simpleInput1 = "10";
        simpleInput2 = "10";

        attributes.setAttribute("ajaxRendered", true);
        attributes.setAttribute("for", "simpleInput1");
        attributes.setAttribute("escape", true);
        attributes.setAttribute("globalOnly", false);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("showSummary", true);

        // add attributes with missing appropriate annotation
        attributes.setAttribute("lang", null);
        attributes.setAttribute("dir", null);
        attributes.setAttribute("style", null);
        attributes.setAttribute("styleClass", null);
        attributes.setAttribute("title", null);

        attributes.setAttribute("onclick", null);
        attributes.setAttribute("ondblclick", null);
        attributes.setAttribute("onkeydown", null);
        attributes.setAttribute("onkeypress", null);
        attributes.setAttribute("onkeyup", null);
        attributes.setAttribute("onmousedown", null);
        attributes.setAttribute("onmousemove", null);
        attributes.setAttribute("onmouseup", null);
        attributes.setAttribute("onmouseout", null);
        attributes.setAttribute("onmouseover", null);

    }

    private void generateAllKindOfMessagesForID(String componentID) {
        String value;
        for (FacesMessage.Severity severity : msgsTypes) {
            value = "Generated " + severity.toString() + " message for input " + componentID;
            FacesContext.getCurrentInstance().addMessage(componentID,
                    new FacesMessage(severity, value, value));
        }
    }

    public void generateFacesMsgs(ActionEvent event) {
        logger.info(" ### Just called generateFacesError()");
        generateAllKindOfMessagesForID(null);
        generateAllKindOfMessagesForID(INPUT1_ID);
        generateAllKindOfMessagesForID(INPUT2_ID);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public String getSimpleInput1() {
        return simpleInput1;
    }

    public String getSimpleInput2() {
        return simpleInput2;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setSimpleInput1(String simpleInput1) {
        this.simpleInput1 = simpleInput1;
    }

    public void setSimpleInput2(String simpleInput2) {
        this.simpleInput2 = simpleInput2;
    }
}
