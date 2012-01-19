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
package org.richfaces.tests.metamer.ftest.a4jPush;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import static org.testng.Assert.assertNotSame;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.testng.annotations.Test;


/**
 * Test case for simple usage of a4j:push component.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23061 $
 */
public class TestCounter extends AbstractPushTest {
    private static final long SIMPLE_INTERVAL = 2500;

    /**
     * <p>
     * Sets the interval to value {@link #SIMPLE_INTERVAL}.
     * </p>
     * 
     * <p>
     * Then try to generate push event, wait for client observation.
     * </p>
     * 
     * <p>
     * Validates that counter is changed between iterations.
     * </p>
     */
    @Test(enabled = false)
    public void testSimple() throws HttpException, IOException {
        
        int beginCounter = getCounter();
        pushAndWait(1);
        int endCounter = getCounter();

        assertNotSame(beginCounter < endCounter,
            format("The counter before push is greater {0} or equal the end counter {1}", beginCounter, endCounter));
    }
}
