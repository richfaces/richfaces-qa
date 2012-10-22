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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class AccordionItemImpl implements AccordionItem {

    public static final String ACTIVE_HEADER_CLASS = "rf-ac-itm-lbl-act";
    public static final String DISABLED_HEADER_CLASS = "rf-ac-itm-lbl-dis";
    public static final String INACTIVE_HEADER_CLASS = "rf-ac-itm-lbl-inact";
    public static final String CONTENT_CLASS = "rf-ac-itm-cnt";
    public static final String TO_ACTIVATE_CLASS = "rf-ac-itm-hdr";

    @Root
    private WebElement root;

//    @FindBy(className = "rf-ac-itm-lbl-act")
//    private WebElement activeHeader;
//    @FindBy(className = "rf-ac-itm-lbl-dis")
//    private WebElement disabledHeader;
//    @FindBy(className = "rf-ac-itm-lbl-inact")
//    private WebElement inactiveHeader;
//    @FindBy(className = "rf-ac-itm-cnt")
//    private WebElement content;
//    @FindBy(className="rf-ac-itm-hdr")
//    private WebElement toActivate;

    private String id;

    @Override
    public void activate() {
        getId(); // HACK because of https://issues.jboss.org/browse/ARQGRA-216
        if (isActive()) {
            return;
        } else {
            getToActivateElement().click();
            Graphene.waitAjax().until(getIsActiveCondition());
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
        return getIsActiveCondition().apply(GrapheneContext.getProxy());
    }

    @Override
    public boolean isEnabled() {
        return getIsEnabledCondition().apply(GrapheneContext.getProxy());
    }

    protected final ExpectedCondition<Boolean> getIsActiveCondition() {
        return Graphene.element(By.cssSelector("*[id='" + getId() + "'] ." + ACTIVE_HEADER_CLASS)).isVisible();
    }

    protected final ExpectedCondition<Boolean> getIsEnabledCondition() {
        return Graphene.element(By.cssSelector("*[id='" + getId() + "'] ." + DISABLED_HEADER_CLASS)).not().isPresent();
    }

    protected final WebElement getActiveHeaderElement() {
        // HACK because of https://issues.jboss.org/browse/ARQGRA-216
        return GrapheneContext.getProxy().findElement(By.cssSelector("*[id='" + getId() + "'] ." + ACTIVE_HEADER_CLASS));
        //return activeHeader;
    }

    protected final WebElement getDisabledHeaderElement() {
        // HACK because of https://issues.jboss.org/browse/ARQGRA-216
        return GrapheneContext.getProxy().findElement(By.cssSelector("*[id='" + getId() + "'] ." + DISABLED_HEADER_CLASS));
        //return disabledHeader;
    }

    protected final WebElement getInactiveHeaderElement() {
        // HACK because of https://issues.jboss.org/browse/ARQGRA-216
        return GrapheneContext.getProxy().findElement(By.cssSelector("*[id='" + getId() + "'] ." + INACTIVE_HEADER_CLASS));
        //return inactiveHeader;
    }

    protected final WebElement getContentElement() {
        return GrapheneContext.getProxy().findElement(By.cssSelector("*[id='" + getId() + "'] ." + CONTENT_CLASS));
        //return content;
    }

    protected final WebElement getToActivateElement() {
        return GrapheneContext.getProxy().findElement(By.cssSelector("*[id='" + getId() + "'] ." + TO_ACTIVATE_CLASS));
        //return toActivate;
    }

    private String getId() {
        // HACK because of https://issues.jboss.org/browse/ARQGRA-216
        if (id == null) {
            id = root.getAttribute("id");
        }
        return id;
    }
}
