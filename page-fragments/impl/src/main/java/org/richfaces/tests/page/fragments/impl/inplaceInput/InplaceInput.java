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
package org.richfaces.tests.page.fragments.impl.inplaceInput;

import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.VisibleComponent;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface InplaceInput extends VisibleComponent {

    public enum State {

        active("rf-ii-act"),
        changed("rf-ii-chng");
        protected final String cssClass;

        private State(String cssClass) {
            this.cssClass = cssClass;
        }
    }

    public enum OpenBy {

        CLICK("click"),
        DBLCLICK("dblclick"),
        KEYDOWN("keydown"),
        KEYPRESS("keypress"),
        KEYUP("keyup"),
        MOUSEDOWN("mousedown"),
        MOUSEMOVE("mousemove"),
        MOUSEOUT("mouseout"),
        MOUSEOVER("mouseover"),
        MOUSEUP("mouseup");
        //
        protected final String eventName;

        private OpenBy(String eventName) {
            this.eventName = eventName;
        }
    }

    /**
     * Opens inplace input by triggering an event defined by @by
     * @param by defines the event, which will 'open' inplace input
     * @return State from which user can send a value to input and confirm/cancel it.
     */
    EditingState editBy(OpenBy by);

    /**
     * Returns an instance, with which can user interact with control buttons
     * (OK and Cancel button).
     */
    Controls getControls();

    /**
     * Returns value which is saved in Edit input.
     */
    String getEditValue();

    /**
     * Returns value which is saved in label.
     */
    String getLabelValue();

    WebElement getRoot();

    /**
     * Checks state of inplace input.
     */
    boolean is(State state);
}
