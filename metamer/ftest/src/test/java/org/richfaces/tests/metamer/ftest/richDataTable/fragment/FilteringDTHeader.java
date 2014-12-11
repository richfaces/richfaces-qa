/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richDataTable.fragment;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.inputNumberSpinner.RichFacesInputNumberSpinner;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.AbstractFilteringHeader;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class FilteringDTHeader extends AbstractFilteringHeader {

    @FindByJQuery("[id$='columnName:flt']")
    private TextInputComponentImpl nameBuiltInInput;
    @FindByJQuery(value = "[id$='columnHeaderNameInput']")
    private TextInputComponentImpl nameInput;
    @FindByJQuery("[id$='columnNumberOfKids1:flt']")
    private TextInputComponentImpl numberOfKids1BuiltInInput;
    @FindByJQuery("[id$='columnNumberOfKids2:flt']")
    private TextInputComponentImpl numberOfKids2BuiltInInput;
    @FindByJQuery("[id$='spinnerFilter']")
    private RichFacesInputNumberSpinner numberOfKidsSpinner;
    @FindByJQuery(value = "[id$='columnHeaderSexInput']")
    private Select sexSelect;
    @FindByJQuery(value = "[id$='columnTitle:flt']")
    private TextInputComponentImpl titleBuiltInInput;
    @FindByJQuery(value = "[id$='columnHeaderTitleInput']")
    private TextInputComponentImpl titleInput;

    @Override
    public TextInputComponentImpl getNameBuiltInInput() {
        return nameBuiltInInput;
    }

    @Override
    public TextInputComponentImpl getNameInput() {
        return nameInput;
    }

    @Override
    public TextInputComponentImpl getNumberOfKidsInput() {
        return numberOfKids1BuiltInInput;
    }

    @Override
    public RichFacesInputNumberSpinner getNumberOfKidsSpinner() {
        return numberOfKidsSpinner;
    }

    @Override
    public Select getSexSelect() {
        return sexSelect;
    }

    @Override
    public TextInputComponentImpl getTitleBuildInInput() {
        return titleBuiltInInput;
    }

    @Override
    public TextInputComponentImpl getTitleInput() {
        return titleInput;
    }
}
