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
package org.richfaces.tests.page.fragments.impl.calendar.common;

import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.VisibleComponent;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.TimeEditor;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface FooterControls extends VisibleComponent {

    /**
     * Clicks on 'Clean' button. Waits for cleaning of set date.
     */
    void cleanDate();

    WebElement getCleanButtonElement();

    /**
     * Returns Time editor component without opening it.
     */
    TimeEditor getTimeEditor();

    WebElement getTimeEditorOpenerElement();

    WebElement getTodayButtonElement();

    /**
     * Opens time editor with waiting.
     */
    TimeEditor openTimeEditor();

    /**
     * Clicks on 'Today' button. Does not wait for date setting.
     */
    void setTodaysDate();

    /**
     * Clicks on 'Today' button. Does not wait for date setting.
     */
    void todayDate();

    WebElement getSelectedDateElement();
}
