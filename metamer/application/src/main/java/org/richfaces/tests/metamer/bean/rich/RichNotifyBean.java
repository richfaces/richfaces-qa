/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.bean.abstractions.MessageTestingBean;
import org.richfaces.ui.message.notify.UINotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "richNotifyBean")
@ViewScoped
public class RichNotifyBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger LOGGER = LoggerFactory.getLogger(RichNotifyBean.class);
    public static final String DEFAULT_DETAIL = "Message detail";
    public static final String DEFAULT_SUMMARY = "Message summary";
    private Attributes attributes;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        LOGGER.debug("initializing bean " + getClass().getName());
        attributes = Attributes.getComponentAttributesFromFacesConfig(UINotify.class, getClass());

        attributes.setAttribute("detail", DEFAULT_DETAIL);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("showCloseButton", true);
        attributes.setAttribute("stayTime", 100000);
        attributes.setAttribute("summary", DEFAULT_SUMMARY);
    }

    public void generateFacesMessagesWithSeverity(String severityOfMessageToBeCreated) {
        LOGGER.debug(" ### Just called generateFacesMessagesWithSeverity('" + severityOfMessageToBeCreated + "')");
        severityOfMessageToBeCreated = severityOfMessageToBeCreated.toUpperCase(Locale.ENGLISH);
        for (FacesMessage.Severity referenceSeverity : MessageTestingBean.MSGS_TYPES) {
            if (referenceSeverity.toString().startsWith(severityOfMessageToBeCreated)) {
                MessageTestingBean.generateFacesMessage(referenceSeverity, null);
            }
        }
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}
