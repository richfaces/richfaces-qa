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
package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "rf12654")
@SessionScoped
public class RF12654 implements Serializable {

    private static final long serialVersionUID = -1L;

    private Integer[] integers;
    private Row[] rows;

    public void dummyAction() {
    }

    public Integer[] getIntegers() {
        return integers;
    }

    public Row[] getRows() {
        return rows;
    }

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        rows = new Row[]{ new Row(1), new Row(2) };
        integers = new Integer[]{ 1 };
    }

    public static class Row {

        private final int number;

        public Row(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }
}
