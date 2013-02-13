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

import com.google.common.base.Predicate;

import org.joda.time.DateTime;
import org.richfaces.tests.page.fragments.impl.log.Log.LogEntryLevel;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface LogFilterBuilder {

    /**
     * Function that will build this filter. Merges all filter properties to one.
     */
    Predicate<LogEntry> build();

    /**
     * Filters out all entries with time after the specified time in @time.
     */
    LogFilterBuilder filterOutLogsNewerThan(DateTime time);

    /**
     * Filters out all entries with time before the specified time in @time.
     */
    LogFilterBuilder filterOutLogsOlderThan(DateTime time);

    /**
     * Filters out all entries with different content than specified in @content.
     */
    LogFilterBuilder filterToContentContains(String content);

    /**
     * Filters out all entries that contains String specified in @content.
     */
    LogFilterBuilder filterToContentNotContains(String content);

    /**
     * Filters out all entries with different level.
     */
    LogFilterBuilder filterToLevel(LogEntryLevel level);

    /**
     * Filters out all entries with different levels.
     */
    LogFilterBuilder filterToLevels(LogEntryLevel... levels);
}
