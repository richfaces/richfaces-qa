package org.richfaces.tests.page.fragments.impl.log;

public enum LogEntryLevel {
    ALL("all"),
    DEBUG("debug"),
    INFO("info"),
    WARN("warn"),
    FATAL("fatal"),
    ERROR("error");

    private final String text;

    LogEntryLevel(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
