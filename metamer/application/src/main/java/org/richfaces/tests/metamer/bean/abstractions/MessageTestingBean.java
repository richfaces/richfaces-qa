/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.bean.abstractions;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.bean.TemplateBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class MessageTestingBean implements AttributesHolder {

    public static final List<FacesMessage.Severity> MSGS_TYPES = Lists.newArrayList(
            FacesMessage.SEVERITY_FATAL,
            FacesMessage.SEVERITY_ERROR,
            FacesMessage.SEVERITY_WARN,
            FacesMessage.SEVERITY_INFO);
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageTestingBean.class);
    protected Attributes attributes;
    protected String simpleInput1;
    protected String simpleInput2;
    @ManagedProperty("#{templateBean}")
    protected TemplateBean templateBean;

    /**
     * Generates Faces Message with chosen severity and for chosen componentID
     */
    public static void generateFacesMessage(FacesMessage.Severity severity, String componentID) {
        String value = "Generated " + severity.toString() + " message for input " + componentID;
        FacesContext.getCurrentInstance().addMessage(componentID, new FacesMessage(severity, value, value));
    }

    /**
     * Generates Faces Message for chosen componentID
     */
    public static void generateAllKindsOfMessagesForID(String componentID) {
        for (FacesMessage.Severity severity : MSGS_TYPES) {
            generateFacesMessage(severity, componentID);
        }
    }

    /**
     * Generates All kinds (all severities) of Faces Messages for components -- simpleInput1, simpleInput2, null.
     */
    public void generateAllFacesMessages() {
        LOGGER.debug(" ### Just called generateAllFacesMessages()");
        generateAllKindsOfMessagesForID(null);
        generateAllKindsOfMessagesForID(templateBean.getComponentPrefix() + "simpleInput1");
        generateAllKindsOfMessagesForID(templateBean.getComponentPrefix() + "simpleInput2");
    }

    /**
     * Generates Faces Message of chosen severity for components -- simpleInput1, simpleInput2, null.
     */
    public void generateFacesMessagesWithSeverity(String severityOfMessageToBeCreated) {
        LOGGER.debug(" ### Just called generateFacesMessagesWithSeverity('" + severityOfMessageToBeCreated + "')");
        severityOfMessageToBeCreated = severityOfMessageToBeCreated.toUpperCase(Locale.ENGLISH);
        for (FacesMessage.Severity referenceSeverity : MSGS_TYPES) {
            if (referenceSeverity.toString().startsWith(severityOfMessageToBeCreated)) {
                generateFacesMessage(referenceSeverity, null);
                generateFacesMessage(referenceSeverity, templateBean.getComponentPrefix() + "simpleInput1");
                generateFacesMessage(referenceSeverity, templateBean.getComponentPrefix() + "simpleInput2");
            }
        }
    }

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    public String getSimpleInput1() {
        return simpleInput1;
    }

    public String getSimpleInput2() {
        return simpleInput2;
    }

    public TemplateBean getTemplateBean() {
        return templateBean;
    }

    @Override
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setSimpleInput1(String simpleInput1) {
        this.simpleInput1 = simpleInput1;
    }

    public void setSimpleInput2(String simpleInput2) {
        this.simpleInput2 = simpleInput2;
    }

    public void setTemplateBean(TemplateBean templateBean) {
        this.templateBean = templateBean;
    }
}
