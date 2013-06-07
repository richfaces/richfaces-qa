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
package org.richfaces.tests.page.fragments.impl.list.pick;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.list.common.SelectableListLayout;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SourceListLayout implements SelectableListLayout {

    @FindBy(className = "rf-pick-opt")
    private List<WebElement> items;
    @FindBy(className = "rf-pick-sel")
    private List<WebElement> selectedItems;
    @FindBy(className = "rf-pick-lst-scrl")
    private WebElement listAreaElement;
    @FindBy(className = "rf-pick-src-cptn")
    private WebElement captionElement;
    @FindBy(css = "thead.rf-pick-lst-hdr > tr.rf-pick-hdr")
    private WebElement headerElement;

    @Override
    public WebElement getCaptionElement() {
        return captionElement;
    }

    @Override
    public WebElement getHeaderElement() {
        return headerElement;
    }

    @Override
    public List<WebElement> getItems() {
        return items;
    }

    @Override
    public WebElement getListAreaElement() {
        return listAreaElement;
    }

    @Override
    public List<WebElement> getSelectedItems() {
        return selectedItems;
    }
}
