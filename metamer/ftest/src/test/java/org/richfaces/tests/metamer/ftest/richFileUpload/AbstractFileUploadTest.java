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
package org.richfaces.tests.metamer.ftest.richFileUpload;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.fileUpload.RichFacesFileUpload;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractFileUploadTest extends AbstractWebDriverTest {

    @FindBy(css = "div[id$=fileUpload]")
    protected RichFacesFileUpload fileUpload;

    protected static final String notAcceptableFile = "file1.x";
    protected static final String acceptableFile = "file1.txt";
    protected static final String bigFile = "bigFile.txt";
    protected static final String[] filenames = { acceptableFile, "file2.txt" };
    protected static final String ap = "\"";
    protected int filesToUploadCount;
    protected int filesUploadedCount;

    @BeforeMethod(alwaysRun = true)
    public void resetCounters() {
        filesToUploadCount = 0;
        filesUploadedCount = 0;
    }

    protected File createFileFromString(String filename) {
        File file = null;
        try {
            file = new File(AbstractFileUploadTest.class.getResource(filename).toURI());
        } catch (URISyntaxException ex) {
        }
        assertTrue(file != null, "File does not exist.");
        assertTrue(file.exists(), "File does not exist.");
        return file;
    }

    /**
     * Sends file to fileupload input field and waits for page update.
     *
     * @param filename
     * @param willBeAccepted
     */
    protected void sendFileToInputWithWaiting(String filename, boolean willBeAccepted) {
        fileUpload.addFile(createFileFromString(filename));
        if (willBeAccepted) {
            this.filesToUploadCount++;
        }
    }

    /**
     * Sends file to fileupload input field and waits for page update. Then
     * clicks on submit button to upload files and waits for page update.
     *
     * @param filename
     * @param willBeAccepted
     * @param willBeUploaded
     */
    protected void sendFileWithWaiting(String filename, boolean willBeAccepted, boolean willBeUploaded) {
        sendFileToInputWithWaiting(filename, willBeAccepted);
        if (willBeUploaded) {
            MetamerPage.waitRequest(fileUpload, WaitRequestType.XHR).upload();
        } else {
            //Metamer's request time will not change, but XHR request will be send
            Graphene.guardAjax(fileUpload).upload();
        }
    }

    /**
     * Waits until page renders elements for all expected uploaded files in
     * uploaded files list.
     *
     * @param expectedNumberOfFiles
     */
    protected void waitUntilUploadedFilesListShow(int expectedNumberOfFiles) {
        if (expectedNumberOfFiles == 0) {
        } else {
            By by;
            for (int i = 1; i <= expectedNumberOfFiles; i++) {
                by = By.xpath("//span[contains(@id, 'uploadedFilesPanel')]//ul //li[" + i + "]");
                Graphene.waitGui().until().element(by).is().visible();
            }
        }
    }
}
