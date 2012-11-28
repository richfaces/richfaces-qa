package org.richfaces.tests.page.fragments.impl.log;

import java.util.List;

public interface Log {

    List<LogEntry> getAllLogEntries();

    List<LogEntry> getAllDebugEntries();

    List<LogEntry> getAllInfoEntries();

    List<LogEntry> getAllWarnEntries();

    List<LogEntry> getAllErrorEntries();

    void clear();

    void changeLevel(LogEntryLevel lvl);
}
