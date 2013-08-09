package org.richfaces.tests.metamer.ftest.a4jMediaOutput;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class RF13098 extends AbstractMediaOutputTest{

    @FindBy (id = "img1")
    private WebElement imageOne;

    @FindBy (id = "img2")
    private WebElement imageTwo;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jMediaOutput/rf13098.xhtml");
    }

    @Test
    public void testImageWithSerializedResource() {
        BufferedImage image = null;
        URL imageURL = buildUrl(contextPath, imageOne.getAttribute("src"));
        try {
            image = ImageIO.read(imageURL);
        } catch (IOException ex) {
            fail("Could not download image from URL " + imageURL.getPath());
        }
        assertEquals(image.getHeight(), 120, "Height of the image");
        assertEquals(image.getWidth(), 300, "Width of the image");
    }

    @Test
    public void testImageWithSerializationOnly() {
        BufferedImage image = null;
        URL imageURL = buildUrl(contextPath, imageTwo.getAttribute("src"));
        try {
            image = ImageIO.read(imageURL);
        } catch (IOException ex) {
            fail("Could not download image from URL " + imageURL.getPath());
        }
        assertEquals(image.getHeight(), 120, "Height of the image");
        assertEquals(image.getWidth(), 300, "Width of the image");
    }
}
