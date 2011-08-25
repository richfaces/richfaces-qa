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
package org.richfaces.tests.metamer.ftest;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunAsClient
public abstract class AbstractMetamerTest extends Arquillian {
    
    @Deployment
    public static WebArchive createTestArchive() {
        WebArchive war = ShrinkWrap.createFromZipFile(WebArchive.class,
            new File("target/metamer.war"));
        return war;        
    }    
    
    /**
     * Context path will be used to retrieve pages from right URL. Don't hesitate to use it in cases of building absolute
     * URLs.
     * 
     * @return context path
     */
    protected String getContextPath() {
        return System.getProperty("context.path", "metamer");
    }
    
    /**
     * Context root can be used to obtaining full URL paths, is set to actual tested application's context root
     * 
     * @return context root
     */    
    protected String getContextRoot() {
        return System.getProperty("context.root", "http://localhost:8080");
    }
    
    protected String getPath() {
        return getContextRoot() + "/" + getContextPath() + "/" + getTestUrl();
    }
    
    /**
     * Returns the url to test page to be opened by Selenium (without context root and context path)
     * 
     * @return url to the test page to be opened by Selenium - it doesn't contain context root and context path
     */    
    protected abstract String getTestUrl();
    
}
