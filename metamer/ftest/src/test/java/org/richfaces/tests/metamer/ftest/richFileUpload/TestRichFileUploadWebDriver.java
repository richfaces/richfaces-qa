/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.fileUploadAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;
import javax.faces.event.PhaseId;
import org.jboss.test.selenium.support.ui.ElementNotDisplayed;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.TextEquals;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichFileUploadWebDriver extends AbstractFileUploadWebDriverTest {

    private SuccesfulFileUploadAction succesfulFileUploadAction = new SuccesfulFileUploadAction();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFileUpload/simple.xhtml");
    }

    @Test
    @Templates(exclude = { "richExtendedDataTable", "richCollapsibleSubTable" })
    public void testSingleFileUpload() {
        sendFileToInputWithWaiting(filenames[0], true);

        List<WebElement> filesToUpload = guardListSize(page.itemsToUpload, 1);

        assertTrue(filesToUpload.size() == 1, "File not loaded");
        assertTrue(filesToUpload.get(0).getText().equals(filenames[0]), "Label with filename does not appear.");
        assertTrue(ElementPresent.getInstance().element(page.itemClear).apply(driver), "Clear button does not appear.");
        assertTrue(ElementPresent.getInstance().element(page.uploadButton).apply(driver), "Upload button should be on the page.");
        assertTrue(ElementPresent.getInstance().element(page.clearAllButton).apply(driver), "Clear all button should be on the page.");

        waitRequest(page.uploadButton, WaitRequestType.HTTP).click();

        waitUntilUploadedFilesListShow(1);
        List<WebElement> uploadedFiles = guardListSize(page.uploadedFilesList, 1);
        assertTrue(uploadedFiles.size() == 1, "List of uploaded files should contain one file.");
        assertTrue(uploadedFiles.get(0).getText().equals(filenames[0]),
                "Uploaded file does not appear in uploadedList.");
    }

    @Test(groups = "4.Future")
    @Templates("richExtendedDataTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testSingleFileUploadInEDT() {
        testSingleFileUpload();
    }

    @Test(groups = "4.Future")
    @Templates("richCollapsibleSubTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testSingleFileUploadInCST() {
        testSingleFileUpload();
    }

    @Test
    public void testAcceptedTypes() {
        String acceptable = "txt";
        fileUploadAttributes.set(FileUploadAttributes.acceptedTypes, acceptable);

        sendFileToInputWithWaiting(notAcceptableFile, false);

        List<WebElement> filesToUpload = guardListSize(page.itemsToUpload, 0);
        assertTrue(filesToUpload.isEmpty(), "AcceptedType does not work.");

        sendFileWithWaiting(acceptableFile, true, true);

        filesToUpload = guardListSize(page.itemsToUpload, 1);
        assertFalse(filesToUpload.isEmpty(), "AcceptedType does not work.");
    }

    @IssueTracking(value = "https://issues.jboss.org/browse/RF-12039")
    @Test(groups = "4.Future")
    public void testData() {
        String testData = "Richfaces 4";
        fileUploadAttributes.set(FileUploadAttributes.data, testData);
        fileUploadAttributes.set(FileUploadAttributes.onuploadcomplete, "data = event.data");

        executeJS("window.data = \"\";");

        succesfulFileUploadAction.perform();

        String data = expectedReturnJS("return window.data", testData);
        assertEquals(data, testData);
    }

    @Test
    @Templates(exclude = { "richExtendedDataTable", "richCollapsibleSubTable" })
    public void testDoneLabel() {
        String doneLabel = "Done and done";
        fileUploadAttributes.set(FileUploadAttributes.doneLabel, doneLabel);

        succesfulFileUploadAction.perform();

        new WebDriverWait(driver, 10).failWith("DoneLabel does not work.").
                until(TextEquals.getInstance().element(page.uploadStatusLabel).
                text(doneLabel));
    }

    @Test(groups = "4.Future")
    @Templates("richExtendedDataTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testDoneLabelInEDT() {
        testDoneLabel();
    }

    @Test(groups = "4.Future")
    @Templates("richCollapsibleSubTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testDoneLabelInCST() {
        testDoneLabel();
    }

    @Test
    public void testOnFileSubmit() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onfilesubmit, succesfulFileUploadAction);
    }

    @Test
    public void testOnUploadcomplete() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onuploadcomplete, succesfulFileUploadAction);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12037")
    @Test(groups = "4.Future")
    public void testOnBeforeDOMUpdate() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onbeforedomupdate, succesfulFileUploadAction);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12037")
    @Test(groups = "4.Future")
    public void testOnBegin() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onbegin, succesfulFileUploadAction);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12037")
    @Test(groups = "4.Future")
    public void testOnComplete() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.oncomplete, succesfulFileUploadAction);
    }

    @Test
    public void testOnTypeRejected() {
        String acceptable = "txt";
        fileUploadAttributes.set(FileUploadAttributes.acceptedTypes, acceptable);
        testFireEvent(fileUploadAttributes, FileUploadAttributes.ontyperejected, new Action() {

            @Override
            public void perform() {
                sendFileToInputWithWaiting(notAcceptableFile, false);
            }
        });
    }

    @Test
    @Templates(exclude = { "richExtendedDataTable", "richCollapsibleSubTable" })
    public void testExecute() {
        String cmd = "executeChecker";
        final String expectedValue = "* executeChecker";

        fileUploadAttributes.set(FileUploadAttributes.execute, cmd);

        succesfulFileUploadAction.perform();

        phaseInfo.assertListener(PhaseId.UPDATE_MODEL_VALUES, "executeChecker");
    }

    @Test(groups = "4.Future")
    @Templates("richExtendedDataTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testExecuteInEDT() {
        testExecute();
    }

    @Test(groups = "4.Future")
    @Templates("richCollapsibleSubTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testExecuteInCST() {
        testExecute();
    }

    @Test
    @Templates(exclude = { "richExtendedDataTable", "richCollapsibleSubTable" })
    public void testLimitRender() {
        //sendFile depends on requestTime and uploadedFilesPanel
        fileUploadAttributes.set(FileUploadAttributes.render, "statusCheckerOutput, requestTime, uploadedFilesPanel");
        fileUploadAttributes.set(FileUploadAttributes.limitRender, Boolean.TRUE);

        String statusCheckerTime = page.statusCheckerOutput.getText();
        String renderCheckerTime = page.renderCheckerOutput.getText();

        succesfulFileUploadAction.perform();

        String statusCheckerTime2 = page.statusCheckerOutput.getText();
        String renderCheckerTime2 = page.renderCheckerOutput.getText();
        assertNotEquals(statusCheckerTime, statusCheckerTime2, "status checker time did not change as expected");
        assertEquals(renderCheckerTime, renderCheckerTime2, "render checker time changed as not expected");
    }

    @Test(groups = "4.Future")
    @Templates("richExtendedDataTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testLimitRenderInEDT() {
        testLimitRender();
    }

    @Test(groups = "4.Future")
    @Templates("richCollapsibleSubTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12122")
    public void testLimitRenderInCST() {
        testLimitRender();
    }

    @Test
    public void testStatus() {
        String cmd = ap + "statusChecker" + ap;
        fileUploadAttributes.set(FileUploadAttributes.status, cmd);

        String statusCheckerTime1 = page.statusCheckerOutput.getText();

        succesfulFileUploadAction.perform();

        String statusCheckerTime2 = page.statusCheckerOutput.getText();

        assertEquals(statusCheckerTime1, statusCheckerTime2);
    }

    @Test
    public void testSizeExceededLabel() {
        String testData = "size exceeded";
        fileUploadAttributes.set(FileUploadAttributes.sizeExceededLabel, testData);

        sendFileWithWaiting(bigFile, true, false);

        new WebDriverWait(driver, 2).failWith("Status is not present.").until(ElementPresent.getInstance().element(page.uploadStatusLabel));

        assertEquals(testData, page.uploadStatusLabel.getText(),
                "Attribute sizeExceededLabel does not work.");
    }

    @Test
    public void testMaxFilesQuantity() {
        final int maxFilesQuantity = 1;
        fileUploadAttributes.set(FileUploadAttributes.maxFilesQuantity, maxFilesQuantity);

        sendFileToInputWithWaiting(filenames[0], true);

        new WebDriverWait(driver, 2).failWith("Add File button should not be in page.").
                until(ElementNotDisplayed.getInstance().element(page.addButton));

        List<WebElement> filesToUpload = guardListSize(page.itemsToUpload, 1);
        assertTrue(filesToUpload.size() == maxFilesQuantity, "Files to upload list contains less/more files than there should be. List: " + filesToUpload + " .");
        for (int i = 0; i < maxFilesQuantity; i++) {
            String x = filesToUpload.get(i).getText();
            assertTrue(x.equals(filenames[i]), "Added file " + filenames[i] + " does not appear in files to upload list.");
        }

        waitRequest(page.uploadButton, WaitRequestType.HTTP).click();

        List<WebElement> uploadedFiles = guardListSize(page.uploadedFilesList, maxFilesQuantity);
        assertTrue(uploadedFiles.size() == maxFilesQuantity, "Uploaded files list contains more/less files than there should be. List: " + uploadedFiles + " .");
        for (int i = 0; i < maxFilesQuantity; i++) {
            assertTrue(uploadedFiles.get(i).getText().equals(filenames[i]),
                    "Uploaded file " + filenames[i] + " does not appear in uploadedList.");
        }
    }

    @Test
    public void testOnFileSelect() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onfileselect, new Action() {

            @Override
            public void perform() {
                sendFileToInputWithWaiting(filenames[0], true);
            }
        });
    }

    @Test
    public void testImmediateUpload() {
        fileUploadAttributes.set(FileUploadAttributes.immediateUpload, Boolean.TRUE);
        sendFileToInputWithWaiting(filenames[0], true);
        waitUntilUploadedFilesListShow(1);
        assertEquals(page.uploadedFilesList.size(), 1);
        assertEquals(page.uploadedFilesList.get(0).getText(), filenames[0]);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "upload listener");
    }

    private class SuccesfulFileUploadAction implements Action {

        @Override
        public void perform() {
            sendFileWithWaiting(filenames[0], true, true);
        }
    }
}
