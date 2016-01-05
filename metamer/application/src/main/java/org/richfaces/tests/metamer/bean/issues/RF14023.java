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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.richfaces.component.AbstractExtendedDataTable;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.model.Capital;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "rf14023")
@SessionScoped
public class RF14023 implements Serializable {

    private static final long serialVersionUID = 1L;
    private String currentItem;
    private List<Capital> items;
    private Collection<Object> selection;
    private List<Capital> selectionItems;

    public String getCurrentItem() {
        return currentItem;
    }

    public List<Capital> getItems() {
        return items;
    }

    public Collection<Object> getSelection() {
        return selection;
    }

    public List<Capital> getSelectionItems() {
        return selectionItems;
    }

    @PostConstruct
    public void init() {
        items = Model.unmarshallCapitals().subList(0, 14);
        selectionItems = Lists.newArrayList();
    }

    public void selectionListener(AjaxBehaviorEvent event) {
        AbstractExtendedDataTable dataTable = (AbstractExtendedDataTable) event.getComponent();
        Object originalKey = dataTable.getRowKey();
        selectionItems.clear();
        for (Object selectionKey : selection) {
            dataTable.setRowKey(selectionKey);
            if (dataTable.isRowAvailable()) {
                selectionItems.add((Capital) dataTable.getRowData());
            }
        }
        dataTable.setRowKey(originalKey);
    }

    public void setCurrentItem(String currentItem) {
        this.currentItem = currentItem;
    }

    public void setItems(List<Capital> items) {
        this.items = items;
    }

    public void setSelection(Collection<Object> selection) {
        this.selection = selection;
    }

    public void setSelectionItems(List<Capital> selectionItems) {
        this.selectionItems = selectionItems;
    }
}
