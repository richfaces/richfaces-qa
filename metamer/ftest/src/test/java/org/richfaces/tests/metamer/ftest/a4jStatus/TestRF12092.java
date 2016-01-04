/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.utils.ElementVisibilityObserver;
import org.testng.annotations.Test;

/**
 * Test for RF-12092.
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12092 extends AbstractStatusTest {

    private static final int TOLERANCE = 1500;

    @Override
    public String getComponentTestPagePath() {
        return "a4jStatus/RF-12092.xhtml";
    }

    @Override
    public void setupDelay() {
        // do not setup any additional response delay
    }

    @Test
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-12092")
    public void testStatusIsClearedWhenRequestCompleted() {
        // start observing visibility of the STOP element of the a4j:status
        ElementVisibilityObserver evo = ElementVisibilityObserver.getInstance();
        evo.watchForVisibilityChangeOfElement(getStatus().advanced().getStopElement());
        // the page should update after 5 seconds
        String requestTimeBefore = getMetamerPage().getRequestTimeElement().getText();
        Graphene.waitModel().withTimeout(7, TimeUnit.SECONDS).until().element(
            getMetamerPage().getRequestTimeElement()).text().not().equalTo(requestTimeBefore);
        // the tested status text should change back to "Done" after the request is processed and before the next request starts
        getStatus().advanced().waitUntilStatusTextChanges("Done").withTimeout(3, TimeUnit.SECONDS).perform();

        // check the visibility records
        List<ElementVisibilityObserver.Record> records = evo.getRecords();
        assertTrue(records.size() >= 2, "There should be at least 2 records");
        assertFalse(records.get(0).isVisible(), "When the request is beeing processed, the STOP element of the a4j:status should be hidden.");
        assertTrue(records.get(1).isVisible(), "After the ajax request is processed, the STOP element should be visible again.");
        // check that delay between STOP element reappearance is in interval <0,2000> ms
        assertEquals(records.get(1).getTime().getMillis() - records.get(0).getTime().getMillis(), 0, TOLERANCE);
    }
}
