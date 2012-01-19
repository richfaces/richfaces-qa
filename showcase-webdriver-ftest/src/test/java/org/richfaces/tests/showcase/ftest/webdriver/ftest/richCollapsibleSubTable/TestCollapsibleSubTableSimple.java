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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.richCollapsibleSubTable;

import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.richCollapsibleSubTable.SubTableToggleControlPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestCollapsibleSubTableSimple extends AbstractWebDriverTest<SubTableToggleControlPage>{

    @Test
    public void testToggleTwiceAll() {
        getPage().toggleChevrolet();
        getPage().toggleFord();
        getPage().toggleGmc();
        getPage().toggleInfinity();
        getPage().toggleNissan();
        getPage().toggleToyota();
        getPage().toggleChevrolet();
        getPage().toggleFord();
        getPage().toggleGmc();
        getPage().toggleInfinity();
        getPage().toggleNissan();
        getPage().toggleToyota();        
    }
    
    @Override
    protected SubTableToggleControlPage createPage() {
        return new SubTableToggleControlPage(getWebDriver());
    }

    
    
}
