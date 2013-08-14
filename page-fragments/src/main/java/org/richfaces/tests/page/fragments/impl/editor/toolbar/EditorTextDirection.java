package org.richfaces.tests.page.fragments.impl.editor.toolbar;

public enum EditorTextDirection implements EditorButton {

    FROM_RIGHT_TO_LEFT("cke_button_bidirtl"),
    FROM_LEFT_TO_RIGHT("cke_button_bidiltr");

    private final String className;

    private EditorTextDirection(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return this.className;
    }

    public String getCSSClassName() {
        return className;
    }
}