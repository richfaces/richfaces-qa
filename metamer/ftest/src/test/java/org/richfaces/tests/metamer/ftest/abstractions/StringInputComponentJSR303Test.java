/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class StringInputComponentJSR303Test extends AbstractStringInputComponentValidationTest {

    @Inject
    @Use(strings = { A4J_COMMANDBUTTON, H_COMMANDBUTTON })
    private String submitMethod;

    public abstract String getComponentName();

    @Override
    public String getSubmitMethod() {
        return submitMethod;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/" + getComponentName() + "/jsr303.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich " + getComponentName(), "JSR-303 Bean Validation");
    }
}
