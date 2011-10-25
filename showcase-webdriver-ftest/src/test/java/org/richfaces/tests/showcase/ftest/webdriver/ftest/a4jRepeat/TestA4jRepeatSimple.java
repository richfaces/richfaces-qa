/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jRepeat;

import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.model.State;
import org.richfaces.tests.showcase.ftest.webdriver.page.a4jRepeat.SimplePage;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jRepeatSimple extends AbstractWebDriverTest<SimplePage>{

    private static final State FIRST_STATE_FIRST_PAGE = new State("Alabama", "Montgomery", "GMT-6");
    private static final State FIRST_STATE_SECOND_PAGE = new State("Massachusetts", "Boston", "GMT-5");
    private static final State FIRST_STATE_THIRD_PAGE = new State("South Dakota", "Pierre", "GMT-6");
    private static final State LAST_STATE_FIRST_PAGE = new State("Maryland", "Annapolis", "GMT-5");
    private static final State LAST_STATE_SECOND_PAGE = new State("South Carolina", "Columbia", "GMT-5");
    private static final State LAST_STATE_THIRD_PAGE = new State("Wyoming", "Cheyenne", "GMT-7");
    
    @Test
    public void testInit() {
        assertEquals(getPage().getFirstState(), FIRST_STATE_FIRST_PAGE, "The first state on doesn't match.");
        assertEquals(getPage().getLastState(), LAST_STATE_FIRST_PAGE, "The last state on doesn't match.");
    }
    
    @Test
    public void testSecondPage() {
        getPage().next();
        assertEquals(getPage().getFirstState(), FIRST_STATE_SECOND_PAGE, "The first state on doesn't match.");
        assertEquals(getPage().getLastState(), LAST_STATE_SECOND_PAGE, "The last state on doesn't match.");        
    }
    
    @Test
    public void testThirdPage() {
        getPage().next();
        getPage().next();
        assertEquals(getPage().getFirstState(), FIRST_STATE_THIRD_PAGE, "The first state on doesn't match.");
        assertEquals(getPage().getLastState(), LAST_STATE_THIRD_PAGE, "The last state on doesn't match.");        
    }    
    
    @Override
    protected SimplePage createPage() {
        return new SimplePage(getWebDriver());
    }

}
