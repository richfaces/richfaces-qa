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

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import org.jboss.test.selenium.support.ui.ElementDisplayed;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractFileUploadWebDriverTest extends AbstractWebDriverTest {

    protected static final String notAcceptableFile = "file1.x";
    protected static final String acceptableFile = "file1.txt";
    protected static final String bigFile = "bigFile.txt";
    protected static final String[] filenames = { acceptableFile, "file2.txt", notAcceptableFile, bigFile };
    protected static final String ap = "\"";
    protected FileUploadPage page = new FileUploadPage();
    protected int filesToUploadCount;
    protected int filesUploadedCount;


    @BeforeMethod
    public void pageLoad() {
        injectWebElementsToPage(page);
        filesToUploadCount = 0;
        filesUploadedCount = 0;
    }

    /**
     * Sends file to fileupload input field and waits for page update.
     *
     * @param filename
     * @param willBeAccepted
     */
    protected void sendFileToInput(String filename, boolean willBeAccepted) {
        File file = null;
        try {
            file = new File(AbstractFileUploadWebDriverTest.class.getResource(filename).toURI());
        } catch (URISyntaxException ex) {
        }
        assertTrue(file != null, "File does not exist.");
        assertTrue(file.exists(), "File does not exist.");

        //send file to input field
        page.fileInputField.sendKeys(file.getAbsolutePath());
        if (willBeAccepted) {
            this.filesToUploadCount++;
        }
        waitUntilFilesToUploadListShow(filesToUploadCount);
    }

    /**
     * Sends file to fileupload input field and waits for page update. Then
     * clicks on submit button to upload files and waits for page update.
     *
     * @param filename
     * @param willBeAccepted
     * @param willBeUploaded
     */
    protected void sendFile(String filename, boolean willBeAccepted, boolean willBeUploaded) {
        sendFileToInput(filename, willBeAccepted);
        waitRequest(page.uploadButton, AbstractWebDriverTest.WaitRequestType.HTTP).click();
        if (willBeUploaded) {
            this.filesUploadedCount++;
        }
        waitUntilUploadedFilesListShow(filesUploadedCount);
    }

    /**
     * Waits until page renders elements for all expected uploaded files in
     * uploaded files list.
     *
     * @param expectedNumberOfFiles
     */
    protected void waitUntilUploadedFilesListShow(int expectedNumberOfFiles) {
        waitForPageToLoad();
        if (expectedNumberOfFiles == 0) {
        } else {
            for (int i = 1; i <= expectedNumberOfFiles; i++) {
                new WebDriverWait(driver).failWith("Expected number of files was not added to uploaded files list.").
                        until(ElementDisplayed.getInstance().
                        element(driver.findElement(
                        By.xpath("//div[@class='rf-fu-itm'][" + i + "]"
                        + "//span[@class='rf-fu-itm-st']"))));
            }
        }
    }

    /**
     * Waits until page renders elements for all expected files to be uploaded
     * in list of files to be uploaded
     *
     * @param expectedNumberOfFiles
     */
    protected void waitUntilFilesToUploadListShow(int expectedNumberOfFiles) {
        if (expectedNumberOfFiles == 0) {
        } else {
            for (int i = 1; i <= expectedNumberOfFiles; i++) {
                new WebDriverWait(driver).failWith("Expected number of files was not added to upload list.").
                        until(ElementDisplayed.getInstance().
                        element(driver.findElement(
                        By.xpath("//div[@class='rf-fu-itm'][" + i + "]"))));
            }
        }
    }
}
