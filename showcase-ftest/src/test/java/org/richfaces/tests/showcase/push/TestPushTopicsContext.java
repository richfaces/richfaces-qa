/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.push;

import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitAjax;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestPushTopicsContext extends AbstractAjocadoTest {

    /* *****************************************************************************
     * Locators ****************************************************************** ***********
     */
    private JQueryLocator uuid = jq("div[id$=uuid]");

    /* *****************************************************************************
     * Tests ********************************************************************* ********
     */
    @Test
    public void testUuidIsChangingInSomeIntervals() {
        TextRetriever uuidRetriever = retrieveText.locator(uuid);
        uuidRetriever.initializeValue();

        checkTheUuid(uuidRetriever);

        List<Long> deviations = new ArrayList<Long>();

        for (int i = 0; i < 20; i++) {

            Long deviation = checkDeviation(uuidRetriever);

            deviations.add(deviation);
        }

        Collections.sort(deviations);

        long median = deviations.get(9);
        assertTrue(((median > 4800) && (median < 5200)),
            "The median of five measurements should be in range of (4800ms, 5200ms)");
    }

    /* *******************************************************************************
     * Help methods ************************************************************** *****************
     */

    /**
     * Checks the deviation between two pushes, also checking that the uuid is changing
     *
     * @param uuidRetriever
     *            retriever which points to the uuid text
     * @return the deviation between two pushes
     */
    private Long checkDeviation(TextRetriever uuidRetriever) {
        Long beforePush = System.currentTimeMillis();

        waitAjax.failWith(new RuntimeException("The uuid is not changing, it is still the same!"))
            .waitForChangeAndReturn(uuidRetriever);

        Long afterPush = System.currentTimeMillis();

        checkTheUuid(uuidRetriever);

        return new Long(afterPush - beforePush);
    }

    /**
     * Check very simply whether uuid is correct, it means whether it has 36 characters and that contains 4 hyphens
     */
    private void checkTheUuid(TextRetriever uuidRetriever) {
        String uuid = uuidRetriever.getValue();

        assertEquals(uuid.length(), 36, "The length of uuid is wrong!");
        assertEquals(StringUtils.countMatches(uuid, "-"), 4, "Wrong uuid, there should be 4 hyphens");
    }
}