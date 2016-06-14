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
package org.richfaces.tests.metamer.ftest.richPickList;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.list.ListItem;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.fragment.orderingList.OrderingList;
import org.richfaces.fragment.pickList.RichFacesPickList;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.abstractions.AbstractListScrollingTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
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
public class TestPickList extends AbstractListScrollingTest {

    private final Attributes<PickListAttributes> pickListAttributes = getAttributes();

    @FindBy(css = "input[id$=a4jButton]")
    private WebElement ajaxSubmit;
    @FindBy(css = "input[id$=hButton]")
    private WebElement hSubmit;
    private Boolean keepSourceOrder;
    @FindBy(css = "[id$=msg]")
    private RichFacesMessage message;
    @FindBy(css = "span[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=pickList]")
    private RichFacesPickList pickList;

    private void assertButtonDisabled(WebElement e) {
        assertFalse(PickListButton.from(e).isEnabled(), "Button should be disabled.");
    }

    private void assertButtonEnabled(WebElement e) {
        assertTrue(PickListButton.from(e).isEnabled(), "Button should be enabled.");
    }

    @Override
    public String getComponentTestPagePath() {
        return "richPickList/simple.xhtml";
    }

    private List<String> getStringsFromElements(List<WebElement> elements) {
        List<String> result = Lists.newArrayList();
        for (WebElement element : elements) {
            result.add(element.getText());
        }
        return result;
    }

    private void submitAjax() {
        Graphene.guardAjax(ajaxSubmit).click();
    }

    private void submitHTTP() {
        Graphene.guardHttp(hSubmit).click();
    }

    @Test(groups = "smoke")
    public void testAddAllBtn() {
        pickList.addAll();// fragment contains checking
        assertEquals(pickList.advanced().getSourceList().getItems().size(), 0);
        assertEquals(pickList.advanced().getTargetList().getItems().size(), 54);
    }

