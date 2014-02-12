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
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.ajax4jsf.model.DataComponentState;
import org.richfaces.component.UIDataTableBase;
import org.richfaces.component.UIExtendedDataTable;
import org.richfaces.model.Filter;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.ColumnSortingMap;
import org.richfaces.tests.metamer.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:extendedDataTable.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "richExtendedDataTableBean")
@ViewScoped
public class RichExtendedDataTableBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger logger;
    private Attributes attributes;
    private transient DataComponentState dataTableState;
    private Map<Object, Integer> stateMap = new HashMap<Object, Integer>();
    private int page = 1;
    // true = model, false = empty table
    private boolean state = true;
    // sorting
    private ColumnSortingMap sorting = new ColumnSortingMap() {

        private static final long serialVersionUID = 1L;

        protected UIDataTableBase getBinding() {
            return binding;
        }

        protected Attributes getAttributes() {
            return attributes;
        }
    };
    // filtering
    private Map<String, Object> filtering = new HashMap<String, Object>();
    // facets
    private Map<String, String> facets = new HashMap<String, String>();
    private transient UIExtendedDataTable binding;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIExtendedDataTable.class, getClass());

        attributes.setAttribute("rendered", true);
        attributes.setAttribute("rows", 30);
        attributes.setAttribute("styleClass", "extended-data-table");
        attributes.setAttribute("style", null);

        // setup types
        attributes.get("selection").setType(TreeSet.class);
        attributes.get("selection").setMemberType(Integer.class);

        // attributes which are already in page
        attributes.remove("value");
        attributes.remove("var");

        // TODO attributes which needs to be tested another way
        attributes.remove("filterVar");
        attributes.remove("iterationStatusVar");
        attributes.remove("rowKeyVar");
        attributes.remove("stateVar");

        // facets initial values
        facets.put("noData", "There is no data.");
        facets.put("header", "Header");
        facets.put("footer", "Footer");
        facets.put("columnStateHeader", "State Header");
        facets.put("columnStateFooter", "State Footer");
        facets.put("columnCapitalHeader", "Capital Header");
        facets.put("columnCapitalFooter", "Capital Footer");
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public UIExtendedDataTable getBinding() {
        return binding;
    }

    public void setBinding(UIExtendedDataTable binding) {
        this.binding = binding;
    }

    /**
     * Getter for page.
     *
     * @return page number that will be used by data scroller
     */
    public int getPage() {
        return page;
    }

    /**
     * Setter for page.
     *
     * @param page
     *            page number that will be used by data scroller
     */
    public void setPage(int page) {
        this.page = page;
    }

    public Map<Object, Integer> getStateMap() {
        return stateMap;
    }

    public void setStateMap(Map<Object, Integer> stateMap) {
        this.stateMap = stateMap;
    }

    public DataComponentState getDataTableState() {
        return dataTableState;
    }

    public void setDataTableState(DataComponentState dataTableState) {
        this.dataTableState = dataTableState;
    }

    /**
     * Getter for state.
     *
     * @return true if data should be displayed in table
     */
    public boolean isState() {
        return state;
    }

    /**
     * Setter for state.
     *
     * @param state
     *            true if data should be displayed in table
     */
    public void setState(boolean state) {
        this.state = state;
    }

    public Filter<?> getFilterSexImpl() {
        return new Filter<Employee>() {

            public boolean accept(Employee e) {
                String sex = (String) getFiltering().get("sex");
                if (sex == null || sex.length() == 0 || sex.equalsIgnoreCase("all")
                        || sex.equalsIgnoreCase(e.getSex().toString())) {
                    return true;
                }
                return false;
            }
        };
    }

    public Map<String, String> getFacets() {
        return facets;
    }

    public ColumnSortingMap getSorting() {
        return sorting;
    }

    public Map<String, Object> getFiltering() {
        return filtering;
    }
}
