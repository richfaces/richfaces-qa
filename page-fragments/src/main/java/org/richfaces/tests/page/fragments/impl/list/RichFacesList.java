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
package org.richfaces.tests.page.fragments.impl.list;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesList extends ListComponentImpl {

    @FindBy(className = "rf-dlst-dfn")
    private List<WebElement> definitionsList;
    @FindBy(className = "rf-olst-itm")
    private List<WebElement> orderedList;
    @FindBy(className = "rf-ulst-itm")
    private List<WebElement> unorderedList;

    private AdvancedInteractions interactions;

    private static enum RichFacesListType {

        DEFINITIONS("rf-dlst"),
        ORDERED("rf-olst"),
        UNORDERED("rf-ulst"),
        UNKNOWN("");

        private RichFacesListType(String containsClass) {
            this.containsClass = containsClass;
        }
        private final String containsClass;

        private static RichFacesListType[] getPossibleValues() {
            return new RichFacesListType[]{ UNORDERED, ORDERED, DEFINITIONS };
        }

        private static RichFacesListType getListTypeFromRootElement(WebElement root) {
            String styleClasses = root.getAttribute("class");
            for (RichFacesListType type : getPossibleValues()) {
                if (styleClasses.contains(type.containsClass)) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

    public AdvancedInteractions advanced() {
        if (interactions == null) {
            interactions = new AdvancedInteractions();
        }
        return interactions;
    }

    private List<WebElement> getCorrectList() {
        RichFacesListType type = RichFacesListType.getListTypeFromRootElement(getRoot());
        switch (type) {
            case ORDERED:
                return orderedList;
            case UNORDERED:
                return unorderedList;
            case DEFINITIONS:
                return definitionsList;
            default:
                return super.getItems();
        }
    }

    @Override
    protected List<WebElement> getItems() {
        return getCorrectList();
    }

    public class AdvancedInteractions {

        public List<WebElement> getListItems() {
            return Collections.unmodifiableList(getItems());
        }
    }
}
