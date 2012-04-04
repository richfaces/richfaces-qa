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
package org.richfaces.tests.metamer.ftest.richMenuSeparator;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.testng.annotations.Test;


/**
 * Test case for page faces/components/richMenuSeparator/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22407 $
 */
public class TestRichMenuSeparator extends AbstractGrapheneTest {

    private JQueryLocator separators = pjq("div.rf-ddm-sep");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richMenuSeparator/simple.xhtml");
    }

    @Test
    public void testRendered() {
        int count = selenium.getCount(separators);
        assertEquals(count, 2, "There should be two separators on the page - after 'Open Recent' and after 'Close'.");

        selenium.click(pjq("input[type=radio][name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();
        count = selenium.getCount(separators);
        assertEquals(count, 1, "There should be only one separator on the page - after 'Close'.");

        selenium.click(pjq("input[type=radio][name$=renderedInput][value=true]"));
        selenium.waitForPageToLoad();
        count = selenium.getCount(separators);
        assertEquals(count, 2, "There should be two separators on the page - after 'Open Recent' and after 'Close'.");
    }
}
