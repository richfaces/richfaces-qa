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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.richfaces.component.UICollapsibleSubTableToggleControl;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.model.Employee;
import org.richfaces.tests.metamer.model.Employee.Sex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:collapsibleSubTableToggler.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name="richSubTableTogglerBean")
@SessionScoped
public class RichCollapsibleSubTableTogglerBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private static Logger logger;
    private Attributes attributes;
    @ManagedProperty("#{model.employees}")
    private List<Employee> employees;
    private List<List<Employee>> lists;
    // true = model, false = empty table
    private boolean state;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UICollapsibleSubTableToggleControl.class, getClass());
        attributes.setAttribute("event", "click");
        attributes.setAttribute("rendered", true);
        // TODO these attributes have to be tested in another way
        attributes.remove("for");

        List<Employee> men = new ArrayList<Employee>();
        List<Employee> women = new ArrayList<Employee>();

        for (Employee e : employees) {
            if (e.getSex() == Sex.MALE) {
                men.add(e);
            } else {
                women.add(e);
            }
        }

        lists = new ArrayList<List<Employee>>();
        lists.add(men);
        lists.add(women);

        state = true;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<List<Employee>> getLists() {
        return lists;
    }

    public void setLists(List<List<Employee>> lists) {
        this.lists = lists;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

}
