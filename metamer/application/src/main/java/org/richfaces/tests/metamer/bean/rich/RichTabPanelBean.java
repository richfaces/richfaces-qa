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
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.richfaces.component.UITab;
import org.richfaces.component.UITabPanel;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:tabPanel.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version$Revision: 23062$
 */
@ManagedBean(name = "richTabPanelBean")
@ViewScoped
public class RichTabPanelBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private static Logger logger;
    private Attributes attributes;

    private List<UITab> tabs = new ArrayList<UITab>();
    private List<TabBean> tabBeans = new ArrayList<TabBean>();

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UITabPanel.class, getClass());

        attributes.setAttribute("activeItem", "tab1");
        attributes.setAttribute("rendered", true);

        // will be tested in another way
        attributes.remove("converter");
        attributes.remove("itemChangeListener");

        // remove as soon as https://issues.jboss.org/browse/RF-10588 is fixed
        attributes.setAttribute("bypassUpdates", null);
        attributes.get("bypassUpdates").setType(Boolean.class);
    }

    public void createNewTab() {

        UITab tab = new UITab();
        int index = tabs.size() + 6; // there is already 5 tabs

        tab.setHeader("tab" + index + " header");
        tab.setId("tab" + index);
        tab.setName("tab" + index);
        tab.setRender("tab" + index);

        tabs.add(tab);
    }

    public void generateNewTab() {

        System.out.println(" ###### adding new tabBean... ");

        int i = tabBeans.size();
        String idBase = "tab" + (i + 6); // there is already 5 tabs
        tabBeans.add(new TabBean(idBase, idBase, idBase + " header", "Content of dynamicaly created " + idBase));

        System.out.println(" Now is tabBeans list " + tabBeans.size() + " long");
    }

    public void removeTab() throws Exception {
        // setActiveTabId("TAB1");

        String tabIdToRemove = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
            .get("removeTabId");

        TabBean currentTab = getTabById(tabIdToRemove);

        if (currentTab != null) {
            tabBeans.remove(currentTab);
        } else {
            throw new Exception("Tab Id parameter is null");
        }
    }

    private TabBean getTabById(String tabId) {
        System.out.println(" #tabId to remove: '" + tabId + "'");
        for (TabBean currentTab : tabBeans) {
            if (currentTab.getTabId().equals(tabId)) {
                return currentTab;
            }
        }
        return null;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<UITab> getTabs() {
        return tabs;
    }

    public void setTabs(List<UITab> tabs) {
        this.tabs = tabs;
    }

    public class TabBean {
        private String tabId;
        private String tabName;
        private String tabHeader;
        private String tabContentText;
        private boolean closable;

        public TabBean(String tabId, String tabName, String tabHeader, String tabContentText) {
            this.tabId = tabId;
            this.tabName = tabName;
            this.tabHeader = tabHeader;
            this.tabContentText = tabContentText;

            // default is closable
            this.closable = true;
        }

        public String getTabId() {
            return tabId;
        }

        public void setTabId(String tabId) {
            this.tabId = tabId;
        }

        public String getTabName() {
            return tabName;
        }

        public void setTabName(String tabName) {
            this.tabName = tabName;
        }

        public String getTabHeader() {
            return tabHeader;
        }

        public void setTabHeader(String tabHeader) {
            this.tabHeader = tabHeader;
        }

        public String getTabContentText() {
            return tabContentText;
        }

        public void setTabContentText(String tabContentText) {
            this.tabContentText = tabContentText;
        }

        public boolean isClosable() {
            return closable;
        }

        public void setClosable(boolean closable) {
            this.closable = closable;
        }

    }

    public List<TabBean> getTabBeans() {
        return tabBeans;
    }

    public void setTabBeans(List<TabBean> tabBeans) {
        this.tabBeans = tabBeans;
    }

    public List<String> getTestListA() {
        logger.info("getTestListA");

        List<String> listA = new ArrayList<String>(4);
        listA.add("Hyundai i30");
        listA.add("Renault Megane");
        listA.add("Renault Clio");
        listA.add("Toyota Auris");

        return listA;
    }

    public List<String> getTestListB() {
        logger.info("getTestListB");

        List<String> listB = new ArrayList<String>(4);
        listB.add("pineapple");
        listB.add("banana");
        listB.add("strawberry");
        listB.add("blueberry");

        return listB;
    }
}
