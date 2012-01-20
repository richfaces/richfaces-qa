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
package org.richfaces.tests.metamer.validation;

import javax.faces.bean.ManagedBean;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Simple bean with property with more than one validation.
 * This is not very logic validation, but its useful for testing
 * rich:messages component - where need more validation messages per input
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version$Revision: 22492$
 */
@ManagedBean
public class MultipleValidationRulesBean extends Validable<Integer> {

    public MultipleValidationRulesBean(){
        value = 10;
    }

    @Digits(integer=2, fraction=0)
    @Min(5)
    @Max(150)
    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String getDescription() {
        return "Integer between 5 and 150, but less than 3 digits";
    }

    @Override
    public String getLabel() {
        return "Number size and value";
    }
}
