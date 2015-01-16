package org.richfaces.tests.metamer.ftest.richPanelMenu;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.panelMenu.RichFacesPanelMenu;
import org.richfaces.fragment.panelMenu.RichFacesPanelMenuItem;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

public class TestPanelMenuFragmentShowcase extends AbstractWebDriverTest {

    private final Attributes<PanelMenuAttributes> panelMenuAttributes = getAttributes();

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
    @Templates(exclude = "uiRepeat")
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

    @Test (groups = { "Future", "uiRepeat" }) // fails with JSF 2.2
    @IssueTracking("https://issues.jboss.org/browse/RF-13727")
    @Templates (value = "uiRepeat")
    public void testFragmentInUiRepeat(){
        testFragment();
    }
}
