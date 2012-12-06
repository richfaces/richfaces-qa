/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.page.fragments.impl.accordion;

import java.util.Iterator;
import java.util.List;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RichFacesAccordion implements Accordion {

    @Root
    private WebElement root;

    @FindBy(className="rf-ac-itm")
    private List<RichFacesAccordionItem> items;

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public AccordionItem getActiveItem() {
        for (AccordionItem item: items) {
            if (item.isActive()) {
                return item;
            }
        }
        return null;
    }

    @Override
    public AccordionItem getItem(int index) {
        return items.get(index);
    }

    public WebElement getRootElement() {
        return root;
    }

    @Override
    public Iterator<AccordionItem> iterator() {
        final Iterator<RichFacesAccordionItem> iterator = items.iterator();
        return new Iterator<AccordionItem>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public AccordionItem next() {
                return iterator.next();
            }
            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

}
