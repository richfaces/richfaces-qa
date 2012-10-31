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
package org.richfaces.tests.page.fragments.impl.calendar.common.editor.time;

import org.joda.time.DateTime;
import org.richfaces.tests.page.fragments.impl.VisibleComponent;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface TimeEditor extends VisibleComponent {

    public enum SetValueBy {

        typing, buttons;
    }

    /**
     * Clicks on the 'Cancel' button.
     */
    void cancelTime();

    /**
     * Clicks on the 'OK' button.
     */
    void confirmTime();

    /**
     * Returns time set in the spinners.
     * @return
     */
    DateTime getTime();

    /**
     * Sets time: hours, minutes, seconds and time-sign. Returns same instance so
     * the time can be easily confirmed or canceled.
     * @param time time that will be set
     * @param inputType how will be the time set
     * @return this time editor
     */
    TimeEditor setTime(DateTime time, SetValueBy by);
}
