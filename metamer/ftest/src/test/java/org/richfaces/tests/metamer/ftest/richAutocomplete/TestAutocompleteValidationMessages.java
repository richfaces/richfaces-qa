/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import org.richfaces.tests.metamer.ftest.abstractions.AbstractValidationMessagesTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteValidationMessages extends AbstractValidationMessagesTest {

    public TestAutocompleteValidationMessages() {
        super("richAutocomplete");
    }

    @Test
    @Override
    public void testInit() {
        super.testInit();
    }

    @Test
    @Override
    public void testJSR303MessagesJSF() {
        super.testJSR303MessagesJSF();
    }

    @Test
    @Override
    public void testJSR303MessagesRF() {
        super.testJSR303MessagesRF();
    }

    @Test
    @Override
    public void testCSVMessagesJSF() {
        super.testCSVMessagesJSF();
    }

    @Test
    @Override
    public void testCSVMessagesRF() {
        super.testCSVMessagesRF();
    }

    @Test
    @Override
    public void testJSFMessagesJSF() {
        super.testJSFMessagesJSF();
    }

    @Test
    @Override
    public void testJSFMessagesRF() {
        super.testJSFMessagesRF();
    }
}
