/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import com.google.common.collect.Lists;

@ManagedBean(name = "rf13896")
@SessionScoped
public class RF13896 implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private List<String> names;

    public void addName(ActionEvent event) {
        getNames().add(getName());
        setName("");
    }

    public String getName() {
        return name;
    }

    public List<String> getNames() {
        return names;
    }

    @PostConstruct
    public void init() {
        names = Lists.newArrayList();
        setName("");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

}
