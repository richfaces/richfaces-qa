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
package org.richfaces.tests.metamer.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.richfaces.event.ItemChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean storing glogal setting for the application, e.g. skin.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 23169 $
 */
@ManagedBean
@SessionScoped
public class RichBean implements Serializable {

    private static final long serialVersionUID = 5590865106686406193L;
    private static final Logger logger = LoggerFactory.getLogger(RichBean.class);
    private String skin;
    private List<SelectItem> skinningList;
    private Skinning skinning;
    private List<String> skins;
    private boolean reDefault;
    private boolean reComponent;
    private boolean reTests;
    private boolean log;
    private String component;
    private Map<String, String> a4jComponents; // [a4jCommandLink; A4J Command Link]
    private Map<String, String> richComponents;
    private Map<String, String> otherComponents;
    private String container;
    private boolean dummyBooleanResp;
    private String activeTabOnIndex = "a4j";

    public enum Skinning {

        NONE, SKINNING, SKINNING_CLASSES
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
    }

    private void createComponentsMap() {
        a4jComponents = new TreeMap<String, String>();
        richComponents = new TreeMap<String, String>();
        otherComponents = new TreeMap<String, String>();

        a4jComponents.put("a4jActionListener", "A4J Action Listener");
        a4jComponents.put("a4jAjax", "A4J Ajax");
        a4jComponents.put("a4jAttachQueue", "A4J Attach Queue");
        a4jComponents.put("a4jCommandLink", "A4J Command Link");
        a4jComponents.put("a4jCommandButton", "A4J Command Button");
        a4jComponents.put("a4jJSFunction", "A4J JavaScript Function");
        a4jComponents.put("a4jLog", "A4J Log");
        a4jComponents.put("a4jMediaOutput", "A4J Media Output");
        a4jComponents.put("a4jOutputPanel", "A4J Output Panel");
        a4jComponents.put("a4jParam", "A4J Action Parameter");
        a4jComponents.put("a4jPoll", "A4J Poll");
        a4jComponents.put("a4jPush", "A4J Push");
        a4jComponents.put("a4jQueue", "A4J Queue");
        a4jComponents.put("a4jRegion", "A4J Region");
        a4jComponents.put("a4jRepeat", "A4J Repeat");
        a4jComponents.put("a4jStatus", "A4J Status");

        otherComponents.put("expressionLanguage", "Expression Language");
        otherComponents.put("commandButton", "JSF Command Button");
        otherComponents.put("hDataTable", "JSF Data Table");
        otherComponents.put("uiRepeat", "UI Repeat");

        richComponents.put("richAccordion", "Rich Accordion");
        richComponents.put("richAccordionItem", "Rich Accordion Item");
        richComponents.put("richAutocomplete", "Rich Autocomplete");
        richComponents.put("richCalendar", "Rich Calendar");
        richComponents.put("richCollapsiblePanel", "Rich Collapsible Panel");
        richComponents.put("richCollapsibleSubTable", "Rich Collapsible Sub Table");
        richComponents.put("richCollapsibleSubTableToggler", "Rich Collapsible Sub Table Toggler");
        richComponents.put("richColumn", "Rich Column");
        richComponents.put("richColumnGroup", "Rich Column Group");
        richComponents.put("richComponentControl", "Rich Component Control");
        richComponents.put("richContextMenu", "Rich Context Menu");
        richComponents.put("richDataGrid", "Rich Data Grid");
        richComponents.put("richDataScroller", "Rich Data Scroller");
        richComponents.put("richDataTable", "Rich Data Table");
        richComponents.put("richDragSource", "Rich Drag Source");
        richComponents.put("richDragIndicator", "Rich Drag Indicator");
        richComponents.put("richDropTarget", "Rich Drop Target");
        richComponents.put("richDropDownMenu", "Rich Drop Down Menu");
        richComponents.put("richEditor", "Rich Editor");
        richComponents.put("richExtendedDataTable", "Rich Extended Data Table");
        richComponents.put("richFileUpload", "Rich File Upload");
        richComponents.put("richFunctions", "Rich Functions");
        richComponents.put("richGraphValidator", "Rich Graph Validator");
        richComponents.put("richHashParam", "Rich Hash Parameter");
        richComponents.put("richHotKey", "Rich Hot Key");
        richComponents.put("richInplaceInput", "Rich Inplace Input");
        richComponents.put("richInplaceSelect", "Rich Inplace Select");
        richComponents.put("richItemChangeListener", "Rich Item Change Listener");
        richComponents.put("richInputNumberSlider", "Rich Input Number Slider");
        richComponents.put("richInputNumberSpinner", "Rich Input Number Spinner");
        richComponents.put("richJQuery", "Rich jQuery");
        richComponents.put("richList", "Rich List");
        richComponents.put("richMenuGroup", "Rich Menu Group");
        richComponents.put("richMenuItem", "Rich Menu Item");
        richComponents.put("richMenuSeparator", "Rich Menu Separator");
        richComponents.put("richMessage", "Rich Message");
        richComponents.put("richMessages", "Rich Messages");
        richComponents.put("richNotify", "Rich Notify");
        richComponents.put("richOrderingList", "Rich Ordering List");
        richComponents.put("richPanel", "Rich Panel");
        richComponents.put("richPanelMenu", "Rich Panel Menu");
        richComponents.put("richPanelMenuGroup", "Rich Panel Menu Group");
        richComponents.put("richPanelMenuItem", "Rich Panel Menu Item");
        richComponents.put("richPanelToggleListener", "Rich Panel Toggle Listener");
        richComponents.put("richPickList", "Rich Pick List");
        richComponents.put("richPopupPanel", "Rich Popup Panel");
        richComponents.put("richProgressBar", "Rich Progress Bar");
        richComponents.put("richSelect", "Rich Select");
        richComponents.put("richTab", "Rich Tab");
        richComponents.put("richTabPanel", "Rich Tab Panel");
        richComponents.put("richToggleControl", "Rich Toggle Control");
        richComponents.put("richTogglePanel", "Rich Toggle Panel");
        richComponents.put("richTogglePanelItem", "Rich Toggle Panel Item");
        richComponents.put("richToolbar", "Rich Toolbar");
        richComponents.put("richToolbarGroup", "Rich Toolbar Group");
        richComponents.put("richTooltip", "Rich Tooltip");
        richComponents.put("richTree", "Rich Tree");
        richComponents.put("richTreeSelectionChangeListener", "Rich Tree Selection Change Listener");
        richComponents.put("richTreeToggleListener", "Rich Tree Toggle Listener");
        richComponents.put("richValidator", "Rich Validator");
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
     * Getter for user's skin.
     *
     * @return a RichFaces skin
     */
    public String getSkin() {
        return skin;
    }

    /**
     * Setter for user's skin.
     *
     * @param skin
     *            a RichFaces skin
     */
    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getSkinning() {
        if (skinning == Skinning.SKINNING) {
            return "enabled";
        } else {
            return "disabled";
        }
    }

    public void setSkinning(String skinning) {
        this.skinning = Skinning.valueOf(skinning);
    }

    public String getSkinningClasses() {
        if (skinning == Skinning.SKINNING_CLASSES) {
            return "enabled";
        } else {
            return "disabled";
        }
    }

    public void setSkinningClasses(String skinningClasses) {
        this.skinning = Skinning.valueOf(skinningClasses);
    }

    public List<SelectItem> getSkinningList() {
        return skinningList;
    }

    public void setSkins(List<String> skins) {
        this.skins = skins;
    }

    public List<String> getSkins() {
        return skins;
    }

    public void setReDefault(boolean reDefault) {
        this.reDefault = reDefault;
    }

    public boolean isReDefault() {
        return reDefault;
    }

    public void setReComponent(boolean reComponent) {
        this.reComponent = reComponent;
    }

    public boolean isReComponent() {
        return reComponent;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    public boolean isLog() {
        return log;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponent() {
        return component;
    }

    public Map<String, String> getRichComponents() {
        return richComponents;
    }

    public Object[] getRichComponentsList() {
        return richComponents.keySet().toArray();
    }

    public Map<String, String> getA4jComponents() {
        return a4jComponents;
    }

    public Object[] getA4jComponentsList() {
        return a4jComponents.keySet().toArray();
    }

    public Map<String, String> getOtherComponents() {
        return otherComponents;
    }

    public Object[] getOtherComponentsList() {
        return otherComponents.keySet().toArray();
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public boolean isReTests() {
        return reTests;
    }

    public void setReTests(boolean reTests) {
        this.reTests = reTests;
    }

    public String getActiveTabOnIndex() {
        return activeTabOnIndex;
    }

    public void setActiveTabOnIndex(String activeTabOnIndex) {
        this.activeTabOnIndex = activeTabOnIndex;
    }

    public String getTestsPage() {
        if (component.equals("none")) {
            return "/blank.xhtml";
        } else {
            return String.format("/components/%s/tests.xhtml", component);
        }
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

    public static void logToPage(String msg) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExpressionFactory factory = ctx.getApplication().getExpressionFactory();
        ValueExpression exp = factory.createValueExpression(ctx.getELContext(), "#{phasesBean.phases}", List.class);
        List<String> phases = (List<String>) exp.getValue(ctx.getELContext());
        phases.add(msg);
    }

    /**
     * Action that causes an error. Suitable for testing 'onerror' attribute.
     *
     * @return method never returns any value
     * @throws FacesException
     *             thrown always
     */
    public String causeError() {
        throw new FacesException("Ajax request caused an error. This is intentional behavior.");
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
     * An action listener that does nothing.
     *
     * @param event
     *            an event representing the activation of a user interface component (not used)
     */
    public void dummyActionListener(ActionEvent event) {
        logToPage("* action listener invoked");
    }

    /**
     * An action listener that does nothing.
     *
     * @param event
     *            an event representing the activation of a user interface component (not used)
     */
    public void actionListener(AjaxBehaviorEvent event) {
        logToPage("* action listener invoked");
    }

    /**
     * An action listener that does nothing.
     *
     * @param event
     *            an event representing the activation of a user interface component (not used)
     */
    public void actionListener2(AjaxBehaviorEvent event) {
        logToPage("* action listener *2 invoked");
    }

    /**
     * An item change listener that logs to the page old and new value.
     *
     * @param event
     *            an event representing the activation of a user interface component
     */
    public void itemChangeListener(ItemChangeEvent event) {
        logToPage("* item changed: " + (event.getOldItem() == null ? null : event.getOldItem().getId()) + " -> "
            + (event.getNewItem() != null ? event.getNewItem().getId() : null));
    }

    /**
     * A value change listener that logs to the page old and new value.
     *
     * @param event
     *            an event representing the activation of a user interface component
     */
    public void valueChangeListener(ValueChangeEvent event) {
        logToPage("*1 value changed: " + event.getOldValue() + " -> " + event.getNewValue());
    }

    /**
     * A change event listener that logs to the page old and new value. This is 2nd ValueChacgeListener. Use 2 different
     * listeners is useful when testing more than one listener definition for one component
     *
     * @param event
     *            an event representing the activation of a user interface component
     */
    public void changeEventListener(AjaxBehaviorEvent event) {
        logToPage("*2 value changed ");
    }

    /**
     * A value change listener that logs to the page old and new value. But if event value was longer that 20 chars,
     * only first 20 chars will be logged
     *
     * @param event
     *            an event representing the activation of a user interface component
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

        logToPage("*3 value changed: "
            + (oldVal != null && oldVal.length() > 21 ? oldVal.substring(0, 20) : oldVal != null ? oldVal : "null")
            + " -> "
            + (newVal != null && newVal.length() > 21 ? newVal.substring(0, 20) : newVal != null ? newVal : "null"));
    }

    public boolean getExecuteChecker() {
        return true;
    }

    public void setExecuteChecker(boolean executeChecker) {
        logToPage("* executeChecker");
    }

    public boolean getDummyBooleanResp() {
        return dummyBooleanResp;
    }

    public void setDummyBooleanResp(boolean dummyBooleanResp) {
        this.dummyBooleanResp = dummyBooleanResp;
    }
}
