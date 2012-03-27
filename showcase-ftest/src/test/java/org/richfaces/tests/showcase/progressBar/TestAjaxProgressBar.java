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

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.waitAjax;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.fail;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.WaitTimeoutException;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestAjaxProgressBar extends AbstractGrapheneTest {

    /* ***************************************************************************
     * Constants ***************************************************************** **********
     */

    private final int MAX_DEVIATION = 3;

    /* *****************************************************************************
     * Fields ******************************************************************** *********
     */

    private List<Integer> numbersOfProcess = new ArrayList<Integer>();

    /* **************************************************************
     * Locators**************************************************************
     */

    protected JQueryLocator progressBar = jq("div.rf-pb-rmng");
    protected JQueryLocator startProcessButton = jq("input[type=submit]");

    /* *********************************************************************
     * Tests *********************************************************************
     */

    @Test
    public void testProgressBarIsRisingByMax3() {

        TextRetriever processRetriever = retrieveText.locator(progressBar);
        processRetriever.initializeValue();

        guardXhr(selenium).click(startProcessButton);

        while (true) {

            try {
                waitAjax.waitForChangeAndReturn(processRetriever);
                getTheNumberFromTextRetrieverAndSaveToList(processRetriever);

            } catch (WaitTimeoutException ex) {
                break;
            }
        }

        checkTheDeviationInList(MAX_DEVIATION);
    }

    /* *****************************************************************************************************
     * Help methods **************************************************************
     * ***************************************
     */

    /**
     * Gets the number from text retriever, the value in the text retriever is in format 'number %'
     *
     * @param textRetriever
     *            the text retriever which holds the number
     */
    private void getTheNumberFromTextRetrieverAndSaveToList(TextRetriever textRetriever) {

        String valueWithPercentageSign = textRetriever.getValue();
        String[] partsOfvalueWithPercentageSign = valueWithPercentageSign.split(" ");
        String valueWithoutPercentageSign = partsOfvalueWithPercentageSign[0];

        if (!valueWithoutPercentageSign.isEmpty()) {
            numbersOfProcess.add(Integer.valueOf(valueWithoutPercentageSign));
        }

    }

    private void checkTheDeviationInList(int maxDeviation) {

        for (int i = 0; i < numbersOfProcess.size(); i++) {

            if (i == (numbersOfProcess.size() - 1)) {
                break;
            }

            if ((numbersOfProcess.get(i + 1) - numbersOfProcess.get(i)) > maxDeviation) {

                fail("The deviation between each step in the progress should not be higher than " + maxDeviation);
            }

        }
    }

}
