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
package org.richfaces.tests.metamer.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.google.common.collect.Lists;

/**
 * Bean used in tests for @rows function.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean
@SessionScoped
public class RowsBean {

    private List<Integer> values;
    private List<Integer> firstAndLastItemIndexes;
    private List<Integer> exceptFirstAndLastItemIndexes;
    private List<Integer> subTables;

    @PostConstruct
    public void init() {
        resetValues();
        firstAndLastItemIndexes = Lists.newArrayList(0, values.size() - 1);
        exceptFirstAndLastItemIndexes = Lists.newArrayList();
        for (int i = 1; i < values.size() - 1; i++) {
            exceptFirstAndLastItemIndexes.add(i);
        }
        subTables = Lists.newArrayList(0);
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public void incrementAll() {
        List<Integer> incrementedVals = Lists.newArrayList();
        for (Integer integer : values) {
            incrementedVals.add(integer + 1);
        }
        setValues(incrementedVals);
    }

    public List<Integer> getFirstAndLastItemIndexes() {
        return firstAndLastItemIndexes;
    }

    public void setFirstAndLastItemIndexes(List<Integer> firstAndLastItemIndexes) {
        this.firstAndLastItemIndexes = firstAndLastItemIndexes;
    }

    public List<Integer> getExceptFirstAndLastItemIndexes() {
        return exceptFirstAndLastItemIndexes;
    }

    public void setExceptFirstAndLastItemIndexes(List<Integer> exceptFirstAndLastItemIndexes) {
        this.exceptFirstAndLastItemIndexes = exceptFirstAndLastItemIndexes;
    }

    public List<Integer> getSubTables() {
        return subTables;
    }

    public void setSubTables(List<Integer> subTables) {
        this.subTables = subTables;
    }

    public final void resetValues() {
        values = Lists.newArrayList(0, 0, 0, 0);
    }
}
