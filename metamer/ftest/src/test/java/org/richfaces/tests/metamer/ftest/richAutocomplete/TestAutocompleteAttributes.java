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
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * Many tests set mode attribute to 'client'. This is to stabilize tests since with WebDriver I had some issues concerning
 * timeouts when selecting n-th item from list which renders based on ajax request.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class TestAutocompleteAttributes extends AbstractAutocompleteTest {

    @Page
    private SimplePage page;

    private static final String FIRST_LISTENER_MSG_FORMAT = "1 value changed: %s -> %s";
    private static final String SECOND_LISTENER_MSG = "2 value changed";
    private static final String THIRD_LISTENER_MSG = "action listener invoked";

    @FindByJQuery(value = "span[class$=btn-arrow]")
    private WebElement button;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/autocomplete.xhtml");
    }

    @Test
    public void testValueChangeListener() {
        Graphene.guardAjax(autocomplete).type("h").select("Hawaii");
        checkOutput("Hawaii");

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, String.format(FIRST_LISTENER_MSG_FORMAT, "null", "Hawaii"));
        page.assertListener(PhaseId.INVOKE_APPLICATION, THIRD_LISTENER_MSG);

        Graphene.guardAjax(page).blur();
        page.assertListener(PhaseId.INVOKE_APPLICATION, SECOND_LISTENER_MSG);

        autocomplete.clear();
        Graphene.guardAjax(autocomplete).type("ka").select("Kansas");
        checkOutput("Kansas");

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, String.format(FIRST_LISTENER_MSG_FORMAT, "Hawaii", "Kansas"));
        page.assertListener(PhaseId.INVOKE_APPLICATION, THIRD_LISTENER_MSG);

        Graphene.guardAjax(page).blur();
        page.assertListener(PhaseId.INVOKE_APPLICATION, SECOND_LISTENER_MSG);

        autocomplete.clear();
        Graphene.guardAjax(autocomplete).type("nonexisting");
        Graphene.guardAjax(page).blur();
        checkOutput("nonexisting");

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, String.format(FIRST_LISTENER_MSG_FORMAT, "Kansas", "nonexisting"));
        page.assertListener(PhaseId.INVOKE_APPLICATION, SECOND_LISTENER_MSG);
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12820")
    public void testLayout() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "ajax");
        String[] layouts = new String[]{ "div", "list", "table" };
        for (String layout : layouts) {
            autocompleteAttributes.set(AutocompleteAttributes.layout, layout);
            autocomplete.clear();

            Graphene.guardAjax(autocomplete).type("ala").select(ChoicePickerHelper.byVisibleText().contains("Alaska"));
            // code before refactoring
            // Graphene.guardAjax(autocomplete).autocompleteWithSuggestion(expected, ScrollingType.BY_MOUSE);

            waiting(500);
            assertEquals(autocomplete.advanced().getInput().getStringValue(), "Alaska",
                "The input value doesn't match when layout is set to '" + layout + "'.");
        }
    }

    @Test(groups = "Future")
    @IssueTracking(value = "https://issues.jboss.org/browse/RF-13537")
    public void testOnbegin() {
        // requires ajax mode since it happens before sending ajax request
        autocompleteAttributes.set(AutocompleteAttributes.mode, "ajax");
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onbegin, new Action() {
            @Override
            public void perform() {
                autocomplete.type("h");
            }
        });
    }

    @Test
    public void testOnchange() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "ajax");
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onchange, new Action() {
            @Override
            public void perform() {
                autocomplete.type("h");
                autocomplete.advanced().waitForSuggestionsToBeVisible();
                // lose focus to trigger handler
                driver.findElement(By.cssSelector("input[id$=autocompleteListInput]")).click();
            }
        });
    }

    @Test
    public void testOncomplete() {
        // requires ajax mode since it reacts on DOM changes
        autocompleteAttributes.set(AutocompleteAttributes.mode, "ajax");
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.oncomplete, new Action() {
            @Override
            public void perform() {
                autocomplete.type("h");
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onkeydown, new Action() {
            @Override
            public void perform() {
                new Actions(driver).keyDown(autocomplete.advanced().getInput().advanced().getInputElement(), Keys.CONTROL)
                    .build().perform();
            }
        });
        new Actions(driver).keyUp(Keys.CONTROL).build().perform();
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onkeyup, new Action() {
            @Override
            public void perform() {
                new Actions(driver).keyDown(autocomplete.advanced().getInput().advanced().getInputElement(), Keys.CONTROL)
                    .build().perform();
                new Actions(driver).keyUp(Keys.CONTROL).build().perform();
            }
        });
    }

    @Test
    public void testOnlistmousedown() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onlistmousedown, new Action() {
            @Override
            public void perform() {
                autocomplete.type("h");
                autocomplete.advanced().waitForSuggestionsToBeVisible();
                new Actions(driver).clickAndHold(autocomplete.advanced().getSuggestionsElements().get(0)).build().perform();
            }
        });
        new Actions(driver).release().build().perform();
    }

    @Test
    public void testOnlistmouseout() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onlistmouseout, new Action() {
            @Override
            public void perform() {
                autocomplete.type("h");
                autocomplete.advanced().waitForSuggestionsToBeVisible();
                new Actions(driver).moveToElement(autocomplete.advanced().getSuggestionsElements().get(0))
                    .moveToElement(page.getOutput()).build().perform();
            }
        });
    }

    @Test
    public void testOnlistmouseup() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onlistmouseup, new Action() {
            @Override
            public void perform() {
                autocomplete.type("h");
                autocomplete.advanced().waitForSuggestionsToBeVisible();
                new Actions(driver).click(autocomplete.advanced().getSuggestionsElements().get(0)).build().perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onmousemove, new Action() {
            @Override
            public void perform() {
                new Actions(driver).moveToElement(autocomplete.advanced().getRootElement()).build().perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onmouseover, new Action() {
            @Override
            public void perform() {
                new Actions(driver).moveToElement(driver.findElement(By.cssSelector("span[id$=autocomplete]"))).build()
                    .perform();
            }
        });
    }

    @Test
    public void testOnselectitem() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onselectitem, new Action() {
            @Override
            public void perform() {
                autocomplete.type("h");
                autocomplete.advanced().waitForSuggestionsToBeVisible();
                new Actions(driver).click(autocomplete.advanced().getSuggestionsElements().get(0)).build().perform();
            }
        });
    }

    @Test
    public void testOnblur() {
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onblur, new Action() {
            @Override
            public void perform() {
                // page.getAutocompleteAsWebElement().click();
                autocomplete.advanced().getInput().advanced().focus();
                page.blur();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onclick, new Action() {
            @Override
            public void perform() {
                autocomplete.advanced().getInput().advanced().getInputElement().click();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.ondblclick, new Action() {
            @Override
            public void perform() {
                new Actions(driver).doubleClick(autocomplete.advanced().getInput().advanced().getInputElement()).build()
                    .perform();
            }
        });
    }

    @Test
    public void testOnfocus() {
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onfocus, new Action() {
            @Override
            public void perform() {
                autocomplete.advanced().getInput().advanced().focus();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onkeypress, new Action() {
            @Override
            public void perform() {
                autocomplete.type("h");
            }
        });
    }

    @Test
    public void testOnlistclick() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onlistclick, new Action() {
            @Override
            public void perform() {
                autocomplete.type("h");
                autocomplete.advanced().waitForSuggestionsToBeVisible();
                new Actions(driver).click(autocomplete.advanced().getSuggestionsElements().get(0)).build().perform();
            }
        });
    }

    @Test
    public void testOnlistmousemove() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onlistmousemove, new Action() {
            @Override
            public void perform() {
                autocomplete.type("h");
                autocomplete.advanced().waitForSuggestionsToBeVisible();
                new Actions(driver).moveToElement(autocomplete.advanced().getSuggestionsElements().get(0))
                    .moveToElement(page.getOutput()).build().perform();
            }
        });
    }

    @Test
    public void testOnlistmouseover() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onlistmouseover, new Action() {
            @Override
            public void perform() {
                autocomplete.type("h");
                autocomplete.advanced().waitForSuggestionsToBeVisible();
                new Actions(driver).moveToElement(autocomplete.advanced().getSuggestionsElements().get(0)).build().perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onmousedown, new Action() {
            @Override
            public void perform() {
                new Actions(driver).clickAndHold(page.getAutocompleteAsWebElement()).build().perform();
            }
        });
        new Actions(driver).release().build().perform();
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onmouseout, new Action() {
            @Override
            public void perform() {
                new Actions(driver).moveToElement(page.getAutocompleteAsWebElement()).moveToElement(page.getOutput()).build()
                    .perform();
            }
        });
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(autocompleteAttributes, AutocompleteAttributes.onmouseup, new Action() {
            @Override
            public void perform() {
                new Actions(driver).clickAndHold(page.getAutocompleteAsWebElement()).release().build().perform();
            }
        });
    }

    @Test
    public void testMode() {
        // default state, should be ajax
        autocompleteAttributes.set(AutocompleteAttributes.mode, "null");
        guardAjax(autocomplete.type("a"));
        autocomplete.clear();

        // set ajax mode
        autocompleteAttributes.set(AutocompleteAttributes.mode, "ajax");
        guardAjax(autocomplete.type("h"));
        autocomplete.clear();

        // set cached ajax mode, triggering the same query twice should not trigger ajax request
        autocompleteAttributes.set(AutocompleteAttributes.mode, "cachedAjax");
        guardAjax(autocomplete.type("w"));
        autocomplete.clear();
        guardNoRequest(autocomplete.type("w"));
        autocomplete.clear();

        // set client mode
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        guardNoRequest(autocomplete.type("f"));
    }

    @Test
    public void testMinChars() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "ajax");
        autocompleteAttributes.set(AutocompleteAttributes.minChars, 3);
        // no request is sent until min number of chars is provided
        guardNoRequest(autocomplete.type("ha"));
        autocomplete.clear();

        guardAjax(autocomplete.type("haw"));
        autocomplete.advanced().waitForSuggestionsToBeVisible();
    }

    @Test
    public void testDisabled() {
        // defaultly autocomplete is enabled
        autocomplete.type("h").select("Hawaii");
        checkOutput("Hawaii");

        // disable
        autocompleteAttributes.set(AutocompleteAttributes.disabled, Boolean.TRUE);
        try {
            autocomplete.type("w").select("Washington");
        } catch (Exception e) {
            // OK exception should be thrown since typing is disabled
        }
    }

    @Test
    public void testAutofill() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        autocomplete.type("haw");
        autocomplete.advanced().waitForSuggestionsToBeVisible();
        new Actions(driver).sendKeys(Keys.ARROW_DOWN).build().perform();
        assertEquals(autocomplete.advanced().getInput().getStringValue(), "haw");

        autocomplete.clear();

        autocompleteAttributes.set(AutocompleteAttributes.autofill, Boolean.TRUE);
        autocomplete.type("haw");
        autocomplete.advanced().waitForSuggestionsToBeVisible();
        new Actions(driver).moveToElement(autocomplete.advanced().getSuggestionsElements().get(0)).build().perform();
        assertEquals(autocomplete.advanced().getInput().getStringValue(), "hawaii");
    }

    @Test
    public void testTokens() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        // default token is ', '
        autocomplete.type("Hawaii, w");
        autocomplete.advanced().waitForSuggestionsToBeVisible();
        autocomplete.advanced().getSuggestionsElements().get(0).click();
        checkOutput("Hawaii, Washington");

        // clear value
        autocompleteAttributes.set(AutocompleteAttributes.value, "");

        // change token and assert then after writing it, it shows suggestions for new input
        autocompleteAttributes.set(AutocompleteAttributes.tokens, "- ");
        autocomplete.type("Hawaii- w");
        autocomplete.advanced().waitForSuggestionsToBeVisible();
        autocomplete.advanced().getSuggestionsElements().get(0).click();
        checkOutput("Hawaii- Washington");
    }

    @Test
    public void testValue() {
        autocompleteAttributes.set(AutocompleteAttributes.value, "Johny Derp speaking!");
        checkOutput("Johny Derp speaking!");

        autocompleteAttributes.set(AutocompleteAttributes.value, "Area 51");
        checkOutput("Area 51");
    }

    @Test
    public void testSelectFirst() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        autocompleteAttributes.set(AutocompleteAttributes.selectFirst, Boolean.TRUE);
        autocomplete.type("wa");
        autocomplete.advanced().waitForSuggestionsToBeVisible();
        // via actions click enter, therefore confirm the selection of the first suggested item
        new Actions(driver).pause(1000).sendKeys(Keys.RETURN).build().perform();
        checkOutput("Washington");
    }

    @Test
    public void testShowButton() {
        // defaultly button should not be present
        assertNotPresent(button, "In default state, button should not be present on page!");

        // show button and assert
        autocompleteAttributes.set(AutocompleteAttributes.showButton, Boolean.TRUE);
        assertPresent(button, "Button should be present on page!");

        // hide again and assert
        autocompleteAttributes.set(AutocompleteAttributes.showButton, Boolean.FALSE);
        assertNotPresent(button, "Button should not be present on the page!");
    }
}
