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

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface EditingState {

    public enum FinishEditingBy {

        keys,
        controls;
    }

    /**
     * Cancels the input.
     * Cancel is done by keys, by pressing ESCAPE
     */
    void cancel();

    /**
     * Cancels the input.
     * @param by specifies how will be the cancel done
     */
    void cancel(FinishEditingBy by);

    /**
     * Confirmation of input.
     * Confirms by keys, by pressing RETURN
     */
    void confirm();

    /**
     * Confirmation of input.
     * @param by specifies how will be the confirmation done
     */
    void confirm(FinishEditingBy by);

    /**
     * Returns an instance, with which can user interact with control buttons
     * (OK and Cancel button).
     */
    Controls getControls();

    /**
     * Clears input, types to input the @newValue and then it returns same instance
     * to be easily confirmed or canceled.
     * @param newValue value to be set
     * @return same instance after typing to input
     */
    EditingState type(String newValue);
}
