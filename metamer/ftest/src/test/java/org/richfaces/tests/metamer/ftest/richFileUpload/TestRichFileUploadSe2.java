/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2012, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
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
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richFileUpload;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichFileUploadSe2 extends AbstractWebDriverTest {

    private final String uploadedFilesList = "span[id$=uploadedFilesPanel] li";
    private final String fileInputField = "input[type=file].rf-fu-inp:last-child";
    private final String fileInputField2 = "input[type=file].rf-fu-inp";
    private final String uploadButton = "span.rf-fu-btn-upl";
    private final String clearAllButton = "span.rf-fu-btn-clr";
    private final String itemsToUpload = "div.rf-fu-itm .rf-fu-itm-lbl";
    private final String itemClear = "div.rf-fu-itm .rf-fu-itm-rgh a";
    private final String notAcceptableFile = "file1.x";
    private final String acceptableFile = "file1.txt";
    private final String bigFile = "bigFile.txt";
    private final String[] filenames = { acceptableFile, "file2.txt", bigFile, notAcceptableFile };
    private final String ap = "\"";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFileUpload/simple.xhtml");
    }

    private boolean isElementPresent(String css) {
        try {
            driver.findElement(By.cssSelector(css));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isElementVisible(String css) {
        try {
            return driver.findElement(By.cssSelector(css)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void sendFileToInput(String filename) {
        File file = null;
        try {
            file = new File(TestRichFileUploadSe2.class.getResource(filename).toURI());
        } catch (URISyntaxException ex) {
        }
        assertTrue(file != null, "File does not exist.");
        assertTrue(file.exists(), "File does not exist.");

        waitForEnabledWE(fileInputField).sendKeys(file.getAbsolutePath());
    }

    private void sendFile(String filename) {
        sendFileToInput(filename);
        waitForEnabledVisibleWE(uploadButton).click();
    }

    @Test
    @Templates(exclude = { "richExtendedDataTable", "richCollapsibleSubTable" })
    public void testSingleFileUpload() {
        sendFileToInput(filenames[0]);

        List<WebElement> fileToUpload = waitForWEList(itemsToUpload);
        assertTrue(fileToUpload.size() == 1, "File not loaded");
        assertTrue(fileToUpload.get(0).getText().equals(filenames[0]), "Label with filename does not appear.");
        assertTrue(isElementPresent(itemClear), "Clear button does not appear.");
        assertTrue(isElementVisible(uploadButton), "Upload button should be on the page.");
        assertTrue(isElementVisible(clearAllButton), "Clear all button should be on the page.");

        waitForEnabledVisibleWE(uploadButton).click();

        List<WebElement> uploadedFiles = waitForWEList(uploadedFilesList);
        assertTrue(uploadedFiles.size() == 1, "No uploaded files.");
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
        sendAndSubmit("input[type=text][id*=acceptedTypesInput]", acceptable);

        sendFileToInput(notAcceptableFile);

        List<WebElement> filesToUpload = waitForWEList(itemsToUpload, 0);
        assertTrue(filesToUpload.isEmpty(), "AcceptedType does not work.");

        sendFile(acceptableFile);

        filesToUpload = waitForWEList(itemsToUpload, 0);
        assertFalse(filesToUpload.isEmpty(), "AcceptedType does not work.");
    }

    @IssueTracking(value = "https://issues.jboss.org/browse/RF-12039")
    @Test(groups = "4.Future")
    public void testData() {
        String testData = "Richfaces 4";
        sendAndSubmit("input[type=text][id$=dataInput]", testData);
        sendAndSubmit("input[type=text][id$=oncompleteInput]", "data = event.data");

        executeJS("window.data = \"\";");

        sendFile(filenames[0]);

        String data = (String) executeJS("return window.data");
        assertEquals(data, testData);
    }

    @Test
    @Templates(exclude = { "richExtendedDataTable", "richCollapsibleSubTable" })
    public void testDoneLabel() {
        String doneLabel = "Done and done";
        sendAndSubmit("input[type=text][id$=doneLabelInput]", doneLabel);

        sendFile(filenames[0]);

        String readDoneLabel = waitForWEWithExpectedText("span.rf-fu-itm-st", doneLabel).getText();
        assertEquals(readDoneLabel, doneLabel);
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
        sendAndSubmit("input[type=text][id$=onfilesubmitInput]", "metamerEvents += \"filesubmit \"");

        executeJS("window.metamerEvents = \"\";");

        sendFile(filenames[0]);

        String event = (String) executeJS("return window.metamerEvents");
        event = event.trim();

        assertEquals(event, "filesubmit", "Attribute onfilesubmit doesn't work");
    }

    @Test
    public void testOnUploadcomplete() {
        sendAndSubmit("input[type=text][id$=onuploadcompleteInput]", "metamerEvents += \"uploadcomplete \"");

        executeJS("window.metamerEvents = \"\";");

        sendFile(filenames[0]);

        String event = (String) executeJS("return window.metamerEvents");
        event = event.trim();

        assertEquals(event, "uploadcomplete", "Attribute onuploadcomplete doesn't work");
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12037")
    @Test(groups = "4.Future")
    public void testEvents() {
        sendAndSubmit("input[type=text][id$=onbeforedomupdateInput]", "metamerEvents += \"beforedomupdate \"");
        sendAndSubmit("input[type=text][id$=onbeginInput]", "metamerEvents += \"begin \"");
        sendAndSubmit("input[type=text][id$=oncompleteInput]", "metamerEvents += \"complete \"");

        executeJS("window.metamerEvents = \"\";");

        sendFile(filenames[0]);

        String event = (String) executeJS("return window.metamerEvents");
        String[] events = event.split(" ");

        assertEquals(events.length, 3, "3 events should be fired.");
        assertEquals(events[0], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[1], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[2], "complete", "Attribute oncomplete doesn't work");
    }

    @Test
    public void testOnTypeRejected() {
        String varName = "reject";
        String testData = "RichFaces 4";
        String acceptable = "txt";
        String cmd = varName + " = " + ap + testData + ap;

        sendAndSubmit("input[type=text][id$=ontyperejectedInput]", cmd);
        sendAndSubmit("input[type=text][id*=acceptedTypesInput]", acceptable);

        File file = null;
        try {
            file = new File(TestRichFileUploadSe2.class.getResource(notAcceptableFile).toURI());
        } catch (URISyntaxException ex) {
        }
        assertTrue(file != null, "File does not exist.");
        assertTrue(file.exists(), "File does not exist.");

        executeJS(varName + " = " + ap + ap);

        waitForEnabledWE(fileInputField).sendKeys(file.getAbsolutePath());

        String data = (String) executeJS("return window." + varName);
        assertEquals(data, testData);
    }

    @Test
    @Templates(exclude = { "richExtendedDataTable", "richCollapsibleSubTable" })
    public void testExecute() {
        String cmd = "executeChecker";
        String expectedValue = "* executeChecker";
        String phasesLocator = "ul.phases-list li";
        sendAndSubmit("input[type=text][id$=executeInput]", cmd);

        sendFile(filenames[0]);

        List<WebElement> phases = waitForWEList(phasesLocator, 8);// 8 = number of expected phases
        for (WebElement webElement : phases) {
            String value = webElement.getText();
            if (value.equals(expectedValue)) {
                return;
            }
        }
        fail("Attribute execute does not work");
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
        sendAndSubmit("input[type=text][id$=renderInput]", "renderChecker");
        waitForEnabledVisibleWE("input[type=radio][id*=limitRenderInput][value=true]").click();

        String statusCheckerTime = waitForEnabledVisibleWE("span[id$=statusCheckerOutput]").getText();
        String renderCheckerTime = waitForEnabledVisibleWE("span[id$=renderChecker]").getText();

        sendFile(filenames[0]);

        String statusCheckerTime2 = waitForEnabledVisibleWE("span[id$=statusCheckerOutput]").getText();
        String renderCheckerTime2 = waitForEnabledVisibleWE("span[id$=renderChecker]").getText();

        assertEquals(statusCheckerTime, statusCheckerTime2);
        assertNotEquals(renderCheckerTime, renderCheckerTime2);
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
        String statusCheckerLocator = "span[id$=statusCheckerOutput]";
        String cmd = ap + "statusChecker" + ap;
        sendAndSubmit("input[type=text][id$=statusInput]", cmd);

        String statusCheckerTime1 = waitForEnabledVisibleWE(statusCheckerLocator).getText();

        sendFile(filenames[0]);

        String statusCheckerTime2 = waitForEnabledVisibleWE(statusCheckerLocator).getText();

        assertEquals(statusCheckerTime1, statusCheckerTime2);
    }

    @Test
    public void testSizeExceededLabel() {
        String testData = "size exceeded";
        sendAndSubmit("input[type=text][id$=sizeExceededLabelInput]", testData);

        File file = null;
        try {
            file = new File(TestRichFileUploadSe2.class.getResource(bigFile).toURI());
        } catch (URISyntaxException ex) {
        }
        assertTrue(file != null, "File does not exist.");
        assertTrue(file.exists(), "File does not exist.");

        waitForEnabledWE(fileInputField).sendKeys(file.getAbsolutePath());

        waitForEnabledVisibleWE(uploadButton).click();

        assertEquals(testData, waitForEnabledVisibleWE("span.rf-fu-itm-st").getText(),
            "Attribute sizeExceededLabel does not work.");
    }

    /**
     * Test disabled due to selenium problems
     */
    @Test(enabled = false)
    public void testMultiFileUploadWithMaxFilesQuantity() {
        final int maxFilesQuantity = 2;
        final int testedFilesCount = filenames.length;
        assertTrue(testedFilesCount > maxFilesQuantity);

        sendAndSubmit("input[type=text][id$=maxFilesQuantityInput]", String.valueOf(maxFilesQuantity));

        List<File> files = new ArrayList<File>();
        try {
            for (String fs : filenames) {
                files.add(new File(TestRichFileUploadSe2.class.getResource(fs).toURI()));
            }
        } catch (URISyntaxException ex) {
        }
        assertTrue(files.size() == filenames.length, "Some file(s) does not exist.");

        for (int i = 0; i < files.size(); i++) {
            waitForEnabledWE(fileInputField).sendKeys(files.get(i).getAbsolutePath());
            //            Other way:
            //
            //            List<WebElement> waitForWEList = waitForWEList(fileInputField2, i + 1);
            //            for (WebElement webElement : waitForWEList) {
            //                if (webElement.isEnabled() && !webElement.getAttribute("style").equals("display: none;")) {
            //                    webElement.sendKeys(files.get(i).getAbsolutePath());
            //                }
            //            }
        }

        List<WebElement> filesToUpload = waitForWEList(itemsToUpload, maxFilesQuantity);
        assertTrue(filesToUpload.size() == maxFilesQuantity, "MaxFilesQuantity is not working");
        for (int i = 0; i < maxFilesQuantity; i++) {
            String x = filesToUpload.get(i).getText();
            assertTrue(x.equals(filenames[i]), "Uploaded file does not appear in uploadedList.");
        }

        waitForEnabledWE(uploadButton).click();

        List<WebElement> uploadedFiles = waitForWEList(uploadedFilesList, maxFilesQuantity);

        assertTrue(uploadedFiles.size() == maxFilesQuantity, "Uploades more files than was maxFilesQuantity");
        for (int i = 0; i < maxFilesQuantity; i++) {
            assertTrue(uploadedFiles.get(i).getText().equals(filenames[i]),
                "Uploaded file does not appear in uploadedList.");
        }
    }
}
