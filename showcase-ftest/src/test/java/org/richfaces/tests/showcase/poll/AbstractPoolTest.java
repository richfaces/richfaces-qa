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
package org.richfaces.tests.showcase.poll;

import static org.jboss.arquillian.ajocado.Graphene.waitAjax;
import static org.testng.Assert.assertTrue;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.richfaces.tests.showcase.AbstractAjocadoTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class AbstractPoolTest extends AbstractAjocadoTest {

    /**
     * Initialize GregorianCalendar with time which is give from dateRetriever
     *
     * @param dateRetriever
     *            TextRetriever from which the calendar with specific time will be inicialized
     */
    public GregorianCalendar initializeCalendarFromDateRetriever(TextRetriever dateRetriever) {

        String[] serverDateParsed = dateRetriever.getValue().split(":");

        String hours = serverDateParsed[1].substring(serverDateParsed[1].length() - 2, serverDateParsed[1].length());
        String minutes = serverDateParsed[2];
        String seconds = serverDateParsed[3].substring(0, 2);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(Time.valueOf(hours.trim() + ":" + minutes + ":" + seconds));

        return calendar;

    }

    /**
     * Computes deviation between two times, consider possibility of minute changing
     *
     * @param calendarInitial
     *            the calendar with specific time which was before calendarAfterPool
     * @param calendarAferPool
     *            the calendar with specific time which was after calendarInitial
     */
    public Integer computeDeviation(GregorianCalendar calendarInitial, GregorianCalendar calendarAfterServerAction) {

        int secondsInitial = calendarInitial.get(Calendar.SECOND);
        int secondsAfterServerAction = calendarAfterServerAction.get(Calendar.SECOND);

        // if there is more than one minute or one hour or deviation is return
        // error value -1
        if (secondsAfterServerAction == secondsInitial) {

            return -1;
        }

        if ((calendarAfterServerAction.get(Calendar.MINUTE) - calendarInitial.get(Calendar.MINUTE)) >= 2) {
            return -1;
        }

        if ((calendarAfterServerAction.get(Calendar.HOUR) - calendarInitial.get(Calendar.HOUR)) >= 2) {
            return -1;
        }

        int deviation = -1;

        if (secondsAfterServerAction < secondsInitial) {

            deviation = secondsAfterServerAction + (60 - secondsInitial);

        } else {

            deviation = secondsAfterServerAction - secondsInitial;
        }

        return deviation;
    }

    /**
     * Waits for particular server action, gets the deviation between two states(before particular server action and
     * after), checks that the deviation is not zero or bigger than one hour or one minute
     *
     * @param dateRetriever
     *            retriever which points to the server date
     * @return deviation between two states of rendered server date(before particular server action and after)
     */
    public Integer waitForServerActionAndReturnDeviation(TextRetriever dateRetriever, String whatServerAction) {

        GregorianCalendar calendarInitial = initializeCalendarFromDateRetriever(dateRetriever);

        waitAjax.waitForChangeAndReturn(dateRetriever);

        GregorianCalendar calendarAfterPush = initializeCalendarFromDateRetriever(dateRetriever);

        assertTrue(calendarAfterPush.after(calendarInitial), "The time after" + whatServerAction
            + "is before the initial time! You are returning to the past!");

        Integer deviation = computeDeviation(calendarInitial, calendarAfterPush);

        assertTrue(deviation > 0, "Deviaton: " + deviation + " between two " + whatServerAction + "s/es is either too "
            + "big (more than one minute/hour) or too small(zero)");

        return deviation;
    }
}
