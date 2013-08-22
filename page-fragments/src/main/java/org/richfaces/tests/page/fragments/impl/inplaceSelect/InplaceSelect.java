package org.richfaces.tests.page.fragments.impl.inplaceSelect;

import org.richfaces.tests.page.fragments.impl.common.TextInputComponent;
import org.richfaces.tests.page.fragments.impl.inplaceInput.ConfirmOrCancel;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;

public interface InplaceSelect {

    ConfirmOrCancel select(ChoicePicker picker);

    ConfirmOrCancel select(int index);

    ConfirmOrCancel select(String text);

    TextInputComponent getTextInput();
}