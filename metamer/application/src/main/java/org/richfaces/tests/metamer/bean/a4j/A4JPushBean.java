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
package org.richfaces.tests.metamer.bean.a4j;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;

import org.richfaces.application.push.TopicsContext;
import org.richfaces.cdi.push.Push;
import org.richfaces.component.UIPush;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.richfaces.demo.push.TopicsContextMessageProducer.PUSH_TOPICS_CONTEXT_TOPIC;

/**
 * Managed bean for a4j:push.
 *
 * @author Nick Belaevski, <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 23169 $
 */
@ManagedBean(name = "a4jPushBean")
@SessionScoped
public class A4JPushBean implements Serializable {

    private static final long serialVersionUID = 4810889475400649809L;
    public static final String DATE_PATTERN = "'day:' d', month:' M', time:' HH:mm:ss";
    public static final String METAMER_SUBTOPIC = "xxx";
    public static final String METAMER_TOPIC_CDI = "topic2";
    private static final Logger LOGGER = LoggerFactory.getLogger(A4JPushBean.class);
    private transient TopicsContext topicsContext;
    private Attributes attributes;
    private String username;
    private String message;

    @Inject
    @Push(topic = "cdiSampleAddress1")
    private Event<Date> messageProducerForAddress1;
    @Inject
    @Push(topic = "cdiSampleAddress2")
    private Event<Date> messageProducerForAddress2;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        LOGGER.debug("initializing bean " + getClass().getName());
        // set up messaging
        // TODO JJa 2011-12-07: make sure if initialization is required even use initialization mechanism from showcase
        topicsContext = TopicsContext.lookup();

        attributes = Attributes.getComponentAttributesFromClass(UIPush.class, getClass());
        attributes.setAttribute("onerror", "alert('Error: ' + event.rf.data)");
        attributes.setAttribute("rendered", true);
        attributes.remove("address");
        attributes.remove("ondataavailable");
    }

    /**
     * just new date instance (current date)
     * @return
     */
    public String getDate() {
        return new DateTime().toString(DATE_PATTERN);
    }

    public void pushCDI1() {
        messageProducerForAddress1.fire(new Date());
        LOGGER.debug("cdi push event 1");
    }

    public void pushCDI2() {
        messageProducerForAddress2.fire(new Date());
        LOGGER.debug("cdi push event 2");
    }

    /**
     * Trigger to start push topic
     * @throws MessageException
     */
    public void pushJMS1() throws MessageException {
        TopicKey topicKey = new TopicKey("jmsSampleAddress1");
        TopicsContext topicsContext = TopicsContext.lookup();

        topicsContext.publish(topicKey, new DateTime().toString(DATE_PATTERN));
        LOGGER.debug("push event 1");
    }

    /**
     * Trigger to start push with another topic
     * @throws MessageException
     */
    public void pushJMS2() throws MessageException {
        TopicKey topicKey = new TopicKey("jmsSampleAddress2");
        TopicsContext topicsContext = TopicsContext.lookup();

        topicsContext.publish(topicKey, new DateTime().toString(DATE_PATTERN));

        LOGGER.debug("push event 2");
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
            LOGGER.debug("sending message \"" + message + "\" by user " + username);
            Message msg = new Message(message, username, new Date().toString());
            getTopicsContext().publish(new TopicKey(PUSH_TOPICS_CONTEXT_TOPIC, METAMER_SUBTOPIC), msg);
        } catch (MessageException messageException) {
            LOGGER.error("Could not send message \"" + message + "\" by user " + username + ".", messageException);
        }
    }
}
