package org.richfaces.tests.metamer.bean.issues.rf13098;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;

import org.richfaces.component.UIMediaOutput;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("a4jMediaOutputBeanTest")
@RequestScoped
public class A4jMediaOutputBeanTest implements Serializable {
    public static final String CSS_TEXT = "#mediaOutputMarked { color: red }";
    public static final String HTML_TEXT = "<span id='htmlText' style='color: red'>SOME RED HTML TEXT</span>";
    public static final String JAVASCRIPT_TEXT = "document.write('<span id=mediaOutputJavascriptText>JAVASCRIPT TEXT</span>')";
    public static final String PLAIN_TEXT = "SOME PLAIN TEXT";
    private static final long serialVersionUID = -1L;
    private static Logger logger;
    private Attributes attributes;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIMediaOutput.class, getClass());

        attributes.setAttribute("session", true);
        attributes.setAttribute("rendered", true);
        attributes.remove("createContent");
        attributes.remove("element");
        attributes.remove("value");
        attributes.remove("mimeType");
        attributes.remove("uriAttribute");
    }

    /**
     * Getter for attributes.
     *
     * @return A map containing all attributes of tested component. Name of the component is key in the map.
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * Setter for attributes.
     *
     * @param attributes map containing all attributes of tested component. Name of the component is key in the map.
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void createContentImagePng(OutputStream out, Object data) throws IOException {
        if (data instanceof RF13098) {
            RF13098 paintData = (RF13098) data;
            BufferedImage img = new BufferedImage(paintData.getWidth(), paintData.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = img.createGraphics();
            graphics2D.clearRect(0, 0, paintData.getWidth(), paintData.getHeight());

            graphics2D.setColor(Color.YELLOW);
            graphics2D.fillRect(0, 0, paintData.getWidth() / 2, paintData.getHeight() / 2);

            graphics2D.setColor(Color.RED);
            graphics2D.fillRect(paintData.getWidth() / 2, 0, paintData.getWidth() / 2, paintData.getHeight() / 2);

            graphics2D.setColor(Color.BLUE);
            graphics2D.fillRect(0, paintData.getHeight() / 2, paintData.getWidth() / 2, paintData.getHeight() / 2);

            graphics2D.setColor(Color.GREEN);
            graphics2D.fillRect(paintData.getWidth() / 2, paintData.getHeight() / 2, paintData.getWidth() / 2,
                paintData.getHeight() / 2);

            ImageIO.write(img, "png", out);
        }
    }
}
