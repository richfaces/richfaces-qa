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
package org.richfaces.tests.metamer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Representation of a template. The name of the template can be used as
 * a view parameter.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public enum Template {

    PLAIN(new String[]{ "plain", "no", "none" }, "Plain", ""),
    REDDIV(new String[]{ "redDiv", "red" }, "Red div", ""),
    BLUEDIV(new String[]{ "blueDiv", "blue" }, "Blue div", ""),
    RICHACCORDION(new String[]{ "richAccordion", "accordion", "acc" }, "Rich Accordion", ""),
    RICHCOLLAPSIBLEPANEL(new String[]{ "richCollapsiblePanel", "collapsiblePanel", "cPanel", "cp" }, "Rich Collapsible Panel", ""),
    RICHCOLLAPSIBLESUBTABLE(new String[]{ "richCollapsibleSubTable", "collapsibleSubTable", "cst" }, "Rich Collapsible Sub Table", "containerRichCollapsibleSubTable:0:richSubTable:2:"),
    RICHDATATABLE(new String[]{ "richDataTable", "dataTable", "dt", "table" }, "Rich Data Table", "containerRichDataTable:2:"),
    RICHDATAGRID(new String[]{ "richDataGrid", "dataGrid", "grid", "dg" }, "Rich Data Grid", "containerRichDataGrid:3:"),
    RICHEXTENDEDDATATABLE(new String[]{ "richExtendedDataTable", "extendedDataTable", "edt", "eTable" }, "Rich Extended Data Table", "containerRichExtendedDataTable:2:"),
    RICHLIST(new String[]{ "richList", "list", "l" }, "Rich List", "containerRichList:2:"),
    RICHPANEL(new String[]{ "richPanel", "panel", "p" }, "Rich Panel", ""),
    RICHPOPUPPANEL(new String[]{ "richPopupPanel", "popupPanel", "popup", "ppanel", "pp" }, "Rich Popup Panel", ""),
    RICHTABPANEL(new String[]{ "richTabPanel", "tabPanel", "tab" }, "Rich Tab Panel", ""),
    RICHTOGGLEPANEL(new String[]{ "richTogglePanel", "togglePanel", "toggle" }, "Rich Toggle Panel", ""),
    HDATATABLE(new String[]{ "hDataTable", "hTable" }, "JSF Data Table", "containerHDataTable:2:"),
    HPANELGRID(new String[]{ "hPanelGrid", "hGrid" }, "JSF Panel Grid", ""),
    UIREPEAT(new String[]{ "uiRepeat", "uRepeat" }, "UI Repeat", "containerUiRepeat:1:"),
    A4JREGION(new String[]{ "a4jRegion", "region" }, "A4J Region", ""),
    A4JREPEAT(new String[]{ "a4jRepeat", "aRepeat" }, "A4J Repeat", "containerA4JRepeat:1:");
    /**
     * identifier of a template
     */
    private final String templateName;
    private final Set<String> aliases;
    /**
     * human-readable name of the template
     */
    private final String desc;
    /**
     * prefix of the component nested in this template
     */
    private final String nestedComponentPrefix;

    /**
     * Private constructor.
     *
     * @param name
     * @param prefix
     */
    private Template(String name, String desc, String nestedComponentPrefix) {
        this(new String[]{ name }, desc, nestedComponentPrefix);
    }

    private Template(String[] names, String desc, String nestedComponentPrefix) {
        this.aliases = new HashSet<String>();
        templateName = names[0];
        for (String name : names) {
            aliases.add(name.toLowerCase());
        }
        this.desc = desc;
        this.nestedComponentPrefix = nestedComponentPrefix;
    }

    private Set<String> getAliasses() {
        return Collections.unmodifiableSet(aliases);
    }

    /**
     * Gets value of name field.
     * @return value of name field
     */
    public String getName() {
        return templateName;
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
     * @return prefix of component nested in this template
     */
    public String getNestedComponentPrefix() {
        return nestedComponentPrefix;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return getName();
    }

    public static Template valueFrom(String templateString) {
        for (Template t : values()) {
            if (t.getAliasses().contains(templateString.toLowerCase())) {
                return t;
            }
        }
        throw new RuntimeException("No such template " + templateString);
    }
}
