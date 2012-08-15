/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.webdriver;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.event.PhaseId;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StringEqualsWrapper;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class MetamerPage {

    @FindBy(css = "div#phasesPanel li")
    public List<WebElement> phases;
    @FindBy(css = "span[id$=requestTime]")
    public WebElement requestTime;
    @FindBy(css = "span[id$=statusCheckerOutput]")
    public WebElement statusCheckerOutput;
    @FindBy(css = "span[id$=renderChecker]")
    public WebElement renderCheckerOutput;

    public List<String> getPhases() {
        List<String> result = new ArrayList<String>();
        for (WebElement webElement : phases) {
            result.add(webElement.getText());
        }
        return result;
    }

    public boolean checkPhasesContainAllOf(String... s) {
        return new PhasesWrapper(getPhases()).containsAllOf(s);
    }

    public boolean checkPhasesDontContainSomeOf(PhaseId... phase) {
        return new PhasesWrapper(getPhases()).notContainsSomeOf(phase);
    }

    public void assertPhasesContainAllOf(String... s) {
        assertTrue(checkPhasesContainAllOf(s), "Phases {" + getPhases() + "} don't contain some of " + Arrays.asList(s));
    }

    public void assertPhasesDontContainSomeOf(PhaseId... phase) {
        assertTrue(checkPhasesDontContainSomeOf(phase), "Phases {" + getPhases() + "} contain some of " + Arrays.asList(phase));
    }

    ///////////////////////////////////////
    // Helper classes
    ///////////////////////////////////////
    /**
     * Wrapper for Metamer's phases list.
     */
    protected class PhasesWrapper {

        private final List<String> phases;

        public PhasesWrapper(List<String> phases) {
            this.phases = phases;
        }

        /**
         * Checks if the wrapped phases do not contain some of a PhaseIds (JSF
         * phases).
         *
         * @param values PhasesIds that phases should not contain
         * @return false if the wrapped phases contains some PhaseId value
         */
        public boolean notContainsSomeOf(PhaseId... values) {
            if (values == null) {
                throw new IllegalArgumentException("No Phases specified.");
            }
            String[] valuesAsString = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                valuesAsString[i] = values[i].toString();
            }
            for (String value : valuesAsString) {
                if (new StringEqualsWrapper(value).isSimilarToSomeOfThis(phases)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Checks if the wrapped phases do not contain some of a given values.
         *
         * @param values given values, that the wrapped phases should not
         * contain
         * @return false if the wrapped phases contains some of given values
         */
        public boolean notContainsSomeOf(String... values) {
            if (values == null) {
                throw new IllegalArgumentException("No String specified.");
            }
            for (String value : values) {
                if (new StringEqualsWrapper(value).isSimilarToSomeOfThis(phases)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Checks if phases contains all of given values.
         *
         * @param values given values, that the wrapped phases should contain
         * @return true if phases contain all of given values
         */
        public boolean containsAllOf(String... values) {
            if (values == null) {
                throw new IllegalArgumentException("No String specified.");
            }
            for (String value : values) {
                if (!new StringEqualsWrapper(value).isSimilarToSomeOfThis(phases)) {
                    return false;
                }
            }
            return true;
        }
    }
}
