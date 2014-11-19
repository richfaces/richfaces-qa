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
package org.richfaces.tests.metamer.ftest.richDragIndicator;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class DragIndicatorSimplePage extends MetamerPage {

    @FindBy(css = "div.rf-ind-drag[id$=indicatorClone]")
    private WebElement indicatorElement;

    @FindBy(css = "[id$=draggable1]")
    private WebElement drag1Element;
    @FindBy(css = "[id$=draggable2]")
    private WebElement drag2Element;
    @FindBy(css = "[id$=draggable3]")
    private WebElement drag3Element;

    @FindBy(css = "[id$=droppable1]")
    private WebElement drop1Element;
    @FindBy(css = "[id$=droppable2]")
    private WebElement drop2Element;

    /**
     * @return the drag1Element
     */
    public WebElement getDrag1Element() {
        return drag1Element;
    }

    /**
     * @return the drag2Element
     */
    public WebElement getDrag2Element() {
        return drag2Element;
    }

    /**
     * @return the drag3Element
     */
    public WebElement getDrag3Element() {
        return drag3Element;
    }

    /**
     * @return the drop1Element
     */
    public WebElement getDrop1Element() {
        return drop1Element;
    }

    /**
     * @return the drop2Element
     */
    public WebElement getDrop2Element() {
        return drop2Element;
    }

    /**
     * @return the indicatorElement
     */
    public WebElement getIndicatorElement() {
        return indicatorElement;
    }

}
