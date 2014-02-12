/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.bean.abstractions;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.richfaces.tests.metamer.validator.LastYearValidator;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface DateInputValidationBean extends AttributesHolder {

    DateTime PAST_VALUE_DEFAULT = new DateTime(1980, 1, 1, 12, 0, DateTimeZone.UTC);
    DateTime FUTURE_VALUE_DEFAULT = new DateTime(3000, 1, 1, 12, 0, DateTimeZone.UTC);
    DateTime LAST_YEAR_VALUE_DEFAULT = new DateTime(DateTimeZone.UTC).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).minusYears(1);
    DateTime REQUIRED_VALUE_DEFAULT = PAST_VALUE_DEFAULT;
    //
    Date PAST_DATE_DEFAULT = PAST_VALUE_DEFAULT.toDate();
    Date FUTURE_DATE_DEFAULT = FUTURE_VALUE_DEFAULT.toDate();
    Date LAST_YEAR_DATE_DEFAULT = LAST_YEAR_VALUE_DEFAULT.toDate();
    Date REQUIRED_DATE_DEFAULT = PAST_DATE_DEFAULT;
    //
    String FUTURE_VALIDATION_MSG = "must be in the future";
    String LAST_YEAR_VALIDATION_MSG = LastYearValidator.VALIDATOR_ERROR_MSG;
    String PAST_VALIDATION_MSG = "must be in the past";
    String REQUIRED_VALIDATION_MSG = "value is required";

    Date getLastYearValue();

    @Future(message = FUTURE_VALIDATION_MSG)
    @NotNull
    Date getFutureValue();

    @Past(message = PAST_VALIDATION_MSG)
    @NotNull
    Date getPastValue();

    Date getRequiredValue();

    void setLastYearValue(Date value);

    void setFutureValue(Date value);

    void setPastValue(Date value);

    void setRequiredValue(Date value);
}
