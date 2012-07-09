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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.ManagedBean;

import org.richfaces.component.UIFileUpload;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.bean.RichBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:fileUpload.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22507 $
 */
@ManagedBean(name = "richFileUploadBeanCScoped")
@ConversationScoped
public class RichFileUploadBeanCScoped implements Serializable {
    
    // @Inject private Conversation conversation;

    private static final long serialVersionUID = -1L;
    private static Logger logger;
    private Attributes attributes;
    private List<UploadedFile> files;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());
        files = new ArrayList<UploadedFile>();

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIFileUpload.class, getClass());

        attributes.setAttribute("addLabel", "Add ...");
        attributes.setAttribute("clearAllLabel", "Clear All");
        attributes.setAttribute("clearLabel", "Clear");
        attributes.setAttribute("deleteLabel", "Delete");
        attributes.setAttribute("noDuplicate", false);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("uploadLabel", "Upload");

        // will be tested in another way
        attributes.remove("validator");
        attributes.remove("fileUploadListener");

        // this.conversation.begin();
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<UploadedFile> getItems() {
        return files;
    }

    public int getSize() {
        if (getItems().size() > 0) {
            return getItems().size();
        } else {
            return 0;
        }
    }

    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public void listener(FileUploadEvent event) {
        RichBean.logToPage("* upload listener");
        UploadedFile file = event.getUploadedFile();

        if (file != null) {
            files.add(file);
        }
    }

    public void listener2(FileUploadEvent event) {
        RichBean.logToPage("* upload listener2");
        UploadedFile file = event.getUploadedFile();

        if (file != null) {
            files.add(file);
        }
    }

    public String clearUploadedData() {
        files.clear();
        return null;
    }

    public boolean isRenderButton() {
        return files.size() > 0;
    }

    public void paint(OutputStream stream, Object object) throws IOException {
        stream.write(getItems().get((Integer) object).getData());
        stream.close();
    }
/*
    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
    */
}
