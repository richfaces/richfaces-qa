/**
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
 */
package org.richfaces.tests.metamer.ftest.richEditor;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.editor.RichFacesEditor;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class EditorSimplePage extends MetamerPage {

    @FindBy(className = "rf-ed")
    private RichFacesEditor editor;
    @FindBy(css = "input[type=submit][id$=a4jButton]")
    private WebElement a4jButton;
    @FindBy(tagName = "iframe")
    private WebElement editorFrame;
    @FindBy(css = "input[type=submit][id$=hButton]")
    private WebElement hButton;
    @FindBy(css = "span[id$=output]")
    private WebElement output;
    @FindBy(xpath = "//div[@id='phasesPanel']//li[4]")
    private WebElement valueChangeListener;
    @FindBy(xpath = "//div[@id='phasesPanel']//li[3]")
    private WebElement valueChangeListenerAfterImmediate;
    @FindBy(className = "rf-msgs-sum")
    private WebElement errorMsg;

    public RichFacesEditor getEditor() {
        return editor;
    }

    public WebElement getA4jButton() {
        return a4jButton;
    }

    public WebElement getEditorFrame() {
        return editorFrame;
    }

    public WebElement getHButton() {
        return hButton;
    }

    public WebElement getOutput() {
        return output;
    }

    public WebElement getValueChangeListener() {
        return valueChangeListener;
    }

    public WebElement getValueChangeListenerAfterImmediate() {
        return valueChangeListenerAfterImmediate;
    }

    public WebElement getErrorMsg() {
        return errorMsg;
    }
}
