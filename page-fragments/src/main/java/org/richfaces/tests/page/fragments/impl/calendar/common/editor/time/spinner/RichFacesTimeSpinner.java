/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.spinner;

/**
 * Spinner component for setting minutes/seconds/milliseconds <0;59>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesTimeSpinner extends RichFacesSpinner<Integer> {

    protected final int maxValue;

    public RichFacesTimeSpinner() {
        this(60);
    }

    public RichFacesTimeSpinner(int maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public Integer getValue() {
        return Integer.parseInt(inputElement.getAttribute("value"));
    }

    @Override
    public void setValueByButtons(Integer value) {
        if (!isSameValueAreadySet(value)) {
            int actual = getValue();
            int difference = actual - value;
            int optimizedDifference = (difference < 0 ? maxValue + difference : difference - maxValue);
            optimizedDifference = (Math.abs(optimizedDifference) > Math.abs(difference) ? difference : optimizedDifference);
            if (optimizedDifference < 0) {
                clickUp(optimizedDifference);
            } else if (optimizedDifference > 0) {
                clickDown(optimizedDifference);
            }
        }
    }

    private void clickUp(int times) {
        for (int i = 0; i > times; i--) {
            buttonUpElement.click();
        }
    }

    private void clickDown(int times) {
        for (int i = 0; i < times; i++) {
            buttonDownElement.click();
        }
    }
}
