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

import java.util.List;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.model.Capital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@FacesConverter(value = "capitalConverter")
public class CapitalConverter implements Converter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CapitalConverter.class); 
    // FIXME: @ManagedProperty(value="#{model.capitals}")
    private List<Capital> capitals = Model.unmarshallCapitals();
   
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for(Capital capital : capitals) {
            if (capitalAsString(capital).equals(value)) {
                Capital toReturn = new Capital();
                toReturn.setName(capital.getName());
                toReturn.setState(capital.getState());
                toReturn.setTimeZone(capital.getTimeZone());
                LOGGER.info("converting string [" + value + "] to object [" + toReturn + "].");
                return toReturn;
            }
        }
        LOGGER.info("converting [" + value + "] to object wasn't succuessful.");
        throw new FacesException("Cannot convert parameter \"" + value + "\" to the Capital.");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (!(value instanceof Capital)) {
            LOGGER.info("converting [" + value + "] to string wasn't successful.");
            throw new FacesException("Cannot convert parameter \"" + value + "\" to the String.");
        }
        LOGGER.info("converting object [" + value + "] to string [" + capitalAsString((Capital) value) + "].");
        return capitalAsString((Capital) value);
    }

    public List<Capital> getCapitals() {
        return capitals;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }

    private String capitalAsString(Capital capital) {
        if (capital == null) {
            return null;
        } else {
            return capital.getName();
        }
    }
    
}
 