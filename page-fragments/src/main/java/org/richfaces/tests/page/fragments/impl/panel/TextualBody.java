package org.richfaces.tests.page.fragments.impl.panel;

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;

public class TextualBody {

    @Root
    private WebElement root;

    public String getText() {
        return root.getText();
    }
}
