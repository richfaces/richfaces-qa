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
package org.richfaces.tests.metamer.bean;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.richfaces.tests.metamer.model.Capital;
import org.richfaces.tests.metamer.model.Employee;
import org.richfaces.tests.metamer.model.tree.CompactDiscXmlDescriptor;

/**
 * Application scoped managed bean holding models usable e.g. in iteration components.
 *
 * @author Exadel
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22372 $
 */
@ManagedBean
@ApplicationScoped
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Capital> capitalsList;
    private List<Employee> employeesList;
    private Set<String> jobTitles;
    private List<SelectItem> jobTitlesSelectItems;
    private List<CompactDiscXmlDescriptor> compactDiscList;
    private List<String> simple = new ArrayList<String>();

    @PostConstruct
    public void init() {
        simple.add("row 1");
        simple.add("row 2");
        simple.add("row 3");
        simple.add("row 4");
    }

    /**
     * Model containing US states, their capitals and timezones.
     *
     * @return list of US states and their capitals
     */
    public synchronized List<Capital> getCapitals() {
        if (capitalsList == null) {
            capitalsList = unmarshallCapitals();
        }

        return capitalsList;
    }

    /**
     * Model containing employees. Can be used to test various components inside iteration components.
     *
     * @return list of employees
     */
    public synchronized List<Employee> getEmployees() {
        if (employeesList == null) {
            employeesList = unmarshallEmployees();
        }

        return employeesList;
    }

    /**
     * Model containing compact discs. Suitable to be used in Tree-structured components.
     *
     * @return list of compact discs
     */
    public synchronized List<CompactDiscXmlDescriptor> getCompactDiscs() {
        if (compactDiscList == null) {
            compactDiscList = unmarshallCompactDiscs();
        }

        return compactDiscList;
    }

    /**
     * Unmarshalls the list of capitals
     *
     * @return the list of capitals
     * @throws JAXBException
     *             if any unexpected errors occurs during unmarshalling
     */
    public static final List<Capital> unmarshallCapitals() {
        try {
            return unmarshall(CapitalsHolder.class, "org/richfaces/tests/metamer/model/capitals.xml");
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final List<Employee> unmarshallEmployees() {
        try {
            return unmarshall(EmployeesHolder.class, "org/richfaces/tests/metamer/model/employees.xml");
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final List<CompactDiscXmlDescriptor> unmarshallCompactDiscs() {
        try {
            return unmarshall(CompactDiscsHolder.class, "org/richfaces/tests/metamer/model/compact-discs.xml");
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    static <R, T extends ListHolder<R>> List<R> unmarshall(Class<T> rootElementType, String resourceURL)
        throws JAXBException {
        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        URL resource = ccl.getResource(resourceURL);
        JAXBContext context = JAXBContext.newInstance(rootElementType);
        T holder = (T) context.createUnmarshaller().unmarshal(resource);
        return holder.getList();
    }

    private interface ListHolder<T> {
        List<T> getList();

        void setList(List<T> list);
    }

    @XmlRootElement(name = "capitals")
    private static final class CapitalsHolder implements ListHolder<Capital> {

        private List<Capital> list;

        @XmlElement(name = "capital")
        public List<Capital> getList() {
            return list;
        }

        public void setList(List<Capital> list) {
            this.list = list;
        }
    }

    @XmlRootElement(name = "employees")
    private static final class EmployeesHolder implements ListHolder<Employee> {

        private List<Employee> list;

        @XmlElement(name = "employee")
        public List<Employee> getList() {
            return list;
        }

        public void setList(List<Employee> list) {
            this.list = list;
        }
    }

    @XmlRootElement(name = "CATALOG")
    private static final class CompactDiscsHolder implements ListHolder<CompactDiscXmlDescriptor> {

        private List<CompactDiscXmlDescriptor> list;

        @XmlElement(name = "CD")
        public List<CompactDiscXmlDescriptor> getList() {
            return list;
        }

        public void setList(List<CompactDiscXmlDescriptor> list) {
            this.list = list;
        }
    }

    /**
     * Model containing various job titles, e.g. CEO, President, Director.
     *
     * @return set of job titles
     */
    public synchronized Set<String> getJobTitles() {
        if (jobTitles == null) {
            jobTitles = new HashSet<String>();
            for (Employee e : getEmployees()) {
                jobTitles.add(e.getTitle());
            }
        }

        return jobTitles;
    }

    /**
     * Model containing select items with various job titles.
     *
     * @return set of job titles
     */
    public synchronized List<SelectItem> getJobTitlesSelectItems() {
        if (jobTitlesSelectItems == null) {
            jobTitlesSelectItems = new ArrayList<SelectItem>();

            for (String title : getJobTitles()) {
                jobTitlesSelectItems.add(new SelectItem(title, title));
            }
        }

        return jobTitlesSelectItems;
    }

    public List<String> getSimple() {
        return simple;
    }
}
