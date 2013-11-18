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
package org.richfaces.tests.metamer.ftest.richValidator;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Test demonstrating RF-12031
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestRF12031 extends AbstractWebDriverTest {

    @Page
    ValidatorSimplePage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richValidator/RF-12031.xhtml");
    }

    @Test(groups = { "Future" })
    @IssueTracking({ "https://issues.jboss.org/browse/RF-12031", "https://issues.jboss.org/browse/RF-12536" })
    public void testCSVOnConditionallyRenderedInput() {

        page.toggleButton.click();
        Graphene.waitGui().until().element(page.simpleInput).is().present();

        page.simpleInput.sendKeys("RichFaces 4");
        fireEvent(page.simpleInput, Event.BLUR);

        Graphene.waitModel().until().element(page.simpleErrorMessage).is().present();
    }

}
