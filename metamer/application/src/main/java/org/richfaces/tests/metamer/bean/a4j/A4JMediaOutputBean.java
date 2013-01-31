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
package org.richfaces.tests.metamer.bean.a4j;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.imageio.ImageIO;

import org.richfaces.component.UIMediaOutput;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.bean.MediaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for a4j:mediaOutput.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22720 $
 */
@ManagedBean(name = "a4jMediaOutputBean")
@RequestScoped
public class A4JMediaOutputBean implements Serializable {

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
     * @param attributes
     *            map containing all attributes of tested component. Name of the component is key in the map.
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void createContentAudioMpeg(OutputStream out, Object data) throws IOException {
        createContentFromResource(out, data, "audio/alarm.mp3");
    }

    public void createContentFlash(OutputStream out, Object data) throws IOException {
        createContentFromResource(out, data, "flash/text.swf");
    }

    public void createContentImagePng(OutputStream out, Object data) throws IOException {
        if (data instanceof MediaData) {
            MediaData paintData = (MediaData) data;
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
            graphics2D.fillRect(paintData.getWidth() / 2, paintData.getHeight() / 2, paintData.getWidth() / 2, paintData.getHeight() / 2);

            ImageIO.write(img, "png", out);
        }
    }

    public void createContentTextCss(OutputStream out, Object data) throws IOException {
        out.write(CSS_TEXT.getBytes());
        out.flush();
    }

    public void createContentTextHtml(OutputStream out, Object data) throws IOException {
        out.write(HTML_TEXT.getBytes());
        out.flush();
    }

    public void createContentTextJavascript(OutputStream out, Object data) throws IOException {
        out.write(JAVASCRIPT_TEXT.getBytes());
        out.flush();
    }

    public void createContentTextPlain(OutputStream out, Object data) throws IOException {
        out.write(PLAIN_TEXT.getBytes());
        out.flush();
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        int read;

        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void createContentFromResource(OutputStream out, Object data, String resourceName) throws IOException {
        InputStream in = null;
        try {
            in = A4JMediaOutputBean.class.getClassLoader().getResourceAsStream(resourceName);
            copy(in, out);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
