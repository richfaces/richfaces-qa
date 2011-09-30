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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jAjax;

import org.jboss.test.selenium.By;
import org.jboss.test.selenium.android.support.ui.Select;
import org.jboss.test.selenium.support.ui.ElementNotPresent;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractAndroidTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jAjaxSelects extends AbstractAndroidTest {

    private static final By FIRST_SELECT = By.xpath("//*[@class='example-cnt']/form/select");
    private static final By SECOND_SELECT = By.xpath("//*[@class='example-cnt']/form/div/select");
    
    @Override
    protected String getDemoName() {
        return "ajax";
    }

    @Override
    protected String getSampleName() {
        return "selectsUpdates";
    }

    @Test(enabled=false)
    public void testDisplaySecondSelect() throws Exception {
        Select firstSelect = new Select(getToolKit(), getWebDriver().findElement(FIRST_SELECT));
        
        firstSelect.selectByValue("fruits");
        new WebDriverWait(getWebDriver())
            .failWith("After selecting [fruit] option, the vegetables select should be present.")
            .until(ElementPresent.getInstance().locator(SECOND_SELECT));
        
        firstSelect.selectByValue("vegetables");
        new WebDriverWait(getWebDriver())
            .failWith("After selecting [vegetables] option, the vegetables select should be present.")
            .until(ElementPresent.getInstance().locator(SECOND_SELECT));    
        
        firstSelect.selectByValue("");
        new WebDriverWait(getWebDriver())
            .failWith("After selecting [] option, the vegetables select shouldn't be present.")
            .until(ElementNotPresent.getInstance().locator(SECOND_SELECT));           
    }
}
