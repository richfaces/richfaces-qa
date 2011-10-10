/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jMediaOutput;

import static org.testng.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jboss.test.selenium.android.support.ui.Select;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractAndroidTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.MediaOutputImgUsagePage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jMediaOutputSimple extends AbstractAndroidTest<MediaOutputImgUsagePage> {

    @Test
    public void testStates() throws Exception {
        Select leftColor = new Select(getWebDriver(), getToolKit(), getPage().getSelectLeftColor());
        Select rightColor = new Select(getWebDriver(), getToolKit(), getPage().getSelectRightColor());
        Select textColor = new Select(getWebDriver(), getToolKit(), getPage().getSelectTextColor());
        for (ImageState state : ImageState.values()) {
            leftColor.selectByIndex(state.getLeftColor().getIndex());
            rightColor.selectByIndex(state.getRightColor().getIndex());
            textColor.selectByIndex(state.getTextColor().getIndex());
            getPage().getSubmitButton().click();
            testImage(
                state.getLeftColor().getValue(),
                state.getRightColor().getValue(),
                state.getTextColor().getValue()
            );
        }
    }
    
    @Override
    protected MediaOutputImgUsagePage createPage() {
        return new MediaOutputImgUsagePage();
    }

    private void testImage(long expectedRightBottomCornerColor, long expectedLeftTopCornerColor, long expectedTextColor)
        throws Exception {
        URL imageUrl = new URL(getPage().getImage().getAttribute("src").replace(getConfiguration().getContextRoot(), "http://localhost:8080")); // FIXME
        BufferedImage image = ImageIO.read(imageUrl);
        int widthOfImage = image.getWidth();
        int heightOfImage = image.getHeight();
        assertEquals(image.getRGB(0, 0), expectedLeftTopCornerColor, "The top-left corner should be different");
        assertEquals(image.getRGB(widthOfImage - 1, heightOfImage - 1), expectedRightBottomCornerColor, "The bottom-right corner should be different");
        assertEquals(image.getRGB(95, 45), expectedTextColor, "The text color should be different");

    }

}
