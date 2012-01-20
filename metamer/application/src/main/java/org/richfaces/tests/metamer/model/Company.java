/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Java bean representing a company.
 *
 * @author Exadel, <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version$Revision: 23001$
 */
@XmlRootElement(name = "company")
public class Company implements Labeled, Serializable {

    private static final long serialVersionUID = -1L;
    private String name;
    private String state;
    private String phone;

    /**
     * Empty no-arg constructor needed for JAXB.
     */
    public Company() {
    }

    public Company(String name) {
        this.name = name;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @XmlElement
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Company)) {
            return false;
        }
        Company company = (Company) obj;
        return name.equals(company.getName());
    }

    @Override
    public int hashCode() {
        return 31 + 17 * name.hashCode();
    }

    public String getLabel() {
        return name;
    }

    @Override
    public String toString() {
        return "Company [name=" + name + "]";
    }
}
