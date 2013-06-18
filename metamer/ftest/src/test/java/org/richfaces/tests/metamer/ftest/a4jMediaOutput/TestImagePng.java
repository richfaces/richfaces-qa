/*******************************************************************************
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
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.a4jMediaOutput;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/a4jMediaOutput/imagePng.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22721 $
 */
public class TestImagePng extends AbstractMediaOutputTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jMediaOutput/imagePng.xhtml");
    }

    @Test
    public void init() {
        URL imageURL = buildUrl(contextPath, mediaOutput.getAttribute("src"));

        BufferedImage bufferedImage = null;

        try {
            bufferedImage = ImageIO.read(imageURL);
        } catch (IOException ex) {
            fail("Could not download image from URL " + imageURL.getPath());
        }

        assertEquals(bufferedImage.getHeight(), 120, "Height of the image");
        assertEquals(bufferedImage.getWidth(), 300, "Width of the image");

        for (int x = 0; x < 150; x++) {
            for (int y = 0; y < 60; y++) {
                assertEquals(bufferedImage.getRGB(x, y), Color.YELLOW.getRGB(), "Top-left quadrant should be yellow [" + x + ", " + y + "].");
            }
        }

        for (int x = 150; x < 300; x++) {
            for (int y = 0; y < 60; y++) {
                assertEquals(bufferedImage.getRGB(x, y), Color.RED.getRGB(), "Top-right quadrant should be red [" + x + ", " + y + "].");
            }
        }

        for (int x = 0; x < 150; x++) {
            for (int y = 60; y < 120; y++) {
                assertEquals(bufferedImage.getRGB(x, y), Color.BLUE.getRGB(), "Bottom-left quadrant should be blue [" + x + ", " + y + "].");
            }
        }

        for (int x = 150; x < 300; x++) {
            for (int y = 60; y < 120; y++) {
                assertEquals(bufferedImage.getRGB(x, y), Color.GREEN.getRGB(), "Bottom-right quadrant should be yellow [" + x + ", " + y + "].");
            }
        }
    }


}
