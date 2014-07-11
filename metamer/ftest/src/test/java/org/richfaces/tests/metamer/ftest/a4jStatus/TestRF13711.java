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
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.AssertJUnit.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Test for RF-13711
 * Referenced a4j:status does not trigged JS in onstart/onstop.
 * This uses a reproducer page where there are two a4j:status(es) one of which is referenced
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 *
 */
public class TestRF13711 extends AbstractStatusTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jStatus/rf-13711.xhtml");
    }

    private final String expected = "Success!";

    @FindByJQuery(value = "input[id$='buttonWithoutStatusAjax']")
    WebElement notReferencedStatusButton;

    @FindByJQuery(value = "input[id$='buttonWithStatusAjax']")
    WebElement referencedStatusButton;

    @FindByJQuery(value = "span[id$='outputTextOnStart2']")
    WebElement notReferencedOnStart;

    @FindByJQuery(value = "span[id$='outputTextOnStop2']")
    WebElement notReferencedOnStop;

    @FindByJQuery(value = "span[id$='outputTextOnStart']")
    WebElement referencedOnStart;

    @FindByJQuery(value = "span[id$='outputTextOnStop']")
    WebElement referencedOnStop;

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-13711")
    public void testBothOptions() {
        // click the button without referenced status and assert changes
        guardAjax(notReferencedStatusButton).click();
        assertEquals(expected, notReferencedOnStart.getText());
        assertEquals(expected, notReferencedOnStop.getText());

        // click the button which has reference to status
        // this was the problematic part in RF-13711
        guardAjax(referencedStatusButton).click();
        assertEquals(expected, referencedOnStart.getText());
        assertEquals(expected, referencedOnStop.getText());
    }
}
