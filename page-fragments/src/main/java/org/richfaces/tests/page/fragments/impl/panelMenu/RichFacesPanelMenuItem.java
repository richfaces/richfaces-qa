package org.richfaces.tests.page.fragments.impl.panelMenu;

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RichFacesPanelMenuItem implements PanelMenuItem {

    @FindBy(css = "td[class*=rf-][class*=-itm-lbl]")
    private WebElement label;
    @FindBy(css = "td[class*=rf-][class*=-itm-ico]")
    private WebElement leftIcon;
    @FindBy(css = "td[class*=rf-][class*=-itm-exp-ico]")
    private WebElement rightIcon;
    @FindBy(css = "td[class*=rf-][class*=-itm-ico] img")
    private WebElement leftIconImg;
    @FindBy(css = "td[class*=rf-][class*=-itm-exp-ico] img")
    private WebElement rightIconImg;

    @Root
    private WebElement root;

    private AdvancedInteractions advancedInteractions;

    @Override
    public void select() {
        root.click();
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions {

        public WebElement getLeftIcon() {
            return leftIcon;
        }

        public WebElement getRightIcon() {
            return rightIcon;
        }

        public WebElement getRightIconImg() {
            return rightIconImg;
        }

        public WebElement getLeftIconImg() {
            return leftIconImg;
        }

        public boolean isSelected() {
            return root.getAttribute("class").contains("-sel");
        }

        public WebElement getRootElement() {
            return root;
        }

        public boolean isDisabled() {
            return root.getAttribute("class").contains("-dis");
        }

        public boolean isTransparent(WebElement icon) {
            return icon.getAttribute("class").contains("-transparent");
        }
    }
}
