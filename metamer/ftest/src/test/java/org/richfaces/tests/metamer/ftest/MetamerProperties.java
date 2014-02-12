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
package org.richfaces.tests.metamer.ftest;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.richfaces.tests.metamer.TemplatesList;
import org.richfaces.tests.metamer.converter.TemplatesListConverter;

/**
 * @author <a href="mailto:ppitonak@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22332 $
 */
public final class MetamerProperties {

    private MetamerProperties() {
    }

    public static List<TemplatesList> getTemplates() {
        final String templatesString = System.getProperty("templates");

        if (templatesString == null || "null".equals(templatesString) || "*".equals(templatesString)
            || "".equals(templatesString)) {
            return null;
        }

        String[] templatesArray = StringUtils.split(templatesString, ";");

        return parseTemplates(templatesArray);
    }

    public static List<TemplatesList> parseTemplates(String[] templatesArray) {
        final TemplatesListConverter convertor = new TemplatesListConverter();
        List<TemplatesList> templates = new LinkedList<TemplatesList>();

        for (String template : templatesArray) {
            templates.add((TemplatesList) convertor.getAsObject(null, null, template));
        }

        return templates;
    }
}
