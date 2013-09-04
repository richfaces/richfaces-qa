package org.richfaces.tests.metamer.ftest.richDropDownMenu;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;
/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 *
 */
public class TestDropDownMenuSideMenu extends AbstractDropDownMenuTest {
    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDropDownMenu/sideMenu.xhtml");
    }

    @Inject
    @Use(empty = false)
    private Integer delay;

    @Test
    public void testInit() {
        super.testInit();
    }

    @Test
    public void testShowEvent() {
        super.testShowEvent();
    }

    @Test
    public void testStyle() {
        super.testStyle();
    }

    @Test
    public void testStyleClass() {
        super.testStyleClass();
    }

    @Test
    public void testDir() {
        super.testDir();
    }

    @Test
    public void testLang() {
        super.testLang();
    }

    @Test
    public void testMode() {
        super.testMode();
    }

    @Test
    public void testDisabled() {
        super.testDisabled();
    }

    @Test
    @Use(field = "delay", ints = { 1000, 1500, 2500 })
    public void testHideDelay() {
        super.testHideDelay(delay);
    }

    @Test
    public void testPopupWidth() {
        super.testPopupWidth();
    }

    @Test
    public void testRendered() {
        super.testRendered();
    }

    @Test
    @Use(field = "delay", ints = { 1000, 1500, 2500 })
    public void testShowDelay() {
        super.testShowDelay(delay);
    }

    @Test
    public void testTitle() {
        super.testTitle();
    }

    @Test
    public void testOnclick() {
        super.testOnclick();
    }

    @Test
    public void testOndblclick() {
        super.testOndblclick();
    }

    @Test
    public void testOngrouphide() {
        super.testOngrouphide();
    }

    @Test
    public void testOngroupshow() {
        super.testOngroupshow();
    }

    @Test
    public void testOnhide() {
        super.testOnhide();
    }

    @Test
    public void testOnitemclick() {
        super.testOnitemclick();
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12792")
    public void testOnkeydown() {
        super.testOnkeydown();
    }

    @Test
    public void testOnkeypress() {
        super.testOnkeypress();
    }

    @Test
    public void testOnkeyup() {
        super.testOnkeyup();
    }

    @Test
    public void testOnmousedown() {
        super.testOnmousedown();
    }

    @Test
    public void testOnmousemove() {
        super.testOnmousemove();
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12854")
    public void testOnmouseout() {
        super.testOnmouseout();
    }

    @Test
    public void testOnmouseover() {
        super.testOnmouseover();
    }

    @Test
    public void testOnmouseup() {
        super.testOnmouseup();
    }

    @Test
    public void testOnshow() {
        super.testOnshow();
    }

    @Test
    public void testHorizontalOffset() {
        super.testHorizontalOffset();
    }

    @Test
    public void testVerticalOffset() {
        super.testVerticalOffset();
    }
}
