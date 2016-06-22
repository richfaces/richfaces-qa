/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.richfaces.event.ItemChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * Managed bean storing glogal setting for the application, e.g. skin.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 */
@ManagedBean
@SessionScoped
public class RichBean implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(RichBean.class);
    private static final long serialVersionUID = 5590865106686406193L;

    private String activeTabOnIndex = "a4j";
    private Map<String, String> allComponentsPermanentList;
    private String component;
    private String componentFormEnctype;
    private List<SelectItem> componentFormEnctypes;
    private String container;
    private int delay;
    private boolean dummyBooleanResp;
    private Map<String, String> filteredComponents;
    private boolean log;
    private boolean reComponent;
    private boolean reDefault;
    private boolean reTests;
    private String selectedComponent;
    private String skin;
    private Skinning skinning;
    private List<SelectItem> skinningList;
    private List<String> skins;
    private boolean stateless;
    private boolean uiDebugEnabled;

    public static void logToPage(String msg) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExpressionFactory factory = ctx.getApplication().getExpressionFactory();
        ELContext elContext = ctx.getELContext();
        ValueExpression exp = factory.createValueExpression(elContext, "#{phasesBean.phases}", List.class);
        List<String> phases = (List<String>) exp.getValue(elContext);
        phases.add(msg);
    }

    private static Map<String, String> sortComponents(Map<String, String> map) {
        Map<String, String> result;
        ArrayList<Map.Entry<String, String>> entries = Lists.newArrayList(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> a, Map.Entry<String, String> b) {
                return a.getValue().compareTo(b.getValue());
            }
        });

        result = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * An action listener that does nothing.
     *
     * @param event an event representing the activation of a user interface component (not used)
     */
    public void actionListener(AjaxBehaviorEvent event) {
        logToPage("* action listener invoked");
    }

    /**
     * An action listener that does nothing.
     *
     * @param event an event representing the activation of a user interface component (not used)
     */
    public void actionListener2(AjaxBehaviorEvent event) {
        logToPage("* action listener *2 invoked");
    }

    public void causeAjaxErrorAction() {
        sendErrorResponse();
    }

    public void causeAjaxErrorActionListener(ActionEvent event) {
        sendErrorResponse();
    }

    public void causeAjaxErrorListener(AjaxBehaviorEvent event) {
        sendErrorResponse();
    }

    public void causeAjaxErrorValueChangeListener(ValueChangeEvent event) {
        sendErrorResponse();
    }

    /**
     * A change event listener that logs to the page old and new value. This is 2nd ValueChacgeListener. Use 2 different
     * listeners is useful when testing more than one listener definition for one component
     *
     * @param event an event representing the activation of a user interface component
     */
    public void changeEventListener(AjaxBehaviorEvent event) {
        logToPage("* 2 value changed ");
    }

    private void createComponentsMap() {
        filteredComponents = new LinkedHashMap<String, String>(90);
        allComponentsPermanentList = new LinkedHashMap<String, String>(90);

        allComponentsPermanentList.put("a4jActionListener", "A4J Action Listener");
        allComponentsPermanentList.put("a4jAjax", "A4J Ajax");
        allComponentsPermanentList.put("a4jAttachQueue", "A4J Attach Queue");
        allComponentsPermanentList.put("a4jCommandLink", "A4J Command Link");
        allComponentsPermanentList.put("a4jCommandButton", "A4J Command Button");
        allComponentsPermanentList.put("a4jJSFunction", "A4J JavaScript Function");
        allComponentsPermanentList.put("a4jLog", "A4J Log");
        allComponentsPermanentList.put("a4jMediaOutput", "A4J Media Output");
        allComponentsPermanentList.put("a4jOutputPanel", "A4J Output Panel");
        allComponentsPermanentList.put("a4jParam", "A4J Action Parameter");
        allComponentsPermanentList.put("a4jPoll", "A4J Poll");
        allComponentsPermanentList.put("a4jPush", "A4J Push");
        allComponentsPermanentList.put("a4jQueue", "A4J Queue");
        allComponentsPermanentList.put("a4jRegion", "A4J Region");
        allComponentsPermanentList.put("a4jRepeat", "A4J Repeat");
        allComponentsPermanentList.put("a4jStatus", "A4J Status");

        allComponentsPermanentList.put("richAccordion", "Rich Accordion");
        allComponentsPermanentList.put("richAccordionItem", "Rich Accordion Item");
        allComponentsPermanentList.put("richAutocomplete", "Rich Autocomplete");
        allComponentsPermanentList.put("richCalendar", "Rich Calendar");
        allComponentsPermanentList.put("richChart", "Rich Chart");
        allComponentsPermanentList.put("richCollapsiblePanel", "Rich Collapsible Panel");
        allComponentsPermanentList.put("richCollapsibleSubTable", "Rich Collapsible Sub Table");
        allComponentsPermanentList.put("richCollapsibleSubTableToggler", "Rich Collapsible Sub Table Toggler");
        allComponentsPermanentList.put("richColumn", "Rich Column");
        allComponentsPermanentList.put("richColumnGroup", "Rich Column Group");
        allComponentsPermanentList.put("richComponentControl", "Rich Component Control");
        allComponentsPermanentList.put("richContextMenu", "Rich Context Menu");
        allComponentsPermanentList.put("richDataGrid", "Rich Data Grid");
        allComponentsPermanentList.put("richDataScroller", "Rich Data Scroller");
        allComponentsPermanentList.put("richDataTable", "Rich Data Table");
        allComponentsPermanentList.put("richDragSource", "Rich Drag Source");
        allComponentsPermanentList.put("richDragIndicator", "Rich Drag Indicator");
        allComponentsPermanentList.put("richDropTarget", "Rich Drop Target");
        allComponentsPermanentList.put("richDropDownMenu", "Rich Drop Down Menu");
        allComponentsPermanentList.put("richEditor", "Rich Editor");
        allComponentsPermanentList.put("richExtendedDataTable", "Rich Extended Data Table");
        allComponentsPermanentList.put("richFileUpload", "Rich File Upload");
        allComponentsPermanentList.put("richFocus", "Rich Focus");
        allComponentsPermanentList.put("richFunctions", "Rich Functions");
        allComponentsPermanentList.put("richGraphValidator", "Rich Graph Validator");
        allComponentsPermanentList.put("richHashParam", "Rich Hash Parameter");
        allComponentsPermanentList.put("richHotKey", "Rich Hot Key");
        allComponentsPermanentList.put("richInplaceInput", "Rich Inplace Input");
        allComponentsPermanentList.put("richInplaceSelect", "Rich Inplace Select");
        allComponentsPermanentList.put("richItemChangeListener", "Rich Item Change Listener");
        allComponentsPermanentList.put("richInputNumberSlider", "Rich Input Number Slider");
        allComponentsPermanentList.put("richInputNumberSpinner", "Rich Input Number Spinner");
        allComponentsPermanentList.put("richJQuery", "Rich jQuery");
        allComponentsPermanentList.put("richList", "Rich List");
        allComponentsPermanentList.put("richMenuGroup", "Rich Menu Group");
        allComponentsPermanentList.put("richMenuItem", "Rich Menu Item");
        allComponentsPermanentList.put("richMenuSeparator", "Rich Menu Separator");
        allComponentsPermanentList.put("richMessage", "Rich Message");
        allComponentsPermanentList.put("richMessages", "Rich Messages");
        allComponentsPermanentList.put("richNotify", "Rich Notify");
        allComponentsPermanentList.put("richNotifyMessage", "Rich Notify Message");
        allComponentsPermanentList.put("richNotifyMessages", "Rich Notify Messages");
        allComponentsPermanentList.put("richNotifyStack", "Rich Notify Stack");
        allComponentsPermanentList.put("richOrderingList", "Rich Ordering List");
        allComponentsPermanentList.put("richPanel", "Rich Panel");
        allComponentsPermanentList.put("richPanelMenu", "Rich Panel Menu");
        allComponentsPermanentList.put("richPanelMenuGroup", "Rich Panel Menu Group");
        allComponentsPermanentList.put("richPanelMenuItem", "Rich Panel Menu Item");
        allComponentsPermanentList.put("richPanelToggleListener", "Rich Panel Toggle Listener");
        allComponentsPermanentList.put("richPickList", "Rich Pick List");
        allComponentsPermanentList.put("richPlaceholder", "Rich Placeholder");
        allComponentsPermanentList.put("richPopupPanel", "Rich Popup Panel");
        allComponentsPermanentList.put("richProgressBar", "Rich Progress Bar");
        allComponentsPermanentList.put("richSelect", "Rich Select");
        allComponentsPermanentList.put("richTab", "Rich Tab");
        allComponentsPermanentList.put("richTabPanel", "Rich Tab Panel");
        allComponentsPermanentList.put("richToggleControl", "Rich Toggle Control");
        allComponentsPermanentList.put("richTogglePanel", "Rich Toggle Panel");
        allComponentsPermanentList.put("richTogglePanelItem", "Rich Toggle Panel Item");
        allComponentsPermanentList.put("richToolbar", "Rich Toolbar");
        allComponentsPermanentList.put("richToolbarGroup", "Rich Toolbar Group");
        allComponentsPermanentList.put("richTooltip", "Rich Tooltip");
        allComponentsPermanentList.put("richTree", "Rich Tree");
        allComponentsPermanentList.put("richTreeSelectionChangeListener", "Rich Tree Selection Change Listener");
        allComponentsPermanentList.put("richTreeToggleListener", "Rich Tree Toggle Listener");
        allComponentsPermanentList.put("richValidator", "Rich Validator");

        allComponentsPermanentList = sortComponents(allComponentsPermanentList);

        allComponentsPermanentList.put("expressionLanguage", "Expression Language");
        allComponentsPermanentList.put("commandButton", "JSF Command Button");
        allComponentsPermanentList.put("hDataTable", "JSF Data Table");
        allComponentsPermanentList.put("skinning", "Skinning");
        allComponentsPermanentList.put("uiRepeat", "UI Repeat");

        allComponentsPermanentList.put("myComponent",
            "My sample component samples");

        filteredComponents.putAll(allComponentsPermanentList);
    }

    private void createSkinList() {
        skins = new ArrayList<String>();
        skins.add("blueSky");
        skins.add("classic");
        skins.add("deepMarine");
        skins.add("emeraldTown");
        skins.add("japanCherry");
        skins.add("ruby");
        skins.add("wine");
        skins.add("plain");
    }

    /**
     * An action that does nothing.
     *
     * @return null
     */
    public String dummyAction() {
        logToPage("* action invoked");
        return null;
    }

    /**
     * An action listener that does nothing.
     *
     * @param event an event representing the activation of a user interface component (not used)
     */
    public void dummyActionListener(ActionEvent event) {
        logToPage("* action listener invoked");
    }

    /**
     * An action that does nothing but has delay
     *
     * @return null
     */
    public String dummyActionWithDelay() {
        logToPage("* action invoked [start]");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logToPage(" *** Error while dummy action handler sleeps.");
        }

        logToPage("* action invoked [end]");
        dummyBooleanResp = !dummyBooleanResp;
        return null;
    }

    /**
     * An action that just logs message to the page.
     */
    public void dummyListener(AjaxBehaviorEvent event) {
        RichBean.logToPage("* listener invoked");
        // force phasesPanel to render
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("phasesPanel");
    }

    private void filterComponents() {
        // reset all displayed components
        this.filteredComponents.clear();
        // if filter is not set up then put all components in it.
        if (selectedComponent == null || selectedComponent.trim().isEmpty()) {
            filteredComponents.putAll(allComponentsPermanentList);
        } else {
            String valueToFind = selectedComponent.toLowerCase();
            filteredComponents.putAll(findRelevantComponents(valueToFind, allComponentsPermanentList));
        }
    }

    /**
     * This method create Map of components to display. If input parameter searchString match in value of any component from
     * map, then add this component to new map.
     *
     * @param searchString patter to find. if null then empty map is returned.
     * @param map of all components. If null then empty map is returned.
     * @return Map of components with string key and string value which value match with patter in searchString.
     */
    private Map<String, String> findRelevantComponents(String searchString, Map<String, String> map) {
        Map<String, String> result = new TreeMap<String, String>();
        if (searchString != null && map != null) {
            for (String key : map.keySet()) {
                String value = map.get(key);
                Pattern patter = Pattern.compile(searchString);
                Matcher matcher = patter.matcher(value.toLowerCase());
                if (matcher.find()) {
                    result.put(key, value);
                }
            }
        }
        return result;
    }

    public String getActiveTabOnIndex() {
        return activeTabOnIndex;
    }

    public String getComponent() {
        return component;
    }

    public String getComponentFormEnctype() {
        return componentFormEnctype;
    }

    public List<SelectItem> getComponentFormEnctypes() {
        return componentFormEnctypes;
    }

    public String getContainer() {
        return container;
    }

    public int getDelay() {
        return delay;
    }

    public boolean getDummyBooleanResp() {
        return dummyBooleanResp;
    }

    public boolean getExecuteChecker() {
        return true;
    }

    public Map<String, String> getFilteredComponents() {
        return filteredComponents;
    }

    public Object[] getFilteredComponentsList() {
        return filteredComponents.keySet().toArray();
    }

    public String getSelectedComponent() {
        return selectedComponent;
    }

    /**
     * Getter for user's skin.
     *
     * @return a RichFaces skin
     */
    public String getSkin() {
        return skin;
    }

    public String getSkinning() {
        if (skinning == Skinning.SKINNING) {
            return "enabled";
        } else {
            return "disabled";
        }
    }

    public String getSkinningClasses() {
        if (skinning == Skinning.SKINNING_CLASSES) {
            return "enabled";
        } else {
            return "disabled";
        }
    }

    public List<SelectItem> getSkinningList() {
        return skinningList;
    }

    public List<String> getSkins() {
        return skins;
    }

    public String getTestsPage() {
        if (component.equals("none")) {
            return "/blank.xhtml";
        } else {
            return String.format("/components/%s/tests.xhtml", component);
        }
    }

    @PostConstruct
    public void init() {
        logger.debug("initializing bean " + getClass().getName());
        createSkinList();
        createComponentsMap();

        component = "none";
        container = "plain";
        skin = "blueSky";
        skinningList = new ArrayList<SelectItem>();
        skinningList.add(new SelectItem(Skinning.NONE));
        skinningList.add(new SelectItem(Skinning.SKINNING));
        skinningList.add(new SelectItem(Skinning.SKINNING_CLASSES));
        skinning = Skinning.SKINNING;
        reTests = false;
        reComponent = true;
        uiDebugEnabled = false;

        this.stateless = Boolean.valueOf(System.getProperty("statelessViews", "false"));
        this.delay = 0;
        componentFormEnctypes = Lists.newArrayList(
            new SelectItem("application/x-www-form-urlencoded", "default"),
            new SelectItem("multipart/form-data", "multipart"));
        componentFormEnctype = componentFormEnctypes.get(0).getValue().toString();
    }

    public String invalidateSession() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        Object session = ec.getSession(false);

        if (session == null) {
            return "/index";
        }

        if (session instanceof HttpSession) {
            ((HttpSession) session).invalidate();
            return FacesContext.getCurrentInstance().getViewRoot().getViewId();
        }

        throw new IllegalStateException();
    }

    public boolean isLog() {
        return log;
    }

    public boolean isReDefault() {
        return reDefault;
    }

    public boolean isStateless() {
        return stateless;
    }

    public boolean isUiDebugEnabled() {
        return uiDebugEnabled;
    }

    /**
     * An item change listener that logs to the page old and new value.
     *
     * @param event an event representing the activation of a user interface component
     */
    public void itemChangeListener(ItemChangeEvent event) {
        logToPage("* 1 item changed: " + (event.getOldItem() == null ? null : event.getOldItem().getId()) + " -> "
            + (event.getNewItem() != null ? event.getNewItem().getId() : null));
    }

    private void sendErrorResponse() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        try {
            response.sendError(501, "Intentional error");
            // complete the response to prevent throwing of IllegalStateException (response already committed)
            context.responseComplete();
        } catch (IOException ex) {
            logger.error("Was not able to send error response.", ex);
        }
    }

    public void setActiveTabOnIndex(String activeTabOnIndex) {
        this.activeTabOnIndex = activeTabOnIndex;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public void setComponentFormEnctype(String componentFormEnctype) {
        this.componentFormEnctype = componentFormEnctype;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setDummyBooleanResp(boolean dummyBooleanResp) {
        this.dummyBooleanResp = dummyBooleanResp;
    }

    public void setExecuteChecker(boolean executeChecker) {
        logToPage("* executeChecker");
    }

    public void setFilteredComponents(Map<String, String> allComponents) {
        this.filteredComponents = allComponents;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    public void setReComponent(boolean reComponent) {
        this.reComponent = reComponent;
    }

    public void setReDefault(boolean reDefault) {
        this.reDefault = reDefault;
    }

    public void setReTests(boolean reTests) {
        this.reTests = reTests;
    }

    public void setSelectedComponent(String selectedComponent) {
        this.selectedComponent = selectedComponent;
        filterComponents();
    }

    /**
     * Setter for user's skin.
     *
     * @param skin a RichFaces skin
     */
    public void setSkin(String skin) {
        this.skin = skin;
    }

    public void setSkinning(String skinning) {
        this.skinning = Skinning.valueOf(skinning);
    }

    public void setSkinningClasses(String skinningClasses) {
        this.skinning = Skinning.valueOf(skinningClasses);
    }

    public void setSkins(List<String> skins) {
        this.skins = skins;
    }

    public void setStateless(boolean stateless) {
        this.stateless = stateless;
    }

    public void setUiDebugEnabled(boolean uiDebugEnabled) {
        this.uiDebugEnabled = uiDebugEnabled;
    }

    /**
     * A value change listener that logs to the page old and new value.
     *
     * @param event an event representing the activation of a user interface component
     */
    public void valueChangeListener(ValueChangeEvent event) {
        logToPage("* 1 value changed: " + event.getOldValue() + " -> " + event.getNewValue());
    }

    /**
     * A value change listener that logs to the page old and new value. But if event value was longer that 20 chars, only first
     * 20 chars will be logged
     *
     * @param event an event representing the activation of a user interface component
     */
    public void valueChangeListenerImproved(ValueChangeEvent event) {
        String oldVal = event.getOldValue() != null ? event.getOldValue().toString() : null;
        String newVal = event.getNewValue() != null ? event.getNewValue().toString() : null;

        // need to escape these values before logging to the page as it can contain e.g. \r \n
        if (oldVal != null) {
            oldVal = oldVal.replace("\r", "").replace("\n", "");
        }
        if (newVal != null) {
            newVal = newVal.replace("\r", "").replace("\n", "");
        }

        logToPage("* 3 value changed: "
            + (oldVal != null && oldVal.length() > 21 ? oldVal.substring(0, 20) : oldVal != null ? oldVal : "null")
            + " -> "
            + (newVal != null && newVal.length() > 21 ? newVal.substring(0, 20) : newVal != null ? newVal : "null"));
    }

    public enum Skinning {

        NONE, SKINNING, SKINNING_CLASSES
    }
}
