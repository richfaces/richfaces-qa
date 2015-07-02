/*******************************************************************************
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
 *******************************************************************************/
package org.richfaces.tests.metamer.bean;

import java.io.Serializable;

import javax.faces.flow.FlowScoped;
import javax.inject.Named;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@Named("flowtest")
@FlowScoped(value = "flowtest")
public class FlowScopedBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String firstInput = "Empty";
    private String secondInput = "Empty";
    private String activeItem = "tab1";

    public String getFirstInput() {
        return firstInput;
    }

    public String getSecondInput() {
        return secondInput;
    }

    public String getReturnValue() {
        return "/components/richTabPanel/jsfFlowScoped.xhtml";
    }

    public void setFirstInput(String input) {
        this.firstInput = input;
    }

    public void setSecondInput(String input) {
        this.secondInput = input;
    }

    public String getActiveItem() {
        return activeItem;
    }

    public void setActiveItem(String tabName) {
        this.activeItem = tabName;
    }
}
