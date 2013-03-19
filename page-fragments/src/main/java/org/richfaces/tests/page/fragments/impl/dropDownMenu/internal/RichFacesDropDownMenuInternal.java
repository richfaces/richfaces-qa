package org.richfaces.tests.page.fragments.impl.dropDownMenu.internal;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.dropDownMenu.RichFacesDropDownMenu;

public class RichFacesDropDownMenuInternal extends RichFacesDropDownMenu {

    @FindBy(jquery = ".rf-ddm-lbl:eq(0)")
    private WebElement topLvlElement;

    public String getLangAttribute() {
        return topLvlElement.getAttribute("lang");
    }

    public WebElement getTopLvlElement() {
        return topLvlElement;
    }

    public void setTopLvlElement(WebElement topLvlElement) {
        this.topLvlElement = topLvlElement;
    }
}
