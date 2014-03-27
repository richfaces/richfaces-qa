/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2014, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.demo.push;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Starts the thread with TopicsContext message producer.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class TopicsContextMessageProducerInitializer implements SystemEventListener, ServletContextListener {

    private Thread messageProducerThread;
    private MessageProducer messageProducer;
    private MessageProducerRunnable messageProducerRunnable;
    private boolean correctlyInitialized = false;

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.SystemEventListener#processEvent(javax.faces.event. SystemEvent)
     */
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (isCapabilityEnabled()) {
            if (event instanceof PostConstructApplicationEvent) {
                Application application = FacesContext.getCurrentInstance().getApplication();
                application.subscribeToEvent(PreDestroyApplicationEvent.class, this);

                try {
                    initializeCapability();
                    correctlyInitialized = true;
                } catch (Exception e) {
                    throw new RuntimeException("Capability " + this.getClass().getName()
                        + " was not correctly initialized", e);
                }
            } else {
                try {
                    finalizeCapability();
                } catch (Exception e) {
                    throw new RuntimeException("Capability " + this.getClass().getName()
                        + " was not correctly finalized", e);
                }
            }
        }
    }

    public void contextInitialized(ServletContextEvent sce) {
        try {
            initializeCapability();
            correctlyInitialized = true;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        try {
            finalizeCapability();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns true if initialization method has been processed without errors.
     *
     * @return true if initialization method has been processed without errors.
     */
    protected boolean isCorrentlyInitialized() {
        return correctlyInitialized;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.SystemEventListener#isListenerForSource(java.lang.Object )
     */
    public boolean isListenerForSource(Object source) {
        return true;
    }

    /**
     * Returns true when capability should be initialized.
     *
     * @return true when capability should be initialized.
     */
    public boolean isCapabilityEnabled() {
        return true;
    }

    /**
     * Initializes provided capability.
     *
     * @throws Exception
     */
    public void initializeCapability() throws Exception {
        messageProducer = createMessageProducer();
        messageProducerRunnable = new MessageProducerRunnable(messageProducer);
        messageProducerThread = new Thread(messageProducerRunnable, "MessageProducerThread");
        messageProducerThread.setDaemon(false);
        messageProducerThread.start();
    }

    /**
     * Finalizes provided capability and frees allocated resources.
     *
     * @throws Exception
     */
    public void finalizeCapability() throws Exception {
        if (messageProducer != null) {
            messageProducer.finalizeProducer();
        }
        if (messageProducerRunnable != null) {
            messageProducerRunnable.stop();
        }
        if (messageProducerThread != null) {
            messageProducerThread.interrupt();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.AbstractMessageProducerInitializer#createMessageProducer()
     */
    public MessageProducer createMessageProducer() {
        return new TopicsContextMessageProducer();
    }
}
