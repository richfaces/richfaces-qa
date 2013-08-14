package org.richfaces.tests.page.fragments.impl.editor.toolbar;

public enum EditorMode implements EditorButton {

    SOURCE("cke_button_source"),
    SPELL_CHECK_AS_YOU_TYPE("cke_button_scayt"),
    BOLD("cke_button_bold"),
    ITALIC("cke_button_italic"),
    UNDERLINE("cke_button_underline"),
    STRIKE_THROUGH("cke_button_strike"),
    SUBSCRIPT("cke_button_subscript"),
    SUPERSCRIPT("cke_button_superscript"),
    NUMBERED_LIST("cke_button_numberedlist"),
    BULLETED_LIST("cke_button_bulletedlist"),
    BLOCK_QUOTE("cke_button_blockquote"),
    MAXIMIZE("cke_button_maximize"),
    SHOW_BLOCKS("cke_button_showblocks");

    private final String className;

    private EditorMode(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return className;
    }

    public String getCSSClassName() {
        return className;
    }
}