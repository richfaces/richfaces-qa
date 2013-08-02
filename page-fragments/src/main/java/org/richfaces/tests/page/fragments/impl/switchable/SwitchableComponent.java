package org.richfaces.tests.page.fragments.impl.switchable;

import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;

public interface SwitchableComponent<T extends ComponentContainer> {

    T switchTo(ChoicePicker picker);

    T switchTo(String header);

    T switchTo(int index);

}
