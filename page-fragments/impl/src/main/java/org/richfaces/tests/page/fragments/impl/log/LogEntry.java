package org.richfaces.tests.page.fragments.impl.log;

import org.joda.time.DateTime;

public class LogEntry {

    private LogEntryLevel level;

    private DateTime timeStamp;

    private String content;

    public LogEntryLevel getLevel() {
        return level;
    }

    public void setLevel(LogEntryLevel level) {
        this.level = level;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
