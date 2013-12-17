package org.richfaces.tests.metamer.ftest.richContextMenu.fragmentTest;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.richContextMenu.ContextMenuAttributes;
import org.richfaces.tests.metamer.ftest.richContextMenu.ContextMenuSimplePage;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

public class TestContextMenuFragment extends AbstractWebDriverTest {

    @Page
    private ContextMenuSimplePage page;

    private final Attributes<ContextMenuAttributes> contextMenuAttributes = getAttributes();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richContextMenu/simple.xhtml");
    }

    @Test
    public void testSetupShowEventFromWidget() {
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "contextmenu");
        page.getContextMenu().advanced().setupShowEventFromWidget();
        page.getContextMenu().selectItem(0, page.getTargetPanel1());
        assertEquals(page.getOutput().getText(), "Open", "Menu action was not performed! ShowEvent was not correctly set from widget!.");
    }

    @Test
    public void testSetupTargetFromWidget() {
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "click");
        page.getContextMenu().advanced().setupShowEvent(Event.CLICK);
        page.getContextMenu().advanced().setupTarget(page.getTargetPanel2());
        page.getContextMenu().advanced().setupTargetFromWidget();
        page.getContextMenu().selectItem(0);
        assertEquals(page.getOutput().getText(), "Open", "Menu action was not performed! Target was not correctly set from widget!.");
    }
}
