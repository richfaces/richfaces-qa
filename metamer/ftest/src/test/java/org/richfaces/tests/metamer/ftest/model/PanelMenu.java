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
package org.richfaces.tests.metamer.ftest.model;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import static org.jboss.arquillian.ajocado.dom.Attribute.CLASS;
import static org.jboss.arquillian.ajocado.dom.Attribute.SRC;

import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;

import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.jboss.test.selenium.GuardRequest;
import org.jboss.test.selenium.JQuerySelectors;
import org.jboss.test.selenium.RequestTypeModelGuard.Model;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;
import org.richfaces.PanelMenuMode;

public class PanelMenu extends AbstractModel<JQueryLocator> implements Model {

    AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    PanelMenuMode groupMode;
    PanelMenuMode itemMode;

    private ReferencedLocator<JQueryLocator> topItems = ref(root, "> .rf-pm-top-itm");
    private ReferencedLocator<JQueryLocator> topGroups = ref(root, "> .rf-pm-top-gr");
    private ReferencedLocator<JQueryLocator> anySelectedItem = ref(root, "div[class*=rf-pm][class*=-itm-sel]");
    private ReferencedLocator<JQueryLocator> anySelectedGroup = ref(root, "div[class*=rf-pm][class*=-gr-sel]");
    private ReferencedLocator<JQueryLocator> anyDisabledItem = ref(root, "div[class*=rf-pm-][class*=-itm-dis]");
    private ReferencedLocator<JQueryLocator> anyDisabledGroup = ref(root, "div[class*=rf-pm-][class*=-gr-dis]");

    public PanelMenu(JQueryLocator root) {
        super(root);
    }

    public void setGroupMode(PanelMenuMode groupMode) {
        this.groupMode = groupMode;
    }

    public void setItemMode(PanelMenuMode itemMode) {
        this.itemMode = itemMode;
    }

    public int getItemCount() {
        return selenium.getCount(topItems);
    }

    public int getGroupCount() {
        return selenium.getCount(topGroups);
    }

    /*
    public Iterable<Item> getAllItems() {
        return new ModelIterable<JQueryLocator, Item>(topItems.getAllOccurrences(), Item.class);
    }

    public Iterable<Group> getAllGroups() {
        return new ModelIterable<JQueryLocator, Group>(topGroups.getAllOccurrences(), Group.class);
    }*/

    public Item getItem(int index) {
        return new Item(topItems.get(index));
    }

    public Item getItemContains(String string) {
        return new Item(JQuerySelectors.append(topItems, SimplifiedFormat.format(":contains('{0}')", string)));
    }

    public Group getGroup(int index) {
        return new Group(topGroups.get(index));
    }

    public Group getGroupContains(String string) {
        return new Group(JQuerySelectors.append(topGroups, SimplifiedFormat.format(":contains('{0}')", string)));
    }

    public Item getAnyTopItem() {
        return new Item(topItems.getReferenced());
    }

    public Group getAnyTopGroup() {
        return new Group(topGroups.getReferenced());
    }

    public Item getAnySelectedItem() {
        return new Item(anySelectedItem.getReferenced());
    }

    public Group getAnySelectedGroup() {
        return new Group(anySelectedGroup.getReferenced());
    }

    public Item getAnyDisabledItem() {
        return new Item(anyDisabledItem.getReferenced());
    }

    public Group getAnyDisabledGroup() {
        return new Group(anyDisabledGroup.getReferenced());
    }

    public Group getAnyExpandedGroup() {
        return new Group(JQuerySelectors.append(topGroups, ":has(.rf-pm-hdr-exp)"));
    }

    public class Group extends AbstractModel<JQueryLocator> implements Model {
        ReferencedLocator<JQueryLocator> header = ref(root, "> div[class*=rf-pm-][class*=-gr-hdr]");
        ReferencedLocator<JQueryLocator> label = ref(header, "> table > tbody > tr > td[class*=rf-pm-][class*=-gr-lbl]");
        ReferencedLocator<JQueryLocator> leftIcon = ref(header,
            "> table > tbody > tr > td[class*=rf-pm-][class*=-gr-ico]");
        ReferencedLocator<JQueryLocator> rightIcon = ref(header,
            "> table > tbody > tr > td[class*=rf-pm-][class*=-gr-exp-ico]");

        private ReferencedLocator<JQueryLocator> content = ref(root, "> div[class*=rf-pm-][class*=gr-cnt]");
        private ReferencedLocator<JQueryLocator> items = ref(content, "> .rf-pm-itm");
        private ReferencedLocator<JQueryLocator> groups = ref(content, "> .rf-pm-gr");

        public Group(JQueryLocator root) {
            super(root);
        }

        public int getItemCount() {
            return selenium.getCount(items);
        }

        public int getGroupCount() {
            return selenium.getCount(groups);
        }

        /*
        public Iterable<Item> getAllItems() {
            return new ModelIterable<JQueryLocator, Item>(items.getAllOccurrences(), Item.class);
        }*/

        public Item getItem(int index) {
            return new Item(items.get(index));
        }

        public Item getItemContains(String string) {
            return new Item(JQuerySelectors.append(items, SimplifiedFormat.format(":contains('{0}')", string)));
        }

