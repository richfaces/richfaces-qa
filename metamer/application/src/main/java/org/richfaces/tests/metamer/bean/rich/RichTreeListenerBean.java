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
package org.richfaces.tests.metamer.bean.rich;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.richfaces.event.TreeSelectionChangeEvent;
import org.richfaces.event.TreeToggleEvent;
import org.richfaces.tests.metamer.bean.RichBean;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "richTreeListenerBean")
@RequestScoped
public class RichTreeListenerBean {

    private TreeSelectionChangeEvent treeSelectionChangeEvent;
    private TreeToggleEvent treeToggleEvent;

    public void processSelectionChange(TreeSelectionChangeEvent event) {
        this.treeSelectionChangeEvent = event;
        RichBean.logToPage("* selection change listener invoked");
    }

    public void processToggle(TreeToggleEvent event) {
        this.treeToggleEvent = event;
        RichBean.logToPage("* tree toggle listener invoked");
    }

    public void processNodeToggle(TreeToggleEvent event) {
        this.treeToggleEvent = event;
        RichBean.logToPage("* node toggle listener invoked");
    }

    public TreeSelectionChangeEvent getTreeSelectionChangeEvent() {
        return treeSelectionChangeEvent;
    }

    public TreeToggleEvent getTreeToggleEvent() {
        return treeToggleEvent;
    }
}
