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
package org.richfaces.tests.page.fragments.impl.editor;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class RichFacesEditor {

    @Root
    private WebElement root;

    @FindBy(css = ".cke_toolbox")
    private EditorToolbar toolbar;

    private static final String BUTTON_ON = "cke_on";
    private static final String BUTTON_OFF = "cke_off";

    public boolean turnOnMode(EditorToolbar.EditorMode mode) {
        return true;
    }

    public boolean turnOffMode(EditorToolbar.EditorMode mode) {
        return false;
    }

    public void clickOnButton() {

    }

    /**
     * Types into the editor given text.
     *
     * @author Jan Papousek
     * @param text
     */
    public void typeTextToEditor(String text) {
        WebDriver driver = GrapheneContext.getProxy();
        try {
            // driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(0);// must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            activeArea.click();
            activeArea.sendKeys(text);
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    public EditorToolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(EditorToolbar toolbar) {
        this.toolbar = toolbar;
    }

}
