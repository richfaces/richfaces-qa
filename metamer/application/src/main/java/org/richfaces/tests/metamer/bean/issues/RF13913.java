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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "rf13913")
@SessionScoped
public class RF13913 implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean panel1Expanded;
    private boolean panel2Expanded;
    private boolean panel3Expanded;
    private boolean panel4Expanded;

    public void collapseAll() {
        panel1Expanded = false;
        panel2Expanded = false;
        panel3Expanded = false;
        panel4Expanded = false;
    }

    public void expandAll() {
        panel1Expanded = true;
        panel2Expanded = true;
        panel3Expanded = true;
        panel4Expanded = true;
    }

    @PostConstruct
    public void init() {
        expandAll();
    }

    public boolean isPanel1Expanded() {
        return panel1Expanded;
    }

    public boolean isPanel2Expanded() {
        return panel2Expanded;
    }

    public boolean isPanel3Expanded() {
        return panel3Expanded;
    }

    public boolean isPanel4Expanded() {
        return panel4Expanded;
    }

    public void setPanel1Expanded(boolean panel1Expanded) {
        this.panel1Expanded = panel1Expanded;
    }

    public void setPanel2Expanded(boolean panel2Expanded) {
        this.panel2Expanded = panel2Expanded;
    }

    public void setPanel3Expanded(boolean panel3Expanded) {
        this.panel3Expanded = panel3Expanded;
    }

    public void setPanel4Expanded(boolean panel4Expanded) {
        this.panel4Expanded = panel4Expanded;
    }
}
