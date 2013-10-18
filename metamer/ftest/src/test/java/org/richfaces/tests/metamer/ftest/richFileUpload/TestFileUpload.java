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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.fileUploadAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestFileUpload extends AbstractFileUploadTest {

    private Action succesfulFileUploadAction = new SuccesfulFileUploadAction();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFileUpload/simple.xhtml");
    }

    @Test
    public void testInit() {

        assertVisible(fileUpload.advanced().getRootElement(), "File upload is not on the page.");
        assertVisible(fileUpload.advanced().getAddButtonElement(), "Add button should be on the page.");
        assertNotVisible(fileUpload.advanced().getDisabledAddButtonElement(), "Disabled add button should not be on the page.");
        assertNotVisible(fileUpload.advanced().getUploadButtonElement(), "Upload button should not be on the page.");
        assertNotVisible(fileUpload.advanced().getClearAllButtonElement(), "Clear all button should not be on the page.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12122")
    public void testSingleFileUpload() {
        sendFileToInputWithWaiting(filenames[0], true);

        assertEquals(fileUpload.advanced().getItems().size(), 1, "File not loaded");
        assertEquals(fileUpload.advanced().getItems().getItem(0).getFilename(), filenames[0], "Label with filename does not appear.");
        assertPresent(fileUpload.advanced().getItems().getItem(0).getClearOrDeleteElement(), "Clear button does not appear.");
        assertPresent(fileUpload.advanced().getUploadButtonElement(), "Upload button should be on the page.");
        assertPresent(fileUpload.advanced().getClearAllButtonElement(), "Clear all button should be on the page.");

        MetamerPage.waitRequest(fileUpload, WaitRequestType.XHR).upload();

        waitUntilUploadedFilesListShow(1);
        assertEquals(uploadedFilesList.size(), 1, "List of uploaded files should contain one file.");
        assertEquals(uploadedFilesList.get(0).getText(), filenames[0],
            "Uploaded file does not appear in uploadedList.");
    }

    @Test
    public void testAcceptedTypes() {
        String acceptable = "txt";
        fileUploadAttributes.set(FileUploadAttributes.acceptedTypes, acceptable);

        sendFileToInputWithWaiting(notAcceptableFile, false);
        assertTrue(fileUpload.advanced().getItems().isEmpty(), "AcceptedType does not work.");

        sendFileWithWaiting(acceptableFile, true, true);
        assertFalse(fileUpload.advanced().getItems().isEmpty(), "AcceptedType does not work.");
    }

    @Test
    @Templates("plain")
    public void testAddLabel() {
        testLabelChanges(fileUpload.advanced().getAddButtonElement(), fileUploadAttributes, FileUploadAttributes.addLabel, null);
    }

    @Test
    @Templates("plain")
    public void testClearAllLabel() {
        testLabelChanges(fileUpload.advanced().getClearAllButtonElement(), fileUploadAttributes, FileUploadAttributes.clearAllLabel, succesfulFileUploadAction);
    }

    @Test
    @Templates("plain")
    public void testClearLabel() {
        testLabelChanges(new FutureTarget<WebElement>() {
            @Override
            public WebElement getTarget() {
                return fileUpload.advanced().getItems().getItem(0).getClearOrDeleteElement();
            }
        }, fileUploadAttributes, FileUploadAttributes.clearLabel, succesfulFileUploadAction);
    }

    @IssueTracking(value = "https://issues.jboss.org/browse/RF-12039")
    @Test(groups = "Future")
    public void testData() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.data, succesfulFileUploadAction);
    }

    @Test
    @Templates("plain")
    public void testDeleteLabel() {
        testLabelChanges(new FutureTarget<WebElement>() {
            @Override
            public WebElement getTarget() {
                return fileUpload.advanced().getItems().getItem(0).getClearOrDeleteElement();
            }
        }, fileUploadAttributes, FileUploadAttributes.deleteLabel, new Action() {
            @Override
            public void perform() {
                sendFileToInputWithWaiting(acceptableFile, true);
            }
        });
    }

    @Test
    @Templates("plain")
    public void testDir() {
        testDir(fileUpload.advanced().getRootElement());
    }

    @Test
    @Templates("plain")
    public void testDisabled() {
        fileUploadAttributes.set(FileUploadAttributes.disabled, Boolean.TRUE);
        assertVisible(fileUpload.advanced().getRootElement(), "File upload is not on the page.");
        assertVisible(fileUpload.advanced().getDisabledAddButtonElement(), "Disabled add button should be on the page.");
        assertNotVisible(fileUpload.advanced().getAddButtonElement(), "Add button should not be on the page.");
        assertNotVisible(fileUpload.advanced().getUploadButtonElement(), "Upload button should not be on the page.");
        assertNotVisible(fileUpload.advanced().getClearAllButtonElement(), "Clear all button should not be on the page.");
    }

    @Test
    @Templates("plain")
    @RegressionTest("https://issues.jboss.org/browse/RF-12122")
    public void testDoneLabel() {
        testLabelChanges(new FutureTarget<WebElement>() {
            @Override
            public WebElement getTarget() {
                return fileUpload.advanced().getItems().getItem(0).getStateElement();
            }
        }, fileUploadAttributes, FileUploadAttributes.doneLabel, succesfulFileUploadAction);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12122")
    public void testExecute() {
        String cmd = "executeChecker";
        fileUploadAttributes.set(FileUploadAttributes.execute, cmd);
        succesfulFileUploadAction.perform();
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, "executeChecker");
    }

    @Test
    public void testImmediateUpload() {
        fileUploadAttributes.set(FileUploadAttributes.immediateUpload, Boolean.TRUE);
        sendFileToInputWithWaiting(filenames[0], true);
        waitUntilUploadedFilesListShow(1);
        assertEquals(uploadedFilesList.size(), 1);
        assertEquals(uploadedFilesList.get(0).getText(), filenames[0]);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "upload listener");
    }

    @Test
    @Templates("plain")
    public void testLang() {
        testAttributeLang(fileUpload.advanced().getRootElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12122")
    public void testLimitRender() {
        //sendFile depends on requestTime and uploadedFilesPanel
        fileUploadAttributes.set(FileUploadAttributes.render, "statusCheckerOutput, requestTime, uploadedFilesPanel");
        fileUploadAttributes.set(FileUploadAttributes.limitRender, Boolean.TRUE);

        String statusCheckerTime = page.getStatusCheckerOutputElement().getText();
        String renderCheckerTime = page.getRenderCheckerOutputElement().getText();

        succesfulFileUploadAction.perform();

        String statusCheckerTime2 = page.getStatusCheckerOutputElement().getText();
        String renderCheckerTime2 = page.getRenderCheckerOutputElement().getText();
        assertNotEquals(statusCheckerTime, statusCheckerTime2, "status checker time did not change as expected");
        assertEquals(renderCheckerTime, renderCheckerTime2, "render checker time changed as not expected");
    }

    @Test
    public void testListHeight() {
        String height = "100px";
        fileUploadAttributes.set(FileUploadAttributes.listHeight, height);
        String cssValue = fileUpload.advanced().getItems().getRoot().getCssValue("height");
        assertEquals(cssValue, height, "List height was not changed");
    }

    @Test
    public void testMaxFilesQuantity() {
        final int maxFilesQuantity = 1;
        fileUploadAttributes.set(FileUploadAttributes.maxFilesQuantity, maxFilesQuantity);

        sendFileToInputWithWaiting(filenames[0], true);

        Graphene.waitGui().until().element(fileUpload.advanced().getAddButtonElement()).is().not().visible();

        assertEquals(fileUpload.advanced().getItems().size(), maxFilesQuantity, "Files to upload list contains less/more files than there should be. List: " + fileUpload.advanced().getItems() + " .");
        for (int i = 0; i < maxFilesQuantity; i++) {
            String x = fileUpload.advanced().getItems().getItem(i).getFilename();
            assertEquals(x, filenames[i], "Added file " + filenames[i] + " does not appear in files to upload list.");
        }

        MetamerPage.waitRequest(fileUpload, WaitRequestType.XHR).upload();

        assertEquals(uploadedFilesList.size(), maxFilesQuantity, "Uploaded files list contains more/less files than there should be. List: " + uploadedFilesList + " .");
        for (int i = 0; i < maxFilesQuantity; i++) {
            assertTrue(uploadedFilesList.get(i).getText().equals(filenames[i]),
                "Uploaded file " + filenames[i] + " does not appear in uploadedList.");
        }
    }

    @Test
    public void testNoDuplicate() {
        fileUploadAttributes.set(FileUploadAttributes.noDuplicate, Boolean.TRUE);
        sendFileToInputWithWaiting(acceptableFile, true);
        sendFileToInputWithWaiting(acceptableFile, false);
        assertEquals(fileUpload.advanced().getItems().size(), 1);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12037")
    @Test(groups = "Future")
    public void testOnBeforeDOMUpdate() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onbeforedomupdate, succesfulFileUploadAction);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12037")
    @Test(groups = "Future")
    public void testOnBegin() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onbegin, succesfulFileUploadAction);
    }

    @Test
    @Templates("plain")
    public void testOnClear() {
        testFireEvent("clear", new Action() {
            @Override
            public void perform() {
                sendFileToInputWithWaiting(acceptableFile, true);
                fileUpload.advanced().removeFile(0);
            }
        });
    }

    @Test
    @Templates("plain")
    public void testOnclick() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onclick,
            new Actions(driver).click(fileUpload.advanced().getFileInputElement()).build());
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12037")
    @Test(groups = "Future")
    public void testOnComplete() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.oncomplete, succesfulFileUploadAction);
    }

    @Test
    @Templates("plain")
    public void testOndblclick() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.ondblclick, new Actions(driver)
            .doubleClick(fileUpload.advanced().getFileInputElement()).build());
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
    @Templates("plain")
    public void testOnkeydown() {
        testFireEventWithJS(fileUpload.advanced().getRootElement(), fileUploadAttributes, FileUploadAttributes.onkeydown);
    }

    @Test
    @Templates("plain")
    public void testOnkeypress() {
        testFireEventWithJS(fileUpload.advanced().getRootElement(), fileUploadAttributes, FileUploadAttributes.onkeypress);
    }

    @Test
    @Templates("plain")
    public void testOnkeyup() {
        testFireEventWithJS(fileUpload.advanced().getRootElement(), fileUploadAttributes, FileUploadAttributes.onkeyup);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onmousedown, new Actions(driver).clickAndHold(fileUpload.advanced().getFileInputElement()).build());
        // mouse button needs to be released
        new Actions(driver).release().perform();
    }

    @Test
    @Templates("plain")
    public void testOnmousemove() {
        testFireEventWithJS(fileUpload.advanced().getRootElement(), fileUploadAttributes, FileUploadAttributes.onmousemove);
    }

    @Test
    @Templates("plain")
    public void testOnmouseout() {
        testFireEventWithJS(fileUpload.advanced().getRootElement(), fileUploadAttributes, FileUploadAttributes.onmouseout);
    }

    @Test
    @Templates("plain")
    public void testOnmouseover() {
        testFireEventWithJS(fileUpload.advanced().getRootElement(), fileUploadAttributes, FileUploadAttributes.onmouseover);
    }

    @Test
    @Templates("plain")
    public void testOnmouseup() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onmousedown, new Actions(driver).click(fileUpload.advanced().getFileInputElement()).build());
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
    @Templates("plain")
    public void testRendered() {
        fileUploadAttributes.set(FileUploadAttributes.rendered, Boolean.FALSE);
        assertFalse(new WebElementConditionFactory(fileUpload.advanced().getRootElement()).isPresent().apply(driver), "Component should not be rendered when rendered=false.");
    }

    @Test(enabled = false)
    public void testServerErrorLabel() {
        //TODO how to test it
    }

    @Test
    public void testSizeExceededLabel() {
        testLabelChanges(new FutureTarget<WebElement>() {
            @Override
            public WebElement getTarget() {
                return fileUpload.advanced().getItems().getItem(0).getStateElement();
            }
        }, fileUploadAttributes, FileUploadAttributes.sizeExceededLabel, new Action() {
            @Override
            public void perform() {
                sendFileWithWaiting(bigFile, true, false);
            }
        });
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12879")
    public void testStatus() {
        testStatus(succesfulFileUploadAction);
    }

    @Test
    @Templates("plain")
    public void testStyle() {
        testStyle(fileUpload.advanced().getRootElement());
    }

    @Test
    @Templates("plain")
    public void testStyleClass() {
        testStyleClass(fileUpload.advanced().getRootElement());
    }

    @Test
    @Templates("plain")
    public void testTitle() {
        testTitle(fileUpload.advanced().getRootElement());
    }

    @Test
    @Templates("plain")
    public void testUploadLabel() {
        testLabelChanges(fileUpload.advanced().getUploadButtonElement(), fileUploadAttributes, FileUploadAttributes.uploadLabel, new Action() {
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
