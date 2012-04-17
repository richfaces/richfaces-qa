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

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.validation.constraints.Size;

/**
 * Helper bean for testing JSR-303.
 *
 * This bean doesn't extend the {@link org.richfaces.tests.metamer.validation.Validable}
 * because of the <a href="https://issues.jboss.org/browse/RF-11710">RF-11710</a>.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision: 23064 $
 *
 */
@ManagedBean
public class SizeBean {

    private List<String> value;

    public SizeBean() {
        value = new ArrayList<String>();
        value.add("A");
        value.add("B");
    }

    @Size(min = 2, max = 4)
    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> intValue) {
        this.value = intValue;
    }

    public String getDescription() {
        return "Selection size, from 2 to 4";
    }

    public String getLabel() {
        return "size";
    }
}
