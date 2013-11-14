/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
import java.util.Comparator;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.UIColumn;
import org.richfaces.model.Filter;
import org.richfaces.model.SortOrder;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.model.Capital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:column
 *
 * @author <a href="mailto:pjha@redhat.com">Prabhat Jha</a>, <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "richColumnBean")
@ViewScoped
public class RichColumnBean implements Serializable {

    public static final Comparator<Capital> STATE_NAME_LENGTH_COMPARATOR = new Comparator<Capital>() {
        @Override
        public int compare(Capital o1, Capital o2) {
            return o1.getState().length() - o2.getState().length();
        }
    };

    private static final long serialVersionUID = -1L;
    private static Logger logger;

    private Attributes attributes;
    private String stateNameToFilter;
    private SortOrder sortOrder = SortOrder.ascending;

    private Filter<Capital> stateFilter = new StateFilter();

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIColumn.class, getClass());
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("colspan", 2);
        attributes.setAttribute("rowspan", 2);
        attributes.setAttribute("breakRowBefore", true);

        // attributes which needs to be tested another way
        attributes.remove("filter");
        attributes.remove("filterValue");
        attributes.remove("filterExpression");
        attributes.remove("comparator");
        attributes.remove("sortBy");
        attributes.remove("sortOrder");
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Comparator<Capital> getStateNameLengthComparator() {
        return STATE_NAME_LENGTH_COMPARATOR;
    }

    public Filter<Capital> getStateNameFilter() {
        return stateFilter;
    }

    public String getStateNameToFilter() {
        return stateNameToFilter;
    }

    public void setStateNameToFilter(String stateNameToFilter) {
        this.stateNameToFilter = stateNameToFilter;
    }

    public SortOrder[] getSortOrders() {
        return SortOrder.values();
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    private class StateFilter implements Filter<Capital>, Serializable {
        private static final long serialVersionUID = 1L;

        public boolean accept(Capital c) {
            return c.getState().toLowerCase()
                .contains(stateNameToFilter == null ? "" : stateNameToFilter.toLowerCase());
        }
    }
}
