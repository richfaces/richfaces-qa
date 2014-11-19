/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richDropTarget;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class DropTargetSimplePage extends MetamerPage {

    @FindBy(css = "span[id$=clientId]")
    private WebElement clientId;

    @FindBy(css = "span[id$=dragValue]")
    private WebElement dragValue;
    @FindBy(css = "span[id$=dropValue]")
    private WebElement dropValue;

    @FindBy(css = "div.rf-ind[id$=indicatorClone]")
    private GrapheneElement indicator;

    @FindBy(css = "[id$=draggable1]")
    private WebElement drg1;
    @FindBy(css = "[id$=draggable2]")
    private WebElement drg2;
    @FindBy(css = "[id$=draggable3]")
    private WebElement drg3;

    @FindBy(css = "[id$=droppable1]")
    private WebElement drop1;
    @FindBy(css = "[id$=droppable2]")
    private WebElement drop2;

    /**
     * @return the clientId
     */
    public WebElement getClientId() {
        return clientId;
    }

    /**
     * @return the dragValue
     */
    public WebElement getDragValue() {
        return dragValue;
    }

    /**
     * @return the dropValue
     */
    public WebElement getDropValue() {
        return dropValue;
    }

    /**
     * @return the indicator
     */
    public GrapheneElement getIndicator() {
        return indicator;
    }

    /**
     * @return the drg1
     */
    public WebElement getDrg1() {
        return drg1;
    }

    /**
     * @return the drg2
     */
    public WebElement getDrg2() {
        return drg2;
    }

    /**
     * @return the drg3
     */
    public WebElement getDrg3() {
        return drg3;
    }

    /**
     * @return the drop1
     */
    public WebElement getDrop1() {
        return drop1;
    }

    /**
     * @return the drop2
     */
    public WebElement getDrop2() {
        return drop2;
    }
}
