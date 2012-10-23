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
package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@ManagedBean(name = "rf12510")
// should not be view-scoped (see https://jira.jboss.org/browse/RF-9287)
@ViewScoped
public class RF12510 implements Serializable {

    private static final long serialVersionUID = 54887878L;
    private int counter = 0;
    private int year;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        year = 1001;
    }

    public String action() {
        return year + "";
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getYear() {
        counter++;
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
