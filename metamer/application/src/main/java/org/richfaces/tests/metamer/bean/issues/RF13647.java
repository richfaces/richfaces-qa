/*******************************************************************************
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
 *******************************************************************************/
package org.richfaces.tests.metamer.bean.issues;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@ManagedBean(name = "rf13647")
@SessionScoped
public class RF13647 implements Serializable {

    private static final long serialVersionUID = -1L;

    private void createFile(File f) {
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            new BufferedWriter(new FileWriter(f)).append("Hello from RichFaces").close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void download() throws Exception {
        File f = new File("hello.txt");
        createFile(f);
        try {
            FacesContext context = FacesContext.getCurrentInstance();

            /* Code that reads content from file and store it in object */
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "attachment; filename="
                + f.getName());
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream iStream = new FileInputStream(f);

            int b;
            while ((b = iStream.read()) != -1) {
                outputStream.write(b);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            FacesContext.getCurrentInstance().responseComplete();
        }
    }
}
