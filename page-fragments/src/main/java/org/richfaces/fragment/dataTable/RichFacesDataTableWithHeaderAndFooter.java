/*
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
 */
package org.richfaces.fragment.dataTable;

import java.util.List;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TypeResolver;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class RichFacesDataTableWithHeaderAndFooter<HEADER, ROW, FOOTER> extends RichFacesDataTable<ROW> implements DataTableWithHeaderAndFooter<HEADER, ROW, FOOTER> {

    @SuppressWarnings("unchecked")
    private final Class<HEADER> headerClass = (Class<HEADER>) TypeResolver.resolveRawArguments(DataTableWithHeaderAndFooter.class, getClass())[0];

    @SuppressWarnings("unchecked")
    private final Class<FOOTER> footerClass = (Class<FOOTER>) TypeResolver.resolveRawArguments(DataTableWithHeaderAndFooter.class, getClass())[2];

    @FindBy(className = "rf-dt-thd")
    private WebElement wholeTableHeader;

    @FindBy(className = "rf-dt-tft")
    private WebElement wholeTableFooter;

    @FindBy(css = "th.rf-dt-hdr-c")
    private WebElement headerElement;

    @FindBy(className = "rf-dt-ftr-c")
    private WebElement footerElement;

    @FindBy(className = "rf-dt-shdr-c")
    private List<WebElement> columnHeaders;

    @FindBy(className = "rf-dt-sftr-c")
    private List<WebElement> columnFooters;

    @Drone
    private WebDriver browser;

    public HEADER getHeader() {
        return Graphene.createPageFragment(headerClass, wholeTableHeader);
    }

    public FOOTER getFooter() {
        return Graphene.createPageFragment(footerClass, wholeTableFooter);
    }

    private final AdvancedDataTableWithHeaderAndFooterInteractions advancedInteractions = new AdvancedDataTableWithHeaderAndFooterInteractions();

    @Override
    public AdvancedDataTableWithHeaderAndFooterInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedDataTableWithHeaderAndFooterInteractions extends AdvancedDataTableInteractions {

        public WebElement getHeaderElement() {
            return headerElement;
        }

        public WebElement getFooterElement() {
            return footerElement;
        }

        public WebElement getColumnHeaderElement(int column) {
            return columnHeaders.get(column);
        }

        public WebElement getColumnFooterElement(int column) {
            return columnFooters.get(column);
        }
    }
}