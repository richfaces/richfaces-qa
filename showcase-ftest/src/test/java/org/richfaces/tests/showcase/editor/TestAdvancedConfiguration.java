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
package org.richfaces.tests.showcase.editor;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.editor.page.AdvancedConfigurationPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Lukas Fryc and Juraj Huska</a>
 */
public class TestAdvancedConfiguration extends AbstractWebDriverTest {

    @Page
    private AdvancedConfigurationPage page;

    @Test
    public void testEnglishLanguage() {

        guardXhr(page.englishRadio).click();

        String titleOfNewPageButton = page.newPageButton.getAttribute("title");
        assertEquals(titleOfNewPageButton, AdvancedConfigurationPage.NEW_PAGE_ENG, "The language was not changed to english!");
    }

    @Test
    public void testFrenchLanguage() {
        guardXhr(page.frenchRadio).click();

        String titleOfNewPageButton = page.newPageButton.getAttribute("title");
        assertEquals(titleOfNewPageButton, AdvancedConfigurationPage.NEW_PAGE_FR, "The language was not changed to french!");
    }

    @Test
    public void testGermanLanguage() {
        guardXhr(page.germanRadio).click();

        String titleOfNewPageButton = page.newPageButton.getAttribute("title");
        assertEquals(titleOfNewPageButton, AdvancedConfigurationPage.NEW_PAGE_DE, "The language was not changed to german!");
    }

    @Test
    public void testUserFocusAutomaticallyOnEditor() {
        WebDriver driver = GrapheneContext.getProxy();
        try {
            driver.switchTo().frame(0);
            WebElement activeArea = driver.findElement(By.tagName("body"));

            String expected = "Test String";
            activeArea.sendKeys(expected);
            assertTrue(activeArea.getText().contains(expected),
                "The text should be writen to editor without explicit previos focusing!");
        } finally {
            driver.switchTo().defaultContent();
        }
    }
}
