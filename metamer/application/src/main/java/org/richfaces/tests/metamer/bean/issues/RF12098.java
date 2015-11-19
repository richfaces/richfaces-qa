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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "rf12098")
@SessionScoped
public class RF12098 implements Serializable {

    private static final long serialVersionUID = -1L;

    private List<SelectItem> selectItemsValue;
    private List<String> optionsWithComma;
    private List<SelectItem> selectItemsWithComma;

    @PostConstruct
    public void init() {
        selectItemsValue = new ArrayList<SelectItem>();
        optionsWithComma = Lists.newArrayList("one", "one,one", "one, two", "one ,three", "one , four", "two.one", "three one", "four,one,one", "four, two,one", "four, two, two");
        selectItemsWithComma = Lists.newArrayList();
        for (String option : optionsWithComma) {
            selectItemsWithComma.add(new SelectItem(option, option));
        }
    }

    public List<SelectItem> getSelectItemsValue() {
        return selectItemsValue;
    }

    public void setSelectItemsValue(List<SelectItem> selectItemsValue) {
        this.selectItemsValue = selectItemsValue;
    }

    public List<String> getOptionsWithComma() {
        return optionsWithComma;
    }

    public void setOptionsWithComma(List<String> optionsWithComma) {
        this.optionsWithComma = optionsWithComma;
    }

    public List<SelectItem> getSelectItemsWithComma() {
        return selectItemsWithComma;
    }

    public void setSelectItemsWithComma(List<SelectItem> selectItemsWithComma) {
        this.selectItemsWithComma = selectItemsWithComma;
    }
}
