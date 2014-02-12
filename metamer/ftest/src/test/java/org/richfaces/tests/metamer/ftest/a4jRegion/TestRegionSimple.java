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
package org.richfaces.tests.metamer.ftest.a4jRegion;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22691 $
 */
public class TestRegionSimple extends AbstractWebDriverTest {

    private final Attributes<RegionAttributes> regionAttributes = getAttributes();

    @FindByJQuery("input:text[id$=user2NameInput]")
    private GrapheneElement nameInput;
    @FindByJQuery("input:text[id$=user2EmailInput]")
    private GrapheneElement emailInput;

    @FindBy(css="span[id$=user2NameOutput]")
    private GrapheneElement nameOutput;
    @FindBy(css="span[id$=user2EmailOutput]")
    private GrapheneElement emailOutput;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jRegion/simple.xhtml");
    }

    @Test
    public void testExecution() {
        String name = nameOutput.getText();

        nameInput.click();
        nameInput.clear();
        nameInput.sendKeys("abc");

        Graphene.waitAjax()
                .until()
                .element(nameOutput)
                .text()
                .not().equalTo(name);

        String email = emailOutput.getText();


        emailInput.click();
        emailInput.clear();
        emailInput.sendKeys("abc");

        Graphene.waitAjax()
                .until()
                .element(emailOutput)
                .text()
                .not().equalTo(email);
    }

    @Test
    public void testRendered() {
        assertTrue(nameInput.isPresent());
        assertTrue(emailInput.isPresent());
        assertTrue(nameOutput.isPresent());
        assertTrue(emailOutput.isPresent());

        regionAttributes.set(RegionAttributes.rendered, false);

        assertFalse(nameInput.isPresent());
        assertFalse(emailInput.isPresent());
        assertTrue(nameOutput.isPresent());
        assertTrue(emailOutput.isPresent());
    }

}
