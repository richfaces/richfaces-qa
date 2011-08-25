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
package org.richfaces.tests.metamer.ftest.hCommandButton;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestHCommandButton extends AbstractWebDriverTest {
 
    private By button = By.id("form:commandButton");
    private By input = By.id("form:input");
    private By output1 = By.id("form:output1");
    private By output2 = By.id("form:output2");
    private By output3 = By.id("form:output3");
    
    
    @Override
    protected String getTestUrl() {
        return "faces/components/commandButton/simple.xhtml";
    }
    
    @Test
    public void testSimpleClick() {
        WebElement inputElement = getWebDriver().findElement(input);
        inputElement.sendKeys("RichFaces 4");
        WebElement buttonElement = getWebDriver().findElement(button);
        buttonElement.click();
        assertEquals(getWebDriver().findElement(output1).getText(), "RichFaces 4", "output1 when 'RichFaces 4' in input");
        assertEquals(getWebDriver().findElement(output2).getText(), "RichFa", "output2 when 'RichFaces 4' in input");
        assertEquals(getWebDriver().findElement(output3).getText(), "RICHFACES 4", "output3 when 'RichFaces 4' in input");
    }

}
