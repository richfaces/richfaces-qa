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
package org.richfaces.tests.metamer.ftest.richValidator;

import static org.jboss.arquillian.ajocado.Graphene.elementPresent;
import static org.jboss.arquillian.ajocado.Graphene.jq;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Test demonstrating RF-12031
 * 
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestRF12031 extends AbstractGrapheneTest {

    private JQueryLocator toggleButton = jq("input[type=submit]");
    private JQueryLocator input = jq("input[type=text]");
    private JQueryLocator errorMessage = jq(".rf-msg-det");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richValidator/RF-12031.xhtml");
    }

    @Test
    @IssueTracking(value = "https://issues.jboss.org/browse/RF-12031")
    public void testCSVOnConditionallyRenderedInput() {
        selenium.click(toggleButton);
        waitModel.timeout(1000).until(elementPresent.locator(input));

        selenium.type(input, "RichFaces 4");
        selenium.fireEvent(input, Event.BLUR);

        waitModel.failWith(new Exception("Client side validation should be performed, but there are no error messages!"))
            .timeout(1000).until(elementPresent.locator(errorMessage));
    }

}
