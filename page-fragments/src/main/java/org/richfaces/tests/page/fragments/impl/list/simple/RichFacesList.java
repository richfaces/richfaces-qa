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
package org.richfaces.tests.page.fragments.impl.list.simple;

import java.util.List;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.list.AbstractListFragment;
import org.richfaces.tests.page.fragments.impl.list.ListItem;
import org.richfaces.tests.page.fragments.impl.list.ListItems;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T> type of ListItem
 */
public abstract class RichFacesList<T extends ListItem> extends AbstractListFragment<T, ListItems<T>> implements SimpleList<T> {

    @FindBy(className = "rf-dlst-dfn")
    private List<WebElement> definitionsList;
    @FindBy(className = "rf-olst-itm")
    private List<WebElement> orderedList;
    @FindBy(className = "rf-ulst-itm")
    private List<WebElement> unorderedList;


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
    public ListItems<T> getItems() {
        return createItems(getActualItems());
    }

    private List<WebElement> getActualItems() {
        ListType type = RichFacesListType.getListType(root);
        switch (type) {
            case DEFINITIONS:
                return definitionsList;
            case ORDERED:
                return orderedList;
            case UNORDERED:
                return unorderedList;
            default:
                throw new UnsupportedOperationException("Unknown list type " + type);
        }
    }

    @Override
    public ListType getType() {
        return RichFacesListType.getListType(root);
    }

    @Override
    protected ListItems<T> instantiateListItems() {
        return new RichFacesListItems<T>();
    }
}
