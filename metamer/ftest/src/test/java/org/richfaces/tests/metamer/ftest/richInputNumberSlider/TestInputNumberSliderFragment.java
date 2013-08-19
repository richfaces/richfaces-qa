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
package org.richfaces.tests.metamer.ftest.richInputNumberSlider;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.page.fragments.impl.inputNumberSlider.RichFacesInputNumberSlider;
import org.richfaces.tests.page.fragments.impl.inputNumberSlider.SliderComponent;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInputNumberSliderFragment extends AbstractWebDriverTest {

    @FindBy(css = "span[id$=slider]")
    private RichFacesInputNumberSlider slider;

    @FindBy
    private WebElement outOfViewDiv;
    @FindBy(css = "span[id$=output]")
    private WebElement output;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInputNumberSlider/fragment.xhtml");
    }

    private SliderComponent moveOutOfViewOfSliderAndGetItAjaxGuarded() {
        scrollOutOfView();
        return Graphene.guardAjax(slider.slider());
    }

    private int parseIntInOutput() throws NumberFormatException {
        int result = Integer.parseInt(output.getText());
        return result;
    }

    private void scrollOutOfView() {
        outOfViewDiv.click();
    }

    @Test
    @Templates(value = "plain")
    public void testMoveHandleToPointInTraceByPercentage() {
        moveOutOfViewOfSliderAndGetItAjaxGuarded().dragHandleToPointInTrace(0.0);
        assertEquals(parseIntInOutput(), -10);

        moveOutOfViewOfSliderAndGetItAjaxGuarded().dragHandleToPointInTrace(1.0);
        assertEquals(parseIntInOutput(), 10);

        moveOutOfViewOfSliderAndGetItAjaxGuarded().dragHandleToPointInTrace(0.5);
        assertEquals(parseIntInOutput(), 0);

        moveOutOfViewOfSliderAndGetItAjaxGuarded().dragHandleToPointInTrace(0.75);
        assertEquals(parseIntInOutput(), 5);

        moveOutOfViewOfSliderAndGetItAjaxGuarded().dragHandleToPointInTrace(0.25);
        assertEquals(parseIntInOutput(), -5);
    }

    @Test
    @Templates(value = "plain")
    public void testMoveHandleToPointInTraceByPixels() {
        moveOutOfViewOfSliderAndGetItAjaxGuarded().dragHandleToPointInTrace(0);
        assertEquals(parseIntInOutput(), -10);

        moveOutOfViewOfSliderAndGetItAjaxGuarded().dragHandleToPointInTrace(slider.slider().getWidth());
        assertEquals(parseIntInOutput(), 10);

        moveOutOfViewOfSliderAndGetItAjaxGuarded().dragHandleToPointInTrace((int) (slider.slider().getWidth() / 2));
        assertEquals(parseIntInOutput(), 0);
    }
}
