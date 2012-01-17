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

package org.richfaces.tests.metamer.converter;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.richfaces.tests.metamer.Template;
import org.richfaces.tests.metamer.TemplatesList;

/**
 * Converter used for view parameter "template".
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22330 $
 */
@FacesConverter(value = "templatesListConverter")
public class TemplatesListConverter implements Converter {

    /**
     * {@inheritDoc}
     */
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            TemplatesList list = new TemplatesList();

            for (String template : value.split(",")) {
                list.add(Template.valueOf(template.toUpperCase()));
            }

            return list;
        } catch (IllegalArgumentException iae) {
            throw new FacesException("Cannot convert parameter \"" + value + "\" to the list of templates.", iae);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof String) {
            return (String) value;
        }

        if (value instanceof TemplatesList) {
            return value.toString();
        }

        throw new FacesException("Cannot convert parameter \"" + value + "\" to the list of templates.");
    }
}
