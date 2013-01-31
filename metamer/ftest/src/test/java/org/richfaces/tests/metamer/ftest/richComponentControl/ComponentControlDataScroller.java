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
package org.richfaces.tests.metamer.ftest.richComponentControl;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.componentControllAttributes;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.model.AssertingDataScroller;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22739 $
 */
public class ComponentControlDataScroller extends AssertingDataScroller {

    JQueryLocator button = jq(":submit[id$=button]");

    public ComponentControlDataScroller() {
        super(jq("span.rf-ds[id$=scroller]"));
    }

    @Override
    public void clickFirstPageButton() {
        doOperation("first");
    }

    @Override
    public void clickFastRewind() {
        doOperation("previous");
    }

    @Override
    public void clickFastForward() {
        doOperation("next");
    }

    @Override
    public void clickLastPageButton() {
        doOperation("last");
    }

    private void doOperation(String operation) {
        componentControllAttributes.set(ComponentControlAttributes.operation, operation);
        guardXhr(selenium).click(button);
    }

    @Override
    public void clickPageButton(int pageNumber) {
        int lastPage = getCurrentPage();
        while (pageNumber < lastPage) {
            clickFastRewind();
            int page = getCurrentPage();
            assertEquals(page, lastPage - 1);
            lastPage = page;
        }
        while (pageNumber > lastPage) {
            clickFastForward();
            int page = getCurrentPage();
            assertEquals(page, lastPage + 1);
            lastPage = page;
        }
    }
}
