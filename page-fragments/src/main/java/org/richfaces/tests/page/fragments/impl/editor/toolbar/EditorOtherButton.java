package org.richfaces.tests.page.fragments.impl.editor.toolbar;

public enum EditorOtherButton implements EditorToolbar {
    SAVE("cke_button_save"),
    NEW_PAGE("cke_button_newpage"),
    PREVIEW("cke_button_preview"),
    PRINT("cke_button_print"),
    TEMPLATES("cke_button_templates"),
    CUT("cke_button_cut"),
    COPY("cke_button_copy"),
    PASTE("cke_button_paste"),
    PASTE_AS_PLAIN_TEXT("cke_button_pastetext"),
    PASTE_FROM_WORD("cke_button_pastefromword"),
    UNDO("cke_button_undo"),
    REDO("cke_button_redo"),
    FIND("cke_button_find"),
    REPLACE("cke_button_replace"),
    SELECT_ALL("cke_button_selectAll"),
    CHECK_SPELLING("cke_button_checkspell"),
    FORM("cke_button_form"),
    CHECKBOX("cke_button_checkbox"),
    RADIO_BUTTON("cke_button_radio"),
    TEXT_FIELD("cke_button_textfield"),
    TEXT_AREA("cke_button_textarea"),
    SELECTION_FIELD("cke_button_select"),
    BUTTON("cke_button_button"),
    IMAGE_BUTTON("cke_button_imagebutton"),
    HIDDEN_FIELD("cke_button_hiddenfield"),
    REMOVE_FORMAT("cke_button_removeFormat"),
    DECREASE_INDENT("cke_button_outdent"),
    INCREASE_INDENT("cke_button_indent"),
    CREATE_DIV("cke_button_creatediv"),
    LINK("cke_button_link"),
    UNLINK("cke_button_unlink"),
    ANCHOR("cke_button_anchor"),
    IMAGE("cke_button_image"),
    FLASH("cke_button_flash"),
    TABLE("cke_button_table"),
    INSERT_HORIZONTAL_RULE("cke_button_horizontalrule"),
    SMILEY("cke_button_smiley"),
    INSERT_SPECIAL_CHARACTER("cke_button_specialchar"),
    INSERT_PAGE_BREAK_FOR_PRINTING("cke_button_pagebreak"),
    IFRAME("cke_button_iframe"),
    ABOUT_CKE_EDITOR("cke_button_about");

    private final String className;

    private EditorOtherButton(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return className;
    }

    @Override
    public String getCSSClassName() {
        return className;
    }
}