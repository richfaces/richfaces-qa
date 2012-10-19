/**g
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the
 *
 * @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.richfaces.tests.page.fragments.impl.accordion;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class AccordionItemImpl implements AccordionItem {

    @Root
    private WebElement root;

    @FindBy(className = "rf-ac-itm-lbl-act")
    private WebElement activeHeader;
    @FindBy(className = "rf-ac-itm-lbl-dis")
    private WebElement disabledHeader;
    @FindBy(className = "rf-ac-itm-lbl-inact")
    private WebElement inactiveHeader;
    @FindBy(className = "rf-ac-itm-cnt")
    private WebElement content;
    @FindBy(className="rf-ac-itm-hdr")
    private WebElement toActivate;

    @Override
    public void activate() {
        if (isActive()) {
            return;
        } else {
            toActivate.click();
            Graphene.waitAjax().until(Graphene.element(getActiveHeaderElement()).isVisible());
        }
    }

    @Override
    public String getHeader() {
        if (isActive()) {
            return getActiveHeaderElement().getText();
        } else if (isEnabled()) {
            return getInactiveHeaderElement().getText();
        } else {
            return getDisabledHeaderElement().getText();
        }
    }

    @Override
    public String getContent() {
        if (isActive()) {
            return getContentElement().getText();
        } else {
            return null;
        }
    }

    @Override
    public boolean isActive() {
        return Graphene.element(getActiveHeaderElement()).isVisible().apply(GrapheneContext.getProxy());
    }

    @Override
    public boolean isEnabled() {
        return Graphene.element(getActiveHeaderElement()).not().isPresent().apply(GrapheneContext.getProxy());
    }

    protected final WebElement getActiveHeaderElement() {
        return activeHeader;
    }

    protected final WebElement getDisabledHeaderElement() {
        return disabledHeader;
    }

    protected final WebElement getInactiveHeaderElement() {
        return inactiveHeader;
    }

    protected final WebElement getContentElement() {
        return content;
    }
}
