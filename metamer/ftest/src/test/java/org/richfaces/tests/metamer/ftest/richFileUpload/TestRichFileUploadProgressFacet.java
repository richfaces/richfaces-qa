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
import static org.testng.Assert.assertTrue;

import java.net.URL;
import org.jboss.test.selenium.support.ui.ElementDisplayed;
import org.jboss.test.selenium.support.ui.ElementNotDisplayed;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRichFileUploadProgressFacet extends AbstractFileUploadWebDriverTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFileUpload/progressFacet.xhtml");
    }

    @Test
    public void testCustomProgressBarPresenceBeforeFinishedUpload() {
        assertTrue(ElementPresent.getInstance().element(page.customPB).apply(driver), "No custom progress bar is present on page.");
        assertTrue(ElementNotDisplayed.getInstance().element(page.customPB).apply(driver), "Custom progress bar should not be displayed now.");

        //stop page refreshing/rendering after file is sent
        fileUploadAttributes.set(FileUploadAttributes.onfilesubmit, "window.stop()");

        //send file to server, the file will not be shown in uploaded files list, because we stop the rendering before it
        sendFile(acceptableFile, true, false);

        new WebDriverWait(driver, 5).failWith("Progress bar should be displayed.").until(ElementDisplayed.getInstance().element(page.customPB));
    }

    @Test
    public void testCustomProgressBarPresenceAfterFinishedUpload() {
        //send file to server
        sendFile(acceptableFile, true, true);

        new WebDriverWait(driver, 5).failWith("Done label should be on displayed after upload.").until(ElementDisplayed.getInstance().element(page.uploadStatusLabel));
        new WebDriverWait(driver, 5).failWith("Progress bar should not be displayed after upload is completed.").until(ElementNotDisplayed.getInstance().element(page.customPB));
    }
}
