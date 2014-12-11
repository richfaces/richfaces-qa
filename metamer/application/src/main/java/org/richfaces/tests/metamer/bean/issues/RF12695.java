/*******************************************************************************
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
 *******************************************************************************/
package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.richfaces.tests.metamer.bean.abstractions.SelectValidationBean;

@ManagedBean(name = "rf12695")
@SessionScoped
public class RF12695 extends SelectValidationBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<String> strings = null;
    private List<SelectItem> selectOptions = null;

    @PostConstruct
    public void init() {
        strings = new ArrayList<String>();
        selectOptions = new ArrayList<SelectItem>();
        // generate huge amount of items
        for (int i = 0; i < 800; i++) {
            strings.add(String.valueOf(i));
        }
        // add those items into select
        for (String string : strings) {
            selectOptions.add(new SelectItem(string, string));
        }
    }

    public List<SelectItem> getOptions() {
        return selectOptions;
    }

    public void setOptions(List<SelectItem> capitalsOptions) {
        this.selectOptions = capitalsOptions;
    }
}
