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

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.richfaces.component.NotifyAttributes;
import org.richfaces.component.UINotify;
import org.richfaces.component.UINotifyMessages;
import org.richfaces.component.UINotifyStack;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for notify:notify
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@ManagedBean(name = "notifyBean")
@ViewScoped
public class RichNotifyBean implements Serializable {

    public static final String DEFAULT_DETAIL = "Message detail";
    public static final String DEFAULT_SUMMARY = "Message summary";
    private static final long serialVersionUID = 8408544368608166106L;
    private static Logger logger;
    private Attributes attributesNotify;
    private Attributes attributesNotifyMessages;
    private Attributes attributesNotifyStackFirst;
    private Attributes attributesNotifyStackSecond;
    private Attributes attributesBean;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());
        attributesNotify = Attributes.getComponentAttributesFromFacesConfig(UINotify.class, getClass());
        attributesNotifyMessages = Attributes.getComponentAttributesFromFacesConfig(UINotifyMessages.class, getClass());
        attributesNotifyStackFirst = Attributes.getComponentAttributesFromFacesConfig(UINotifyStack.class, getClass());
        attributesNotifyStackSecond = Attributes.getComponentAttributesFromFacesConfig(UINotifyStack.class, getClass());
        attributesBean = Attributes.getEmptyAttributes(getClass());

        // FIXME these attributes should be in the list after fixing RF-12144
        attributesNotify.setAttribute("showHistory", false);
        attributesNotify.get("showHistory").setType(Boolean.class);

        attributesNotify.setAttribute("delay", null);
        attributesNotify.setAttribute("detail", DEFAULT_DETAIL);
        attributesNotify.setAttribute("rendered", true);
        attributesNotify.setAttribute("showCloseButton", true);
        attributesNotify.setAttribute("stayTime", 100000);
        attributesNotify.setAttribute("summary", DEFAULT_SUMMARY);
        attributesNotify.remove("stack");

        // FIXME these attributes should be in the list after fixing RF-12144
        attributesNotifyMessages.setAttribute("showHistory", false);
        attributesNotifyMessages.get("showHistory").setType(Boolean.class);
        attributesNotifyMessages.setAttribute("onclick", null);
        attributesNotifyMessages.setAttribute("ondblclick", null);
        attributesNotifyMessages.setAttribute("onkeydown", null);
        attributesNotifyMessages.setAttribute("onkeypress", null);
        attributesNotifyMessages.setAttribute("onkeyup", null);
        attributesNotifyMessages.setAttribute("onmousedown", null);
        attributesNotifyMessages.setAttribute("onmousemove", null);
        attributesNotifyMessages.setAttribute("onmouseout", null);
        attributesNotifyMessages.setAttribute("onmouseover", null);
        attributesNotifyMessages.setAttribute("onmouseup", null);
        attributesNotifyMessages.setAttribute("style", null);
        attributesNotifyMessages.setAttribute("title", null);

        attributesNotifyMessages.setAttribute("animationSpeed", 100);
        attributesNotifyMessages.setAttribute("appearAnimation", "fade");
        attributesNotifyMessages.setAttribute("hideAnimation", "show");
        attributesNotifyMessages.setAttribute("showCloseButton", true);
        attributesNotifyMessages.setAttribute("stayTime", 100000);
        attributesNotifyMessages.remove("stack");

        attributesNotifyMessages.setAttribute("delay", null);
        attributesNotifyMessages.setAttribute("interval", 800);
        attributesNotifyMessages.setAttribute("showSummary", true);
        attributesNotifyMessages.setAttribute("rendered", true);
        attributesNotifyMessages.remove("for");
        attributesNotifyMessages.remove("ajaxRendered");

        attributesNotifyStackFirst.setAttribute("direction", "vertical");
        attributesNotifyStackFirst.setAttribute("method", "first");
        attributesNotifyStackFirst.setAttribute("position", "topRight");
        attributesNotifyStackFirst.setAttribute("rendered", true);

        attributesNotifyStackSecond.setAttribute("direction", "vertical");
        attributesNotifyStackSecond.setAttribute("method", "first");
        attributesNotifyStackSecond.setAttribute("position", "bottomRight");
        attributesNotifyStackSecond.setAttribute("rendered", true);

        attributesBean.setAttribute("messageCount", "1");
        attributesBean.setAttribute("messageDetail", DEFAULT_DETAIL);
        attributesBean.setAttribute("messageText", DEFAULT_SUMMARY);
    }

    /**
     * Produces error message which is handled by notify:notifyMessages component.
     */
    public void produceError() {
        produceMessage(FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Produces fatal message which is handled by notify:notifyMessages component.
     */
    public void produceFatal() {
        produceMessage(FacesMessage.SEVERITY_FATAL);
    }

    /**
     * Produces info message which is handled by notify:notifyMessages component.
     */
    public void produceInfo() {
        produceMessage(FacesMessage.SEVERITY_INFO);
    }

    /**
     * Produces warn message which is handled by notify:notifyMessages component.
     */
    public void produceWarn() {
        produceMessage(FacesMessage.SEVERITY_WARN);
    }

    /**
     * General attributes for this bean
     */
    public Attributes getAttributesBean() {
        return attributesBean;
    }

    /**
     * Attributes for notify:notify component
     *
     * @return A map containing all attributes of tested component. Name of the component is key in the map.
     */
    public Attributes getAttributesNotify() {
        return attributesNotify;
    }

    /**
     * Attributes for notify:notifyMessages component
     *
     * @return A map containing all attributes of tested component. Name of the component is key in the map.
     */
    public Attributes getAttributesNotifyMessages() {
        return attributesNotifyMessages;
    }

    /**
     * Attributes for notify:notifyStack (1) component
     *
     * @return A map containing all attributes of tested component. Name of the component is key in the map.
     */
    public Attributes getAttributesNotifyStackFirst() {
        return attributesNotifyStackFirst;
    }

    /**
     * Attributes for notify:notifyStack (2) component
     *
     * @return A map containing all attributes of tested component. Name of the component is key in the map.
     */
    public Attributes getAttributesNotifyStackSecond() {
        return attributesNotifyStackSecond;
    }
    /**
     * General attributes for this bean
     *
     * @param attributesBean
     *            map containing all attributes of tested component. Name of the component is key in the map.
     */
    public void setAttributesBean(Attributes attributesBean) {
        this.attributesBean = attributesBean;
    }

    /**
     * Attributes for notify:notify component
     *
     * @param attributesNotify
     *            map containing all attributes of tested component. Name of the component is key in the map.
     */
    public void setAttributesNotify(Attributes attributesNotify) {
        this.attributesNotify = attributesNotify;
    }

    /**
     * Attributes for notify:notifyMessages component
     *
     * @param attributesNotifyMessages
     *            map containing all attributes of tested component. Name of the component is key in the map.
     */
    public void setAttributesNotifyMessages(Attributes attributesNotifyMessages) {
        this.attributesNotifyMessages = attributesNotifyMessages;
    }

    /**
     * Attributes for notify:notifyStack (1) component
     *
     * @param attributesNotifyStack
     *            map containing all attributes of tested component. Name of the component is key in the map.
     */
    public void setAttributesNotifyStackFirst(Attributes attributesNotifyStack) {
        this.attributesNotifyStackFirst = attributesNotifyStack;
    }

    /**
     * Attributes for notify:notifyStack (2) component
     *
     * @param attributesNotifyStack
     *            map containing all attributes of tested component. Name of the component is key in the map.
     */
    public void setAttributesNotifyStackSecond(Attributes attributesNotifyStack) {
        this.attributesNotifyStackSecond = attributesNotifyStack;
    }

    /**
     * Produces a message which is handled by notify:notifyMessages component.
     *
     * @param severity the severity
     */
    private void produceMessage(FacesMessage.Severity severity) {
        int messageCount = Integer.valueOf((String)attributesBean.get("messageCount").getValue());
        String text = (String) attributesBean.get("messageText").getValue();
        String detail = (String) attributesBean.get("messageDetail").getValue();
        for(int i=0; i<messageCount; i++) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, text, detail));
        }
    }

    /**
     * It returns attributes shared by AbstractNotify and AbstractNotifyMessages (NotifyAttributes)
     *
     * @return map containing all attributes of tested component. Name of the component is key in the map.
     */
    private Attributes getGeneralAttributes() {
        Attributes attributes = Attributes.getAttributesFromClass(NotifyAttributes.class, getClass());
        attributes.setAttribute("animationSpeed", 100);
        attributes.setAttribute("appearAnimation", "fade");
        attributes.setAttribute("hideAnimation", "show");
        attributes.setAttribute("showCloseButton", true);
        attributes.setAttribute("stayTime", 100000);
        attributes.remove("stack");
        return attributes;
    }
}