        /*
        public Iterable<Group> getAllGroups() {
            return new ModelIterable<JQueryLocator, Group>(groups.getAllOccurrences(), Group.class);
        }*/

        public Group getGroup(int index) {
            return new Group(groups.get(index));
        }

        public Group getGroupContains(String string) {
            return new Group(JQuerySelectors.append(groups, SimplifiedFormat.format(":contains('{0}')", string)));
        }

        public Item getAnyItem() {
            return new Item(items.getReferenced());
        }

        public Group getAnyGroup() {
            return new Group(groups.getReferenced());
        }

        public boolean isSelected() {
            return selenium.getAttribute(header.getAttribute(CLASS)).contains("-sel");
        }

        public boolean isExpanded() {
            return selenium.getAttribute(header.getAttribute(CLASS)).contains("-exp");
        }

        public boolean isCollapsed() {
            return selenium.getAttribute(header.getAttribute(CLASS)).contains("-colps");
        }

        public boolean isHovered() {
            return selenium.getAttribute(this.getAttribute(CLASS)).contains("-hov");
        }

        public boolean isDisabled() {
            return selenium.getAttribute(this.getAttribute(CLASS)).contains("-dis");
        }

        public boolean isVisible() {
            return selenium.isElementPresent(this) && selenium.isVisible(this);
        }

        public Icon getLeftIcon() {
            return new Icon(leftIcon.getReferenced());
        }

        public Icon getRightIcon() {
            return new Icon(rightIcon.getReferenced());
        }

        public Label getLabel() {
            return new Label(label.getReferenced());
        }

        public void toggle() {
            if (groupMode == null) {
                selenium.click(label);
            } else {
                new GuardRequest(getRequestTypeForMode(groupMode)) {
                    public void command() {
                        selenium.click(label);
                    }
                }.waitRequest();
            }
        }

        public class Icon extends PanelMenu.Icon {

            public Icon(JQueryLocator root) {
                super(root);
            }

            @Override
            public ElementLocator<JQueryLocator> getIcon() {
                return this.getChild(jq("div:visible[class*=rf-pm-ico-]"));
            }
        }
    }

    public class Item extends AbstractModel<JQueryLocator> implements Model {
        ReferencedLocator<JQueryLocator> label = ref(root, "> table > tbody > tr > td[class*=rf-][class*=-itm-lbl]");
        ReferencedLocator<JQueryLocator> leftIcon = ref(root, "> table > tbody > tr > td[class*=rf-][class*=-itm-ico]");
        ReferencedLocator<JQueryLocator> rightIcon = ref(root,
            "> table > tbody > tr > td[class*=rf-][class*=-itm-exp-ico]");

        public Item(JQueryLocator root) {
            super(root);
        }

        public boolean isSelected() {
            return selenium.getAttribute(this.getAttribute(CLASS)).contains("-sel");
        }

        public boolean isHovered() {
            return selenium.getAttribute(this.getAttribute(CLASS)).contains("-hov");
        }

        public boolean isDisabled() {
            return selenium.getAttribute(this.getAttribute(CLASS)).contains("-dis");
        }

        public boolean isVisible() {
            return selenium.isElementPresent(this) && selenium.isVisible(this);
        }

        public Icon getLeftIcon() {
            return new Icon(leftIcon.getReferenced());
        }

        public Icon getRightIcon() {
            return new Icon(rightIcon.getReferenced());
        }

        public Label getLabel() {
            return new Label(label.getReferenced());
        }

        public void select() {
            if (itemMode == null) {
                selenium.click(label);
            } else {
                new GuardRequest(getRequestTypeForMode(itemMode)) {
                    public void command() {
                        selenium.click(label);
                    }
                }.waitRequest();
            }
        }

        public void hover() {
            selenium.mouseOver(this);
        }

        public class Icon extends PanelMenu.Icon {

            public Icon(JQueryLocator root) {
                super(root);
            }

            @Override
            public ElementLocator<JQueryLocator> getIcon() {
                return this;
            }
        }
    }

    public abstract class Icon extends AbstractModel<JQueryLocator> {

        ReferencedLocator<JQueryLocator> img = ref(root, "> img");
        AttributeLocator<?> imgSrc = img.getAttribute(SRC);

        public Icon(JQueryLocator root) {
            super(root);
        }

        public abstract ElementLocator<JQueryLocator> getIcon();

        public boolean isTransparent() {
            return selenium.getAttribute(getIcon().getAttribute(CLASS)).contains("-transparent");
        }

        public boolean containsClass(String styleClass) {
            return selenium.getAttribute(getIcon().getAttribute(CLASS)).contains(styleClass);
        }

        public boolean isCustomURL() {
            return selenium.isElementPresent(img);
        }

        public String getCustomURL() {
            return selenium.getAttribute(imgSrc);
        }
    }

    public class Label extends AbstractModel<JQueryLocator> {

        public Label(JQueryLocator root) {
            super(root);
        }

    }

    public static RequestType getRequestTypeForMode(PanelMenuMode mode) {
        switch (mode) {
            case ajax:
                return RequestType.XHR;
            case server:
                return RequestType.HTTP;
            default:
                return RequestType.NONE;
        }
    }

}