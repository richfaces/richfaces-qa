package org.richfaces.tests.showcase.editor.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.editor.RichFacesEditor;

public class AutosavingPage {

    @FindBy(css = ".example-cnt")
    private RichFacesEditor editor;

    @FindBy(css = ".rf-p-b")
    private WebElement outputFromEditor;

    public RichFacesEditor getEditor() {
        return editor;
    }

    public WebElement getOutputFromEditor() {
        return outputFromEditor;
    }

}
