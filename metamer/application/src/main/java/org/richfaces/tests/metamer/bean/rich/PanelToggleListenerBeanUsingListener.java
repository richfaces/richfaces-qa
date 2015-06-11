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
package org.richfaces.tests.metamer.bean.rich;

import static java.text.MessageFormat.format;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.event.PanelToggleEvent;
import org.richfaces.event.PanelToggleListener;
import org.richfaces.tests.metamer.bean.RichBean;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean
@SessionScoped
public class PanelToggleListenerBeanUsingListener implements PanelToggleListener {

    public static final String COLLAPSED = "collapsed";
    public static final String EXPANDED = "expanded";
    public static final String MSG_TEMPLATE = "* panelToggleListener using @listener invoked, panel {0}";
    private static final long serialVersionUID = 1L;

    @Override
    public void processPanelToggle(PanelToggleEvent event) {
        RichBean.logToPage(format(MSG_TEMPLATE, event.getExpanded() ? EXPANDED : COLLAPSED));
    }
}
