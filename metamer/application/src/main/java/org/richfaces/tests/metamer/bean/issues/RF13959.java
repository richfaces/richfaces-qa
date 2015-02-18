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
package org.richfaces.tests.metamer.bean.issues;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;

@SessionScoped
@ManagedBean(name = "rf13959")
public class RF13959 implements Serializable {

    public static final int IMG_HEIGHT = 50;
    public static final int IMG_WIDTH = 140;

    private static final long serialVersionUID = -1L;

    public void createImage(OutputStream output, Object input) throws IOException {
        BufferedImage img = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = img.createGraphics();
        graphics2D.setBackground(Color.BLACK);
        graphics2D.setColor(Color.WHITE);
        graphics2D.clearRect(0, 0, img.getWidth(), img.getHeight());
        graphics2D.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics2D.drawString("Created image.", 20, 27);
        ImageIO.write(img, "png", output);
    }
}
