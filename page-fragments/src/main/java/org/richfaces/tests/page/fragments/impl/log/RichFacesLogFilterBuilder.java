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

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

import org.joda.time.DateTime;
import org.richfaces.tests.page.fragments.impl.log.Log.LogEntryLevel;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesLogFilterBuilder implements LogFilterBuilder {

    private List<Predicate<LogEntry>> filters = Lists.newArrayList();

    public RichFacesLogFilterBuilder() {
    }

    private RichFacesLogFilterBuilder(RichFacesLogFilterBuilder tfb) {
        this.filters = Lists.newArrayList(tfb.filters);
    }

    @Override
    public Predicate<LogEntry> build() {
        Preconditions.checkArgument(!filters.isEmpty(), "No filters specified. Cannot create filter.");
        return new Predicate<LogEntry>() {
            @Override
            public boolean apply(LogEntry input) {
                //apply all filters
                for (Predicate<LogEntry> predicate : filters) {
                    if (!predicate.apply(input)) {
                        return Boolean.FALSE;
                    }
                }
                return Boolean.TRUE;
            }
        };
    }

    @Override
    public RichFacesLogFilterBuilder filterOutLogsNewerThan(DateTime time) {
        RichFacesLogFilterBuilder tfb = new RichFacesLogFilterBuilder(this);
        tfb.filters.add(new AbstractDateTimePredicate(time) {
            @Override
            public boolean apply(LogEntry input) {
                return input.getTimeStamp().isAfter(time);
            }
        });
        return tfb;
    }

    @Override
    public RichFacesLogFilterBuilder filterOutLogsOlderThan(DateTime time) {
        RichFacesLogFilterBuilder tfb = new RichFacesLogFilterBuilder(this);
        tfb.filters.add(new AbstractDateTimePredicate(time) {
            @Override
            public boolean apply(LogEntry input) {
                return input.getTimeStamp().isBefore(time);
            }
        });
        return tfb;
    }

    @Override
    public RichFacesLogFilterBuilder filterToContentContains(String content) {
        RichFacesLogFilterBuilder tfb = new RichFacesLogFilterBuilder(this);
        tfb.filters.add(new ContentContainsPredicate(content));
        return tfb;
    }

    @Override
    public RichFacesLogFilterBuilder filterToContentNotContains(String content) {
        RichFacesLogFilterBuilder tfb = new RichFacesLogFilterBuilder(this);
        tfb.filters.add(new ContentNotContainsPredicate(content));
        return tfb;
    }

    @Override
    public RichFacesLogFilterBuilder filterToLevel(LogEntryLevel level) {
        RichFacesLogFilterBuilder tfb = new RichFacesLogFilterBuilder(this);
        tfb.filters.add(new LogLevelFilterPredicate(level));
        return tfb;
    }

    @Override
    public RichFacesLogFilterBuilder filterToLevels(LogEntryLevel... levels) {
        RichFacesLogFilterBuilder tfb = new RichFacesLogFilterBuilder(this);
        tfb.filters.add(new LogLevelFilterPredicate(levels));
        return tfb;
    }

    private abstract static class AbstractDateTimePredicate implements Predicate<LogEntry> {

        protected final DateTime time;

        public AbstractDateTimePredicate(DateTime time) {
            this.time = time;
        }
    }

    private static class ContentContainsPredicate implements Predicate<LogEntry> {

        protected final String content;

        public ContentContainsPredicate(String content) {
            this.content = content;
        }

        @Override
        public boolean apply(LogEntry input) {
            return input.getContent().contains(content);
        }
    }

    private static class ContentNotContainsPredicate extends ContentContainsPredicate {

        public ContentNotContainsPredicate(String content) {
            super(content);
        }

        @Override
        public boolean apply(LogEntry input) {
            return !super.apply(input);
        }
    }

    private static final class LogLevelFilterPredicate implements Predicate<LogEntry> {

        private final List<LogEntryLevel> levels;

        public LogLevelFilterPredicate(LogEntryLevel... levels) {
            this.levels = Lists.newArrayList(levels);
        }

        public LogLevelFilterPredicate(LogEntryLevel level) {
            this.levels = Lists.newArrayList(level);
        }

        @Override
        public boolean apply(LogEntry input) {
            for (LogEntryLevel logLevel : levels) {
                if (logLevel.equals(LogEntryLevel.ALL) || logLevel.equals(input.getLevel())) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        }
    }
}
