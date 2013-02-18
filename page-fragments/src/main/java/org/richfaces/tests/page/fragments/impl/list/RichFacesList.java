/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.list;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class RichFacesList implements ListFragment {

    @Root
    private WebElement root;
    @FindBy(className = "rf-dlst-dfn")
    private List<RichFacesListItem> definitionsList;
    @FindBy(className = "rf-olst-itm")
    private List<RichFacesListItem> orderedList;
    @FindBy(className = "rf-ulst-itm")
    private List<RichFacesListItem> unorderedList;
    //
    private WebDriver driver = GrapheneContext.getProxy();

    private enum RichFacesListType {

        ORDERED(ListType.ORDERED, "rf-olst"),
        UNORDERED(ListType.UNORDERED, "rf-ulst"),
        DEFINITIONS(ListType.DEFINITIONS, "rf-dlst");

        private RichFacesListType(ListType listType, String containsClass) {
            this.containsClass = containsClass;
            this.listType = listType;
        }
        private final String containsClass;
        private final ListType listType;

        static ListType getListType(WebElement root) {
            String styleClasses = root.getAttribute("class");
            for (RichFacesListType type : values()) {
                if (styleClasses.contains(type.containsClass)) {
                    return type.listType;
                }
            }
            throw new RuntimeException("Cannot obtain list type from root element " + root);
        }
    }

    @Override
    public ListItems getItems() {
        ListType type = RichFacesListType.getListType(root);
        switch (type) {
            case DEFINITIONS:
                return new RichFacesListItems(definitionsList);
            case ORDERED:
                return new RichFacesListItems(orderedList);
            case UNORDERED:
                return new RichFacesListItems(unorderedList);
            default:
                throw new UnsupportedOperationException("Unknown list type " + type);
        }
    }

    @Override
    public WebElement getRootElement() {
        return root;
    }

    @Override
    public ListType getType() {
        return RichFacesListType.getListType(root);
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }
}
