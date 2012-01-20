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
package org.richfaces.tests.metamer;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * An implementation of list for storing templates. This list ensures that the last item
 * in list is always template "plain".
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version$Revision: 21299$
 */
public class TemplatesList extends ArrayList<Template> {

    private static final long serialVersionUID = 3822067099609304855L;

    public TemplatesList() {
        super.add(Template.PLAIN);
    }

    @Override
    public boolean add(Template e) {
        if (e == Template.PLAIN) {
            return false;
        }

        super.add(super.size() - 1, e);

        return true;
    }

    @Override
    public void add(int index, Template element) {
        super.add(index, element);

        // remove the rest of list if plain is set
        if (element == Template.PLAIN) {
            while (super.size() > index + 1) {
                super.remove(index + 1);
            }
        }
    }

    @Override
    public Template set(int index, Template element) {
        Template old = super.set(index, element);

        // remove the rest of list if plain is set
        if (element == Template.PLAIN) {
            while (super.size() > index + 1) {
                super.remove(index + 1);
            }
        } else {
            if (index == super.size() - 1) {
                super.add(Template.PLAIN);
            }
        }

        return old;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Iterator<Template> iterator = iterator();
        while (iterator.hasNext()) {
            final Template template = iterator.next();
            if (sb.length() > 0) {
                if (template == Template.PLAIN) {
                    continue;
                }
                sb.append(",");
            }
            sb.append(template.toString());
        }

        return sb.toString();
    }
}