    @Test
    @CoversAttributes("addAllText")
    @Templates(value = "plain")
    public void testAddAllText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.addAllText, label);
        assertEquals(pickList.advanced().getAddAllButtonElement().getText(), label);
    }

    @Test
    @CoversAttributes("addText")
    @Templates(value = "plain")
    public void testAddText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.addText, label);
        assertEquals(pickList.advanced().getAddButtonElement().getText(), label);
    }

    @Test
    @UseWithField(field = "keepSourceOrder", valuesFrom = ValuesFrom.FROM_FIELD, value = "booleans")
    @IssueTracking("https://issues.jboss.org/browse/RF-14094")
    public void testButtonsStateUsingKeepSourceOrdered() {
        pickListAttributes.set(PickListAttributes.keepSourceOrder, keepSourceOrder);
        assertButtonDisabled(pickList.advanced().getAddButtonElement());
        assertButtonEnabled(pickList.advanced().getAddAllButtonElement());
        assertButtonDisabled(pickList.advanced().getRemoveButtonElement());
        assertButtonDisabled(pickList.advanced().getRemoveAllButtonElement());

        pickList.add(0);
        assertButtonDisabled(pickList.advanced().getAddButtonElement());
        assertButtonEnabled(pickList.advanced().getAddAllButtonElement());
        assertButtonEnabled(pickList.advanced().getRemoveButtonElement());
        assertButtonEnabled(pickList.advanced().getRemoveAllButtonElement());

        pickList.remove(0);
        assertButtonEnabled(pickList.advanced().getAddButtonElement());
        assertButtonEnabled(pickList.advanced().getAddAllButtonElement());
        assertButtonDisabled(pickList.advanced().getRemoveButtonElement());
        assertButtonDisabled(pickList.advanced().getRemoveAllButtonElement());
    }

    @Test
    @CoversAttributes("collectionType")
    public void testCollectionType() {
        int i = 0;
        int size = pickList.advanced().getSourceList().size();
        String textSource1, textSource2, textTarget1, textTarget2;
        // the @collectionType attribute accepts both String and Class values, which are resolved in bean by according prefix
        for (String testedValue : new String[] { "class-ArrayList", "string-LinkedList", "class-Stack", "string-Vector" }) {
            pickListAttributes.set(PickListAttributes.collectionType, testedValue);

            textSource1 = pickList.advanced().getSourceList().getItem(i).getText();
            textSource2 = pickList.advanced().getSourceList().getItem(ChoicePickerHelper.byIndex().beforeLast(i)).getText();

            pickList.add(i);
            submitAjax();
            textTarget1 = pickList.advanced().getTargetList().getItem(0).getText();
            assertEquals(textTarget1, textSource1);
            assertEquals(output.getText(), "[" + textSource1 + "]");

            pickList.add(ChoicePickerHelper.byIndex().beforeLast(i > size - i ? i - 1 : i));
            submitHTTP();
            textTarget1 = pickList.advanced().getTargetList().getItem(0).getText();
            textTarget2 = pickList.advanced().getTargetList().getItem(1).getText();
            assertEquals(textTarget1, textSource1);
            assertEquals(textTarget2, textSource2);
            assertEquals(output.getText(), "[" + textSource1 + ", " + textSource2 + "]");

            pickList.removeAll();
            submitAjax();
            assertEquals(output.getText(), "[]");

            i += 4;// check different values with each collection type
        }
    }

    @Test(groups = "extended")
    @CoversAttributes("collectionType")
    public void testCollectionType_unsupportedTypeThrowsException() {
        try {
            pickListAttributes.set(PickListAttributes.collectionType, "invalid-LinkedHashMap");
            pickList.add(0);
            submitHTTP();
            String exceptionText = driver.findElement(By.tagName("body")).getText();
            assertTrue(exceptionText.contains("java.util.LinkedHashMap cannot be cast to java.util.List")
                || exceptionText.contains("java.util.LinkedHashMap cannot be cast to java.util.Collection"));

            // check another invalid value
            loadPage();
            pickListAttributes.set(PickListAttributes.collectionType, "invalid-LinkedHashSet");
            pickList.add(0);
            submitHTTP();
            exceptionText = driver.findElement(By.tagName("body")).getText();
            assertTrue(exceptionText.contains("java.util.LinkedHashSet cannot be cast to java.util.List"));
        } finally {
            loadPage();
            // set back supported and default collectionType
            pickListAttributes.set(PickListAttributes.collectionType, "string-LinkedList");
        }
    }

    @Test
    public void testDisableAddBtn() {
        assertButtonDisabled(pickList.advanced().getAddButtonElement());
        pickList.advanced().getSourceList().getItem(0).select();// select
        assertButtonEnabled(pickList.advanced().getAddButtonElement());
        pickList.add(0);// no selected item in source list >>> button disabled
        assertButtonDisabled(pickList.advanced().getAddButtonElement());
        pickList.addAll();
        assertButtonDisabled(pickList.advanced().getAddButtonElement());
    }

    @Test
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
    @CoversAttributes("disabled")
    public void testDisabled() {
        String disabledOptionClass = "rf-pick-opt-dis";
        pickList.advanced().getSourceList().getItem(0).select();

        pickListAttributes.set(PickListAttributes.disabled, Boolean.TRUE);
        assertButtonDisabled(pickList.advanced().getAddAllButtonElement());
        assertButtonDisabled(pickList.advanced().getAddButtonElement());
        assertButtonDisabled(pickList.advanced().getRemoveAllButtonElement());
        assertButtonDisabled(pickList.advanced().getRemoveButtonElement());
        for (ListItem item : pickList.advanced().getSourceList().getItems()) {
            assertTrue(item.getRootElement().getAttribute("class").contains(disabledOptionClass),
                "Item should be disabled.");
        }
        try {
            pickList.add(0);
        } catch (TimeoutException e) {
            return;
        }
        Assert.fail("Items cannot be selectable, when picklicst is disabled;");
    }

    @Test
    @CoversAttributes("disabledClass")
    @Templates(value = "plain")
    public void testDisabledClass() {
        pickListAttributes.set(PickListAttributes.disabled, Boolean.TRUE);
        testStyleClass(pickList.advanced().getRootElement(), BasicAttributes.disabledClass);
    }

    @Test
    @CoversAttributes("downBottomText")
    @Templates(value = "plain")
    public void testDownBottomText() {
        String label = "xxx";
        attsSetter()
            .setAttribute(PickListAttributes.downBottomText).toValue(label)
            .setAttribute(PickListAttributes.orderable).toValue(true)
            .asSingleAction().perform();
        assertEquals(pickList.advanced().getBottomButtonElement().getText(), label);
    }

    @Test
    @CoversAttributes("downText")
    @Templates(value = "plain")
    public void testDownText() {
        String label = "xxx";
        attsSetter()
            .setAttribute(PickListAttributes.downText).toValue(label)
            .setAttribute(PickListAttributes.orderable).toValue(true)
            .asSingleAction().perform();
        assertEquals(pickList.advanced().getDownButtonElement().getText(), label);
    }

    @Test
    @CoversAttributes("immediate")
    public void testImmediate() {
        pickListAttributes.set(PickListAttributes.immediate, Boolean.FALSE);
        pickList.add(0);
        submitAjax();
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: [] -> [Alabama]");

        pickListAttributes.set(PickListAttributes.immediate, Boolean.TRUE);
        pickList.add(0);
        submitAjax();
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: [Alabama] -> [Alabama, Alaska]");
    }

    @Test
    @CoversAttributes("itemClass")
    @Templates(value = "plain")
    public void testItemClass() {
        String testedClass = "metamer-ftest-class";
        pickListAttributes.set(PickListAttributes.itemClass, "metamer-ftest-class");
        for (ListItem li : pickList.advanced().getSourceList().getItems()) {
            assertTrue(li.getRootElement().getAttribute("class").contains(testedClass), "Item @class should contain "
                + testedClass);
        }
    }

    /**
     * Verify that item keep selected even moved from source to target, or back. If selected Alaska from sources, and then added
     * to target, it should remain selected in target list
     */
    @Test(groups = "smoke")
    public void testKeepSelected() {
        String textSource = pickList.advanced().getSourceList().getItem(0).getText();
        pickList.add(0);
        ListItem item = pickList.advanced().getTargetList().getItem(0);
        assertTrue(item.getRootElement().getAttribute("class").contains("rf-pick-sel"));
        assertEquals(item.getText(), textSource);

        pickList.remove(0);
        item = pickList.advanced().getSourceList().getItem(ChoicePickerHelper.byIndex().last());
        assertTrue(item.getRootElement().getAttribute("class").contains("rf-pick-sel"));
        assertEquals(item.getText(), textSource);
        assertEquals(Utils.getIndexOfElement(item.getRootElement()), pickList
            .advanced().getSourceList().size() - 1, "Index of removed item.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-14093")
    @CoversAttributes("keepSourceOrder")
    public void testKeepSourceOrder() {
        int maxIndex = 53;
        final Integer[] indexesAtOnce = new Integer[] { 1, 0, 26, maxIndex, maxIndex - 1 };
        final Integer[] indexesOneByOne = new Integer[] { 1, 0, 26 - 2, maxIndex - 3, maxIndex - 4 };

        pickListAttributes.set(PickListAttributes.keepSourceOrder, Boolean.TRUE);
        // save original elements text and size
        final List<String> sourceListOriginal = getStringsFromElements(pickList.advanced().getSourceListItemsElements());
        final int originalSourceListSize = pickList.advanced().getSourceListItemsElements().size();

        // pick some items, all at once
        pickList.addMultiple(ChoicePickerHelper.byIndex().indexes(indexesAtOnce));
        // check that items were added
        assertEquals(pickList.advanced().getTargetListItemsElements().size(), indexesAtOnce.length);
        // save text from picked elements
        final List<String> pickedElementsText = getStringsFromElements(pickList.advanced().getTargetListItemsElements());
        // remove all items from target list
        pickList.removeAll();
        // source list should contain all elements as before testing
        assertEquals(pickList.advanced().getSourceListItemsElements().size(), originalSourceListSize);
        // check that position of elements was not changed
        assertEquals(getStringsFromElements(pickList.advanced().getSourceListItemsElements()), sourceListOriginal);

        // pick some items, one by one, RF-14093
        for (int index : indexesOneByOne) {
            pickList.add(index);
        }
        // check that items were added
        assertEquals(pickList.advanced().getTargetListItemsElements().size(), indexesAtOnce.length);
        // remove all items from target list
        pickList.removeAll();
        // source list should contain all elements as before testing
        assertEquals(pickList.advanced().getSourceListItemsElements().size(), originalSourceListSize);
        // check that position of elements was not changed
        assertEquals(getStringsFromElements(pickList.advanced().getSourceListItemsElements()), sourceListOriginal);

        // check default value of keepSourceOrder == false
        pickListAttributes.set(PickListAttributes.keepSourceOrder, Boolean.FALSE);
        // pick some items, all at once
        pickList.addMultiple(ChoicePickerHelper.byIndex().indexes(indexesAtOnce));
        // check that items were added
        assertEquals(pickList.advanced().getTargetListItemsElements().size(), indexesAtOnce.length);
        // remove all items from target list
        pickList.removeAll();
        // source list should contain all elements as before testing
        assertEquals(pickList.advanced().getSourceListItemsElements().size(), originalSourceListSize);
        // source list should contain removed items in the end of the list and the position of other items should remain the same
        List<String> expectedSourceListAfterRemove = Lists.newArrayList(sourceListOriginal);
        // remove picked elements
        expectedSourceListAfterRemove.removeAll(pickedElementsText);
        // add picked elements to the end of the list
        expectedSourceListAfterRemove.addAll(pickedElementsText);
        assertEquals(getStringsFromElements(pickList.advanced().getSourceListItemsElements()), expectedSourceListAfterRemove);
    }

    @Test
    @CoversAttributes("listHeight")
    @Templates(value = "plain")
    public void testListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        pickListAttributes.set(PickListAttributes.listHeight, testedValue);
        assertEquals(
            Integer.valueOf(pickList.advanced().getSourceListContentAreaElement().getCssValue("height").replace("px", "")),
            testedValue, tolerance);
    }

    @Test
    @CoversAttributes("listWidth")
    @Templates(value = "plain")
    public void testListWidth() {
        int testedValue = 600;
        int tolerance = 20;
        pickListAttributes.set(PickListAttributes.listWidth, testedValue);
        assertEquals(
            Integer.valueOf(pickList.advanced().getSourceListContentAreaElement().getCssValue("width").replace("px", "")),
            testedValue, tolerance);
    }

    @Test
    @CoversAttributes("maxListHeight")
    @Templates(value = "plain")
    public void testMaxListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        pickListAttributes.set(PickListAttributes.maxListHeight, testedValue);
        pickListAttributes.set(PickListAttributes.listHeight, "");
        assertEquals(
            Integer.valueOf(pickList.advanced().getSourceListContentAreaElement().getCssValue("max-height").replace("px", "")),
            testedValue, tolerance);
    }

    @Test
    @CoversAttributes("minListHeight")
    @Templates(value = "plain")
    public void testMinListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        attsSetter()
            .setAttribute(PickListAttributes.listHeight).toValue("")
            .setAttribute(PickListAttributes.minListHeight).toValue(testedValue)
            .asSingleAction().perform();
        assertEquals(
            Integer.valueOf(pickList.advanced().getSourceListContentAreaElement().getCssValue("min-height").replace("px", "")),
            testedValue, tolerance);
    }

    @Test
    @CoversAttributes("onadditems")
    public void testOnadditems() {
        testFireEvent("additems", new Action() {
            @Override
            public void perform() {
                pickList.add(0);
            }
        });
    }

    @Test
    @CoversAttributes("onblur")
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnblur() {
        testFireEvent(pickListAttributes, PickListAttributes.onblur,
            new Actions(driver).click(pickList.advanced().getRootElement()).click(getMetamerPage().getRequestTimeElement()).build());
    }

    @Test
    @CoversAttributes("onchange")
    public void testOnchange() {
        testFireEvent(Event.CHANGE, pickList.advanced().getRootElement(), "change");
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(Event.CLICK, pickList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, pickList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onfocus")
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, pickList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onkeydown")
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, pickList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onkeypress")
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, pickList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, pickList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, pickList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, pickList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, pickList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, pickList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, pickList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onremoveitems")
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
    @CoversAttributes("onsourceblur")
    @IssueTracking("https://issues.jboss.org/browse/RF-11322")
    public void testOnsourceblur() {
        testFireEvent(pickListAttributes, PickListAttributes.onsourceblur, new Action() {
            @Override
            public void perform() {
                pickList.add(0).remove(0);// this will select the item from target list >>> blur
            }
        });
    }

    @Test
    @CoversAttributes("onsourceclick")
    @Templates(value = "plain")
    public void testOnsourceclick() {
        testFireEvent(pickListAttributes, PickListAttributes.onsourceclick,
            new Actions(driver).click(pickList.advanced().getSourceListContentAreaElement()).build());
    }

    @Test
    @CoversAttributes("onsourcedblclick")
    @Templates(value = "plain")
    public void testOnsourcedblclick() {
        testFireEvent(Event.DBLCLICK, pickList.advanced().getSourceListContentAreaElement(), "sourcedblclick");
    }

    @Test
    @CoversAttributes("onsourcefocus")
    @IssueTracking("https://issues.jboss.org/browse/RF-11322")
    public void testOnsourcefocus() {
        testFireEvent(pickListAttributes, PickListAttributes.onsourcefocus,
            new Actions(driver).click(pickList.advanced().getSourceListContentAreaElement()).build());
    }

    @Test
    @CoversAttributes("onsourcekeydown")
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    @Templates(value = "plain")
    public void testOnsourcekeydown() {
        testFireEvent(Event.KEYDOWN, pickList.advanced().getSourceListContentAreaElement(), "sourcekeydown");
    }

    @Test
    @CoversAttributes("onsourcekeypress")
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    @Templates(value = "plain")
    public void testOnsourcekeypress() {
        testFireEvent(Event.KEYPRESS, pickList.advanced().getSourceListContentAreaElement(), "sourcekeypress");
    }

    @Test
    @CoversAttributes("onsourcekeyup")
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    @Templates(value = "plain")
    public void testOnsourcekeyup() {
        testFireEvent(Event.KEYUP, pickList.advanced().getSourceListContentAreaElement(), "sourcekeyup");
    }

    @Test
    @CoversAttributes("onsourcemousedown")
    @Templates(value = "plain")
    public void testOnsourcemousedown() {
        testFireEvent(Event.MOUSEDOWN, pickList.advanced().getSourceListContentAreaElement(), "sourcemousedown");
    }

    @Test
    @CoversAttributes("onsourcemousemove")
    @Templates(value = "plain")
    public void testOnsourcemousemove() {
        testFireEvent(Event.MOUSEMOVE, pickList.advanced().getSourceListContentAreaElement(), "sourcemousemove");
    }

    @Test
    @CoversAttributes("onsourcemouseout")
    @Templates(value = "plain")
    public void testOnsourcemouseout() {
        testFireEvent(Event.MOUSEOUT, pickList.advanced().getSourceListContentAreaElement(), "sourcemouseout");
    }

    @Test
    @CoversAttributes("onsourcemouseover")
    @Templates(value = "plain")
    public void testOnsourcemouseover() {
        testFireEvent(Event.MOUSEOVER, pickList.advanced().getSourceListContentAreaElement(), "sourcemouseover");
    }

    @Test
    @CoversAttributes("onsourcemouseup")
    @Templates(value = "plain")
    public void testOnsourcemouseup() {
        testFireEvent(Event.MOUSEUP, pickList.advanced().getSourceListContentAreaElement(), "sourcemouseup");
    }

    @Test
    @CoversAttributes("ontargetblur")
    @IssueTracking("https://issues.jboss.org/browse/RF-11322")
    public void testOntargetblur() {
        testFireEvent(
            pickListAttributes,
            PickListAttributes.ontargetblur,
            new Action() {
                @Override
                public void perform() {
                    pickList.add(0);
                    pickList.advanced().getTargetListItemsElements().get(0).click();
                    pickList.advanced().getSourceListItemsElements().get(0).click();// >>> blur
                }
            }
        );
    }

    @Test
    @CoversAttributes("ontargetclick")
    @Templates(value = "plain")
    public void testOntargetclick() {
        testFireEvent(pickListAttributes, PickListAttributes.ontargetclick,
            new Actions(driver).click(pickList.advanced().getTargetListContentAreaElement()).build());
    }

    @Test
    @CoversAttributes("ontargetdblclick")
    @Templates(value = "plain")
    public void testOntargetdblclick() {
        testFireEvent(Event.DBLCLICK, pickList.advanced().getTargetListContentAreaElement(), "targetdblclick");
    }

    @Test
    @CoversAttributes("ontargetfocus")
    @IssueTracking("https://issues.jboss.org/browse/RF-11322")
    public void testOntargetfocus() {
        testFireEvent(pickListAttributes, PickListAttributes.ontargetfocus,
            new Action() {
                @Override
                public void perform() {
                    pickList.add(0);
                    pickList.advanced().getTargetListItemsElements().get(0).click();// >>> focus
                }
            }
        );
    }

    @Test
    @CoversAttributes("ontargetkeydown")
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    @Templates(value = "plain")
    public void testOntargetkeydown() {
        testFireEvent(Event.KEYDOWN, pickList.advanced().getTargetListContentAreaElement(), "targetkeydown");
    }

    @Test
    @CoversAttributes("ontargetkeypress")
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    @Templates(value = "plain")
    public void testOntargetkeypress() {
        testFireEvent(Event.KEYPRESS, pickList.advanced().getTargetListContentAreaElement(), "targetkeypress");
    }

    @Test
    @CoversAttributes("ontargetkeyup")
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    @Templates(value = "plain")
    public void testOntargetkeyup() {
        testFireEvent(Event.KEYUP, pickList.advanced().getTargetListContentAreaElement(), "targetkeyup");
    }

    @Test
    @CoversAttributes("ontargetmousedown")
    @Templates(value = "plain")
    public void testOntargetmousedown() {
        testFireEvent(Event.MOUSEDOWN, pickList.advanced().getTargetListContentAreaElement(), "targetmousedown");
    }

    @Test
    @CoversAttributes("ontargetmousemove")
    @Templates(value = "plain")
    public void testOntargetmousemove() {
        testFireEvent(Event.MOUSEMOVE, pickList.advanced().getTargetListContentAreaElement(), "targetmousemove");
    }

    @Test
    @CoversAttributes("ontargetmouseout")
    @Templates(value = "plain")
    public void testOntargetmouseout() {
        testFireEvent(Event.MOUSEOUT, pickList.advanced().getTargetListContentAreaElement(), "targetmouseout");
    }

    @Test
    @CoversAttributes("ontargetmouseover")
    @Templates(value = "plain")
    public void testOntargetmouseover() {
        testFireEvent(Event.MOUSEOVER, pickList.advanced().getTargetListContentAreaElement(), "targetmouseover");
    }

    @Test
    @CoversAttributes("ontargetmouseup")
    @Templates(value = "plain")
    public void testOntargetmouseup() {
        testFireEvent(Event.MOUSEUP, pickList.advanced().getTargetListContentAreaElement(), "targetmouseup");
    }

    @Test
    @CoversAttributes("orderable")
    public void testOrderable() {
        // firstly check ordering controls doesn't appear near pickList if not "orderable"
        pickListAttributes.set(PickListAttributes.orderable, Boolean.FALSE);
        assertNotPresent(pickList.advanced().getBottomButtonElement(), "Button should not be present");
        assertNotPresent(pickList.advanced().getDownButtonElement(), "Button should not be present");
        assertNotPresent(pickList.advanced().getTopButtonElement(), "Button should not be present");
        assertNotPresent(pickList.advanced().getUpButtonElement(), "Button should not be present");
        // then make sure that controls appear near pickList when allow ordering behavior
        pickListAttributes.set(PickListAttributes.orderable, Boolean.TRUE);
        assertVisible(pickList.advanced().getBottomButtonElement(), "Button should be visible");
        assertVisible(pickList.advanced().getDownButtonElement(), "Button should be visible");
        assertVisible(pickList.advanced().getTopButtonElement(), "Button should be visible");
        assertVisible(pickList.advanced().getUpButtonElement(), "Button should be visible");
        // then add some items to target list
        pickList.addMultiple(ChoicePickerHelper.byIndex().index(0).index(1).index(2));
        // all items should remain selected and in this case ordering controls should be disabled
        assertEquals(pickList.advanced().getTargetList().getItems().size(), 3);
        for (ListItem item : pickList.advanced().getTargetList().getItems()) {
            assertTrue(item.getRootElement().getAttribute("class").contains("rf-pick-sel"));
        }
        // so check ordering controls if they are disabled
        assertButtonDisabled(pickList.advanced().getBottomButtonElement());
        assertButtonDisabled(pickList.advanced().getDownButtonElement());
        assertButtonDisabled(pickList.advanced().getTopButtonElement());
        assertButtonDisabled(pickList.advanced().getUpButtonElement());
        // now is time to select one item. This should cause ordering controls enable
        pickList.advanced().getTargetList().getItem(0).select(true);
        // since it was first item, "Down" and "Last" buttons should be enabled
        assertButtonEnabled(pickList.advanced().getBottomButtonElement());
        assertButtonEnabled(pickList.advanced().getDownButtonElement());
        assertButtonDisabled(pickList.advanced().getTopButtonElement());
        assertButtonDisabled(pickList.advanced().getUpButtonElement());
        // move first item to last
        OrderingList orderingTargetList = pickList.advanced().getOrderTargetList();
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

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-12061")
    public void testPickListWontShiftPageWhenBodyHasDirection() {
        final int delta = 10;
        final Long originalWidth = (Long) executeJS("return jQuery(document).width();");
        for (String direction : new String[] { "rtl", "ltr" }) {
            // set body direction
            executeJS("jQuery('body').attr('dir','" + direction + "');");
            // interact with pickList
            pickList.add(0);
            // check page did not shift
            Long actualWidth = (Long) executeJS("return jQuery(document).width();");
            assertEquals(actualWidth, originalWidth, delta);
        }
    }

    @Test
    @CoversAttributes("removeAllText")
    @Templates(value = "plain")
    public void testRemoveAllText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.removeAllText, label);
        assertEquals(pickList.advanced().getRemoveAllButtonElement().getText(), label);
    }

    @Test
    @CoversAttributes("removeText")
    @Templates(value = "plain")
    public void testRemoveText() {
        String label = "xxx";
        pickListAttributes.set(PickListAttributes.removeText, label);
        assertEquals(pickList.advanced().getRemoveButtonElement().getText(), label);
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        pickListAttributes.set(PickListAttributes.rendered, Boolean.TRUE);
        assertPresent(pickList.advanced().getRootElement(), "Picklist should be present");
        pickListAttributes.set(PickListAttributes.rendered, Boolean.FALSE);
        assertNotPresent(pickList.advanced().getRootElement(), "Picklist should not be present");
    }

    @Test
    @CoversAttributes("required")
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
    @CoversAttributes("requiredMessage")
    public void testRequiredMessage() {
        String messageString = "This is new required message.";
        attsSetter()
            .setAttribute(PickListAttributes.required).toValue(true)
            .setAttribute(PickListAttributes.requiredMessage).toValue(messageString)
            .asSingleAction().perform();
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
    @IssueTracking("https://issues.jboss.org/browse/RF-13558")
    public void testScrollingWithKeyboard() {
        final WebElement focusElement = pickList.advanced().getRootElement();

        final Action workaround = new Action() {
            @Override
            public void perform() {
                // workaround for webdriver issue https://code.google.com/p/selenium/issues/detail?id=7937
                // the initial focus of keyboard is in browser's url bar instead on the actual clicked item
                // clicking any button on the page should workaround this problem
                pickList.advanced().getSourceList().getItem(ChoicePickerHelper.byIndex().last()).select();
                pickList.advanced().getAddButtonElement().click();
                pickList.advanced().getRemoveButtonElement().click();
            }
        };

        checkScrollingWithKeyboard(focusElement, pickList.advanced().getSourceListItemsElements(), workaround);
        pickList.addAll();
        checkScrollingWithKeyboard(focusElement, pickList.advanced().getTargetListItemsElements());
    }

    @Test
    @CoversAttributes("selectItemClass")
    @Templates(value = "plain")
    @IssueTracking("https://issues.jboss.org/browse/RF-12777")
    public void testSelectItemClass() {
        testHTMLAttribute(new FutureTarget<WebElement>() {
            @Override
            public WebElement getTarget() {
                return pickList.advanced().getSourceList().getItem(0).getRootElement();
            }
        }, pickListAttributes, PickListAttributes.selectItemClass, "metamer-ftest-class", new Action() {
            @Override
            public void perform() {
                pickList.advanced().getSourceList().getItem(0).select();
            }
        });
        // RF-12777, check add and remove buttons are enabled when some item is selected
        // the item in source list should be selected from previous step
        // the add button should be enabled
        assertTrue(pickList.advanced().getAddButtonElement().isEnabled());
        // add item to the target list
        pickList.advanced().getAddButtonElement().click();
        // the added item remains selected, the remove button should be enabled
        assertTrue(pickList.advanced().getRemoveButtonElement().isEnabled());
    }

    @Test
    @CoversAttributes("sourceCaption")
    public void testSourceCaption() {
        String caption = "This is source";
        pickListAttributes.set(PickListAttributes.sourceCaption, caption);
        assertEquals(pickList.advanced().getSourceCaptionElement().getText(), caption);
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(pickList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @IssueTracking("https://issues.jboss.org/browse/RF-13350")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(pickList.advanced().getRootElement());
    }

    /**
     * Verify switchByClick attribute
     */
    @Test
    @CoversAttributes("switchByClick")
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
        assertTrue(item.getRootElement().getAttribute("class").contains("rf-pick-sel"));
    }

    /**
     * Verify switchByDblClick attribute
     */
    @Test
    @CoversAttributes("switchByDblClick")
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
    @CoversAttributes("targetCaption")
    public void testTargetCaption() {
        String caption = "This is target";
        pickListAttributes.set(PickListAttributes.targetCaption, caption);
        assertEquals(pickList.advanced().getTargetCaptionElement().getText(), caption);
    }

    @Test
    @CoversAttributes("upText")
    @Templates(value = "plain")
    public void testUpText() {
        String label = "xxx";
        attsSetter()
            .setAttribute(PickListAttributes.orderable).toValue(true)
            .setAttribute(PickListAttributes.upText).toValue(label)
            .asSingleAction().perform();
        assertEquals(pickList.advanced().getUpButtonElement().getText(), label);
    }

    @Test
    @CoversAttributes("upTopText")
    @Templates(value = "plain")
    public void testUpTopText() {
        String label = "xxx";
        attsSetter()
            .setAttribute(PickListAttributes.orderable).toValue(true)
            .setAttribute(PickListAttributes.upTopText).toValue(label)
            .asSingleAction().perform();
        assertEquals(pickList.advanced().getTopButtonElement().getText(), label);
    }

    @Test
    @CoversAttributes("validator")
    public void testValidator() {
        pickList.addAll();
        submitAjax();
        assertEquals(message.getDetail(), "We are sorry, but @ is not allowed to join us!");
    }

    @Test
    @CoversAttributes("validatorMessage")
    public void testValidatorMessage() {
        String messageText = "Custom validatorMessage.";
        pickListAttributes.set(PickListAttributes.validatorMessage, messageText);
        pickList.addAll();
        submitAjax();
        assertEquals(message.getDetail(), messageText);
    }

    @Test
    @CoversAttributes("valueChangeListener")
    public void testValueChangeListener() {
        pickList.add(0);
        submitAjax();
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: [] -> [Alabama]");

        pickList.add(0);
        submitAjax();
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: [Alabama] -> [Alabama, Alaska]");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-14256")
    @CoversAttributes({ "keepSourceOrder", "onadditems", "onremoveitems" })
    public void testWhenUsingKeepSourceOrder_addAndRemoveItemsEventsAreFired() {
        pickListAttributes.set(PickListAttributes.keepSourceOrder, Boolean.TRUE);
        // check @onadditems
        testFireEvent("onadditems", new Action() {
            @Override
            public void perform() {
                pickList.add(0);
            }
        });
        // reset @onadditems
        pickListAttributes.set(PickListAttributes.onadditems, "");
        // check @onremoveitems removing single item
        testFireEvent("onremoveitems", new Action() {
            @Override
            public void perform() {
                pickList.add(0).remove(0);
            }
        });
        // check @onremoveitems removing all items
        testFireEvent("onremoveitems", new Action() {
            @Override
            public void perform() {
                pickList.addMultiple(ChoicePickerHelper.byIndex().indexes(1, 5, 20)).removeAll();
            }
        });
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
