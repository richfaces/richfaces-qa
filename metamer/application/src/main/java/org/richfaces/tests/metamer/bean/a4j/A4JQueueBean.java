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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.UIQueue;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for a4j:queue.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "a4jQueueBean")
@ViewScoped
public class A4JQueueBean implements Serializable {

    private static final long serialVersionUID = 486866943531809L;
    private static Logger logger;
    private Attributes attributes;
    private String text;
    private A4JQueueBean globalQueue;
    private A4JQueueBean formQueue1;
    private A4JQueueBean formQueue2;
    private boolean introduceDelay;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        // initialize attributes
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIQueue.class, getClass());
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("requestDelay", 750);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public A4JQueueBean getGlobalQueue() {
        if (globalQueue == null) {
            globalQueue = new A4JQueueBean();
            globalQueue.init();
            globalQueue.attributes.setAttribute("requestDelay", 10000);
        }
        return globalQueue;
    }

    public A4JQueueBean getFormQueue1() {
        if (formQueue1 == null) {
            formQueue1 = new A4JQueueBean();
            formQueue1.init();
            formQueue1.attributes.setAttribute("requestDelay", 500);
        }
        return formQueue1;
    }

    public A4JQueueBean getFormQueue2() {
        if (formQueue2 == null) {
            formQueue2 = new A4JQueueBean();
            formQueue2.init();
            formQueue2.attributes.setAttribute("requestDelay", 2000);
        }
        return formQueue2;
    }

    public A4JQueueBean[] getFormQueues() {
        return new A4JQueueBean[]{getFormQueue1(), getFormQueue2()};
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (introduceDelay) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        this.text = text;
    }

    public boolean isIntroduceDelay() {
        return introduceDelay;
    }

    public void setIntroduceDelay(boolean introduceDelay) {
        this.introduceDelay = introduceDelay;
    }
}
