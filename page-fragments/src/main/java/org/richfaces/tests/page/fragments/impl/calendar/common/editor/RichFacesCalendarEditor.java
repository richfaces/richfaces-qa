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
package org.richfaces.tests.page.fragments.impl.calendar.common.editor;

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.RichFacesTimeEditor;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.TimeEditor;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.yearAndMonth.RichFacesYearAndMonthEditor;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.yearAndMonth.YearAndMonthEditor;

/**
 * Helper component to set root for YearAndMonthEditor and TimeEditor
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesCalendarEditor {

    @Root
    private WebElement root;
    @FindBy(css = "table[id$=DateEditorLayout]")
    private RichFacesYearAndMonthEditor dateEditor;
    @FindBy(css = "table[id$=TimeEditorLayout]")
    private RichFacesTimeEditor timeEditor;

    public YearAndMonthEditor getDateEditor() {
        return dateEditor;
    }

    public TimeEditor getTimeEditor() {
        return timeEditor;
    }
}
