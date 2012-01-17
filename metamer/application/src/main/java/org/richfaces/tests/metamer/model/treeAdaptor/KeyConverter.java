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
package org.richfaces.tests.metamer.model.treeAdaptor;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang.CharUtils;
import org.richfaces.tests.metamer.bean.rich.RichTreeModelRecursiveAdaptorBean;

import com.google.common.base.Strings;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22493 $
 */
@FacesConverter("treeAdaptorKeyConverter")
public class KeyConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<RecursiveNode> list = RichTreeModelRecursiveAdaptorBean.getRootNodesStatically();
        RecursiveNode recursive = null;
        ModelNode model = null;

        char alpha = ' ';
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (CharUtils.isAsciiAlpha(ch)) {
                alpha = ch;
                switch (alpha) {
                    case 'M':
                        model = recursive.getModel();
                        break;
                    default:
                }
            }
            if (CharUtils.isAsciiNumeric(ch)) {
                int num = CharUtils.toIntValue(ch);
                switch (alpha) {
                    case 'R':
                        recursive = list.get(num);
                        list = recursive.getRecursiveList();
                        break;

                    case 'K':
                        for (ModelNodeImpl.K key : model.getMap().keySet()) {
                            if (key.number == num) {
                                return key;
                            }
                        }
                        throw new IllegalStateException();
                    default:
                }
            }
        }

        if (Strings.isNullOrEmpty(value)) {
            return null;
        }

        return null;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof ModelNodeImpl.K) {
            ModelNodeImpl.K k = (ModelNodeImpl.K) value;
            return k.getLabel();
        }

        if (value == null) {
            return "";
        }

        throw new IllegalStateException();
    }

}