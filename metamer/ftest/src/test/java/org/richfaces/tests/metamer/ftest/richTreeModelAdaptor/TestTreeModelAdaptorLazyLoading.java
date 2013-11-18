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
package org.richfaces.tests.metamer.ftest.richTreeModelAdaptor;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.tree.RichFacesTree;
import org.richfaces.fragment.tree.RichFacesTreeNode;
import org.richfaces.fragment.tree.Tree.TreeNode;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class TestTreeModelAdaptorLazyLoading extends AbstractWebDriverTest {

    @Page
    private MetamerPage page;
    @FindBy(css = "div[id$=richTree]")
    private RichFacesTree tree;
    @FindBy(css = "span[id$=lazyInitialized]")
    private WebElement lazyInitialized;
    @FindByJQuery(".rf-tr-nd:visible")
    private List<RichFacesTreeNode> allVisibleNodes;

    private TreeNode treeNode;

    private Integer[][] paths = new Integer[][]{ { 1, 1, 1, 8, 1 }, { 4, 4, 11, 4 } };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTree/treeAdaptors.xhtml");
    }

    @Test
    public void testLazyLoading() {
        assertEquals(getLazyInitialized(), getListOfVisibleNodes());
        for (Integer[] path : paths) {
            treeNode = null;
            for (int i = 0; i < path.length; i++) {
                int index = path[i] - 1;
                treeNode = (treeNode == null ? tree.advanced().getNodes().get(index) : treeNode.advanced().getNodes().get(index));
                if (i < path.length - 1) {
                    Graphene.guardAjax(treeNode.advanced()).expand();
                    assertEquals(getLazyInitialized(), getListOfVisibleNodes());
                }
            }
        }
    }

    private List<String> getListOfVisibleNodes() {
        // takes only recursive nodes + model node (representing leaves)
        Pattern pattern = Pattern.compile("([RM\\-\\.0-9]+)(?:-.*)?");
        SortedSet<String> result = new TreeSet<String>();
        for (TreeNode actTreeNode : allVisibleNodes) {
            String labelText = actTreeNode.advanced().getLabelElement().getText();

            Matcher matcher = pattern.matcher(labelText);

            if (matcher.matches()) {
                String node = matcher.group(1);
                result.add(node);
            }
        }
        return new LinkedList<String>(result);
    }

    private List<String> getLazyInitialized() {
        String unseparated = lazyInitialized.getText();
        String[] separated = StringUtils.split(unseparated, "[], ");
        List<String> result = Arrays.asList(separated);
        Collections.sort(result);
        return result;
    }
}
