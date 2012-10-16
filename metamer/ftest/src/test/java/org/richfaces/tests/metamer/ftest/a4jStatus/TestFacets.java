/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22690 $
 */
public class TestFacets extends AbstractStatusTest {
    JQueryLocator applyFacetsButton = pjq("input[id$=applyFacets]");
    JQueryLocator inputFormat = pjq("input[id$={0}Input]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jStatus/facets.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("A4J", "A4J Status", "Facets");
    }

    @Test
    public void testInterleavedChangingOfFacets() {
        for (int i = 0; i < 13; i++) {
            ElementLocator<?> button = (i % 2 == 0) ? button2 : buttonError;
            IterateStatus iterateStatus = IterateStatus.values()[i % IterateStatus.values().length];
            testChangingFacet(button, iterateStatus);
        }
    }

    void testChangingFacet(ElementLocator<?> button, IterateStatus iterateStatus) {
        switch (iterateStatus) {
            case START:
                setStartText(getStartText() + "*");
                break;
            case STOP:
                setStopText(getStopText() + "*");
                break;
            case ERROR:
                setErrorText(getErrorText() + "*");
                break;
            default:
                throw new IllegalStateException();
        }

        final String startText = getStartText();
        final String stopText = (button.getRawLocator().equals(buttonError.getRawLocator())) ? getErrorText() : getStopText();

        testRequestButton(button, startText, stopText);
    }

    private static enum IterateStatus {
        START, STOP, ERROR
    }

    /**
     * Routine to set value into input with specified locator
     * @param locator
     * @param value
     */
    private void setValueTo(ElementLocator<?> locator, String value) {
        selenium.type(locator, value);
        guardXhr(selenium).click(applyFacetsButton);
    }

    private String getValueFrom(ElementLocator<?> locator) {
        return selenium.getValue(locator);
    }

    public void setStartText(String startText) {
        setValueTo(inputFormat.format("facetStartText"), startText);
    }

    public String getStartText() {
        return getValueFrom(inputFormat.format("facetStartText"));
    }

    public void setStopText(String stopText) {
        setValueTo(inputFormat.format("facetStopText"), stopText);
    }

    public String getStopText() {
        return getValueFrom(inputFormat.format("facetStopText"));
    }

    public void setErrorText(String errorText) {
        setValueTo(inputFormat.format("facetErrorText"), errorText);
    }

    public String getErrorText() {
        return getValueFrom(inputFormat.format("facetErrorText"));
    }
}
