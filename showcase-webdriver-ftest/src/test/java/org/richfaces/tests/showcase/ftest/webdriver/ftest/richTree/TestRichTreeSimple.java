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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.richTree;

import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.richTree.TreePage;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichTreeSimple extends AbstractWebDriverTest<TreePage>{


    @Test
    public void testExpandFirstLevel() {
        getPage().expandFirstLevelAll();
        assertEquals(getPage().countSecondLevelVisible(), 22, "Number of visible second level nodes after expanding the first level nodes doesn't match.");
        assertEquals(getPage().countThirdLevelVisible(), 0, "Number of visible third level nodes after expanding the first level nodes doesn't match.");
    }

    @Test(groups = {"broken"})
    public void testExpandSecondLevel() {
        getPage().expandFirstLevelAll();
        getPage().expandSecondLevel();
        assertEquals(getPage().countThirdLevelVisible(), 26, "Number of visible third level nodes after expanding the second level nodes doesn't match.");
    }

    @Test(groups = {"broken"})
    public void testInit() {
        assertEquals(getPage().countSecondLevelVisible(), 0, "Number of visible second level nodes doesn't match.");
        assertEquals(getPage().countThirdLevelVisible(), 0, "Number of visible third level nodes doesn't match.");
    }

    @Override
    protected TreePage createPage() {
        return new TreePage(getWebDriver());
    }

}
