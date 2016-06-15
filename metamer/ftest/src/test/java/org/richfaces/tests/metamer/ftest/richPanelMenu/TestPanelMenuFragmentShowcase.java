package org.richfaces.tests.metamer.ftest.richPanelMenu;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.panelMenu.PanelMenuGroup;
import org.richfaces.fragment.panelMenu.RichFacesPanelMenu;
import org.richfaces.fragment.panelMenu.RichFacesPanelMenuItem;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

public class TestPanelMenuFragmentShowcase extends AbstractWebDriverTest {

    private final Attributes<PanelMenuAttributes> attributes = getAttributes();

    @FindBy(css = "div[id$=item21]")
    private WebElement item21Element;
    @FindBy(css = "div[id$=item22]")
    private RichFacesPanelMenuItem item22;
    @FindBy(css = "div[id$=item22]")
    private WebElement item22Element;
    @FindBy(css = "div[id$=item31]")
    private WebElement item31Element;
    @FindBy(className = "rf-pm")
    private RichFacesPanelMenu menu;
    @FindByJQuery("*[id*=current]")
    private WebElement selectedItem;

    @Override
    public String getComponentTestPagePath() {
        return "richPanelMenu/simple.xhtml";
    }

    @Test
    @IssueTracking(value = "https://issues.jboss.org/browse/RF-13727")
    public void testFragment() {
        guardAjax(menu.expandGroup("Group 1")).selectItem(ChoicePickerHelper.byVisibleText().match("Item 1.2"));
        assertEquals(selectedItem.getText(), "item12");

        try {
            menu.expandGroup("Group 2.4").selectItem("Item 2.4.1");
            fail("Illegal argument exceptions should be thrown as Group 2.4 is not visible at this moment!");
        } catch (IllegalArgumentException ex) {
            // OK
        }

        try {
            menu.selectItem("Item 2");
            fail("IllegalArgumentExcetpion should be thrown, as item 2 is disabled atm.");
        } catch (IllegalArgumentException ex) {
            // OK
        }

        menu.expandGroup("Group 2").expandGroup("Group 2.4");
        guardAjax(menu).selectItem("Item 2.4.2");
        assertEquals(selectedItem.getText(), "item242");

        attributes.set(PanelMenuAttributes.expandEvent, "dblclick");
        menu.advanced().setExpandEvent(Event.DBLCLICK);

        PanelMenuGroup group3 = menu.expandGroup("Group 3");
        jsUtils.scrollToView(item31Element);
        guardAjax(group3).selectItem("Item 3.1");
        assertEquals(selectedItem.getText(), "item31");

        jsUtils.scrollToView(item22Element);
        guardAjax(item22).select();
        assertEquals(selectedItem.getText(), "item22");

        jsUtils.scrollToView(item21Element);
        guardAjax(menu).selectItem("Item 2.1");
        assertEquals(selectedItem.getText(), "item21");
    }
}
