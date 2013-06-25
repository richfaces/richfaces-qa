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
package org.richfaces.tests.page.fragments.impl.input.inplace.select;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.input.inplace.AbstractInplaceComponent;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesInplaceSelect extends AbstractInplaceComponent<InplaceSelectEditingState> {

    @FindBy(xpath = "//body/span[contains(@class, rf-is-lst-cord)]")//whole page search
    private InplaceSelectPopupList globalList;
    @FindBy(css = "span.rf-is-lst-cord")
    private WebElement localList;
    @FindBy(tagName = "script")
    private WebElement script;

    @ArquillianResource
    private JavascriptExecutor executor;

    @Override
    public InplaceSelectEditingState editBy(OpenBy event) {
        if (!globalList.isVisible()) {
            Utils.triggerJQ(executor, event.getEventName(), root);
            if (isOpenOnEdit()) {
                waitForPopupShow();
            }
        }
        return instantiateFragment();
    }

    @Override
    protected String getCssClassForState(State state) {
        switch (state) {
            case ACTIVE:
                return "rf-is-act";
            case CHANGED:
                return "rf-is-chng";
            default:
                throw new UnsupportedOperationException("Unknown state " + state);
        }
    }

    @Override
    protected InplaceSelectEditingState instantiateFragment() {
        return Graphene.createPageFragment(RichFacesInplaceSelectEditingState.class, root);
    }

    private boolean isOpenOnEdit() {
        String text = Utils.returningJQ(executor, "text()", script);//getting text from hidden element
        if (text.contains("\"openOnEdit\":false")) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private void waitForPopupShow() {
        Graphene.waitModel().until().element(localList).is().not().present();
        Graphene.waitModel().until(globalList.isVisibleCondition());
    }
}
