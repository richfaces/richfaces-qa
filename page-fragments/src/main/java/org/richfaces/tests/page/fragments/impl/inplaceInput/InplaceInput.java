package org.richfaces.tests.page.fragments.impl.inplaceInput;

import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;

public interface InplaceInput {

    TextInputComponentImpl getTextInput();

    ConfirmOrCancel type(String text);
}
