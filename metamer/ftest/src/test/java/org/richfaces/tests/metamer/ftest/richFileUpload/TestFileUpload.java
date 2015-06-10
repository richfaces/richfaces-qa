/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestFileUpload extends AbstractFileUploadTest {

    private final Attributes<FileUploadAttributes> fileUploadAttributes = getAttributes();

    private final Action succesfulFileUploadAction = new SuccesfulFileUploadAction();

    private void _testFireEventWithJS(FileUploadAttributes testedAttribute, WebElement element) {
        // same as 'testFireEventWithJS', but added a ';return false;' to the attribute value to not open the file dialog
        fileUploadAttributes.set(testedAttribute, "metamerEvents += \"" + testedAttribute.toString() + " \";return false;");
        executeJS("metamerEvents = \"\";");
        Event e = new Event(testedAttribute.toString().substring(2));// remove prefix "on"
        fireEvent(element, e);
        String returnedString = expectedReturnJS("return metamerEvents", testedAttribute.toString());
        assertEquals(returnedString, testedAttribute.toString(), "Event " + e + " does not work.");
    }

    @Override
    public String getComponentTestPagePath() {
        return "richFileUpload/simple.xhtml";
    }

    @Test(groups = { "smoke" })
    @CoversAttributes("acceptedTypes")
    @RegressionTest("https://issues.jboss.org/browse/RFPL-3503")
    public void testAcceptedTypes() {
        String acceptable = "txt";
        fileUploadAttributes.set(FileUploadAttributes.acceptedTypes, acceptable);

        sendFileToInputWithWaiting(notAcceptableFile, false);
        assertTrue(fileUpload.advanced().getItems().isEmpty(), "AcceptedType does not work.");

        sendFileWithWaiting(acceptableFile, true, true);
        assertFalse(fileUpload.advanced().getItems().isEmpty(), "AcceptedType does not work.");
    }

    @Test
    @CoversAttributes("addLabel")
    @Templates("plain")
    public void testAddLabel() {
        testLabelChanges(fileUpload.advanced().getAddButtonElement(), fileUploadAttributes, FileUploadAttributes.addLabel, null);
    }

    @Test
    @CoversAttributes("clearAllLabel")
    @RegressionTest("https://issues.jboss.org/browse/RFPL-3503")
    @Templates("plain")
    public void testClearAllLabel() {
        testLabelChanges(fileUpload.advanced().getClearAllButtonElement(), fileUploadAttributes, FileUploadAttributes.clearAllLabel, succesfulFileUploadAction);
    }

    @Test
    @CoversAttributes("clearLabel")
    @RegressionTest("https://issues.jboss.org/browse/RFPL-3503")
    @Templates("plain")
    public void testClearLabel() {
        testLabelChanges(new FutureTarget<WebElement>() {
            @Override
            public WebElement getTarget() {
                return fileUpload.advanced().getItems().getItem(0).getClearOrDeleteElement();
            }
        }, fileUploadAttributes, FileUploadAttributes.clearLabel, succesfulFileUploadAction);
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-3503", "https://issues.jboss.org/browse/RF-12039" })
    @CoversAttributes("data")
    public void testData() {
        testData(succesfulFileUploadAction);
    }

    @Test
    @CoversAttributes("deleteLabel")
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
    @CoversAttributes("dir")
    @Templates("plain")
    public void testDir() {
        testDir(fileUpload.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("disabled")
    @Templates("plain")
    public void testDisabled() {
        fileUploadAttributes.set(FileUploadAttributes.disabled, Boolean.TRUE);
        assertVisible(fileUpload.advanced().getRootElement(), "File upload is not on the page.");
        assertVisible(fileUpload.advanced().getDisabledAddButtonElement(), "Disabled add button should be on the page.");
        assertNotVisible(fileUpload.advanced().getAddButtonElement(), "Add button should not be on the page.");
        assertNotVisible(fileUpload.advanced().getUploadButtonElement(), "Upload button should not be on the page.");
        assertNotVisible(fileUpload.advanced().getClearAllButtonElement(), "Clear all button should not be on the page.");
    }

    @Test(groups = { "smoke" })
    @CoversAttributes("doneLabel")
    @Templates("plain")
    @RegressionTest(value = { "https://issues.jboss.org/browse/RF-12122", "https://issues.jboss.org/browse/RFPL-3503" })
    public void testDoneLabel() {
        testLabelChanges(new FutureTarget<WebElement>() {
            @Override
            public WebElement getTarget() {
                return fileUpload.advanced().getItems().getItem(0).getStateElement();
            }
        }, fileUploadAttributes, FileUploadAttributes.doneLabel, succesfulFileUploadAction);
    }

    @Test
    @CoversAttributes("execute")
    @RegressionTest(value = { "https://issues.jboss.org/browse/RF-12122", "https://issues.jboss.org/browse/RFPL-3503" })
    public void testExecute() {
        String cmd = "executeChecker";
        fileUploadAttributes.set(FileUploadAttributes.execute, cmd);
        succesfulFileUploadAction.perform();
        getMetamerPage().assertListener(PhaseId.UPDATE_MODEL_VALUES, "executeChecker");
    }

    @Test(groups = { "smoke" })
    @CoversAttributes("immediateUpload")
    @RegressionTest("https://issues.jboss.org/browse/RFPL-3503")
    public void testImmediateUpload() {
        fileUploadAttributes.set(FileUploadAttributes.immediateUpload, Boolean.TRUE);
        sendFileToInputWithWaiting(filenames[0], true);
        waitUntilUploadedFilesListShow(1);
        assertEquals(fileUpload.advanced().getItems().size(), 1);
        assertEquals(fileUpload.advanced().getItems().getItem(0).getFilename(), filenames[0]);
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "upload listener");
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
    @CoversAttributes("lang")
    @Templates("plain")
    public void testLang() {
        testLang(fileUpload.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("limitRender")
    @RegressionTest(value = { "https://issues.jboss.org/browse/RF-12122", "https://issues.jboss.org/browse/RFPL-3503" })
    public void testLimitRender() {
        //sendFile depends on requestTime and uploadedFilesPanel
        fileUploadAttributes.set(FileUploadAttributes.render, "statusCheckerOutput, requestTime, uploadedFilesPanel");
        fileUploadAttributes.set(FileUploadAttributes.limitRender, Boolean.TRUE);

        String statusCheckerTime = getMetamerPage().getStatusCheckerOutputElement().getText();
        String renderCheckerTime = getMetamerPage().getRenderCheckerOutputElement().getText();

        succesfulFileUploadAction.perform();

        String statusCheckerTime2 = getMetamerPage().getStatusCheckerOutputElement().getText();
        String renderCheckerTime2 = getMetamerPage().getRenderCheckerOutputElement().getText();
        assertNotEquals(statusCheckerTime, statusCheckerTime2, "status checker time did not change as expected");
        assertEquals(renderCheckerTime, renderCheckerTime2, "render checker time changed as not expected");
    }

    @Test
    @CoversAttributes("listHeight")
    @Templates(value = "plain")
    public void testListHeight() {
        String height = "100px";
        fileUploadAttributes.set(FileUploadAttributes.listHeight, height);
        String cssValue = fileUpload.advanced().getItems().getRootElement().getCssValue("height");
        assertEquals(cssValue, height, "List height was not changed");
    }

    @Test
    @CoversAttributes("maxFileSize")
    public void testMaxFileSize() {
        long fileSize = createFileFromString(acceptableFile).length();
        // set limit to lower number than file size
        fileUploadAttributes.set(FileUploadAttributes.maxFileSize, fileSize - 5);
        // file will not be accepted
        sendFileToInputWithWaiting(acceptableFile, false);
        assertEquals(fileUpload.advanced().getItems().size(), 0);

        // set limit to greater number than file size
        fileUploadAttributes.set(FileUploadAttributes.maxFileSize, fileSize + 1000);
        // file will be accepted and uploaded
        sendFileWithWaiting(acceptableFile, true, true);
        assertEquals(fileUpload.advanced().getItems().size(), 1);
        fileUpload.clearAll();

        // set limit to maximum size
        fileUploadAttributes.set(FileUploadAttributes.maxFileSize, 0);
        // small file will be accepted and uploaded
        sendFileWithWaiting(acceptableFile, true, true);
        assertEquals(fileUpload.advanced().getItems().size(), 1);
        fileUpload.clearAll();
        // big file (size exceeding file upload limit in web.xml) will be accepted, but not uploaded
        sendFileWithWaiting(bigFile, true, false);
    }

    @Test
    @CoversAttributes("maxFilesQuantity")
    @RegressionTest("https://issues.jboss.org/browse/RFPL-3503")
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

        assertEquals(fileUpload.advanced().getItems().size(), maxFilesQuantity, "Uploaded files list contains more/less files than there should be. List: " + fileUpload.advanced().getItems() + " .");
        for (int i = 0; i < maxFilesQuantity; i++) {
            assertTrue(fileUpload.advanced().getItems().getItem(i).getFilename().equals(filenames[i]),
                "Uploaded file " + filenames[i] + " does not appear in uploadedList.");
        }
    }

    @Test
    @CoversAttributes("noDuplicate")
    public void testNoDuplicate() {
        fileUploadAttributes.set(FileUploadAttributes.noDuplicate, Boolean.TRUE);
        sendFileToInputWithWaiting(acceptableFile, true);
        sendFileToInputWithWaiting(acceptableFile, false);
        assertEquals(fileUpload.advanced().getItems().size(), 1);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12037")
    @CoversAttributes("onbeforedomupdate")
    public void testOnbeforedomupdate() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onbeforedomupdate, succesfulFileUploadAction);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12037")
    @CoversAttributes("onbegin")
    public void testOnbegin() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onbegin, succesfulFileUploadAction);
    }

    @Test
    @CoversAttributes("onclear")
    @Templates("plain")
    public void testOnclear() {
        testFireEvent("clear", new Action() {
            @Override
            public void perform() {
                sendFileToInputWithWaiting(acceptableFile, true);
                fileUpload.advanced().removeFile(0);
            }
        });
    }

    @Test
    @CoversAttributes("onclick")
    @Templates("plain")
    public void testOnclick() {
        _testFireEventWithJS(FileUploadAttributes.onclick, fileUpload.advanced().getFileInputElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12037")
    @CoversAttributes("oncomplete")
    public void testOncomplete() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.oncomplete, succesfulFileUploadAction);
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates("plain")
    public void testOndblclick() {
        _testFireEventWithJS(FileUploadAttributes.ondblclick, fileUpload.advanced().getFileInputElement());
    }

    @Test
    @CoversAttributes("onfileselect")
    public void testOnfileselect() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onfileselect, new Action() {
            @Override
            public void perform() {
                sendFileToInputWithWaiting(filenames[0], true);
            }
        });
    }

    @Test
    @CoversAttributes("onfilesubmit")
    @RegressionTest("https://issues.jboss.org/browse/RFPL-3503")
    public void testOnfilesubmit() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onfilesubmit, succesfulFileUploadAction);
    }

    @Test
    @CoversAttributes("onkeydown")
    @Templates("plain")
    public void testOnkeydown() {
        testFireEventWithJS(fileUpload.advanced().getRootElement(), fileUploadAttributes, FileUploadAttributes.onkeydown);
    }

    @Test
    @CoversAttributes("onkeypress")
    @Templates("plain")
    public void testOnkeypress() {
        testFireEventWithJS(fileUpload.advanced().getRootElement(), fileUploadAttributes, FileUploadAttributes.onkeypress);
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates("plain")
    public void testOnkeyup() {
        testFireEventWithJS(fileUpload.advanced().getRootElement(), fileUploadAttributes, FileUploadAttributes.onkeyup);
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        _testFireEventWithJS(FileUploadAttributes.onmousedown, fileUpload.advanced().getFileInputElement());
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates("plain")
    public void testOnmousemove() {
        testFireEventWithJS(fileUpload.advanced().getRootElement(), fileUploadAttributes, FileUploadAttributes.onmousemove);
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates("plain")
    public void testOnmouseout() {
        testFireEventWithJS(fileUpload.advanced().getRootElement(), fileUploadAttributes, FileUploadAttributes.onmouseout);
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates("plain")
    public void testOnmouseover() {
        testFireEventWithJS(fileUpload.advanced().getRootElement(), fileUploadAttributes, FileUploadAttributes.onmouseover);
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates("plain")
    public void testOnmouseup() {
        _testFireEventWithJS(FileUploadAttributes.onmouseup, fileUpload.advanced().getFileInputElement());
    }

    @Test
    @CoversAttributes("onsizerejected")
    @Templates("plain")
    public void testOnsizerejected() {
        fileUploadAttributes.set(FileUploadAttributes.maxFileSize, 1);
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onsizerejected, new Action() {
            @Override
            public void perform() {
                sendFileToInputWithWaiting(acceptableFile, false);
            }
        });
    }

    @Test
    @CoversAttributes("ontyperejected")
    public void testOntyperejected() {
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
    @CoversAttributes("onuploadcomplete")
    @RegressionTest("https://issues.jboss.org/browse/RFPL-3503")
    public void testOnuploadcomplete() {
        testFireEvent(fileUploadAttributes, FileUploadAttributes.onuploadcomplete, succesfulFileUploadAction);
    }

    @Test
    @CoversAttributes("rendered")
    @Templates("plain")
    public void testRendered() {
        fileUploadAttributes.set(FileUploadAttributes.rendered, Boolean.FALSE);
        assertFalse(new WebElementConditionFactory(fileUpload.advanced().getRootElement()).isPresent().apply(driver), "Component should not be rendered when rendered=false.");
    }

    @Test(enabled = false)
    @CoversAttributes("serverErrorLabel")
    @Templates("plain")
    public void testServerErrorLabel() {
        //TODO how to test it
    }

    @Test
    @RegressionTest(value = { "https://issues.jboss.org/browse/RF-12122", "https://issues.jboss.org/browse/RFPL-3503" })
    public void testSingleFileUpload() {
        sendFileToInputWithWaiting(filenames[0], true);

        assertEquals(fileUpload.advanced().getItems().size(), 1, "File not loaded");
        assertEquals(fileUpload.advanced().getItems().getItem(0).getFilename(), filenames[0], "Label with filename does not appear.");
        assertPresent(fileUpload.advanced().getItems().getItem(0).getClearOrDeleteElement(), "Clear button does not appear.");
        assertPresent(fileUpload.advanced().getUploadButtonElement(), "Upload button should be on the page.");
        assertPresent(fileUpload.advanced().getClearAllButtonElement(), "Clear all button should be on the page.");

        MetamerPage.waitRequest(fileUpload, WaitRequestType.XHR).upload();

        waitUntilUploadedFilesListShow(1);

        assertEquals(fileUpload.advanced().getItems().size(), 1, "List of uploaded files should contain one file.");
        assertEquals(fileUpload.advanced().getItems().getItem(0).getFilename(), filenames[0],
            "Uploaded file does not appear in uploadedList.");
    }

    @Test
    @CoversAttributes("sizeExceededLabel")
    @RegressionTest("https://issues.jboss.org/browse/RFPL-3503")
    @Templates("plain")
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

    @Test
    @CoversAttributes("status")
    @RegressionTest(value = { "https://issues.jboss.org/browse/RF-12879", "https://issues.jboss.org/browse/RF-13994", "https://issues.jboss.org/browse/RFPL-3503" })
    public void testStatus() {
        testStatus(succesfulFileUploadAction);
    }

    @Test
    @CoversAttributes("style")
    @Templates("plain")
    public void testStyle() {
        testStyle(fileUpload.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates("plain")
    public void testStyleClass() {
        testStyleClass(fileUpload.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("title")
    @Templates("plain")
    public void testTitle() {
        testTitle(fileUpload.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("uploadLabel")
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
