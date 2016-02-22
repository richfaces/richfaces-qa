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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;


/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "rf14225")
@SessionScoped
public class RF14225 {

    private String value;
    private List<SelectItem> options = null;

    @PostConstruct
    public void init() {
        options = new ArrayList<SelectItem>();
        options.add(new SelectItem("value1", "label1"));
        options.add(new SelectItem("value2", "label1"));
        options.add(new SelectItem("value3", "label1"));
        options.add(new SelectItem("value4", "label2"));
        options.add(new SelectItem("value5", "label2"));
        options.add(new SelectItem("value6", "label2"));
        options.add(new SelectItem("value7", "label1"));
    }

    public List<SelectItem> getOptions() {
        return options;
    }

    public void setOptions(List<SelectItem> options) {
        this.options = options;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
