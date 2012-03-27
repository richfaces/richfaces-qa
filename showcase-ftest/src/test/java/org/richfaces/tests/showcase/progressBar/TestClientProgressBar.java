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
package org.richfaces.tests.showcase.progressBar;

import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.retrieveAttribute;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.retrievers.AttributeRetriever;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;
import org.jboss.arquillian.ajocado.waiting.WaitTimeoutException;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestClientProgressBar extends AbstractAjocadoTest {

    /* **************************************************************************
     * Constants****************************************************************************
     */

    private final int MAX_DEVIATION = 2500;
    private final int GRAPHICAL_DEVIATION = 5;

    /* **************************************************************************
     * Fields**************************************************************************
     */

    List<Integer> widths = null;

    /* **************************************************************************
     * Locators**************************************************************************
     */

    protected JQueryLocator progressBar = jq("div.rf-pb-prgs");
    protected JQueryLocator startProgress = jq("input[type=button]:eq(0)");
    protected JQueryLocator pauseProgress = jq("input[type=button]:eq(1)");

    /* **************************************************************************
     * Tests**************************************************************************
     */

    @Test
    public void testClientProgressBarIsRisingGraphically() {

        widths = new ArrayList<Integer>();

        AttributeRetriever styleRetriever = retrieveAttribute.attributeLocator(progressBar
            .getAttribute(Attribute.STYLE));
        styleRetriever.initializeValue();

        guardNoRequest(selenium).click(startProgress);

        while (true) {

            try {

                long currentTimeBeforeChange = System.currentTimeMillis();

                waitGui.waitForChangeAndReturn(styleRetriever);

                long currentTimeAfterChange = System.currentTimeMillis();

                long duration = currentTimeAfterChange - currentTimeBeforeChange;
                if (duration > MAX_DEVIATION) {

                    fail("The graphical rising of progress bar should not take more than " + MAX_DEVIATION + ", and "
                        + "was " + duration);
                }

                getsTheWidthFromRetrieverAndStoresinList(styleRetriever);

                styleRetriever.initializeValue();

            } catch (WaitTimeoutException ex) {
                break;
            }

        }

        checkTheWidthDeviaton();
    }

    /* *******************************************************************************************
     * Help methods********************************************************************************************
     */

    /**
     * Gets the width from the retriever, the value which the retriever holds is in format 'width value%;'
     *
     * @param retriever
     *            the retriever which holds the value
     */
    private void getsTheWidthFromRetrieverAndStoresinList(AttributeRetriever retriever) {

        String widthWithPercentageSign = retriever.getValue();
        String[] partsOfWidthWithPercentageSign = widthWithPercentageSign.split(" ");
        String width = partsOfWidthWithPercentageSign[1].split("%")[0];

        widths.add(Integer.valueOf(width));
    }

    /**
     * Check the width deviation of progress bar
     */
    private void checkTheWidthDeviaton() {

        for (int i = 0; i < widths.size(); i++) {

            if (i == widths.size() - 1) {
                break;
            }

            if ((widths.get(i + 1) - widths.get(i)) != GRAPHICAL_DEVIATION) {

                fail("The graphical deciation should be " + GRAPHICAL_DEVIATION);
            }
        }
    }

}
