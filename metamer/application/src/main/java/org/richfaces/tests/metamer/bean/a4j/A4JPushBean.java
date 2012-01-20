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
package org.richfaces.tests.metamer.bean.a4j;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;

import org.richfaces.application.push.TopicsContext;
import org.richfaces.component.UIPush;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.richfaces.demo.push.TopicsContextMessageProducer.PUSH_TOPICS_CONTEXT_TOPIC;

/**
 * Managed bean for a4j:push.
 *
 * @author Nick Belaevski, <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version$Revision: 23060$
 */
@ManagedBean(name = "a4jPushBean")
@SessionScoped
public class A4JPushBean implements Serializable {

    private static final long serialVersionUID = 4810889475400649809L;
    public static final String METAMER_SUBTOPIC = "xxx";

    private static Logger logger;
    private transient TopicsContext topicsContext;
    private Attributes attributes;
    private String username;
    private String message;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        // set up messaging
        // TODO JJa 2011-12-07: make sure if initialization is required even use initialization mechanism from showcase

        topicsContext = TopicsContext.lookup();

        attributes = Attributes.getComponentAttributesFromClass(UIPush.class, getClass());
//        attributes.setAttribute("interval", 1000);
//        attributes.setAttribute("action", "increaseCounterAction");
//        attributes.setAttribute("actionListener", "increaseCounterActionListener");
//        attributes.setAttribute("rendered", true);
//        attributes.setAttribute("enabled", true);
//
//        // will be set on page and cannot be changed
//        attributes.remove("eventProducer");
    }

    /**
     * Getter for attributes.
     *
     * @return A map containing all attributes of tested component. Name of the component is key in the map.
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * Setter for attributes.
     *
     * @param attributes
     *            map containing all attributes of tested component. Name of the component is key in the map.
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTopicsContext(TopicsContext topicsContext) {
        this.topicsContext = topicsContext;
    }

    private TopicsContext getTopicsContext() {
        if (topicsContext == null) {
            topicsContext = TopicsContext.lookup();
        }
        return topicsContext;
    }

    public void send() {
        try {
            logger.info("sending message \"" + message + "\" by user " + username);
            Message msg = new Message(message, username, new Date().toString());
            getTopicsContext().publish(new TopicKey(PUSH_TOPICS_CONTEXT_TOPIC, METAMER_SUBTOPIC), msg);
        } catch (MessageException messageException) {
            logger.error("Could not send message \"" + message + "\" by user " + username + ".", messageException);
        }
    }
}
