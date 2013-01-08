/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.input;

import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.VisibleComponent;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface TextInputComponent extends VisibleComponent {

    public enum ClearType {

        //clearing by backspace keys
        BACKSPACE,
        //clearing by delete keys
        DELETE,
        //clearing by escape sequence
        ESCAPE_SQ,
        //clearing by WebDriver
        WD,
        //clearing by JavaScript
        JS
    }

    /**
     * Clears the input  and returns actual component. The method of clearing can be passed as argument to determine the way the input will be cleared
     *
     * @param clearType
     * @throws UnsupportedOperationException when there is illegal  ClearType argument passed
     */
    TextInputComponent clear(ClearType clearType);

    /**
     * Fills in given text and returns actual component.
     *
     * @param text
     */
    TextInputComponent fillIn(String text);

    /**
     * Focuses this input.
     */
    TextInputComponent focus();

    /**
     * Returns the actual WebElement of input.
     *
     * @return
     */
    WebElement getInput();

    /**
     * Returns <code>int</code> value of this input.
     *
     * @throws NumberFormatException if the value of the input cannot be parsed as an integer
     * @return
     */
    int getIntValue();

    /**
     * Returns <code>String</code> value of this input.
     *
     * @return
     */
    String getStringValue();

    /**
     * Submits this input.
     *
     * @param text
     */
    void submit();

    /**
     * Trigger JavaScript event using JQuery
     *
     * @param event JavaScript event to be triggered (blur, click...)
     * @return same component
     */
    TextInputComponent trigger(String event);
}
