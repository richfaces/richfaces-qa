package org.richfaces.tests.page.fragments.impl.contextMenu.internal;

import org.richfaces.tests.page.fragments.impl.contextMenu.RichFacesContextMenu;

public class RichFacesContextMenuInternal extends RichFacesContextMenu {

    public String getLangAttribute() {
        return root.getAttribute("lang");
    }

}
