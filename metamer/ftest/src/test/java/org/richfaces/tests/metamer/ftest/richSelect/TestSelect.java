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
package org.richfaces.tests.metamer.ftest.richSelect;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.component.object.api.scrolling.ScrollingType;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.select.RichFacesSelect;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richSelect/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestSelect extends AbstractWebDriverTest {

    private static final String TESTSIZE = "300px";
    @FindBy(css = "div[id$=select]")
    private RichFacesSelect select;
    @FindBy(css = "div.rf-sel-lst-scrl")
    private WebElement listElement;
    @FindBy(css = "div.rf-sel-lst-cord")
    private WebElement listRoot;
    @FindBy(css = "span[id$=output]")
    private WebElement output;

    @FindBy(css = "div[id$=selectItem10]")
    private WebElement item10;

    private final Attributes<SelectAttributes> selectAttributes = getAttributes();

    @Page
    private MetamerPage page;
    private final Action selectHawaiiGuardedAction = new Action() {
        @Override
        public void perform() {
            Graphene.guardAjax(select.openSelect()).select(10);
        }
    };
    private final Action selectHawaiiWithKeyboardGuardedAction = new Action() {
        @Override
        public void perform() {
            select.advanced().setScrollingType(ScrollingType.BY_KEYS);
            Graphene.guardAjax(select.openSelect()).select(10);
        }
    };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richSelect/simple.xhtml");
    }

    @Test
    public void testClientFilterFunction() {
        selectAttributes.set(SelectAttributes.clientFilterFunction, "filterValuesByLength");
        select.type("4");//get all states with 4 letters
        List<WebElement> suggestions = select.advanced().getSuggestions();
        assertEquals(suggestions.size(), 3);
        assertEquals(suggestions.get(0).getText(), "Iowa");
        assertEquals(suggestions.get(1).getText(), "Ohio");
        assertEquals(suggestions.get(2).getText(), "Utah");

        select.type("5");//get all states with 5 letters
        suggestions = select.advanced().getSuggestions();
        assertEquals(suggestions.size(), 3);
        assertEquals(suggestions.get(0).getText(), "Idaho");
        assertEquals(suggestions.get(1).getText(), "Maine");
        assertEquals(suggestions.get(2).getText(), "Texas");
    }

    @Test
    public void testDefaultLabel() {
        selectAttributes.set(SelectAttributes.defaultLabel, "new label");
        assertEquals(select.advanced().getInput().getStringValue(), "new label", "Default label should change");

        selectAttributes.set(SelectAttributes.defaultLabel, "");
        assertPresent(select.advanced().getInput().getInput(), "Input should be present on the page.");
        select.advanced().waitUntilSuggestionsAreNotVisible();
        assertEquals(select.advanced().getInput().getStringValue(), "", "Default label should change");
    }

    @Test
    public void testDisabled() {
        selectAttributes.set(SelectAttributes.disabled, Boolean.TRUE);
        assertPresent(select.advanced().getInput().getInput(), "Input should be present on the page.");
        select.advanced().waitUntilSuggestionsAreNotVisible();
        try {
            select.openSelect();
        } catch (TimeoutException ex) {
            return;
        }
        Assert.fail("Select should be disabled.");
    }

    @Test
    @RegressionTest(value = { "https://issues.jboss.org/browse/RF-9663", "https://issues.jboss.org/browse/RF-9855" })
    public void testEnableManualInput() {
        selectAttributes.set(SelectAttributes.enableManualInput, Boolean.FALSE);
        String readonly = select.advanced().getInput().getInput().getAttribute("readonly");
        assertTrue("readonly".equals(readonly) || "true".equals(readonly), "Input should be read-only");

        select.advanced().setOpenByInputClick(true);
        select.openSelect();
        select.advanced().waitUntilSuggestionsAreVisible();
        selectHawaiiGuardedAction.perform();
        assertTrue(item10.getAttribute("class").contains("rf-sel-sel"));
        assertEquals(output.getText(), "Hawaii");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11320")
    public void testFiltering() {
        select.type("a");
        List<WebElement> suggestions = select.advanced().getSuggestions();
        assertEquals(suggestions.size(), 4, "Count of filtered options ('a')");
        String[] selectOptions = { "Alabama", "Alaska", "Arizona", "Arkansas" };
        for (int i = 0; i < selectOptions.length; i++) {
            assertEquals(suggestions.get(i).getText(), selectOptions[i]);
        }
        Graphene.guardAjax(select.openSelect()).select(3);
        assertEquals(output.getText(), "Arkansas");
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Arkansas");
    }

    @Test
    public void testImmediate() {
        selectAttributes.set(SelectAttributes.immediate, Boolean.TRUE);
        selectHawaiiGuardedAction.perform();
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: -> Hawaii");
    }

    @Test
    @Templates(value = "plain")
    public void testInit() {
        assertEquals(select.advanced().getInput().getStringValue(), "Click here to edit", "Default label");
        assertPresent(select.advanced().getInput().getInput(), "Input should be present on the page.");
        assertPresent(select.advanced().getShowButton(), "Show button should be present on the page.");
        select.advanced().waitUntilSuggestionsAreNotVisible();
    }

    @Test
    @Templates(value = "plain")
    public void testItemClass() {
        final String value = "metamer-ftest-class";
        selectAttributes.set(SelectAttributes.itemClass, value);
        select.openSelect();
        List<WebElement> suggestions = select.advanced().getSuggestions();
        assertEquals(suggestions.size(), 50);
        for (WebElement webElement : suggestions) {
            assertTrue(webElement.getAttribute("class").contains(value));
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9735")
    public void testListClass() {
        testStyleClass(listRoot, BasicAttributes.listClass);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9737")
    public void testListHeight() {
        selectAttributes.set(SelectAttributes.listHeight, TESTSIZE);
        select.openSelect();
        assertEquals(listElement.getCssValue("height"), TESTSIZE, "Height of list did not change");

        selectAttributes.set(SelectAttributes.listHeight, "");
        select.openSelect();
        assertEquals(listElement.getCssValue("height"), "100px", "Height of list did not change");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9737")
    public void testListWidth() {
        selectAttributes.set(SelectAttributes.listWidth, TESTSIZE);
        select.openSelect();
        assertEquals(listElement.getCssValue("width"), TESTSIZE, "Width of list did not change");

        selectAttributes.set(SelectAttributes.listWidth, "");
        select.openSelect();
        assertEquals(listElement.getCssValue("width"), "200px", "Width of list did not change");
    }

    @Test
    public void testMaxListHeight() {
        selectAttributes.set(SelectAttributes.maxListHeight, TESTSIZE);
        select.openSelect();
        assertEquals(listElement.getCssValue("max-height"), TESTSIZE, "Height of list did not change");

        selectAttributes.set(SelectAttributes.maxListHeight, "");
        select.openSelect();
        assertEquals(listElement.getCssValue("max-height"), "100px", "Height of list did not change");
    }

    @Test
    public void testMinListHeight() {
        selectAttributes.set(SelectAttributes.minListHeight, TESTSIZE);
        select.openSelect();
        assertEquals(listElement.getCssValue("min-height"), TESTSIZE, "Height of list did not change");

        selectAttributes.set(SelectAttributes.minListHeight, "");
        select.openSelect();
        assertEquals(listElement.getCssValue("min-height"), "20px", "Height of list did not change");
    }

    @Test
    public void testOnblur() {
        testFireEvent("blur", new Action() {
            @Override
            public void perform() {
                select.advanced().getInput().getInput().click();// will not be triggered if this step omitted
                page.getRequestTimeElement().click();// will not be triggered if this step omitted
                select.advanced().getInput().fillIn("ABCD").trigger("blur");
            }
        });
    }

    @Test
    public void testOnchange() {
        testFireEvent("change", selectHawaiiGuardedAction);
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(Event.CLICK, select.advanced().getInput().getInput());
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, select.advanced().getInput().getInput());
    }

    @Test
    @Templates(value = "plain")
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, select.advanced().getInput().getInput());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, select.advanced().getInput().getInput());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, select.advanced().getInput().getInput());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, select.advanced().getInput().getInput());
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistclick() {
        testFireEvent("listclick", selectHawaiiGuardedAction);
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistdblclick() {
        testFireEvent("listdblclick", new Action() {
            @Override
            public void perform() {
                select.openSelect();
                Utils.triggerJQ(executor, "dblclick", listElement);
            }
        });
    }

    @Test
    public void testOnlisthide() {
        testFireEvent("listhide", new Action() {
            @Override
            public void perform() {
                select.openSelect();
                page.getRequestTimeElement().click();
                select.advanced().waitUntilSuggestionsAreNotVisible();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistkeydown() {
        testFireEvent("listkeydown", new Action() {
            @Override
            public void perform() {
                select.openSelect();
                Utils.triggerJQ(executor, "keydown", listElement);
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistkeypress() {
        testFireEvent("listkeypress", new Action() {
            @Override
            public void perform() {
                select.openSelect();
                Utils.triggerJQ(executor, "keypress", listElement);
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistkeyup() {
        testFireEvent("listkeyup", new Action() {
            @Override
            public void perform() {
                select.openSelect();
                Utils.triggerJQ(executor, "keyup", listElement);
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmousedown() {
        testFireEvent("listmousedown", new Action() {
            @Override
            public void perform() {
                Utils.triggerJQ(executor, "mousedown", listElement);
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmousemove() {
        testFireEvent("listmousemove", new Action() {
            @Override
            public void perform() {
                Utils.triggerJQ(executor, "mousemove", listElement);
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmouseout() {
        testFireEvent("listmouseout", new Action() {
            @Override
            public void perform() {
                Utils.triggerJQ(executor, "mouseout", listElement);
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmouseover() {
        testFireEvent("listmouseover", new Action() {
            @Override
            public void perform() {
                select.openSelect();
                new Actions(driver).moveToElement(listElement).moveToElement(page.getRequestTimeElement()).perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmouseup() {
        testFireEvent("listmouseup", selectHawaiiGuardedAction);
    }

    @Test
    public void testOnlistshow() {
        testFireEvent("listshow", new Action() {
            @Override
            public void perform() {
                select.openSelect();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, select.advanced().getInput().getInput());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, select.advanced().getInput().getInput());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, select.advanced().getInput().getInput());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, select.advanced().getInput().getInput());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, select.advanced().getInput().getInput());
    }

    @Test
    public void testOnselectitem() {
        testFireEvent("selectitem", selectHawaiiGuardedAction);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        selectAttributes.set(SelectAttributes.rendered, Boolean.FALSE);
        assertNotPresent(select.advanced().getRoot(), "Component should not be rendered when rendered=false.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11320")
    public void testSelectFirst() {
        selectAttributes.set(SelectAttributes.selectFirst, Boolean.TRUE);

        select.type("a");
        List<WebElement> suggestions = select.advanced().getSuggestions();
        assertEquals(suggestions.size(), 4, "Count of filtered options ('a')");
        String[] selectOptions = { "Alabama", "Alaska", "Arizona", "Arkansas" };
        for (int i = 0; i < selectOptions.length; i++) {
            assertEquals(suggestions.get(i).getText(), selectOptions[i]);
        }
        assertTrue(suggestions.get(0).getAttribute("class").contains("rf-sel-sel"), "First item should contain class for selected item.");
        new Actions(driver).sendKeys(Keys.RETURN).perform();

        String previousTime = page.getRequestTimeElement().getText();
        Utils.triggerJQ(executor, "blur", select.advanced().getInput().getInput());
        Graphene.waitModel().until().element(page.getRequestTimeElement()).text().not().equalTo(previousTime);
        assertEquals(output.getText(), "Alabama", "Output should be Alabama");
    }

    @Test
    @Templates(value = "plain")
    public void testSelectItemClass() {
        selectAttributes.set(SelectAttributes.selectItemClass, "metamer-ftest-class");

        Graphene.guardAjax(select.openSelect()).select(0);
        select.openSelect();
        List<WebElement> suggestions = select.advanced().getSuggestions();
        assertTrue(suggestions.get(0).getAttribute("class").contains("metamer-ftest-class"), "Selected item should contain set class");
        for (int i = 1; i < suggestions.size(); i++) {
            assertFalse(suggestions.get(i).getAttribute("class").contains("metamer-ftest-class"), "Not selected item should not contain set class");
        }
    }

    @Test
    public void testSelectWithKeyboard() {
        selectHawaiiWithKeyboardGuardedAction.perform();
        assertTrue(item10.getAttribute("class").contains("rf-sel-sel"), "Selected item should contain class for selected option.");
        assertEquals(output.getText(), "Hawaii");
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }

    @Test
    public void testSelectWithMouse() {
        Graphene.guardAjax(select.openSelect()).select(10);
        assertTrue(item10.getAttribute("class").contains("rf-sel-sel"), "Selected item should contain class for selected option.");
        assertEquals(output.getText(), "Hawaii");
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }

    @Test
    public void testShowButton() {
        selectAttributes.set(SelectAttributes.showButton, Boolean.FALSE);
        assertNotVisible(select.advanced().getShowButton(), "Show button should not be visible.");

        selectAttributes.set(SelectAttributes.showButton, Boolean.TRUE);
        assertVisible(select.advanced().getShowButton(), "Show button should be visible.");
    }

    @Test
    public void testShowButtonClick() {
        selectAttributes.set(SelectAttributes.showButton, Boolean.TRUE);
        assertVisible(select.advanced().getShowButton(), "Show button should be visible.");
        select.advanced().setOpenByInputClick(false);
        select.openSelect();
        List<WebElement> suggestions = select.advanced().getSuggestions();
        assertEquals(suggestions.size(), 50, "There should be 50 options.");

        String[] selectOptions = { "Alabama", "Hawaii", "Massachusetts", "New Mexico", "South Dakota" };
        for (int i = 0; i < suggestions.size(); i += 10) {
            assertEquals(suggestions.get(i).getText(), selectOptions[i / 10], "Select option nr. " + i);
        }
        selectHawaiiGuardedAction.perform();
        assertEquals(output.getText(), "Hawaii");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(select.advanced().getRoot());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(select.advanced().getRoot());
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(select.advanced().getInput().getInput());
    }

    @Test
    public void testValue() {
        selectAttributes.set(SelectAttributes.value, "North Carolina");
        assertEquals(select.advanced().getInput().getStringValue(), "North Carolina", "Input should contain selected value.");
    }
}
