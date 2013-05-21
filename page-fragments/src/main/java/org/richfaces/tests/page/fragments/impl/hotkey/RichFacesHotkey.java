/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.page.fragments.impl.hotkey;

import java.util.EnumSet;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Utils;

public class RichFacesHotkey implements Hotkey {

    @FindBy(tagName = "script")
    private WebElement script;
    @Drone
    private WebDriver driver;
    @Root
    private WebElement rootElement;
    private String previousScriptText = "";
    private String previousHotkey = "";

    private enum ModifierKeys {

        ALT(Keys.ALT), SHIFT(Keys.SHIFT), CONTROL(Keys.CONTROL);
        private final Keys key;

        private ModifierKeys(Keys key) {
            this.key = key;
        }

        public Keys getKey() {
            return key;
        }
    }

    @Override
    public WebElement getRootElement() {
        return rootElement;
    }

    private String getKeysChordFromScript() {
        String keyPrefix = "\"key\":\"";
        String scriptText = Utils.getTextFromHiddenElement(script);
        if (previousScriptText.equals(scriptText)) {
            return previousHotkey;
        }
        previousScriptText = scriptText;
        int index1 = scriptText.indexOf(keyPrefix) + keyPrefix.length();
        int index2 = scriptText.indexOf(",", index1) - 1;
        String hotkey = scriptText.substring(index1, index2);
        String hotkeyTmp = hotkey;
        EnumSet<ModifierKeys> keys = EnumSet.noneOf(ModifierKeys.class);
        if (hotkeyTmp.contains("ctrl")) {
            hotkeyTmp = hotkeyTmp.replaceAll("ctrl", "");
            keys.add(ModifierKeys.CONTROL);
        }
        if (hotkeyTmp.contains("alt")) {
            hotkeyTmp = hotkeyTmp.replaceAll("alt", "");
            keys.add(ModifierKeys.ALT);
        }
        if (hotkeyTmp.contains("shift")) {
            hotkeyTmp = hotkeyTmp.replaceAll("shift", "");
            keys.add(ModifierKeys.SHIFT);
        }
        // there can be some '+', but they can be ignored
        hotkey = "";
        for (ModifierKeys modifierKeys : keys) {
            hotkey += modifierKeys.getKey();
        }
        hotkey += hotkeyTmp;
        previousHotkey = hotkey;
        return hotkey;
    }

    @Override
    public void invoke() {
        invokeOn(driver.findElement(By.xpath("//body")));
    }

    @Override
    public void invokeOn(WebElement element) {
        new Actions(driver).sendKeys(element, Keys.chord(getKeysChordFromScript())).perform();
    }
}
