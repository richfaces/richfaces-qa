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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.richMessages;

import org.richfaces.tests.showcase.ftest.webdriver.ftest.AbstractRichMessageTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.richMessages.MessagesPage;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichMessagesSimple extends AbstractRichMessageTest<MessagesPage> {

    @Test
    public void testAddressWrongAndCorrect() {
        super.testAddressWrongAndCorrect();
    }

    @Test
    public void testAllWrongAndCorrect() {
        super.testAllWrongAndCorrect();
    }

    @Test
    public void testJobWrongAndCorrect() {
        super.testJobWrongAndCorrect();
    }

    @Test
    public void testNameWrongAndCorrect() {
        super.testNameWrongAndCorrect();
    }

    @Test
    public void testZipWrongAndCorrect() {
        super.testZipWrongAndCorrect();
    }

    @Override
    protected MessagesPage createPage() {
        return new MessagesPage();
    }


}
