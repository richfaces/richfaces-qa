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
package org.richfaces.tests.showcase.autocomplete;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.autocomplete.page.ClientFilterPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestClientFilter extends AbstractWebDriverTest {

    @Page
    private ClientFilterPage page;

    @ArquillianResource
    private Actions actions;

    @Test
    public void testClientFilterFunctionContains() {
        page.input.sendKeys("ska");
        waitAjax(webDriver).until("The selection should be visible, since there is correct sequence of chars!")
                .element(page.selection)
                .is()
                .visible();
        actions.sendKeys(page.selection, Keys.ENTER).build().perform();
        assertEquals(page.input.getText(), "Alaska", "The content should be Alaska, since it contains string ska");
    }

}
