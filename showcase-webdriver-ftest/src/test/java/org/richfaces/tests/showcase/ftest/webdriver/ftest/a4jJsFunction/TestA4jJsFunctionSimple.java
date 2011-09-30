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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jJsFunction;

import org.jboss.test.selenium.By;
import org.jboss.test.selenium.support.ui.TextEquals;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jJsFunctionSimple extends AbstractWebDriverTest {
    
    private static final String NAME_XPATH = "//*[@class='example-cnt']//span[text()='{0}']"; 
    private static final By OUTPUT = By.xpath("//*[@class='example-cnt']//b/span");
    
    @Override
    protected String getDemoName() {
        return "jsFunction";
    }

    @Override
    protected String getSampleName() {
        return "jsFunction";
    }

    @Test
    public void testClick() {
        String[] names = new String[] {"Alex", "John", "Kate"};
        for (String name : names) {
            getWebDriver().findElement(By.xpath(NAME_XPATH.replace("{0}", name))).click();
            new WebDriverWait(getWebDriver())
                .failWith("After clicking on the name <" + name + ">, the name should appear in the output area.")
                .until(TextEquals.getInstance().locator(OUTPUT).text(name));
        }
    }
    
}
