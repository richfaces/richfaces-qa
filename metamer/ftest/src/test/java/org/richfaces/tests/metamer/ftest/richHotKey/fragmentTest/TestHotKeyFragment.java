package org.richfaces.tests.metamer.ftest.richHotKey.fragmentTest;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.hotkey.RichFacesHotkey;
import org.richfaces.fragment.log.LogEntry;
import org.richfaces.fragment.log.RichFacesLog;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.richHotKey.HotKeyAttributes;
import org.richfaces.tests.metamer.ftest.richHotKey.TestHotKeyAttributes.KeysEnum;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

public class TestHotKeyFragment extends AbstractWebDriverTest {

    @FindBy(css = "div[id='richfaces.log']")
    protected RichFacesLog log;

    @FindBy(css = "span[id$=richHotKey1]")
    protected RichFacesHotkey hotkey1;

    private Attributes<HotKeyAttributes> firstHotkeyAttributes = getAttributes("attributes1");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richHotKey/simple.xhtml");
    }

    @Test
    public void testSetupSelectorFromWidget() {
        firstHotkeyAttributes.set(HotKeyAttributes.selector, ".first-input");
        hotkey1.setHotkey(KeysEnum.CTRL_X.toString());
        // wrong one
        hotkey1.setSelector(".second-input");
        // this should correct it
        hotkey1.advanced().setSelectorFromWidget();
        hotkey1.invoke();
        List<? extends LogEntry> items = log.getLogEntries().getItems(
            ChoicePickerHelper.byVisibleText().contains("hotkey 1 : onkeydown"));
        assertEquals(items.size(), 1,
            "The number of hotkey events doesn't match. Check whether the selector was set correctly by setupSelectorFromWidget()");
    }

    @Test
    public void testSetupHotKeyFromWidget() {
        // wrong one
        hotkey1.setHotkey(KeysEnum.ALT_CONTROL_X.toString());
        // this should correct it
        hotkey1.advanced().setHotkeyFromWidget();
        hotkey1.invoke();
        List<? extends LogEntry> items = log.getLogEntries().getItems(
            ChoicePickerHelper.byVisibleText().contains("hotkey 1 : onkeydown"));
        assertEquals(items.size(), 1,
            "The number of hotkey events doesn't match. Check whether the hot key sequence was set correctly by setupHotkeyFromWidget()");
    }
}