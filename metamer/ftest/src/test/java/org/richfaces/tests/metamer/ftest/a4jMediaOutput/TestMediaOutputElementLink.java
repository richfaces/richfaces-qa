/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jMediaOutput;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jMediaOutput/elementLink.xhtml
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestMediaOutputElementLink extends AbstractMediaOutputTest {

    @FindBy(css = "*#mediaOutputMarked")
    private WebElement markedText;

    @Override
    public String getComponentTestPagePath() {
        return "a4jMediaOutput/elementLink.xhtml";
    }

    @Test
    @CoversAttributes({ "element", "mimeType", "rel", "type", "uriAttribute" })
    public void init() {
        assertEquals(markedText.getCssValue("color"), "rgba(255, 0, 0, 1)", "The marked text should be red.");
    }

}
