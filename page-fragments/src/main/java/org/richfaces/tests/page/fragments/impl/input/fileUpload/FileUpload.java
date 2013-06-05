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
package org.richfaces.tests.page.fragments.impl.input.fileUpload;

import java.io.File;

import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.VisibleComponent;
import org.richfaces.tests.page.fragments.impl.list.ListItems;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface FileUpload extends VisibleComponent {

    WebElement getAddButtonElement();

    WebElement getClearAllButtonElement();

    WebElement getFileInputElement();

    WebElement getRootElement();

    WebElement getUploadButtonElement();

    /**
     * Checks if the fileUpload is disabled.
     */
    boolean isDisabled();

    /**
     * Adds file to the items list.
     * @param file file which will be uploaded.
     */
    FileUpload addFile(File file);

    FileUpload clearAll();

    /**
     * Returns all items that will be uploaded or are already uploaded.
     */
    ListItems<RichFacesFileUploadItem> getItems();

    RichFacesFileUploadList getItemsList();

    /**
     * Removes file(s) from list of items that will be uploaded or that are already uploaded.
     * @param filename exact filename
     */
    FileUpload removeFile(String filename);

    /**
     * Uploads all files that are not uploaded yet and are in items list.
     */
    FileUpload upload();
}
