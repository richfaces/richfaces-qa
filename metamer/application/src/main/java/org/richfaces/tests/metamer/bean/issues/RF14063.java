/*
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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.tests.metamer.bean.RichBean;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "rf14063")
@SessionScoped
public class RF14063 implements Serializable {

    private static final String REPEATED_TEXT = " green bottles hanging on the wall.</p>";

    private static final long serialVersionUID = 1L;
    private String editorText;

    private String createEditorText(int lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = lines; i >= 0; i--) {
            sb.append("<p>").append(i).append(REPEATED_TEXT);
        }
        return sb.toString();
    }

    public void fileuploadListener(FileUploadEvent event) {
        RichBean.logToPage("* upload listener");
    }

    public String getEditorText() {
        return editorText;
    }

    @PostConstruct
    public void init() {
        editorText = createEditorText(500);
    }

    public void setEditorText(String editorText) {
        this.editorText = editorText;
    }
}
