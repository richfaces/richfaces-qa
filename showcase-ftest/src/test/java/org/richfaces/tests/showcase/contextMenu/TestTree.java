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
package org.richfaces.tests.showcase.contextMenu;

import static org.jboss.arquillian.graphene.Graphene.element;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.contextMenu.page.TableContextMenuPage;
import org.richfaces.tests.showcase.contextMenu.page.TreeContextMenuPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestTree extends AbstractContextMenuTest<TreeContextMenuPage> {

    @Test
    public void testViewNodesInfoByCtxMenu() {
        testPage.expandNodes(4);

        int counter = 0;
        for (WebElement leaf : testPage.getLeaves()) {
            if (counter == TestTable.NUMBER_OF_LINES_TO_TEST_ON) {
                break;
            }

            String artistFromTree = leaf.getText();

            leaf.click();
            waitGui().withTimeout(3, TimeUnit.SECONDS).until(testPage.getExpextedConditionOnNodeSelected(leaf));
            waitGui();

            testPage.getContextMenu().selectFromContextMenu(TableContextMenuPage.VIEW, leaf);
            waitGui().withTimeout(3, TimeUnit.SECONDS).until(element(testPage.getArtistFromPopup()).isVisible());

            String artistFromPopup = testPage.getArtistFromPopup().getText();

            assertTrue(artistFromTree.contains(artistFromPopup),
                "The context menu was not invoked correctly! The popup contains different artist name than the node in the tree!");

            testPage.getCloseButton().click();
            waitGui().withTimeout(3, TimeUnit.SECONDS).until(element(testPage.getArtistFromPopup()).not().isVisible());
            counter++;
        }
    }

    @Test
    public void testContextMenuRenderedAtCorrectPosition() {
        testPage.expandNodes(4);
        WebElement elementToTryOn = testPage.getLeaves().get(0);
        Graphene.waitGui().withTimeout(2, TimeUnit.SECONDS).until(element(elementToTryOn).isVisible());

        checkContextMenuRenderedAtCorrectPosition(elementToTryOn, testPage.getContextMenu().getContextMenuPopup(),
            InvocationType.RIGHT_CLICK, testPage.getExpextedConditionOnNodeSelected(elementToTryOn));
    }
}
