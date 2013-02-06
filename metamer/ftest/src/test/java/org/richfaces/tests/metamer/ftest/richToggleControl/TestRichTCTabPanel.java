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
package org.richfaces.tests.metamer.ftest.richToggleControl;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richToggleControl/tabPanel.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21307 $
 */
public class TestRichTCTabPanel extends AbstractTestToggleControl {

    JQueryLocator[] items1 = {pjq("div[id$=item11]"), pjq("div[id$=item12]"), pjq("div[id$=item13]")};
    JQueryLocator[] items2 = {pjq("div[id$=item21]"), pjq("div[id$=item22]"), pjq("div[id$=item23]")};

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richToggleControl/tabPanel.xhtml");
    }

    @Test
    public void testSwitchFirstPanel() {
        testSwitchFirstPanel(items1);
    }

    @Test
    public void testSwitchSecondPanel() {
        testSwitchSecondPanel(items2);
    }

    @Test
    public void testTargetItem() {
        testTargetItem(items1);
    }

    @Test
    public void testTargetPanel() {
        testTargetPanel(items2);
    }
}
