/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer;

/**
 * Representation of a template. The name of the template can be used as
 * a view parameter.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22617 $
 */
public enum Template {

    PLAIN("plain", "Plain", ""),
    REDDIV("redDiv", "Red div", ""),
    BLUEDIV("blueDiv", "Blue div", ""),
    RICHACCORDION("richAccordion", "Rich Accordion", ""),
    RICHCOLLAPSIBLEPANEL("richCollapsiblePanel", "Rich Collapsible Panel", ""),
    RICHCOLLAPSIBLESUBTABLE("richCollapsibleSubTable", "Rich Collapsible Sub Table", "containerRichCollapsibleSubTable:0:richSubTable:2:"),
    RICHDATATABLE("richDataTable", "Rich Data Table", "containerRichDataTable:2:"),
    RICHDATAGRID("richDataGrid", "Rich Data Grid", "containerRichDataGrid:3:"),
    RICHEXTENDEDDATATABLE("richExtendedDataTable", "Rich Extended Data Table", "containerRichExtendedDataTable:2:"),
    RICHLIST("richList", "Rich List", "containerRichList:2:"),
    RICHPANEL("richPanel", "Rich Panel", ""),
    RICHPOPUPPANEL("richPopupPanel", "Rich Popup Panel", ""),
    RICHTABPANEL("richTabPanel", "Rich Tab Panel", ""),
    RICHTOGGLEPANEL("richTogglePanel", "Rich Toggle Panel", ""),
    HDATATABLE("hDataTable", "JSF Data Table", "containerHDataTable:2:"),
    HPANELGRID("hPanelGrid", "JSF Panel Grid", ""),
    UIREPEAT("uiRepeat", "UI Repeat", "containerUiRepeat:1:"),
    A4JREGION("a4jRegion", "A4J Region", ""),
    A4JREPEAT("a4jRepeat", "A4J Repeat", "containerA4JRepeat:1:");
    /**
     * identifier of a template
     */
    private String name;
    /**
     * human-readable name of the template
     */
    private String desc;
    /**
     * prefix of the component nested in this template
     */
    private String nestedComponentPrefix;

    /**
     * Private constructor.
     * 
     * @param name
     * @param prefix
     */
    private Template(String name, String desc, String nestedComponentPrefix) {
        this.name = name;
        this.desc = desc;
        this.nestedComponentPrefix = nestedComponentPrefix;
    }

    /**
     * Gets value of name field.
     * @return value of name field
     */
    public String getName() {
        return name;
    }

    /**
     * Gets value of desc field.
     * @return value of desc field
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Returns the prefix of component nested in this template
     * @return
     */
    public String getNestedComponentPrefix() {
        return nestedComponentPrefix;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return name;
    }
}
