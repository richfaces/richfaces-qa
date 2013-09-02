package org.richfaces.tests.page.fragments.impl.panelMenu;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RichFacesPanelMenuGroup extends AbstractPanelMenu {

    @FindByJQuery(".rf-pm-gr:visible")
    private List<WebElement> menuGroups;
    @FindByJQuery(".rf-pm-itm:visible")
    private List<WebElement> menuItems;
    @FindBy(css = "td[class*=rf-pm-][class*=-gr-lbl]")
    private WebElement label;
    @FindByJQuery("> div[class*=rf-pm-][class*=-gr-hdr]")
    private WebElement header;
    @FindByJQuery("td[class*=rf-pm-][class*=-gr-ico] :visible[class*=rf-pm-ico-]")
    private WebElement leftIcon;
    @FindByJQuery("td[class*=rf-pm-][class*=-gr-exp-ico] :visible[class*=rf-pm-ico-]")
    private WebElement rightIcon;

    @Root
    private WebElement root;
    private AdvancedInteractions advancedInteractions;

    @Override
    public List<WebElement> getMenuItems() {
        return menuItems;
    }

    @Override
    public List<WebElement> getMenuGroups() {
        return menuGroups;
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions extends AbstractPanelMenu.AdvancedInteractions {

        public boolean isExpanded() {
            return super.isGroupExpanded(root);
        }

        public WebElement getLabel() {
            return label;
        }

        public WebElement getRootElement() {
            return root;
        }

        public boolean isTransparent(WebElement icon) {
            return icon.getAttribute("class").contains("-transparent");
        }

        public WebElement getLeftIcon() {
            return leftIcon;
        }

        public WebElement getRightIcon() {
            return rightIcon;
        }

        public WebElement getHeaderElement() {
            return header;
        }

        public boolean isDisabled() {
            return root.getAttribute("class").contains("-dis");
        }

        public boolean isSelected() {
            return getHeaderElement().getAttribute("class").contains("-sel");
        }
    }
}