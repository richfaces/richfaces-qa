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
package org.richfaces.tests.metamer.ftest.richList;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;
import org.richfaces.component.ListType;
import org.richfaces.tests.metamer.ftest.model.AbstractModel;
import org.richfaces.tests.metamer.ftest.richColumn.ColumnModel;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22499 $
 */
public class ListModel extends AbstractModel<JQueryLocator> {

    ListType type;

    ReferencedLocator<JQueryLocator> rows = ref(root, "li");

    private AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    public ListModel(JQueryLocator root) {
        super(root);
    }

    public ListType getType() {
        return type;
    }

    public void setType(ListType type) {
        this.type = type;
    }

    public boolean isRendered() {
        return selenium.isElementPresent(getRoot());
    }

    public int getTotalRowCount() {
        return selenium.getCount(getRoot().getChild(getInnerElement()));
    }

    public JQueryLocator getRow(int position) {
        return ColumnModel.getNthChildElement(getRoot().getChild(getInnerElement()), position);
    }

    /*
    public Iterable<JQueryLocator> getRows() {
        return getRoot().getChild(getInnerElement()).getAllChildren();
    }*/

    public String getRowText(int position) {
        return selenium.getText(getRow(position));
    }

    private JQueryLocator getInnerElement() {
        switch (type) {
            case ordered:
                return jq("li.rf-olst-itm");
            case unordered:
                return jq("li.rf-ulst-itm");
            case definitions:
                return jq("dd.rf-dlst-dfn");
            default:
                // default case required by checkstyle.
                // Kept Exception throw as without 'default'
                break;
        }
        throw new IllegalStateException();
    }
}