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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.component.SwitchType;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.richTreeNode.TreeNodeAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.tree.RichFacesTree;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractTreeTest extends AbstractWebDriverTest {

    @Page
    protected MetamerPage page;
    @FindBy(css = "div[id$=richTree]")
    protected RichFacesTree tree;

    protected static final String IMAGE_URL = "/resources/images/loading.gif";
    protected final Attributes<TreeAttributes> treeAttributes = getAttributes();
    protected final Attributes<TreeNodeAttributes> firstNodeAttributes = getAttributes("treeNode1Attributes");
    protected final Attributes<TreeNodeAttributes> lastNodeAttributes = getAttributes("treeNode3Attributes");

    protected final Action expandFirstNodeAjaxAction = new Action() {

        @Override
        public void perform() {
            getGuardedTree(SwitchType.ajax).expandNode(0);
        }
    };
    protected final Action collapseFirstNodeAjaxAction = new Action() {

        @Override
        public void perform() {
            getGuardedTree(SwitchType.ajax).collapseNode(0);
        }
    };
    protected final Action selectFirstNodeAjaxAction = new Action() {

        @Override
        public void perform() {
            getGuardedTree(SwitchType.ajax).selectNode(0);
        }
    };
    @Inject
    @Use(strings = { "simpleSwingTreeNode", "simpleRichFacesTreeNode", "simpleRichFacesTreeDataModel" })
    protected String sample;

    protected final String simpleSwingTreeNode = "simpleSwingTreeNode";
    protected final String[] richFacesTreeNodes = { "simpleRichFacesTreeNode", "simpleRichFacesTreeDataModel" };

    protected <T> T getGuarded(T something, SwitchType type) {
        switch (type) {
            case ajax:
                return Graphene.guardAjax(something);
            case client:
                return Graphene.guardNoRequest(something);
            case server:
                return Graphene.guardHttp(something);
        }
        throw new UnsupportedOperationException("Uknown switch " + type);
    }

    protected RichFacesTree getGuardedTree(SwitchType type) {
        return getGuarded(tree, type);
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTree/" + sample + ".xhtml");
    }
}
