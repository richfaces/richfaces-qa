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
package org.richfaces.tests.showcase.poll;


import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.jboss.arquillian.ajocado.Ajocado.waitAjax;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.richfaces.tests.showcase.AbstractShowcaseTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestPoll extends AbstractShowcaseTest {
	
	/* *******************************************************************************************************
	 * Locators
	 * ******************************************************************
	 * *************************************
	 */
	
	protected JQueryLocator serverDate = jq("span[id$=serverDate]");
	protected JQueryLocator button = jq("input[type=submit]");
	
	/* ********************************************************************************************************
	 * Tests
	 * *********************************************************************
	 * ***********************************
	 */
	
	@Test
	public void testPooling() {
		
		List<Integer> deviations = new ArrayList<Integer>();
		
		TextRetriever dateRetriever = retrieveText.locator(serverDate);
		dateRetriever.initializeValue();
	
		//first deviation
		GregorianCalendar calendarInitial = initializeCalendarFromDateRetriever(dateRetriever);
		
		waitAjax.waitForChangeAndReturn(dateRetriever);
		
		GregorianCalendar calendarAfterPool = initializeCalendarFromDateRetriever(dateRetriever);
		
		assertTrue(calendarAfterPool.after(calendarInitial), "The time which was returned after pool is not after initial time");
		
		Integer deviation = computeDeviation(calendarInitial, calendarAfterPool);
		assertTrue(deviation > 0, "There is too big deviation between two poolings");
		deviations.add(deviation);
		
		//second deviation
		calendarInitial = initializeCalendarFromDateRetriever(dateRetriever);
		
		waitAjax.waitForChangeAndReturn(dateRetriever);
		
		calendarAfterPool = initializeCalendarFromDateRetriever(dateRetriever);
		
		assertTrue(calendarAfterPool.after(calendarInitial), "The time which was returned after pool is not after initial time");
		
		deviation = computeDeviation(calendarInitial, calendarAfterPool);
		assertTrue(deviation > 0, "There is too big deviation between two poolings");
		deviations.add(deviation);
		
		//third deviation
		calendarInitial = initializeCalendarFromDateRetriever(dateRetriever);
		
		waitAjax.waitForChangeAndReturn(dateRetriever);
		
		calendarAfterPool = initializeCalendarFromDateRetriever(dateRetriever);
		
		assertTrue(calendarAfterPool.after(calendarInitial), "The time which was returned after pool is not after initial time");
		
		deviation = computeDeviation(calendarInitial, calendarAfterPool);
		assertTrue(deviation > 0, "There is too big deviation between two poolings");
		deviations.add(deviation);
		
		//fourth deviation
		calendarInitial = initializeCalendarFromDateRetriever(dateRetriever);
		
		waitAjax.waitForChangeAndReturn(dateRetriever);
		
		calendarAfterPool = initializeCalendarFromDateRetriever(dateRetriever);
		
		assertTrue(calendarAfterPool.after(calendarInitial), "The time which was returned after pool is not after initial time");
		
		deviation = computeDeviation(calendarInitial, calendarAfterPool);
		assertTrue(deviation > 0, "There is too big deviation between two poolings");
		deviations.add(deviation);
		
		//fifth deviation
		calendarInitial = initializeCalendarFromDateRetriever(dateRetriever);
		
		waitAjax.waitForChangeAndReturn(dateRetriever);
		
		calendarAfterPool = initializeCalendarFromDateRetriever(dateRetriever);
		
		assertTrue(calendarAfterPool.after(calendarInitial), "The time which was returned after pool is not after initial time");
		
		deviation = computeDeviation(calendarInitial, calendarAfterPool);
		assertTrue(deviation > 0, "There is too big deviation between two poolings");
		deviations.add(deviation);
		
		Collections.sort(deviations);
		assertFalse(deviations.get(2) > 3, "The median of deviations is bigger than 3");
		
		
		
	}
	
	/* ********************************************************************************************************
	 * Help methods
	 * *********************************************************************
	 * ***********************************
	 */
	
	/**
	 * Initialize GregorianCalendar with time which is give from dateRetriever
	 * 
	 * @param dateRetriever TextRetriever from which the calendar with specific time will be inicialized
	 */
	private GregorianCalendar initializeCalendarFromDateRetriever(TextRetriever dateRetriever) {
		
		String[] serverDateParsed = dateRetriever.getValue().split(":");  
		
		String hours = serverDateParsed[1].substring(serverDateParsed[1].length() - 2, serverDateParsed[1].length());
		String minutes = serverDateParsed[2];
		String seconds = serverDateParsed[3].substring(0, 2);
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(Time.valueOf(hours + ":" + minutes + ":" + seconds));
		
		return calendar;
		
	}
	
	/**
	 * Computes deviation between two times, consider possibility of minute changing
	 * 
	 * @param calendarInitial the calendar with specific time which was before calendarAfterPool
	 * @param calendarAferPool the calendar with specific time which was after calendarInitial
	 */
	private Integer computeDeviation(GregorianCalendar calendarInitial, GregorianCalendar calendarAfterPool) {
		
		int secondsInitial = calendarInitial.get(Calendar.SECOND);
		int secondsAfterPool = calendarAfterPool.get(Calendar.SECOND);
		
		//if there is more than one minute or one hour deviation return special value -1
		if( (calendarAfterPool.get(Calendar.MINUTE) - calendarInitial.get(Calendar.MINUTE)) >= 2 ) {
			return -1;
		}
		
		if( (calendarAfterPool.get(Calendar.HOUR) - calendarInitial.get(Calendar.HOUR)) >= 2 ) {
			return -1;
		}
		
		if( secondsInitial < 58 ) {
			 
			if ( secondsAfterPool < 59 ) {
				return secondsAfterPool - secondsInitial;
			} else {
				return secondsAfterPool + (60 - secondsInitial);
			}
		
		} else {
			return secondsAfterPool + 2;
		}
		
	}

}
