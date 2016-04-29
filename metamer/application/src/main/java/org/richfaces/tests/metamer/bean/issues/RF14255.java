/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.application.push.SessionPreSubscriptionEvent;
import org.richfaces.application.push.SessionSubscriptionEvent;
import org.richfaces.application.push.SessionTopicListener2;
import org.richfaces.application.push.SessionUnsubscriptionEvent;
import org.richfaces.application.push.Topic;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "rf14255")
@SessionScoped
public class RF14255 implements Serializable {

    private static final String address = "subtopic@topic";
    private static final long serialVersionUID = -1L;
    private String presentedSubTopicName = "... not initialized ...";
    private String subTopicFromSessionPreSubscriptionEvent;

    public String getAddress() {
        return address;
    }

    public String getSubTopicNameFromSessionPreSubscriptionEvent() {
        return presentedSubTopicName;
    }

    @PostConstruct
    public void init() {
        Topic topic = TopicsContext.lookup().getOrCreateTopic(new TopicKey(getAddress()));
        topic.addTopicListener(new NotNullSubTopicKeyListener());
    }

    public void performCheck() {
        presentedSubTopicName = String.valueOf(subTopicFromSessionPreSubscriptionEvent);
    }

    private class NotNullSubTopicKeyListener implements SessionTopicListener2 {

        @Override
        public void processPreSubscriptionEvent(SessionPreSubscriptionEvent sessionPreSubscriptionEvent) {
            subTopicFromSessionPreSubscriptionEvent = sessionPreSubscriptionEvent.getTopicKey().getSubtopicName();
        }

        @Override
        public void processSubscriptionEvent(SessionSubscriptionEvent sessionSubscriptionEvent) {
        }

        @Override
        public void processUnsubscriptionEvent(SessionUnsubscriptionEvent sessionUnsubscriptionEvent) {
        }
    }
}
