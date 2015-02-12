/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Named("rf13929")
@SessionScoped
public class RF13929 implements Serializable {

    public static final String DEFAULT_DATA = "waiting for data";
    private static final Logger LOGGER = LoggerFactory.getLogger(RF13929.class);
    private static final String PUSH_TOPICS_CONTEXT_ADDRESS_1 = "topic1@tcSampleAddress1";
    private static final String PUSH_TOPICS_CONTEXT_ADDRESS_2 = "topic2@tcSampleAddress1";
    private static final String PUSH_TOPICS_CONTEXT_ADDRESS_3 = "tcSampleAddress1";
    private static final long serialVersionUID = 1L;
    private transient TopicsContext topicsContext;

    private int update1;
    private int update2;
    private int update3;

    public String getDefaultData() {
        return DEFAULT_DATA;
    }

    private TopicsContext getTopicsContext() {
        if (topicsContext == null) {
            topicsContext = TopicsContext.lookup();
        }
        return topicsContext;
    }

    public String getTopicsContextAddress1() {
        return PUSH_TOPICS_CONTEXT_ADDRESS_1;

    }

    public String getTopicsContextAddress2() {
        return PUSH_TOPICS_CONTEXT_ADDRESS_2;
    }

    public String getTopicsContextAddress3() {
        return PUSH_TOPICS_CONTEXT_ADDRESS_3;
    }

    @PostConstruct
    public void init() {
        update1 = 0;
        update2 = 0;
        update3 = 0;
    }

    public void pushWithTopicsContext1() throws MessageException {
        TopicKey topicKey = new TopicKey(getTopicsContextAddress1());
        getTopicsContext().publish(topicKey, MessageFormat.format("data from subtopic 1 #{0}", ++update1));
        LOGGER.debug("push event 1");
    }

    public void pushWithTopicsContext2() throws MessageException {
        TopicKey topicKey = new TopicKey(getTopicsContextAddress2());
        getTopicsContext().publish(topicKey, MessageFormat.format("data from subtopic 2 #{0}", ++update2));
        LOGGER.debug("push event 2");
    }

    public void pushWithTopicsContext3() throws MessageException {
        TopicKey topicKey = new TopicKey(getTopicsContextAddress3());
        getTopicsContext().publish(topicKey, MessageFormat.format("data from topic #{0}", ++update3));
        LOGGER.debug("push event 3");
    }
}
