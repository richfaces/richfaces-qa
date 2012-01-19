/*
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
 */
package org.jboss.test.selenium;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;

/**
 * JQuery selectors store
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22505 $
 */
public final class JQuerySelectors {
    
    /* According to checkstyle made class final, without default ctor.
     * In case of need modify class accordingly */
    private JQuerySelectors() { };
    
    public static JQueryLocator not(ExtendedLocator<JQueryLocator> locator, String expression) {
        return jq(SimplifiedFormat.format("{0}:not({1})", locator.getRawLocator(), expression));
    }
    
    public static JQueryLocator append(ExtendedLocator<JQueryLocator> locator, String expression) {
        return jq(SimplifiedFormat.format("{0}{1}", locator.getRawLocator(), expression));
    }
}
