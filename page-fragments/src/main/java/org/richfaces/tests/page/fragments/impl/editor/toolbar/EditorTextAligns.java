package org.richfaces.tests.page.fragments.impl.editor.toolbar;

public enum EditorTextAligns implements EditorButton {
    LEFT("cke_button_justifyleft"),
    CENTER("cke_button_justifycenter"),
    RIGHT("cke_button_justifyright"),
    BLOCK("cke_button_justifyblock");

    private final String className;

    private EditorTextAligns(String className) {
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