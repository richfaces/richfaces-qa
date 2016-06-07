package org.richfaces.tests.metamer.ftest.richContextMenu;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

public class TestContextMenuFragment extends AbstractWebDriverTest {

    private final Attributes<ContextMenuAttributes> contextMenuAttributes = getAttributes();

    @Page
    private ContextMenuSimplePage page;

    @Override
    public String getComponentTestPagePath() {
        return "richContextMenu/simple.xhtml";
    }

    @Test
    public void testSetupShowEventFromWidget() {
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "contextmenu");
        page.getContextMenu().advanced().setShowEventFromWidget();
        page.getContextMenu().selectItem(0, page.getTargetPanel1());
        assertEquals(page.getOutput().getText(), "New", "Menu action was not performed! ShowEvent was not correctly set from widget!.");
    }

    @Test
    public void testSetupTargetFromWidget() {
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "click");
        page.getContextMenu().advanced().setShowEvent(Event.CLICK);
        page.getContextMenu().advanced().setTarget(page.getTargetPanel2());
        page.getContextMenu().advanced().setTargetFromWidget();
        page.getContextMenu().selectItem(0);
        assertEquals(page.getOutput().getText(), "New", "Menu action was not performed! Target was not correctly set from widget!.");
    }
}
