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

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichFileUploadWebDriver extends AbstractFileUploadWebDriverTest {

    private Action succesfulFileUploadAction = new SuccesfulFileUploadAction();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFileUpload/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(Graphene.element(page.fileUploadDiv).isPresent().apply(driver), "File upload is not on the page.");
        assertTrue(Graphene.element(page.addButton).isVisible().apply(driver), "Add button should be on the page.");
        assertFalse(Graphene.element(page.addButtonDisabled).isVisible().apply(driver), "Disabled add button should not be on the page.");
        assertFalse(Graphene.element(page.uploadButton).isVisible().apply(driver), "Upload button should not be on the page.");
        assertFalse(Graphene.element(page.clearAllButton).isVisible().apply(driver), "Clear all button should not be on the page.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12122")
    public void testSingleFileUpload() {
        sendFileToInputWithWaiting(filenames[0], true);

        List<WebElement> filesToUpload = guardListSize(page.itemsToUpload, 1);

        assertTrue(filesToUpload.size() == 1, "File not loaded");
        assertTrue(filesToUpload.get(0).getText().equals(filenames[0]), "Label with filename does not appear.");
        assertTrue(Graphene.waitModel().until(Graphene.element(page.itemClear).isPresent()).booleanValue(), "Clear button does not appear.");
        assertTrue(Graphene.waitModel().until(Graphene.element(page.uploadButton).isPresent()).booleanValue(), "Upload button should be on the page.");
        assertTrue(Graphene.waitModel().until(Graphene.element(page.clearAllButton).isPresent()).booleanValue(), "Clear all button should be on the page.");


        waitRequest(Graphene.guardXhr(page.uploadButton), WaitRequestType.XHR).click();

        waitUntilUploadedFilesListShow(1);
        List<WebElement> uploadedFiles = guardListSize(page.uploadedFilesList, 1);
        assertTrue(uploadedFiles.size() == 1, "List of uploaded files should contain one file.");
        assertTrue(Graphene.element(uploadedFiles.get(0)).textEquals(filenames[0]).apply(driver),
                "Uploaded file does not appear in uploadedList.");
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

    @Test
    public void testAddLabel() {
        testLabelChanges(page.addButton, fileUploadAttributes, FileUploadAttributes.addLabel, null);
    }

    @Test
    public void testClearAllLabel() {
        testLabelChanges(page.clearAllButton, fileUploadAttributes, FileUploadAttributes.clearAllLabel, succesfulFileUploadAction);
    }

    @Test
    public void testClearLabel() {
        testLabelChanges(page.firstAfterUploadClearLink, fileUploadAttributes, FileUploadAttributes.clearLabel, succesfulFileUploadAction);
    }

    @IssueTracking(value = "https://issues.jboss.org/browse/RF-12039")
    @Test(groups = "4.Future")
    public void testData() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.data, succesfulFileUploadAction);
    }

    @Test
    public void testDeleteLabel() {
        testLabelChanges(page.firstItemToUploadDeleteLink, fileUploadAttributes, FileUploadAttributes.deleteLabel, new Action() {
            @Override
            public void perform() {
                sendFileToInputWithWaiting(acceptableFile, true);
            }
        });
    }

    @Test
    public void testDir() {
        testDir(page.fileUploadDiv);
    }

    @Test
    public void testDisabled() {
        fileUploadAttributes.set(FileUploadAttributes.disabled, Boolean.TRUE);
        assertTrue(Graphene.element(page.fileUploadDiv).isPresent().apply(driver), "File upload is not on the page.");
        assertTrue(Graphene.element(page.addButtonDisabled).isVisible().apply(driver), "Disabled add button should be on the page.");
        assertFalse(Graphene.element(page.addButton).isVisible().apply(driver), "Add button should not be on the page.");
        assertFalse(Graphene.element(page.uploadButton).isVisible().apply(driver), "Upload button should not be on the page.");
        assertFalse(Graphene.element(page.clearAllButton).isVisible().apply(driver), "Clear all button should not be on the page.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12122")
    public void testDoneLabel() {
        String doneLabel = "Done and done";
        fileUploadAttributes.set(FileUploadAttributes.doneLabel, doneLabel);

        succesfulFileUploadAction.perform();

        Graphene.waitAjax().until(Graphene.element(page.uploadStatusLabel).textEquals(doneLabel));
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12122")
    public void testExecute() {
        String cmd = "executeChecker";
        fileUploadAttributes.set(FileUploadAttributes.execute, cmd);
        succesfulFileUploadAction.perform();
        phaseInfo.assertListener(PhaseId.UPDATE_MODEL_VALUES, "executeChecker");
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

    @Test
    public void testLang() {
        testAttributeLang(page.fileUploadDiv);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12122")
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

    @Test
    public void testListHeight() {
        String height = "100px";
        fileUploadAttributes.set(FileUploadAttributes.listHeight, height);
        String cssValue = page.itemsToUploadDiv.getCssValue("height");
        assertEquals(cssValue, height, "List height was not changed");
    }

    @Test
    public void testMaxFilesQuantity() {
        final int maxFilesQuantity = 1;
        fileUploadAttributes.set(FileUploadAttributes.maxFilesQuantity, maxFilesQuantity);

        sendFileToInputWithWaiting(filenames[0], true);

        Graphene.waitGui().until(Graphene.element(page.addButton).not().isVisible());

        List<WebElement> filesToUpload = guardListSize(page.itemsToUpload, 1);
        assertTrue(filesToUpload.size() == maxFilesQuantity, "Files to upload list contains less/more files than there should be. List: " + filesToUpload + " .");
        for (int i = 0; i < maxFilesQuantity; i++) {
            String x = filesToUpload.get(i).getText();
            assertTrue(x.equals(filenames[i]), "Added file " + filenames[i] + " does not appear in files to upload list.");
        }

        waitRequest(Graphene.guardXhr(page.uploadButton), WaitRequestType.XHR).click();

        List<WebElement> uploadedFiles = guardListSize(page.uploadedFilesList, maxFilesQuantity);
        assertTrue(uploadedFiles.size() == maxFilesQuantity, "Uploaded files list contains more/less files than there should be. List: " + uploadedFiles + " .");
        for (int i = 0; i < maxFilesQuantity; i++) {
            assertTrue(uploadedFiles.get(i).getText().equals(filenames[i]),
                    "Uploaded file " + filenames[i] + " does not appear in uploadedList.");
        }
    }

    @Test
    public void testNoDuplicate() {
        fileUploadAttributes.set(FileUploadAttributes.noDuplicate, Boolean.TRUE);
        sendFileToInputWithWaiting(acceptableFile, true);
        sendFileToInputWithWaiting(acceptableFile, false);
        assertEquals(page.itemsToUpload.size(), 1);
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

    @Test
    public void testOnClear() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onclear, new Action() {
            @Override
            public void perform() {
                sendFileToInputWithWaiting(acceptableFile, true);
                Graphene.waitGui().until(Graphene.element(page.clearAllButton).isVisible());
                page.clearAllButton.click();
            }
        });
    }

    @Test
    public void testOnclick() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onclick, new Actions(driver).click(page.fileInputField).build());
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12037")
    @Test(groups = "4.Future")
    public void testOnComplete() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.oncomplete, succesfulFileUploadAction);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.ondblclick, new Actions(driver)
                .doubleClick(page.fileInputField).build());
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
    public void testOnFileSubmit() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onfilesubmit, succesfulFileUploadAction);
    }

    @Test
    public void testOnkeydown() {
        testFireEventWithJS(page.fileUploadDiv, fileUploadAttributes, FileUploadAttributes.onkeydown);
    }

    @Test
    public void testOnkeypress() {
        testFireEventWithJS(page.fileUploadDiv, fileUploadAttributes, FileUploadAttributes.onkeypress);
    }

    @Test
    public void testOnkeyup() {
        testFireEventWithJS(page.fileUploadDiv, fileUploadAttributes, FileUploadAttributes.onkeyup);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onmousedown, new Actions(driver).clickAndHold(page.fileInputField).build());
    }

    @Test
    public void testOnmousemove() {
        testFireEventWithJS(page.fileUploadDiv, fileUploadAttributes, FileUploadAttributes.onmousemove);
    }

    @Test
    public void testOnmouseout() {
        testFireEventWithJS(page.fileUploadDiv, fileUploadAttributes, FileUploadAttributes.onmouseout);
    }

    @Test
    public void testOnmouseover() {
        testFireEventWithJS(page.fileUploadDiv, fileUploadAttributes, FileUploadAttributes.onmouseover);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onmousedown, new Actions(driver).click(page.fileInputField).build());
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
    public void testOnUploadcomplete() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onuploadcomplete, succesfulFileUploadAction);
    }

    @Test
    public void testRendered() {
        fileUploadAttributes.set(FileUploadAttributes.rendered, Boolean.FALSE);
        assertFalse(Graphene.element(page.fileUploadDiv).isPresent().apply(driver), "Component should not be rendered when rendered=false.");
    }

    @Test(enabled = false)
    public void testServerErrorLabel() {
    }

    @Test
    public void testSizeExceededLabel() {
        testLabelChanges(page.uploadStatusLabel, fileUploadAttributes, FileUploadAttributes.sizeExceededLabel, new Action() {
            @Override
            public void perform() {
                sendFileWithWaiting(bigFile, true, false);
            }
        });
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
    public void testStyle() {
        testStyle(page.fileUploadDiv);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(page.fileUploadDiv);
    }

    @Test
    public void testTitle() {
        testTitle(page.fileUploadDiv);
    }

    @Test
    public void testUploadLabel() {
        testLabelChanges(page.uploadButton, fileUploadAttributes, FileUploadAttributes.uploadLabel, new Action() {
            @Override
            public void perform() {
                sendFileToInputWithWaiting(acceptableFile, true);
            }
        });
    }

    private class SuccesfulFileUploadAction implements Action {

        @Override
        public void perform() {
            sendFileWithWaiting(filenames[0], true, true);
        }
    }
}
