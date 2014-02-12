/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.list.ListItem;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.fragment.orderingList.OrderingList;
import org.richfaces.fragment.pickList.RichFacesPickList;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

/**
 * Test for rich:pickList on page faces/components/richPickList/simple.xhtml.
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPickList extends AbstractWebDriverTest {

    private final Attributes<PickListAttributes> pickListAttributes = getAttributes();

    @FindBy(css = "input[id$=hButton]")
    private WebElement hSubmit;
    @FindBy(css = "input[id$=a4jButton]")
    private WebElement ajaxSubmit;
    @FindBy(css = "span[id$=output]")
    private WebElement output;
    @FindBy(css = ".pick-list.outer")
    private RichFacesPickList pickList;
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
        pickList.addAll();// fragment contains checking
        assertEquals(pickList.advanced().getSourceList().getItems().size(), 0);
        assertEquals(pickList.advanced().getTargetList().getItems().size(), 54);
    }

    @Test
    @Templates(value = "plain")
    public void testAddAllText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.addAllText, label);
        assertEquals(pickList.advanced().getAddAllButtonElement().getText(), label);
    }

    @Test
    @Templates(value = "plain")
    public void testAddText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.addText, label);
        assertEquals(pickList.advanced().getAddButtonElement().getText(), label);
    }

    @Test(groups = "Future")
    @IssueTracking("https://github.com/richwidgets/richwidgets/issues/106")
    @Templates(value = "plain")
    public void testDisableAddBtn() {
        assertButtonDisabled(pickList.advanced().getAddButtonElement());
        pickList.advanced().getSourceList().getItem(0).select();// select
        assertButtonEnabled(pickList.advanced().getAddButtonElement());
        pickList.add(0);// no selected item in source list >>> button disabled
        assertButtonDisabled(pickList.advanced().getAddButtonElement());
        pickList.addAll();
        assertButtonDisabled(pickList.advanced().getAddButtonElement());
    }

    @Test(groups = "Future")
    @IssueTracking("https://github.com/richwidgets/richwidgets/issues/106")
    public void testDisableRemoveBtn() {
        assertButtonDisabled(pickList.advanced().getRemoveButtonElement());
        pickList.addAll();
        pickList.advanced().getTargetList().getItem(0).select();
        assertButtonEnabled(pickList.advanced().getRemoveButtonElement());
        pickList.remove(0);// no selected item in target list >>> button disabled
        assertButtonDisabled(pickList.advanced().getRemoveButtonElement());
        pickList.removeAll();
        assertButtonDisabled(pickList.advanced().getRemoveButtonElement());
    }

    @Test
    public void testDisabled() {
        pickList.advanced().getSourceList().getItem(0).select();

        pickListAttributes.set(PickListAttributes.disabled, Boolean.TRUE);
        assertTrue(pickList.advanced().isDisabled());

        try {
            pickList.add(0);
        } catch (TimeoutException e) {
            return;
        }
        Assert.fail("Items cannot be selectable, when picklicst is disabled;");
    }

    @Test
    @Templates(value = "plain")
    public void testDownBottomText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.downBottomText, label);
        pickListAttributes.set(PickListAttributes.orderable, Boolean.TRUE);
        assertEquals(pickList.advanced().getBottomButtonElement().getText(), label);
    }

    @Test
    @Templates(value = "plain")
    public void testDownText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.downText, label);
        pickListAttributes.set(PickListAttributes.orderable, Boolean.TRUE);
        assertEquals(pickList.advanced().getDownButtonElement().getText(), label);
    }

    @Test
    public void testImmediate() {
        pickListAttributes.set(PickListAttributes.immediate, Boolean.FALSE);
        pickList.add(0);
        submitAjax();
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: [] -> [Alabama]");

        pickListAttributes.set(PickListAttributes.immediate, Boolean.TRUE);
        pickList.add(0);
        submitAjax();
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: [Alabama] -> [Alaska, Alabama]");
    }

    /**
     * Verify that item keep selected even moved from source to target, or back. If selected Alaska from sources, and
     * then added to target, it should remain selected in target list
     */
    @Test
    public void testKeepSelected() {
        String textSource = pickList.advanced().getSourceList().getItem(0).getText();
        pickList.add(0);
        ListItem item = pickList.advanced().getTargetList().getItem(0);
        assertTrue(pickList.advanced().isItemSelected(item));
        assertEquals(item.getText(), textSource);

        pickList.remove(0);
        item = pickList.advanced().getSourceList().getItem(ChoicePickerHelper.byIndex().first());
        assertTrue(pickList.advanced().isItemSelected(item));
        assertEquals(item.getText(), textSource);
    }

    @Test
    public void testListHeight() {
        double testedValue = 600;
        double tolerance = 10;
        pickListAttributes.set(PickListAttributes.listHeight, testedValue);
        assertEquals(pickList.advanced().getHeight(), testedValue, tolerance);
    }

    @Test
    public void testMaxListHeight() {
        double testedValue = 600;
        double tolerance = 10;
        pickListAttributes.set(PickListAttributes.maxListHeight, testedValue);
        pickListAttributes.set(PickListAttributes.listHeight, "");
        assertEquals(pickList.advanced().getMaxHeight(), testedValue, tolerance);
    }

    @Test
    public void testMinListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        pickListAttributes.set(PickListAttributes.listHeight, "");
        pickListAttributes.set(PickListAttributes.minListHeight, testedValue);
        assertEquals(pickList.advanced().getMinHeight(),testedValue, tolerance);
    }

    @Test
    public void testOnadditems() {
        testFireEvent("additems", new Action() {
            @Override
            public void perform() {
                pickList.add(0);
            }
        });
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnblur() {
        testFireEventWithJS(pickList.advanced().getSourceListAreaElement(), Event.BLUR, pickListAttributes, PickListAttributes.onblur);
    }

    @Test
    public void testOnchange() {
        testFireEvent("onchange", new Action() {
            @Override
            public void perform() {
                pickList.add(0);
            }
        });
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, pickList.advanced().getPickListInteractionPartElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, pickList.advanced().getPickListInteractionPartElement());
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, pickList.advanced().getPickListInteractionPartElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, pickList.advanced().getPickListInteractionPartElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, pickList.advanced().getPickListInteractionPartElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, pickList.advanced().getPickListInteractionPartElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, pickList.advanced().getPickListInteractionPartElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, pickList.advanced().getPickListInteractionPartElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, pickList.advanced().getPickListInteractionPartElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, pickList.advanced().getPickListInteractionPartElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, pickList.advanced().getPickListInteractionPartElement());
    }

    @Test
    public void testOnremoveitems() {
        testFireEvent("removeitems", new Action() {
            @Override
            public void perform() {
                pickList.add(0);
                pickList.remove(0);
            }
        });
    }

    @Test
    public void testOrderable() {
        pickListAttributes.set(PickListAttributes.orderable, Boolean.FALSE);
        assertOrderingListButtonsAreNotPresent();

        pickListAttributes.set(PickListAttributes.orderable, Boolean.TRUE);
        assertOrderingListButtonsAreVisible();

        // check ordering controls if they are disabled
        pickList.add(0);
        pickList.add(0);
        pickList.add(0);
        assertEquals(pickList.advanced().getTargetList().getItems().size(), 3);

        // now is time to select one item. This should cause ordering controls enable
        pickList.advanced().getTargetList().getItem(0).select(true);

        // move first item to last
        OrderingList orderingTargetList = pickList.advanced().orderTargetList();
        List<? extends ListItem> items = pickList.advanced().getTargetList().getItems();
        List<String> targetStrings = Lists.newArrayList(items.get(0).getText(), items.get(1).getText(), items.get(2)
            .getText());

        orderingTargetList.select(0).putItAfter(2);
        // verify that previously first item is now the last item (select 3rd item, and verify text)
        items = pickList.advanced().getTargetList().getItems();
        List<String> targetStringsAfter = Lists.newArrayList(items.get(0).getText(), items.get(1).getText(),
            items.get(2).getText());
        targetStrings.add(targetStrings.remove(0));
        assertEquals(targetStringsAfter, targetStrings);
        // then move this item one step "up"
        orderingTargetList.select(2).putItBefore(1);
        // and then verify if all items are submitted in user defined order as well
        submitAjax();
        items = pickList.advanced().getTargetList().getItems();
        targetStrings = Lists.newArrayList(items.get(0).getText(), items.get(1).getText(), items.get(2).getText());
        assertEquals(output.getText(), targetStrings.toString());
    }

    private void assertOrderingListButtonsAreNotPresent() {
        assertNotPresent(pickList.advanced().getBottomButtonElement(), "Button should not be present");
        assertNotPresent(pickList.advanced().getDownButtonElement(), "Button should not be present");
        assertNotPresent(pickList.advanced().getTopButtonElement(), "Button should not be present");
        assertNotPresent(pickList.advanced().getUpButtonElement(), "Button should not be present");
    }

    private void assertOrderingListButtonsAreVisible() {
        assertVisible(pickList.advanced().getBottomButtonElement(), "Button should be visible");
        assertVisible(pickList.advanced().getDownButtonElement(), "Button should be visible");
        assertVisible(pickList.advanced().getTopButtonElement(), "Button should be visible");
        assertVisible(pickList.advanced().getUpButtonElement(), "Button should be visible");
    }

    @Test
    @Templates(value = "plain")
    public void testRemoveAllText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.removeAllText, label);
        assertEquals(pickList.advanced().getRemoveAllButtonElement().getText(), label);
    }

    @Test
    @Templates(value = "plain")
    public void testRemoveText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.removeText, label);
        assertEquals(pickList.advanced().getRemoveButtonElement().getText(), label);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        pickListAttributes.set(PickListAttributes.rendered, Boolean.TRUE);
        assertPresent(pickList.advanced().getPickListInteractionPartElement(), "Picklist should be present");
        pickListAttributes.set(PickListAttributes.rendered, Boolean.FALSE);
        assertNotPresent(pickList.advanced().getPickListInteractionPartElement(), "Picklist should not be present");
    }

    @Test
    public void testRequired() {
        pickListAttributes.set(PickListAttributes.required, Boolean.TRUE);
        pickList.add(0);
        submitAjax();
        assertFalse(message.advanced().isVisible(), "Message should not be visible.");

        pickList.removeAll();
        submitAjax();
        assertTrue(message.advanced().isVisible(), "Message should be visible.");
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
        String textSource = pickList.advanced().getSourceList().getItem(0).getText();
        pickList.add(0);
        submitHTTP();
        String textTarget = pickList.advanced().getTargetList().getItem(0).getText();
        assertEquals(textTarget, textSource);
        assertEquals(output.getText(), "[" + textSource + "]");
    }

    @Test
    public void testSourceCaption() {
        String caption = "This is source";
        pickListAttributes.set(PickListAttributes.sourceCaption, caption);
        assertEquals(pickList.advanced().getSourceCaptionElement().getText(), caption);
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(pickList.advanced().getPickListInteractionPartElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13350")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(pickList.advanced().getPickListInteractionPartElement());
    }

    /**
     * Verify switchByClick attribute
     */
    @Test
    public void testSwitchByClick() {
        pickListAttributes.set(PickListAttributes.switchByClick, Boolean.TRUE);
        ListItem item = pickList.advanced().getSourceList().getItems().get(0);
        String text = item.getText();
        item.getRootElement().click();
        Graphene.waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return pickList.advanced().getTargetList().getItems().size() == 1;
            }
        });
        item = pickList.advanced().getTargetList().getItem(0);
        assertEquals(item.getText(), text);
        assertTrue(pickList.advanced().isItemSelected(item));
    }

    /**
     * Verify switchByDblClick attribute
     */
    @Test
    public void testSwitchByDblClick() {
        pickListAttributes.set(PickListAttributes.switchByDblClick, Boolean.TRUE);
        ListItem item = pickList.advanced().getSourceList().getItems().get(0);
        String text = item.getText();
        new Actions(driver).doubleClick(item.getRootElement()).perform();
        Graphene.waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return pickList.advanced().getTargetList().getItems().size() == 1;
            }
        });
        item = pickList.advanced().getTargetList().getItem(0);
        assertEquals(item.getText(), text);
    }

    @Test
    public void testTargetCaption() {
        String caption = "This is target";
        pickListAttributes.set(PickListAttributes.targetCaption, caption);
        assertEquals(pickList.advanced().getTargetCaptionElement().getText(), caption);
    }

    @Test
    @Templates(value = "plain")
    public void testUpText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.upText, label);
        pickListAttributes.set(PickListAttributes.orderable, Boolean.TRUE);
        assertEquals(pickList.advanced().getUpButtonElement().getText(), label);
    }

    @Test
    @Templates(value = "plain")
    public void testUpTopText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.upTopText, label);
        pickListAttributes.set(PickListAttributes.orderable, Boolean.TRUE);
        assertEquals(pickList.advanced().getTopButtonElement().getText(), label);
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
        pickList.add(0);
        submitAjax();
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: [] -> [Alabama]");

        pickList.add(0);
        submitAjax();
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: [Alabama] -> [Alaska, Alabama]");
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
