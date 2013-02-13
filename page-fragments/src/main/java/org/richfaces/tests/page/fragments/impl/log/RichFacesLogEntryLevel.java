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
package org.richfaces.tests.page.fragments.impl.log;

import java.util.EnumSet;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.log.Log.LogEntryLevel;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public enum RichFacesLogEntryLevel {

    DEBUG(LogEntryLevel.DEBUG, "rf-log-entry-lbl-debug"),
    INFO(LogEntryLevel.INFO, "rf-log-entry-lbl-info"),
    WARN(LogEntryLevel.WARN, "rf-log-entry-lbl-warn"),
    ERROR(LogEntryLevel.ERROR, "rf-log-entry-lbl-error"),
    ALL(LogEntryLevel.ERROR, null);
    //
    private final LogEntryLevel level;
    private final String containsClass;

    private RichFacesLogEntryLevel(LogEntryLevel level, String containsClass) {
        this.level = level;
        this.containsClass = containsClass;
    }

    static LogEntryLevel getLevelFromLabel(WebElement label) {
        String styleClasses = label.getAttribute("class");
        for (RichFacesLogEntryLevel logEntryLevel : EnumSet.of(DEBUG, INFO, WARN, ERROR)) {
            if (styleClasses.contains(logEntryLevel.containsClass)) {
                return logEntryLevel.level;
            }
        }
        throw new RuntimeException("Cannot obtain level from label: " + label);
    }
}