/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestFileUploadProgressFacet extends AbstractFileUploadTest {

    private final Attributes<FileUploadAttributes> fileUploadAttributes = getAttributes();

    @FindBy(css = "div[id$=customProgressBar]")
    private WebElement customPB;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFileUpload/progressFacet.xhtml");
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RFPL-3503")
    public void testCustomProgressBarPresenceBeforeFinishedUpload() {
        assertPresent(customPB, "No custom progress bar is present on page.");
        assertNotVisible(customPB, "Custom progress bar should not be displayed now.");

        // stop page refreshing/rendering after file is sent
        fileUploadAttributes.set(FileUploadAttributes.onfilesubmit, "window.stop()");

        // send file to server, the file will not be shown in uploaded files list, because we stop the rendering before
        // it
        sendFileWithWaiting(acceptableFile, true, false);

        Graphene.waitGui().withMessage("Custom progress bar should be displayed now.").until().element(customPB)
            .is().visible();
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RFPL-3503")
    public void testCustomProgressBarPresenceAfterFinishedUpload() {
        // send file to server
        sendFileWithWaiting(acceptableFile, true, true);

        Graphene.waitGui().withMessage("Done label should be displayed after upload.").until()
            .element(fileUpload.advanced().getItems().getItem(0).getStateElement()).is().visible();
        Graphene.waitGui().withMessage("Progress bar should not be displayed after upload is completed.").until()
            .element(customPB).is().not().visible();
    }
}
