/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.validation.constraints.Future;

/**
 * Helper bean for testing JSR-303.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22492 $
 */
@ManagedBean
public class FutureBean extends Validable<Date> {

    public FutureBean() {
        value = new Date(System.currentTimeMillis() + 4 * 24 * 3600 * 1000);
    }

    @Future
    @Override
    public Date getValue() {
        return value;
    }

    @Override
    public void setValue(Date intValue) {
        this.value = intValue;
    }

    @Override
    public String getDescription() {
        return "Date, future";
    }

    @Override
    public String getLabel() {
        return "future";
    }
}
