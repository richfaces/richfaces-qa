package org.richfaces.tests.showcase.contextMenu.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.contextMenu.ContextMenuComponentImpl;
import org.richfaces.tests.page.fragments.impl.contextMenu.ContextMenuItem;

public class SimpleContextMenuPage {

    @FindBy(css = "img[id$='pic']")
    private WebElement picture;

    @FindBy(className = "rf-ctx-pos")
    private ContextMenuComponentImpl contextMenu;

    public static final ContextMenuItem ZOOM_IN = new ContextMenuItem("Zoom In");
    public static final ContextMenuItem ZOOM_OUT = new ContextMenuItem("Zoom Out");

    public WebElement getPicture() {
        return picture;
    }

    public ContextMenuComponentImpl getContextMenu() {
        contextMenu.setInvoker(ContextMenuComponentImpl.LEFT_CLICK);

        if (contextMenu.getTarget() == null) {
            contextMenu.setTarget(picture);
        }

        return contextMenu;
    }
}