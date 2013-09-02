package org.richfaces.tests.page.fragments.impl.panelMenu;

import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;

public interface PanelMenu {

    PanelMenuGroup expandGroup(ChoicePicker picker);

    PanelMenuGroup expandGroup(String header);

    PanelMenuGroup expandGroup(int index);

    void collapseGroup(ChoicePicker picker);

    void collapseGroup(String header);

    void collapseGroup(int index);

    PanelMenuItem selectItem(ChoicePicker picker);

    PanelMenuItem selectItem(String header);

    PanelMenuItem selectItem(int index);

    PanelMenu expandAll();

    PanelMenu collapseAll();
}
