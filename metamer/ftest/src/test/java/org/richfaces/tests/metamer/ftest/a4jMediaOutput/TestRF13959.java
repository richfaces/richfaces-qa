/**
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
package org.richfaces.tests.metamer.ftest.a4jMediaOutput;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.bean.issues.RF13959;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF13959 extends AbstractMediaOutputTest {

    @FindBy(css = "img[id$=imageNotUsingWrappedBean]")
    private WebElement imageNotUsingWrappedBean;
    @FindBy(css = "img[id$=imageUsingWrappedBean]")
    private WebElement imageUsingWrappedBean;

    private int getHeightOfImage(WebElement image) {
        return Integer.parseInt(Utils.returningJQ("css('height')", image).replaceAll("px", ""));
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jMediaOutput/rf-13959.xhtml");
    }

    private int getWidthOfImage(WebElement image) {
        return Integer.parseInt(Utils.returningJQ("css('width')", image).replaceAll("px", ""));
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13959")
    public void testImageIsCreatedFromWrappedBean() {
        // check that reference image is created
        assertVisible(imageNotUsingWrappedBean, "Image created directly from bean's method should be visible.");
        assertEquals(getHeightOfImage(imageNotUsingWrappedBean), RF13959.IMG_HEIGHT);
        assertEquals(getWidthOfImage(imageNotUsingWrappedBean), RF13959.IMG_WIDTH);

        assertVisible(imageUsingWrappedBean, "Image created from wrapped bean's method should be visible.");
        assertEquals(getHeightOfImage(imageUsingWrappedBean), RF13959.IMG_HEIGHT);
        assertEquals(getWidthOfImage(imageUsingWrappedBean), RF13959.IMG_WIDTH);
    }
}
