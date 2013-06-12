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
package org.richfaces.tests.metamer.ftest.richPickList;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.pickListAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import com.google.common.base.Predicate;
import java.net.URL;
import javax.faces.event.PhaseId;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.list.common.SelectableListItem;
import org.richfaces.tests.page.fragments.impl.list.pick.RichFacesSimplePickList;
import org.richfaces.tests.page.fragments.impl.list.pick.RichFacesSimplePickListItem;
import org.richfaces.tests.page.fragments.impl.message.RichFacesMessage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test for rich:pickList on page faces/components/richPickList/simple.xhtml.
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPickList extends AbstractWebDriverTest {

    @FindBy(css = "input[id$=hButton]")
    private WebElement hSubmit;
    @FindBy(css = "input[id$=a4jButton]")
    private WebElement ajaxSubmit;
    @FindBy(css = "span[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=pickList]")
    private RichFacesSimplePickList pickList;
    @FindBy(css = "[id$=msg]")
    private RichFacesMessage message;
    @Page
    private MetamerPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPickList/simple.xhtml");
    }

    private void submitAjax() {
        MetamerPage.waitRequest(ajaxSubmit, WaitRequestType.XHR).click();
    }

    private void submitHTTP() {
        MetamerPage.waitRequest(hSubmit, WaitRequestType.HTTP).click();
    }

    @Test
    public void testAddAllBtn() {
        pickList.addAll();//fragment contains checking
        assertEquals(pickList.source().getItems().size(), 0);
        assertEquals(pickList.target().getItems().size(), 54);
    }

    @Test
    public void testAddAllText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.addAllText, label);
        assertEquals(pickList.getAddAllButtonElement().getText(), label);
    }

    @Test
    public void testAddText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.addText, label);
        assertEquals(pickList.getAddButtonElement().getText(), label);
    }

    @Test
    public void testDisableAddBtn() {
        assertButtonDisabled(pickList.getAddButtonElement());
        pickList.source().selectItemsByIndex(1, 2);
        assertButtonEnabled(pickList.getAddButtonElement());
        pickList.source().getItems().deselectAll();
        assertButtonDisabled(pickList.getAddButtonElement());
        pickList.addAll();
        assertButtonDisabled(pickList.getAddButtonElement());
    }

    @Test
    public void testDisableRemoveBtn() {
        assertButtonDisabled(pickList.getAddButtonElement());
        pickList.addAll();
        pickList.target().selectItemsByIndex(1, 2);
        assertButtonEnabled(pickList.getRemoveButtonElement());
        pickList.target().getItems().deselectAll();
        assertButtonDisabled(pickList.getAddButtonElement());
        pickList.removeAll();
        assertButtonDisabled(pickList.getAddButtonElement());
    }

    @Test
    public void testDisabled() {
        String disabledOptionClass = "rf-pick-opt-dis";
        pickList.source().selectItemsByIndex(0);

        pickListAttributes.set(PickListAttributes.disabled, Boolean.TRUE);
        assertButtonDisabled(pickList.getAddAllButtonElement());
        assertButtonDisabled(pickList.getAddButtonElement());
        assertButtonDisabled(pickList.getRemoveAllButtonElement());
        assertButtonDisabled(pickList.getRemoveButtonElement());
        for (SelectableListItem item : pickList.source().getItems()) {
            assertTrue(item.getItemElement().getAttribute("class").contains(disabledOptionClass), "Item should be disabled.");
        }
        try {
            pickList.source().selectItemsByIndex(1);
        } catch (TimeoutException e) {
            return;
        }
        Assert.fail("Items cannot be selectable, when picklicst is disabled;");
    }

    @Test
    public void testDisabledClass() {
        pickListAttributes.set(PickListAttributes.disabled, Boolean.TRUE);
        testStyleClass(pickList.getRootElement(), BasicAttributes.disabledClass);
    }

    @Test
    public void testDownBottomText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.downBottomText, label);
        pickListAttributes.set(PickListAttributes.orderable, Boolean.TRUE);
        assertEquals(pickList.target().getBottomButtonElement().getText(), label);
    }

    @Test
    public void testDownText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.downText, label);
        pickListAttributes.set(PickListAttributes.orderable, Boolean.TRUE);
        assertEquals(pickList.target().getDownButtonElement().getText(), label);
    }

    @Test
    public void testImmediate() {
        pickListAttributes.set(PickListAttributes.immediate, Boolean.FALSE);
        pickList.source().selectItemsByIndex(0);
        pickList.add();
        submitAjax();
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: [] -> [Alabama]");

        pickListAttributes.set(PickListAttributes.immediate, Boolean.TRUE);
        pickList.source().selectItemsByIndex(0);
        pickList.add();
        submitAjax();
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: [Alabama] -> [Alabama, Alaska]");
    }

    @Test
    public void testItemClass() {
        String testedClass = "metamer-ftest-class";
        pickListAttributes.set(PickListAttributes.itemClass, "metamer-ftest-class");
        for (SelectableListItem li : pickList.source().getItems()) {
            assertTrue(li.getItemElement().getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
        }
    }

    /**
     * Verify that item keep selected even moved from source to target, or back. If selected Alaska from sources, and
     * then added to target, it should remain selected in target list
     */
    @Test
    public void testKeepSelected() {
        pickList.source().selectItemsByIndex(0);
        String textSource = pickList.source().getSelectedItems().get(0).getText();
        pickList.add();
        RichFacesSimplePickListItem item = pickList.target().getSelectedItems().get(0);
        assertTrue(item.isSelected());
        assertEquals(item.getText(), textSource);

        pickList.remove();
        item = pickList.source().getSelectedItems().get(0);
        assertTrue(item.isSelected());
        assertEquals(item.getText(), textSource);
        assertEquals(item.getIndex(), pickList.source().getItems().size() - 1, "Index of removed item.");
    }

    @Test
    public void testListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        pickListAttributes.set(PickListAttributes.listHeight, testedValue);
        assertEquals(Integer.valueOf(pickList.target().getListAreaElement().getCssValue("height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    public void testListWidth() {
        int testedValue = 600;
        int tolerance = 10;
        pickListAttributes.set(PickListAttributes.listWidth, testedValue);
        assertEquals(Integer.valueOf(pickList.target().getListAreaElement().getCssValue("width").replace("px", "")), testedValue, tolerance);
    }

    @Test
    public void testMaxListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        pickListAttributes.set(PickListAttributes.maxListHeight, testedValue);
        pickListAttributes.set(PickListAttributes.listHeight, "");
        assertEquals(Integer.valueOf(pickList.target().getListAreaElement().getCssValue("max-height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    public void testMinListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        pickListAttributes.set(PickListAttributes.listHeight, "");
        pickListAttributes.set(PickListAttributes.minListHeight, testedValue);
        assertEquals(Integer.valueOf(pickList.target().getListAreaElement().getCssValue("min-height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    public void testOnadditems() {
        testFireEvent("additems", new Action() {
            @Override
            public void perform() {
                pickList.source().selectItemsByIndex(0);
                pickList.add();
            }
        });
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnblur() {
        testFireEvent(pickListAttributes, PickListAttributes.onblur,
                new Actions(driver).click(pickList.getRootElement()).click(page.getRequestTimeElement()).build());
    }

    @Test
    public void testOnchange() {
        testFireEvent(Event.CHANGE, pickList.getRootElement(), "change");
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, pickList.getRootElement());
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, pickList.getRootElement());
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, pickList.getRootElement());
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, pickList.getRootElement());
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, pickList.getRootElement());
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, pickList.getRootElement());
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, pickList.getRootElement());
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, pickList.getRootElement());
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, pickList.getRootElement());
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, pickList.getRootElement());
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, pickList.getRootElement());
    }

    @Test
    public void testOnremoveitems() {
        testFireEvent("removeitems", new Action() {
            @Override
            public void perform() {
                pickList.addAll();
                pickList.target().selectItemsByIndex(1);
                pickList.remove();
            }
        });
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11322")
    public void testOnsourceblur() {
        testFireEvent(pickListAttributes, PickListAttributes.onsourceblur,
                new Action() {
                    @Override
                    public void perform() {
                        pickList.source().getRootElement().click();
                        page.getRequestTimeElement().click();
                    }
                });
    }

    @Test
    public void testOnsourceclick() {
        testFireEvent(pickListAttributes, PickListAttributes.onsourceclick,
                new Actions(driver).click(pickList.source().getListAreaElement()).build());
//        testFireEvent(Event.CLICK, pickList.source().getListAreaElement(), "sourceclick");// this will trigger the event twice
    }

    @Test
    public void testOnsourcedblclick() {
        testFireEvent(Event.DBLCLICK, pickList.source().getListAreaElement(), "sourcedblclick");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11322")
    public void testOnsourcefocus() {
        testFireEvent(pickListAttributes, PickListAttributes.onsourcefocus,
                new Actions(driver).click(pickList.source().getListAreaElement()).build());
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnsourcekeydown() {
        testFireEvent(Event.KEYDOWN, pickList.source().getListAreaElement(), "sourcekeydown");
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnsourcekeypress() {
        testFireEvent(Event.KEYPRESS, pickList.source().getListAreaElement(), "sourcekeypress");
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnsourcekeyup() {
        testFireEvent(Event.KEYUP, pickList.source().getListAreaElement(), "sourcekeyup");
    }

    @Test
    public void testOnsourcemousedown() {
        testFireEvent(Event.MOUSEDOWN, pickList.source().getListAreaElement(), "sourcemousedown");
    }

    @Test
    public void testOnsourcemousemove() {
        testFireEvent(Event.MOUSEMOVE, pickList.source().getListAreaElement(), "sourcemousemove");
    }

    @Test
    public void testOnsourcemouseout() {
        testFireEvent(Event.MOUSEOUT, pickList.source().getListAreaElement(), "sourcemouseout");
    }

    @Test
    public void testOnsourcemouseover() {
        testFireEvent(Event.MOUSEOVER, pickList.source().getListAreaElement(), "sourcemouseover");
    }

    @Test
    public void testOnsourcemouseup() {
        testFireEvent(Event.MOUSEUP, pickList.source().getListAreaElement(), "sourcemouseup");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11322")
    public void testOntargetblur() {
        testFireEvent(pickListAttributes, PickListAttributes.onsourceblur,
                new Action() {
                    @Override
                    public void perform() {
                        pickList.source().selectItemsByIndex(0);
                        pickList.add();
                        pickList.target().getItems().get(0).getItemElement().click();
                        page.getRequestTimeElement().click();
                    }
                });
    }

    @Test
    public void testOntargetclick() {
        testFireEvent(pickListAttributes, PickListAttributes.ontargetclick,
                new Actions(driver).click(pickList.target().getListAreaElement()).build());
//        testFireEvent(Event.CLICK, pickList.target().getListAreaElement(), "sourceclick");// this will trigger the event twice    }
    }

    @Test
    public void testOntargetdblclick() {
        testFireEvent(Event.DBLCLICK, pickList.target().getListAreaElement(), "targetdblclick");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11322")
    public void testOntargetfocus() {
        testFireEvent(pickListAttributes, PickListAttributes.ontargetfocus,
                new Actions(driver).click(pickList.target().getListAreaElement()).build());
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOntargetkeydown() {
        testFireEvent(Event.KEYDOWN, pickList.target().getListAreaElement(), "targetkeydown");
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOntargetkeypress() {
        testFireEvent(Event.KEYPRESS, pickList.target().getListAreaElement(), "targetkeypress");
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOntargetkeyup() {
        testFireEvent(Event.KEYUP, pickList.target().getListAreaElement(), "targetkeyup");
    }

    @Test
    public void testOntargetmousedown() {
        testFireEvent(Event.MOUSEDOWN, pickList.target().getListAreaElement(), "targetmousedown");
    }

    @Test
    public void testOntargetmousemove() {
        testFireEvent(Event.MOUSEMOVE, pickList.target().getListAreaElement(), "targetmousemove");
    }

    @Test
    public void testOntargetmouseout() {
        testFireEvent(Event.MOUSEOUT, pickList.target().getListAreaElement(), "targetmouseout");
    }

    @Test
    public void testOntargetmouseover() {
        testFireEvent(Event.MOUSEOVER, pickList.target().getListAreaElement(), "targetmouseover");
    }

    @Test
    public void testOntargetmouseup() {
        testFireEvent(Event.MOUSEUP, pickList.target().getListAreaElement(), "targetmouseup");
    }

    @Test
    public void testOrderable() {
        // firstly check ordering controls doesn't appear near pickList if not "orderable"
        pickListAttributes.set(PickListAttributes.orderable, Boolean.FALSE);
        assertNotPresent(pickList.target().getBottomButtonElement(), "Button should not be present");
        assertNotPresent(pickList.target().getDownButtonElement(), "Button should not be present");
        assertNotPresent(pickList.target().getTopButtonElement(), "Button should not be present");
        assertNotPresent(pickList.target().getUpButtonElement(), "Button should not be present");
        // then make sure that controls appear near pickList when allow ordering behavior
        pickListAttributes.set(PickListAttributes.orderable, Boolean.TRUE);
        assertVisible(pickList.target().getBottomButtonElement(), "Button should be visible");
        assertVisible(pickList.target().getDownButtonElement(), "Button should be visible");
        assertVisible(pickList.target().getTopButtonElement(), "Button should be visible");
        assertVisible(pickList.target().getUpButtonElement(), "Button should be visible");
        // then add some items to target list
        pickList.source().selectItemsByIndex(0, 1, 2);
        pickList.add();
        // all items should remain selected and in this case ordering controls should be disabled
        assertEquals(pickList.target().getSelectedItems().size(), 3);
        // so check ordering controls if they are disabled
        assertButtonDisabled(pickList.target().getBottomButtonElement());
        assertButtonDisabled(pickList.target().getDownButtonElement());
        assertButtonDisabled(pickList.target().getTopButtonElement());
        assertButtonDisabled(pickList.target().getUpButtonElement());
        // now is time to select one item. This should cause ordering controls enable
        pickList.target().selectItemsByIndex(0);
        // since it was first item, "Down" and "Last" buttons should be enabled
        assertButtonEnabled(pickList.target().getBottomButtonElement());
        assertButtonEnabled(pickList.target().getDownButtonElement());
        assertButtonDisabled(pickList.target().getTopButtonElement());
        assertButtonDisabled(pickList.target().getUpButtonElement());
        // move first item to last
        pickList.target().bottom();
        // verify that previously first item is now the last item (select 3rd item, and verify text)
        assertEquals(pickList.target().getSelectedItems().get(0).getIndex(), 2);
        // then move this item one step "up"
        pickList.target().up();
        assertEquals(pickList.target().getSelectedItems().get(0).getIndex(), 1);
        // and then verify if all items are submitted in user defined order as well
        submitAjax();
        assertEquals(output.getText(), pickList.target().toString());
    }

    @Test
    public void testRemoveAllText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.removeAllText, label);
        assertEquals(pickList.getRemoveAllButtonElement().getText(), label);
    }

    @Test
    public void testRemoveText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.removeText, label);
        assertEquals(pickList.getRemoveButtonElement().getText(), label);
    }

    @Test
    public void testRendered() {
        pickListAttributes.set(PickListAttributes.rendered, Boolean.TRUE);
        assertPresent(pickList.getRootElement(), "Picklist should be present");
        pickListAttributes.set(PickListAttributes.rendered, Boolean.FALSE);
        assertNotPresent(pickList.getRootElement(), "Picklist should not be present");
    }

    @Test
    public void testRequired() {
        pickListAttributes.set(PickListAttributes.required, Boolean.TRUE);
        pickList.source().selectItemsByIndex(0);
        pickList.add();
        submitAjax();
        assertFalse(message.isVisible(), "Message should not be visible.");

        pickList.removeAll();
        submitAjax();
        assertTrue(message.isVisible(), "Message should be visible.");
    }

    @Test
    public void testRequiredMessage() {
        String messageString = "This is new required message.";
        pickListAttributes.set(PickListAttributes.required, Boolean.TRUE);
        pickListAttributes.set(PickListAttributes.requiredMessage, messageString);
        submitAjax();
        assertEquals(message.getDetail(), messageString);
    }

    /**
     * Verify submit by JSF submit button
     */
    @Test
    public void testSaveJSF() {
        pickList.source().selectItemsByIndex(0);
        String textSource = pickList.source().getSelectedItems().get(0).getText();
        pickList.add();
        submitHTTP();
        String textTarget = pickList.target().getItems().get(0).getText();
        assertEquals(textTarget, textSource);
        assertEquals(output.getText(), "[" + textSource + "]");
        assertEquals(output.getText(), pickList.target().toString());
    }

    @Test
    public void testSelectItemClass() {
        testHTMLAttribute(new FutureTarget<WebElement>() {
            @Override
            public WebElement getTarget() {
                return pickList.source().getSelectedItems().get(0).getItemElement();
            }
        }, pickListAttributes, PickListAttributes.selectItemClass, "metamer-ftest-class", new Action() {
            @Override
            public void perform() {
                pickList.source().selectItemsByIndex(0);
            }
        });
    }

    @Test
    public void testSourceCaption() {
        String caption = "This is source";
        pickListAttributes.set(PickListAttributes.sourceCaption, caption);
        assertEquals(pickList.source().getCaption(), caption);
    }

    @Test
    public void testStyle() {
        testStyle(pickList.getRootElement());
    }

    @Test
    public void testStyleClass() {
        testStyleClass(pickList.getRootElement());
    }

    /**
     * Verify switchByClick attribute
     */
    @Test
    public void testSwitchByClick() {
        pickListAttributes.set(PickListAttributes.switchByClick, Boolean.TRUE);
        RichFacesSimplePickListItem item = pickList.source().getItems().get(0);
        String text = item.getText();
        item.getItemElement().click();
        Graphene.waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return pickList.target().getItems().size() == 1;
            }
        });
        item = pickList.target().getSelectedItems().get(0);
        assertEquals(item.getText(), text);
        assertTrue(item.isSelected());
    }

    /**
     * Verify switchByDblClick attribute
     */
    @Test
    public void testSwitchByDblClick() {
        pickListAttributes.set(PickListAttributes.switchByDblClick, Boolean.TRUE);
        RichFacesSimplePickListItem item = pickList.source().getItems().get(0);
        String text = item.getText();
        new Actions(driver).doubleClick(item.getItemElement()).perform();
        Graphene.waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return pickList.target().getItems().size() == 1;
            }
        });
        item = pickList.target().getSelectedItems().get(0);
        assertEquals(item.getText(), text);
        assertTrue(item.isSelected());
    }

    @Test
    public void testTargetCaption() {
        String caption = "This is target";
        pickListAttributes.set(PickListAttributes.targetCaption, caption);
        assertEquals(pickList.target().getCaption(), caption);
    }

    @Test
    public void testUpText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.upText, label);
        pickListAttributes.set(PickListAttributes.orderable, Boolean.TRUE);
        assertEquals(pickList.target().getUpButtonElement().getText(), label);
    }

    @Test
    public void testUpTopText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.upTopText, label);
        pickListAttributes.set(PickListAttributes.orderable, Boolean.TRUE);
        assertEquals(pickList.target().getTopButtonElement().getText(), label);
    }

    @Test
    public void testValidator() {
        pickList.addAll();
        submitAjax();
        assertEquals(message.getDetail(), "We are sorry, but @ is not allowed to join us!");
    }

    @Test
    public void testValidatorMessage() {
        String messageText = "Custom validatorMessage.";
        pickListAttributes.set(PickListAttributes.validatorMessage, messageText);
        pickList.addAll();
        submitAjax();
        assertEquals(message.getDetail(), messageText);
    }

    @Test
    public void testValueChangeListener() {
        pickList.source().selectItemsByIndex(0);
        pickList.add();
        submitAjax();
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: [] -> [Alabama]");

        pickList.source().selectItemsByIndex(0);
        pickList.add();
        submitAjax();
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: [Alabama] -> [Alabama, Alaska]");
    }

    private void assertButtonDisabled(WebElement e) {
        assertFalse(PickListButton.from(e).isEnabled(), "Button should be disabled.");
    }

    private void assertButtonEnabled(WebElement e) {
        assertTrue(PickListButton.from(e).isEnabled(), "Button should be enabled.");
    }

    public static class PickListButton {

        private static final String[] disabledClasses = { "rf-pick-btn-dis", "rf-ord-btn-dis" };
        private final WebElement button;

        public PickListButton(WebElement button) {
            this.button = button;
        }

        public static PickListButton from(WebElement e) {
            return new PickListButton(e);
        }

        public boolean isEnabled() {
            return !button.getAttribute("class").contains(disabledClasses[0])
                    && !button.getAttribute("class").contains(disabledClasses[1]);
        }
    }
}
