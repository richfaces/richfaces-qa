/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.richfaces.tests.metamer.ftest.richFileUpload;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class FileUploadPage {

    @FindBy(css = "span[id$=uploadedFilesPanel] li")
    List<WebElement> uploadedFilesList;
    @FindBy(xpath= "//input[@type='file'][@class='rf-fu-inp']")
    WebElement fileInputField;
    @FindBy(css = "span.rf-fu-btn-upl")
    WebElement uploadButton;
    @FindBy(css = "span.rf-fu-btn-clr")
    WebElement clearAllButton;
    @FindBy(css = "div.rf-fu-itm .rf-fu-itm-lbl")
    List<WebElement> itemsToUpload;
    @FindBy(css = "div.rf-fu-itm .rf-fu-itm-rgh a")
    WebElement itemClear;
    @FindBy(css = "span.rf-fu-itm-st")
    WebElement uploadStatusLabel;
    @FindBy(css = "ul.phases-list li")
    List<WebElement> phases;
    @FindBy(css = "span[id$=statusCheckerOutput]")
    WebElement statusCheckerOutput;
    @FindBy(css = "span[id$=renderChecker]")
    WebElement renderCheckerOutput;
}
