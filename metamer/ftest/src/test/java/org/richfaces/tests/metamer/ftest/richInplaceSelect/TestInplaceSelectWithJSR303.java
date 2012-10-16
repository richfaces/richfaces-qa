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
package org.richfaces.tests.metamer.ftest.richInplaceSelect;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.testng.annotations.Test;


/**
 * Test for page faces/components/richInplaceSelect/jsr303.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 22534 $
 */
public class TestInplaceSelectWithJSR303 extends TestSelectsWithJSR303 {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceSelect/jsr303.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Inplace Select", "JSR-303 Bean Validation");
    }

    @Test
    public void testNotEmpty() {
        verifyNotEmpty();
    }

    @Test
    public void testRegExpPattern() {
        verifyRegExpPattern();
    }

    @Test
    public void testStringSize() {
        verifyStringSize();
    }

    @Test
    public void testCustomString() {
        verifyCustomString();
    }

    @Test
    public void testRequired() {
        verifyRequired();
    }

    @Test
    public void testAllInputsWrong() {
        verifyAllInputsWrong();
    }

    @Test
    public void testAllInputsCorrect() {
        verifyAllInputsCorrect();
    }

}
