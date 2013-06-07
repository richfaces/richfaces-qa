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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
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
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.richTree.TreeSimplePage;
import org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNode;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22753 $
 */
public class TestTreeModelAdaptorLazyLoading extends AbstractWebDriverTest {

    protected RichFacesTreeNode treeNode;

    protected Integer[][] paths = new Integer[][] {{1, 1, 1, 8, 1}, {4, 4, 11, 4}};

    @Page
    TreeSimplePage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTree/treeAdaptors.xhtml");
    }

    @Test
    public void testLazyLoading() {
        List<String> expected = getListOfVisibleNodes();
        List<String> actual = getLazyInitialized();

        assertEquals(actual, expected);

        for (Integer[] path : paths) {
            treeNode = null;
            for (int i = 0; i < path.length; i++) {
                int index = path[i];
                treeNode = (treeNode == null) ? page.tree.getNodes().get(index - 1) : treeNode.getNode(index - 1);
                if (i < path.length - 1) {
                    treeNode.expand();

                    expected = getListOfVisibleNodes();
                    actual = getLazyInitialized();

                    assertEquals(actual, expected);
                }
            }
        }
    }

    private List<String> getListOfVisibleNodes() {
        // takes only recursive nodes + model node (representing leaves)
        Pattern pattern = Pattern.compile("([RM\\-\\.0-9]+)(?:-.*)?");
        SortedSet<String> result = new TreeSet<String>();
        for (RichFacesTreeNode treeNode : page.tree.getAnyNodes()) {
            String labelText = treeNode.getNodeLabelText();

            Matcher matcher = pattern.matcher(labelText);

            if (matcher.matches()) {
                String node = matcher.group(1);
                result.add(node);
            }
        }
        return new LinkedList<String>(result);
    }

    private List<String> getLazyInitialized() {
        String unseparated = page.lazyInitialized.getText();
        String[] separated = StringUtils.split(unseparated, "[], ");
        List<String> result = Arrays.asList(separated);
        Collections.sort(result);
        return result;
    }
}