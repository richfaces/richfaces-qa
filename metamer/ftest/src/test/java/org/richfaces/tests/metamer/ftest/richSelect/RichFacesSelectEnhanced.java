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
package org.richfaces.tests.metamer.ftest.richSelect;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.select.Option;
import org.richfaces.tests.page.fragments.impl.select.OptionList;
import org.richfaces.tests.page.fragments.impl.select.RichFacesSelect;
import org.richfaces.tests.page.fragments.impl.select.RichFacesSelectPopup;
import org.richfaces.tests.page.fragments.impl.select.Selection;

/**
 * Enhanced fragment for r:select to blur the input after the option is selected.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesSelectEnhanced extends RichFacesSelect {

    public static final ByJQuery GLOBAL_POPUP = ByJQuery.jquerySelector("div.rf-sel-shdw:visible");
    @Drone
    private WebDriver browser;

    @Override
    public OptionList getPopup() {// made public to be accessible for GrapheneProxyHandler
        RFSelectOptionList list = Graphene.createPageFragment(RFSelectOptionList.class, browser.findElement(GLOBAL_POPUP));
        list.setInput(getInput().advanced().getInput());
        return list;
    }

    public static class RFSelectOptionList extends RichFacesSelectPopup {

        private WebElement input;

        @ArquillianResource
        private JavascriptExecutor executor;

        public void setInput(WebElement input) {
            this.input = input;
        }

        @Override
        public Option selectByIndex(int index, Selection selection) {
            Option option = super.selectByIndex(index, selection);
            Utils.triggerJQ(executor, "blur", input);
            return option;
        }
    }
}
