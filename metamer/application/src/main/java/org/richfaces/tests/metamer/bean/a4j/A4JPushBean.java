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
package org.richfaces.tests.metamer.bean.a4j;

import static org.richfaces.demo.push.TopicsContextMessageProducer.PUSH_TOPICS_CONTEXT_TOPIC;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.cdi.push.Push;
import org.richfaces.component.UIPush;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for a4j:push.
 *
 * @author Nick Belaevski, <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 23169 $
 */
@Named("a4jPushBean")
@SessionScoped
public class A4JPushBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DATE_PATTERN = "'day:' d', month:' M', time:' HH:mm:ss.SSS";
    public static final String METAMER_SUBTOPIC = "xxx";
    public static final String METAMER_TOPIC_CDI = "topic2";
    private static final Logger LOGGER = LoggerFactory.getLogger(A4JPushBean.class);
    private transient TopicsContext topicsContext;
    private Attributes attributes;
    private String username;
    private String message;

    private static final String CDI_ADDRESS_1 = "cdiSampleAddress1";
    private static final String CDI_ADDRESS_2 = "cdiSampleAddress2";
    private static final String PUSH_TOPICS_CONTEXT_ADDRESS_1 = "tcSampleAddress1";
    private static final String PUSH_TOPICS_CONTEXT_ADDRESS_2 = "tcSampleAddress2";

    @Inject
    @Push(topic = CDI_ADDRESS_1)
    private Event<String> messageProducerForAddress1;
    @Inject
    @Push(topic = CDI_ADDRESS_2)
    private Event<String> messageProducerForAddress2;

    public String getCDIAdress1() {
        return CDI_ADDRESS_1;
    }

    public String getCDIAdress2() {
        return CDI_ADDRESS_2;
    }

    public String getTopicsContextAdress1() {
        return PUSH_TOPICS_CONTEXT_ADDRESS_1;

    }

    public String getTopicsContextAdress2() {
        return PUSH_TOPICS_CONTEXT_ADDRESS_2;
    }

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

    public void pushWithCDI1() {
        messageProducerForAddress1.fire(new DateTime().toString(DATE_PATTERN));
        LOGGER.debug("cdi push event 1");
    }

    public void pushWithCDI2() {
        messageProducerForAddress2.fire(new DateTime().toString(DATE_PATTERN));
        LOGGER.debug("cdi push event 2");
    }

    /**
     * Trigger to start push topic
     * @throws MessageException
     */
    public void pushWithTopicsContext1() throws MessageException {
        TopicKey topicKey = new TopicKey(getTopicsContextAdress1());
        getTopicsContext().publish(topicKey, new DateTime().toString(DATE_PATTERN));
        LOGGER.debug("push event 1");
    }

    /**
     * Trigger to start push with another topic
     * @throws MessageException
     */
    public void pushWithTopicsContext2() throws MessageException {
        TopicKey topicKey = new TopicKey(getTopicsContextAdress2());
        getTopicsContext().publish(topicKey, new DateTime().toString(DATE_PATTERN));
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
            Message msg = new Message(message, username, new DateTime().toString(DATE_PATTERN));
            LOGGER.debug("sending message \"" + message + "\" by user " + username);
            getTopicsContext().publish(new TopicKey(PUSH_TOPICS_CONTEXT_TOPIC, METAMER_SUBTOPIC), msg);
        } catch (MessageException messageException) {
            LOGGER.error("Could not send message \"" + message + "\" by user " + username + ".", messageException);
        }
    }
}
