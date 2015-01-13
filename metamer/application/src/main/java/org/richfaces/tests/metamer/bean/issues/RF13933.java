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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.google.common.collect.Lists;

@ManagedBean(name = "rf13933")
@SessionScoped
public class RF13933 implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<List<String>> lists;

    public List<List<String>> getLists() {
        return lists;
    }

    @PostConstruct
    public void init() {
        lists = Lists.newArrayList();
        lists.add(Lists.newArrayList("firstA", "secondA", "thirdA"));
        lists.add(Lists.newArrayList("firstB", "secondB", "thirdB"));
        lists.add(Lists.newArrayList("firstC", "secondC", "thirdC"));
    }

    public void setLists(List<List<String>> lists) {
        this.lists = lists;
    }

}
