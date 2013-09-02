package org.richfaces.tests.page.fragments.impl.panelMenu;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RichFacesPanelMenu extends AbstractPanelMenu {

    @FindByJQuery(".rf-pm-top-gr,.rf-pm-gr")
    private List<WebElement> menuGroups;
    @FindByJQuery(".rf-pm-top-itm,.rf-pm-itm")
    private List<WebElement> menuItems;

    private AdvancedInteractions advancedInteractions;

    @Root
    private WebElement root;

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

        public List<WebElement> getAllSelectedItems() {
            return root.findElements(By.cssSelector("div[class*=rf-pm][class*=-itm-sel]"));
        }

        public List<WebElement> getAllSelectedGroups() {
            return root.findElements(By.cssSelector("div[class*=rf-pm][class*=-gr-sel]"));
        }

        public List<WebElement> getAllDisabledGroups() {
            return root.findElements(By.cssSelector("div[class*=rf-pm-][class*=-gr-dis]"));
        }

        public List<WebElement> getAllDisabledItems() {
            return root.findElements(By.cssSelector("div[class*=rf-pm-][class*=-itm-dis]"));
        }

        public List<WebElement> getAllExpandedGroups() {
            return root.findElements(By.cssSelector("div.rf-pm-hdr-exp"));
        }

        public WebElement getRootElement() {
            return root;
        }
    }
}
