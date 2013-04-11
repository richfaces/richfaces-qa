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
package org.richfaces.tests.metamer.bean.abstractions;

import java.util.Date;

import org.richfaces.tests.metamer.Attributes;

public class DateInputValidationBeanImpl implements DateInputValidationBean {

    protected Attributes attributes;
    private Date futureValue = FUTURE_DATE_DEFAULT;
    private Date lastYearValue = LAST_YEAR_DATE_DEFAULT;
    private Date pastValue = PAST_DATE_DEFAULT;
    private Date requiredValue = REQUIRED_DATE_DEFAULT;

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public Date getFutureValue() {
        return futureValue;
    }

    @Override
    public Date getLastYearValue() {
        return lastYearValue;
    }

    @Override
    public Date getPastValue() {
        return pastValue;
    }

    @Override
    public Date getRequiredValue() {
        return requiredValue;
    }

    @Override
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public void setFutureValue(Date futureValue) {
        this.futureValue = futureValue;
    }

    @Override
    public void setLastYearValue(Date lastYearValue) {
        this.lastYearValue = lastYearValue;
    }

    @Override
    public void setPastValue(Date pastValue) {
        this.pastValue = pastValue;
    }

    @Override
    public void setRequiredValue(Date requiredValue) {
        this.requiredValue = requiredValue;
    }
}
