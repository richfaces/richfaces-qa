/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.autocomplete;

import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface SelectOrConfirm {

    /**
     * Confirms previously typed value.
     *
     * @return returns back the Autocomplete component.
     */
    Autocomplete confirm();

    /**
     * Selects the first option.
     *
     * @return returns back the Autocomplete component.
     * @throws RuntimeException when no option found
     */
    Autocomplete select();

    /**
     * Selects a choice from suggestions.
     *
     * @param picker for picking from the choices
     * @return returns back the Autocomplete component.
     * @throws RuntimeException when no such option found
     * @see org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByIndexChoicePicker
     * @see org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByVisibleTextChoicePicker
     */
    Autocomplete select(ChoicePicker picker);

}
