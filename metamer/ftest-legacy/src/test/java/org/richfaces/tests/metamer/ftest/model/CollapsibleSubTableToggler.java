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
package org.richfaces.tests.metamer.ftest.model;

import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;

public class CollapsibleSubTableToggler extends AbstractModel<JQueryLocator> {

    ReferencedLocator<JQueryLocator> collapsedTogglers = ref(root, "> span[id$=collapsed]");
    ReferencedLocator<JQueryLocator> expandedTogglers = ref(root, "> span[id$=expanded]");

    public CollapsibleSubTableToggler(JQueryLocator root) {
        super(root);
    }

    public ExtendedLocator<JQueryLocator> getCollapsed() {
        return collapsedTogglers;
    }

    public ExtendedLocator<JQueryLocator> getExpanded() {
        return expandedTogglers;
    }

}
