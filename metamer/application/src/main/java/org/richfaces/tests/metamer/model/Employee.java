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

package org.richfaces.tests.metamer.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Java bean representing an employee.
 *
 * @author Exadel, <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21299 $
 */
@XmlRootElement(name = "employeee")
public class Employee implements Serializable {

    public enum Sex {
        FEMALE, MALE
    }

    private static final long serialVersionUID = -1L;
    private static String[] phoneNames = {"Cell phone", "Work phone", "Home phone"};
    private String name;
    private String title;
    private List <Company> companies;
    private List<String[]> phones = new ArrayList<String[]>();
    private Date birthdate;
    private Color favoriteColor;
    private int numberOfKids;
    private boolean smoker;
    private Sex sex;
    private String email;

    /**
     * Empty no-arg constructor needed for JAXB.
     */
    public Employee() {
    }

    public Employee(String name, String title) {
        this.name = name;
        this.title = title;
    }

    @XmlElement
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElementWrapper(name = "companies")
    @XmlElement(name = "company")
    public List <Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public void setPhones(List<String[]> phones) {
        this.phones = phones;
    }

    @XmlElement
    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    @XmlJavaTypeAdapter(value = ColorAdapter.class)
    @XmlElement
    public Color getFavoriteColor() {
        return favoriteColor;
    }

    public void setFavoriteColor(Color favoriteColor) {
        this.favoriteColor = favoriteColor;
    }

    @XmlElement
    public int getNumberOfKids() {
        return numberOfKids;
    }

    public void setNumberOfKids(int numberOfKids) {
        this.numberOfKids = numberOfKids;
    }

    @XmlElementWrapper(name = "phones")
    @XmlElement(name = "phone")
    public List<String[]> getPhones() {
        return phones;
    }

    @XmlElement
    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @XmlElement
    public boolean isSmoker() {
        return smoker;
    }

    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }

    @XmlElement
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Employee)) {
            return false;
        }
        Employee employee = (Employee) obj;
        return name.equals(employee.getName())
            && title.equals(employee.getTitle())
            && (companies == null ? employee.getCompanies() == null : companies
                .equals(employee.getCompanies()));
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + name.hashCode();
        hash = hash * 17 + title.hashCode();
        hash = hash * 17 + (companies == null ? 0 : companies.hashCode());
        return hash;
    }

    @Override
    public String toString() {
        return "Employee [name=" + name + ", sex=" + sex + ", title=" + title + ", numberOfKids=" + numberOfKids + "]";
    }
}