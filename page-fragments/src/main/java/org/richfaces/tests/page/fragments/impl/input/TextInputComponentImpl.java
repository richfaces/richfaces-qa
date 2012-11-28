/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.input;

import org.jboss.arquillian.graphene.component.object.api.autocomplete.ClearType;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Fragment for text input component. Note that the root of the component has to point to the actual input!
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TextInputComponentImpl {

    @Root
    private WebElement root;

    /**
     * Fills in given text
     *
     * @param text
     */
    public void fillIn(String text) {
        root.sendKeys(text);
    }

    /**
     * Focuses this input.
     */
    public void focus() {
        root.sendKeys("");
    }

    /**
     * Retruns the actual WebElement of input
     *
     * @return
     */
    public WebElement getInput() {
        return root;
    }

    /**
     * Returns <code>String</code> value of this input.
     *
     * @return
     */
    public String getStringValue() {
        return root.getAttribute("value");
    }

    /**
     * Returns <code>int</code> value of this input.
     *
     * @throws NumberFormatException if the value of the input cannot be parsed as an integer
     * @return
     */
    public int getIntValue() {
        return Integer.valueOf(getStringValue());
    }

    /**
     * <p>
     * Clears the input. The method of clearing can be passed as argument to determine the way the input will be cleared
     * </p>
     *
     * @param clearType
     * @throws IllegalArgumentException when there is illegal number of arguments passed
     */
    public void clear(ClearType... clearType) {
        if (clearType.length == 0) {
            root.clear();
            return;
        }
        if (clearType.length > 1) {
            throw new IllegalArgumentException("The number of clear type method arguments should be one!");
        }

        int valueLength = root.getAttribute("value").length();

        switch (clearType[0]) {
            case BACK_SPACE: {
                Actions builder = new Actions(GrapheneContext.getProxy());
                for (int i = 0; i < valueLength; i++) {
                    builder.sendKeys(root, Keys.BACK_SPACE);
                }
                builder.build().perform();
                break;
            }
            case ESCAPE_SQ: {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < valueLength; i++) {
                    sb.append("\b");
                }
                root.sendKeys(sb.toString());
                root.click();
                break;
            }
            case DELETE: {
                Actions builder = new Actions(GrapheneContext.getProxy());
                String ctrlADel = Keys.chord(Keys.CONTROL, "a", Keys.DELETE);
                builder.sendKeys(root, ctrlADel);

                builder.build().perform();
            }
        }
    }
}
