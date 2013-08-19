package org.richfaces.tests.page.fragments.impl.common;


public enum ClearType {

    // clearing by backspace keys
    BACKSPACE,
    // clearing by delete keys
    DELETE,
    // clearing by escape sequence
    ESCAPE_SQ,
    // clearing by WebDriver
    WD,
    // clearing by JavaScript
    JS;

    public static final ClearType DEFAULT_CLEAR_TYPE = JS;
}