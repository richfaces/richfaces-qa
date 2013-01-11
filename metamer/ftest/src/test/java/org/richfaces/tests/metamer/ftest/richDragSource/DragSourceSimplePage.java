/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richDragSource;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class DragSourceSimplePage extends MetamerPage {

    @FindBy(css = "div.rf-ind[id$=indicator]")
    public WebElement indicator;

    @FindBy(css = "div.rf-ind[id$=indicator2Clone]")
    public WebElement indicator2;

    @FindBy(css = "body > div.ui-draggable")
    public WebElement defaultIndicator;

    @FindBy(css = "[id$=draggable1]")
    public WebElement drag1;

    @FindBy(css = "[id$=draggable2]")
    public WebElement drag2;

    @FindBy(css = "[id$=draggable3]")
    public WebElement drag3;

    @FindBy(css = "[id$=droppable1]")
    public WebElement drop1;

    @FindBy(css = "[id$=droppable2]")
    public WebElement drop2;

}
