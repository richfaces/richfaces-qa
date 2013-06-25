/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.panelMenu;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.richfaces.ui.common.Mode;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public abstract class PanelMenuHelper {

    public static final String ATTR_CLASS = "class";

    public static final String CSS_TRANSPARENT_SUFFIX = "-transparent";

    public static final String CSS_SELECTED_SUFFIX = "-sel";

    public static final String CSS_HOVERED_SUFFIX = "-hov";

    public static final String CSS_DISABLED_SUFFIX = "-dis";

    public static final String CSS_COLLAPSED_SUFFIX = "-colps";

    public static final String CSS_EXPANDED_SUFFIX = "-exp";

    public static final By IMG_BY_LOC = By.tagName("img");

    public static <T> T getGuardTypeForMode(T target, Mode mode) {
        switch (mode) {
            case ajax:
                return Graphene.guardAjax(target);
            case server:
                return Graphene.guardHttp(target);
            default:
                return Graphene.guardNoRequest(target);
        }
    }
}
