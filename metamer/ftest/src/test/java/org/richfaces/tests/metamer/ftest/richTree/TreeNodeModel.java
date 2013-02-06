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
package org.richfaces.tests.metamer.ftest.richTree;

import static org.jboss.arquillian.ajocado.guard.RequestGuardFactory.guard;

import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;

import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;
import org.richfaces.component.SwitchType;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public class TreeNodeModel extends AbstractTreeNodeModel {

    private GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

    private String classNodeExpanded = "rf-tr-nd-exp";
    private String classNodeLeaf = "rf-tr-nd-lf";
    private String classNodeCollapsed = "rf-tr-nd-colps";
    private String classSelected = "rf-trn-sel";

    private ReferencedLocator<JQueryLocator> treeNode = ref(root, "> div.rf-trn");
    private ReferencedLocator<JQueryLocator> handle = ref(treeNode, "> span.rf-trn-hnd");
    private ReferencedLocator<JQueryLocator> content = ref(treeNode, "> span.rf-trn-cnt");
    private ReferencedLocator<JQueryLocator> icon = ref(content, "> .rf-trn-ico");
    private ReferencedLocator<JQueryLocator> label = ref(content, "> .rf-trn-lbl");
    private ReferencedLocator<JQueryLocator> handleLoading = ref(treeNode, "> .rf-trn-hnd-ldn-fct");

    public TreeNodeModel(JQueryLocator root, TreeModel tree) {
        super(root);
        this.tree = tree;
    }

    public ExtendedLocator<JQueryLocator> getTreeNode() {
        return treeNode;
    }

    public TreeNodeHandle getHandle() {
        return new TreeNodeHandle(handle.getReferenced());
    }

    public TreeNodeHandleLoading getHandleLoading() {
        return new TreeNodeHandleLoading(handleLoading.getReferenced());
    }

    public TreeNodeIcon getIcon() {
        return new TreeNodeIcon(icon.getReferenced());
    }

    public ExtendedLocator<JQueryLocator> getLabel() {
        return label;
    }

    public boolean isSelected() {
        return selenium.belongsClass(content, classSelected);
    }

    public boolean isExpanded() {
        return selenium.belongsClass(root.getLocator(), classNodeExpanded);
    }

    public boolean isCollapsed() {
        return selenium.belongsClass(root.getLocator(), classNodeCollapsed);
    }

    public boolean isLeaf() {
        return selenium.belongsClass(root.getLocator(), classNodeLeaf);
    }

    public String getLabelText() {
        return selenium.getText(label);
    }

    public void expand() {
        if (tree.getToggleType() == null) {
            selenium.click(getHandle());
        } else {
            guard(selenium, getRequestType(tree.getToggleType())).click(getHandle());
        }
    }

    public void select() {
        guard(selenium, getRequestType(tree.getSelectionType())).clickAt(getLabel(), new Point(0, 0));
    }

    private RequestType getRequestType(SwitchType switchType) {
        switch (switchType) {
            case ajax:
                return RequestType.XHR;
            case server:
                return RequestType.HTTP;
            default:
                return RequestType.NONE;
        }
    }
}
