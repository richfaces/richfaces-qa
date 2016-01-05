/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richValidator;

import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.fragment.select.RichFacesSelect;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Templates("plain")
public class TestValidatorEvent extends AbstractWebDriverTest {

    @FindBy(css = "[id$='blur']")
    private TextInputComponentImpl blurInput;
    @FindBy(css = "[id$='blurMsg']")
    private RichFacesMessage blurMsg;

    @FindBy(css = "[id$='change']")
    private TextInputComponentImpl changeInput;
    @FindBy(css = "[id$='changeMsg']")
    private RichFacesMessage changeMsg;

    @FindBy(css = "[id$='click']")
    private TextInputComponentImpl clickInput;
    @FindBy(css = "[id$='clickMsg']")
    private RichFacesMessage clickMsg;

    @FindBy(css = "[id$='dblclick']")
    private TextInputComponentImpl dblclickInput;
    @FindBy(css = "[id$='dblclickMsg']")
    private RichFacesMessage dblclickMsg;

    @FindBy(css = "[id$='focus']")
    private TextInputComponentImpl focusInput;
    @FindBy(css = "[id$='focusMsg']")
    private RichFacesMessage focusMsg;

    @ArquillianResource
    private Keyboard keyboard;

    @FindBy(css = "[id$='keypress']")
    private TextInputComponentImpl keypressInput;
    @FindBy(css = "[id$='keypressMsg']")
    private RichFacesMessage keypressMsg;

    @FindBy(css = "[id$='keyup']")
    private TextInputComponentImpl keyupInput;
    @FindBy(css = "[id$='keyupMsg']")
    private RichFacesMessage keyupMsg;

    @FindBy(css = "[id$='listhide']")
    private RichFacesSelect listhideInput;
    @FindBy(css = "[id$='listhideMsg']")
    private RichFacesMessage listhideMsg;

    @FindBy(css = "[id$='listshow']")
    private RichFacesSelect listshowInput;
    @FindBy(css = "[id$='listshowMsg']")
    private RichFacesMessage listshowMsg;

    @FindBy(css = "[id$='selectitem']")
    private RichFacesSelect selectitemInput;
    @FindBy(css = "[id$='selectitemMsg']")
    private RichFacesMessage selectitemMsg;

    private void assertMessageIsNotVisible(RichFacesMessage m) {
        try {
            assertMessageIsVisible(m);
            fail("Message should not be visible.");
        } catch (TimeoutException ok) {
        }
    }

    private void assertMessageIsVisible(RichFacesMessage m) {
        m.advanced().waitUntilMessageIsVisible().withTimeout(500, TimeUnit.MILLISECONDS).perform();
    }

    @Override
    public String getComponentTestPagePath() {
        return "richValidator/events.xhtml";
    }

    private void setInvalidValue(TextInputComponentImpl input) {
        // focus
        input.advanced().getInputElement().click();
        keyboard.sendKeys(Keys.BACK_SPACE);
        keyboard.sendKeys("1");
    }

    private void setInvalidValue(RichFacesSelect select) {
        select.openSelect().select(0);
    }

    @Test
    @CoversAttributes("event")
    public void testEventBlur() {
        setInvalidValue(blurInput);
        assertMessageIsNotVisible(blurMsg);
        // trigger the event
        getMetamerPage().getResponseDelayElement().click();
        assertMessageIsVisible(blurMsg);
    }

    @Test
    @CoversAttributes("event")
    public void testEventChange() {
        setInvalidValue(changeInput);
        assertMessageIsNotVisible(changeMsg);
        // trigger the event
        getMetamerPage().getResponseDelayElement().click();
        assertMessageIsVisible(changeMsg);
    }

    @Test
    @CoversAttributes("event")
    public void testEventClick() {
        setInvalidValue(clickInput);
        assertMessageIsNotVisible(clickMsg);
        // blur/change
        getMetamerPage().getResponseDelayElement().click();
        assertMessageIsNotVisible(clickMsg);
        // trigger the event
        clickInput.advanced().getInputElement().click();
        assertMessageIsVisible(clickMsg);
    }

    @Test
    @CoversAttributes("event")
    public void testEventDblclick() {
        setInvalidValue(dblclickInput);
        assertMessageIsNotVisible(dblclickMsg);
        // blur/change
        getMetamerPage().getResponseDelayElement().click();
        assertMessageIsNotVisible(dblclickMsg);
        // click should not trigger the validation
        dblclickInput.advanced().getInputElement().click();
        assertMessageIsNotVisible(dblclickMsg);
        // trigger the event
        new Actions(driver).doubleClick(dblclickInput.advanced().getInputElement()).perform();
        assertMessageIsVisible(dblclickMsg);
    }

    @Test
    @CoversAttributes("event")
    public void testEventFocus() {
        setInvalidValue(focusInput);
        assertMessageIsNotVisible(focusMsg);
        // blur/change
        getMetamerPage().getResponseDelayElement().click();
        assertMessageIsNotVisible(focusMsg);

        // trigger the event
        focusInput.advanced().getInputElement().click();
        assertMessageIsVisible(focusMsg);
    }

    @Test
    @CoversAttributes("event")
    public void testEventKeypress() {
        setInvalidValue(keypressInput);
        assertMessageIsVisible(keypressMsg);
    }

    @Test
    @CoversAttributes("event")
    public void testEventKeyup() {
        setInvalidValue(keyupInput);
        assertMessageIsVisible(keyupMsg);
    }

    @Test
    @CoversAttributes("event")
    public void testEventListhide() {
        listhideInput.openSelect();
        assertMessageIsNotVisible(listhideMsg);

        // trigger the event
        setInvalidValue(listhideInput);
        assertMessageIsVisible(listhideMsg);
    }

    @Test
    @CoversAttributes("event")
    public void testEventListshow() {
        setInvalidValue(listshowInput);
        assertMessageIsNotVisible(listshowMsg);

        waiting(200);// stabilization wait time
        // trigger the event
        listshowInput.openSelect();
        assertMessageIsVisible(listshowMsg);
    }

    @Test
    @CoversAttributes("event")
    public void testEventSelectitem() {
        selectitemInput.openSelect();
        assertMessageIsNotVisible(selectitemMsg);

        // trigger the event
        setInvalidValue(selectitemInput);
        assertMessageIsVisible(selectitemMsg);
    }
}
