package org.richfaces.tests.page.fragments.impl.editor.toolbar;

import org.openqa.selenium.WebElement;

public interface EditorToolbar {
    int count();

    WebElement getButton(EditorButton whichButton);

    boolean isBasic();

    boolean isAdvanced();
}
