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

import static org.richfaces.tests.metamer.ftest.richFileUpload.AbstractFileUploadTest.acceptableFile;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.MetamerAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.richProgressBar.ProgressBarAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.utils.ElementVisibilityObserver;
import org.richfaces.tests.metamer.ftest.webdriver.utils.ElementVisibilityObserver.Record;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestFileUploadProgressFacet extends AbstractFileUploadTest {

    @FindBy(css = ".rf-pb[id$=customProgressBar]")
    private WebElement customPB;

    @Override
    public String getComponentTestPagePath() {
        return "richFileUpload/progressFacet.xhtml";
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-3503", "https://issues.jboss.org/browse/RFPL-2263" })
    public void testCustomProgressBarOnFinish() {
        Attributes<ProgressBarAttributes> pbAtts = getAttributes("attributesProgressBar");
        testFireEvent(pbAtts, ProgressBarAttributes.onfinish, new Action() {
            @Override
            public void perform() {
                sendFileWithWaiting(acceptableFile, true, true);

            }
        });
        assertNotVisible(customPB, "Custom progress bar should not be displayed after upload is finished.");
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-3503", "https://issues.jboss.org/browse/RFPL-2263" })
    public void testCustomProgressBarPresence() {
        assertPresent(customPB, "No custom progress bar is present on page.");
        assertNotVisible(customPB, "Custom progress bar should not be displayed now.");

        // set responseDelay to have enough time to check the progressBar
        long delay = 1000;
        getMetamerAttributes().set(MetamerAttributes.metamerResponseDelay, delay);

        sendFileToInputWithWaiting(acceptableFile, true);
        ElementVisibilityObserver observer = ElementVisibilityObserver.getInstance();
        observer.watchForVisibilityChangeOfElement(customPB);
        fileUpload.upload();
        waitUntilUploadedFilesListShow(1);

        List<Record> records = observer.getRecords();
        assertEquals(records.size(), 2);
        assertTrue(records.get(0).isVisible(), "First change should be to element is visible.");
        assertFalse(records.get(1).isVisible(), "Second change should be to element is not visible.");

        int tolerance = 800;
        long diff = records.get(1).getTime().minus(records.get(0).getTime().getMillis()).getMillis();
        assertEquals(diff, delay, tolerance, "Delay between two changes should be around 1 s (response delay).");
    }
}
