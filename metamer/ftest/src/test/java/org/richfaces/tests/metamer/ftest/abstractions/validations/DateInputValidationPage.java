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
package org.richfaces.tests.metamer.ftest.abstractions.validations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.bean.abstractions.DateInputValidationBean;
import org.richfaces.tests.page.fragments.impl.message.RichFacesMessage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class DateInputValidationPage extends InputValidationPage {

    public static final String MESSAGE_PAST_NAME = "messagePast";
    public static final String MESSAGE_FUTURE_NAME = "messageFuture";
    public static final String MESSAGE_LAST_YEAR_NAME = "messageLastYear";
    public static final String[] ALL_MESSAGE_CASES = { MESSAGE_PAST_NAME,
        MESSAGE_FUTURE_NAME, MESSAGE_LAST_YEAR_NAME };
    //
    @FindBy(css = "span[id$=pastMsg]")
    private RichFacesMessage messagePast;
    @FindBy(css = "span[id$=futureMsg] ")
    private RichFacesMessage messageFuture;
    @FindBy(css = "span[id$=lastYearMsg]")
    private RichFacesMessage messageLastYear;
    //
    @FindBy(css = "input[id$=pastCorrect]")
    private WebElement setPastCorrectButton;
    @FindBy(css = "input[id$=pastWrong]")
    private WebElement setPastWrongButton;
    @FindBy(css = "input[id$=futureCorrect]")
    private WebElement setFutureCorrectButton;
    @FindBy(css = "input[id$=futureWrong]")
    private WebElement setFutureWrongButton;
    @FindBy(css = "input[id$=lastYearCorrect]")
    private WebElement setLastYearCorrectButton;
    @FindBy(css = "input[id$=lastYearWrong]")
    private WebElement setLastYearWrongButton;
    //
    @FindBy(css = "span[id$=lastYearOutput]")
    private WebElement lastYearOutput;
    @FindBy(css = "span[id$=pastOutput]")
    private WebElement pastOutput;
    @FindBy(css = "span[id$=futureOutput]")
    private WebElement futureOutput;

    @Override
    protected List<WebElement> getAllCorrectButtons() {
        return Lists.newArrayList(setLastYearCorrectButton, setFutureCorrectButton, setPastCorrectButton);
    }

    @Override
    protected List<WebElement> getAllWrongButtons() {
        return Lists.newArrayList(setLastYearWrongButton, setFutureWrongButton, setPastWrongButton);
    }

    @Override
    protected void initCustomMessages() {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM d, yyyy HH:mm");
        messageCases.put(MESSAGE_PAST_NAME,
                new ValidationMessageCase(MESSAGE_PAST_NAME, messagePast,
                setPastCorrectButton, setPastWrongButton,
                pastOutput, "Jan 2, 1980 12:00", DateInputValidationBean.PAST_VALUE_DEFAULT.minusHours(1).toString(dtf),
                Sets.newHashSet(DateInputValidationBean.PAST_VALIDATION_MSG)));
        messageCases.put(MESSAGE_FUTURE_NAME,
                new ValidationMessageCase(MESSAGE_FUTURE_NAME, messageFuture,
                setFutureCorrectButton, setFutureWrongButton,
                futureOutput, "Jan 2, 3000 12:00", DateInputValidationBean.FUTURE_VALUE_DEFAULT.minusHours(1).toString(dtf),
                Sets.newHashSet(DateInputValidationBean.FUTURE_VALIDATION_MSG)));
        DateTime lastYear = new DateTime().minusYears(1).withMonthOfYear(1).withDayOfMonth(2).withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
        messageCases.put(MESSAGE_LAST_YEAR_NAME,
                new ValidationMessageCase(MESSAGE_LAST_YEAR_NAME, messageLastYear,
                setLastYearCorrectButton, setLastYearWrongButton,
                lastYearOutput, lastYear.toString(dtf), DateInputValidationBean.LAST_YEAR_VALUE_DEFAULT.minusHours(1).toString(dtf),
                Sets.newHashSet(DateInputValidationBean.LAST_YEAR_VALIDATION_MSG)));
    }
}
