package org.richfaces.tests.metamer.ftest.richPanelMenu;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.panelMenuAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.page.fragments.impl.panelMenu.RichFacesPanelMenu;
import org.richfaces.tests.page.fragments.impl.panelMenu.RichFacesPanelMenuItem;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.testng.annotations.Test;

public class TestPanelMenuFragmentShowcase extends AbstractWebDriverTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenu/simple.xhtml");
    }

    @FindBy(className = "rf-pm")
    private RichFacesPanelMenu menu;
    @FindByJQuery("*[id*=current]")
    private WebElement selectedItem;
    @FindBy(css = "div[id$=item22]")
    private RichFacesPanelMenuItem item22;

    @Test
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

        panelMenuAttributes.set(PanelMenuAttributes.expandEvent, "mouseover");
        menu.advanced().setExpandEvent(Event.MOUSEOVER);
        guardAjax(menu.expandGroup("Group 3")).selectItem("Item 3.1");
        assertEquals(selectedItem.getText(), "item31");

        guardAjax(item22).select();
        assertEquals(selectedItem.getText(), "item22");

        guardAjax(menu).selectItem("Item 2.1");
        assertEquals(selectedItem.getText(), "item21");
    }
}
