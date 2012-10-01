package org.richfaces.tests.page.fragments.ftest.calendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.graphene.component.object.api.calendar.CalendarPopupComponent.CalendarDay;
import org.jboss.arquillian.graphene.component.object.api.calendar.CalendarPopupComponent.CalendarWeek;
import org.jboss.arquillian.graphene.component.object.api.scrolling.ScrollingType;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.ftest.AbstractTest;
import org.richfaces.tests.page.fragments.impl.calendar.CalendarPopupComponentImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestCalendarComponent extends AbstractTest {

    @FindBy(xpath = "//*[contains(@id, 'calendar')]")
    private CalendarPopupComponentImpl calendar;

    private final By CALENDAR_INPUT = By.className("rf-cal-inp");
    // do not forget also to set accordingly the model's date format
    private final String DATE_FORMAT = "dd/M/yyyy hh:mm a";

    private WebElement calendarRoot;

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return createDeployment(TestCalendarComponent.class);
    }

    @BeforeMethod
    public void findCalendarRoot() {
        calendarRoot = webDriver.findElement(By.xpath("//*[contains(@id, 'calendar')]"));
    }

    @Test
    public void testShowCalendar() {

        WebElement popup = calendarRoot.findElement(By.className("rf-cal-day-lbl"));
        assertFalse(popup.isDisplayed(), "The popup should not be displayed yet, since it was not invoked!");

        calendar.showCalendar();

        assertTrue(popup.isDisplayed(), "The popup of the calendar should be displayed!");

        calendar.showCalendar();
        assertTrue(popup.isDisplayed(),
            "The popup of the calendar should be displayed also when the showCalendar was invoked on already shown calendar!");
    }

    @Test
    public void hideCalendar() {

        WebElement popup = calendarRoot.findElement(By.className("rf-cal-day-lbl"));
        assertFalse(popup.isDisplayed(), "The popup should not be displayed yet, since it was not invoked!");

        calendar.hideCalendar();
        assertFalse(popup.isDisplayed(), "The popup should not be displayed yet, since it was not invoked!");

        calendar.showCalendar();
        assertTrue(popup.isDisplayed(), "The popup of the calendar should be displayed!");
        calendar.hideCalendar();
        assertFalse(popup.isDisplayed(), "The popup should not be displayed yet, since it was not invoked!");
    }

//    @Test
//    public void testShowMethodOnInlineCalendar() {
//        calendar.setRoot(webDriver.findElement(By.xpath("//*[contains(@id, 'calendarInline')]")));
//        calendar.showCalendar();
//        // no exception should be thrown
//    }
//
//    @Test
//    public void testHideMethodOnInlineCalendar() {
//        calendar.setRoot(webDriver.findElement(By.xpath("//*[contains(@id, 'calendarInline')]")));
//        try {
//            calendar.hideCalendar();
//            fail("The RuntimeException should be thrown, since invoking hide method on inline calendar!");
//        } catch (RuntimeException ex) {
//            // OK
//        }
//    }

    @Test
    public void testGetDateTime() {
        DateTime dateTime = calendar.getDateTime();
        assertNull(dateTime,
            "The returned dateTime(joda time) should be null, since no dateTime was set before getting of the dateTime");

        DateTime dateTime1 = new DateTime(System.currentTimeMillis());
        calendar.gotoDateTime(dateTime1);

        String inputValue = webDriver.findElement(CALENDAR_INPUT).getText();
        dateTime = DateTime.parse(inputValue);
        assertEquals(dateTime, dateTime1, "The got dateTime(joda time) should be the same as the one which was set!");
    }

    @Test
    public void testGotoDate50YearsInFuture() {
        checkGotoDate(50);
    }

    @Test
    public void testGotoDate50YearsInPast() {
        checkGotoDate(-50);
    }

    /**
     * Checks the the gotoDate
     * 
     * @param yearShift, how is the tested year shifted, when positive number then shifted towards future by amount of this
     *        param, when negative then shifted back to the past
     */
    private void checkGotoDate(int yearShift) {
        Date date1 = new Date(System.currentTimeMillis());
        Calendar cal = new GregorianCalendar();
        cal.setTime(date1);
        cal.roll(Calendar.YEAR, yearShift);
        cal.roll(Calendar.MONTH, false);
        date1 = cal.getTime();
        DateTime dateTime = new DateTime(date1);

        calendar.gotoDateTime(dateTime);

        String inputValue = webDriver.findElement(CALENDAR_INPUT).getAttribute("value");
        Date date2 = getDateFromString(inputValue);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(date2);

        assertEquals(cal2.get(Calendar.MONTH), cal.get(Calendar.MONTH),
            "The got date should be the same as the one which was set!");
        assertEquals(cal2.get(Calendar.YEAR), cal.get(Calendar.YEAR),
            "The got date should be the same as the one which was set!");
    }

    @Test
    public void testGotoDateTime() {
        DateTime dateTime1 = new DateTime();

        calendar.gotoDateTime(dateTime1);

        String inputValue = webDriver.findElement(CALENDAR_INPUT).getText();
        DateTime dateTime2 = DateTime.parse(inputValue);
        assertEquals(dateTime1, dateTime2, "The got dateTime should be the same as the one which was set!");
    }

    @Test
    public void testGotoDateByMouse() {
        Date date1 = new Date(System.currentTimeMillis());
        DateTime dateTime = new DateTime(date1);

        calendar.gotoDateTime(dateTime, ScrollingType.BY_MOUSE);

        String inputValue = webDriver.findElement(CALENDAR_INPUT).getText();
        Date date2 = getDateFromString(inputValue);
        assertEquals(date1, date2, "The got date should be the same as the one which was set!");
    }

    @Test
    public void testGotoDateTimeByMouse() {
        DateTime dateTime1 = new DateTime();

        calendar.gotoDateTime(dateTime1, ScrollingType.BY_MOUSE);

        String inputValue = webDriver.findElement(CALENDAR_INPUT).getText();
        DateTime dateTime2 = DateTime.parse(inputValue);
        assertEquals(dateTime1, dateTime2, "The got dateTime should be the same as the one which was set!");
    }

    @Test
    public void testGotoDateByKeys() {

        try {
            calendar.gotoDateTime(new DateTime(), ScrollingType.BY_KEYS);
            fail("The IllegalArgumentException should be thrown, since RichFaces calendar does not support "
                + ScrollingType.BY_KEYS);
        } catch (IllegalArgumentException ex) {
            // OK
        }
    }

    @Test
    public void testGotoDateTimeByKeys() {

        try {
            calendar.gotoDateTime(new DateTime(), ScrollingType.BY_KEYS);
            fail("The IllegalArgumentException should be thrown, since RichFaces calendar does not support "
                + ScrollingType.BY_KEYS);
        } catch (IllegalArgumentException ex) {
            // OK
        }
    }

    @Test
    public void testGotoNextDay() {
        checkGotoNextMethods(java.util.Calendar.DAY_OF_MONTH);
    }

    @Test
    public void testGotoNextMonth() {
        checkGotoNextMethods(java.util.Calendar.MONTH);
    }

    @Test
    public void testGotoNextYear() {
        checkGotoNextMethods(java.util.Calendar.YEAR);
    }

    @Test
    public void testGotoPreviousDay() {
        checkGotoPreviousMethods(Calendar.DAY_OF_MONTH);
    }

    @Test
    public void testGotoPreviousMonth() {
        checkGotoPreviousMethods(Calendar.MONTH);
    }

    @Test
    public void testGotoPreviousYear() {
        checkGotoPreviousMethods(Calendar.YEAR);
    }

    @Test
    public void testGetDayOfMonth() {
        CalendarDay day = calendar.getDayOfMonth();
        assertNotNull(day, "When no date is set, the returned day should has value of the current date's day!");

        Date date1 = new Date(System.currentTimeMillis());
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(date1);

        assertEquals(day.toInt(), cal1.get(Calendar.DAY_OF_MONTH), "The returned day should be set to the current date's day!");

        int expectedDay = 10;
        cal1.set(2012, 2, expectedDay);
        calendar.gotoDateTime(new DateTime(cal1.getTime()));
        day = calendar.getDayOfMonth();

        assertEquals(day.toInt(), expectedDay, "The returned day is wrong!");
    }

    @Test
    public void testGetSpecificDayOfMonth() {
        int expectedDay = 1;
        CalendarDay day = calendar.getDayOfMonth(expectedDay);
        assertEquals(day.toInt(), expectedDay, "Returned day is wrong!");
    }

    @Test
    public void testGetDaysOfMonth() {
        List<CalendarDay> days = calendar.getDaysOfMonth();
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(new Date(System.currentTimeMillis()));

        assertNotNull(days, "It should not return null, no date was set so it should return all days of current date's month");
        assertTrue(days.size() > 0, "The number of days should be bigger than 0");
        assertEquals(days.size(), cal1.getActualMaximum(Calendar.DAY_OF_MONTH));

        cal1.set(2012, 2, 1);
        calendar.gotoDateTime(new DateTime(cal1.getTime()));

        days = calendar.getDaysOfMonth();
        assertNotNull(days, "It should not return null, it should return number of days of the month in date " + cal1.getTime());
        assertTrue(days.size() > 0, "The number of days should be bigger than 0");
        assertEquals(days.size(), cal1.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testGetWeekOfYear() {
        CalendarWeek week = calendar.getWeekOfYear();
        assertNotNull(week, "It should return the week of the current date, as no date was set!");

        Date date1 = new Date(System.currentTimeMillis());
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(date1);

        assertEquals(week.toInt(), cal1.get(Calendar.WEEK_OF_YEAR),
            "The returned week should be set to the current date's week of the year!");

        int expectedWeek = 1;
        cal1.set(2012, 1, expectedWeek);
        calendar.gotoDateTime(new DateTime(cal1.getTime()));
        week = calendar.getWeekOfYear();

        assertEquals(week.toInt(), expectedWeek, "The returned week is wrong!");
    }

    @Test
    public void testGetSpecificWeekOfYear() {
        CalendarWeek week = calendar.getWeekOfYear(1);
        assertNotNull(week, "The returned week should not be null, it should return the week of the current date!");

        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(new Date(System.currentTimeMillis()));

        assertEquals(week.whichYear(), cal1.get(Calendar.YEAR), "The week is not a part of the correct year!");

        int expectedYear = 2011;
        cal1.set(expectedYear, 1, 1);
        calendar.gotoDateTime(new DateTime(cal1.getTime()));
        week = calendar.getWeekOfYear(1);

        assertEquals(week.whichYear(), expectedYear, "The returned week is not a part of the correct year!");
    }

    @Test
    public void testGetWeeksOfYear() {
        List<CalendarWeek> weeks = calendar.getWeeksOfYear();
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(new Date(System.currentTimeMillis()));

        assertNotNull(weeks,
            "It should not return null, no date was set so it should return all weeks of the current date's month");
        assertTrue(weeks.size() > 0, "The number of days should be bigger than 0");
        assertEquals(weeks.size(), cal1.getActualMaximum(Calendar.WEEK_OF_YEAR));

        cal1.set(2012, 2, 1);
        calendar.gotoDateTime(new DateTime(cal1.getTime()));

        weeks = calendar.getWeeksOfYear();
        assertNotNull(weeks,
            "It should not return null, it should return number of days of the month in date " + cal1.getTime());
        assertTrue(weeks.size() > 0, "The number of days should be bigger than 0");
        assertEquals(weeks.size(), cal1.getActualMaximum(Calendar.WEEK_OF_YEAR));
    }

    // Help Methods //////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks methods for shifting date to the past
     * 
     * @param whatIsShifted can has these values: <code>java.util.Calendar.DAY_OF_MONTH</code>,
     *        <code>java.util.Calendar.MONTH</code>, <code>java.util.Calendar.YEAR</code>
     */
    private void checkGotoPreviousMethods(int whatIsShifted) {
        Date date1 = new Date(System.currentTimeMillis());
        calendar.gotoDateTime(new DateTime(date1));
        java.util.Calendar cal1 = new GregorianCalendar();
        cal1.setTime(date1);

        switch (whatIsShifted) {
            case java.util.Calendar.DAY_OF_MONTH:
                calendar.gotoPreviousDay();
                break;
            case java.util.Calendar.MONTH:
                calendar.gotoPreviousMonth();
                break;
            case java.util.Calendar.YEAR:
                calendar.gotoPreviousYear();
                break;
            default:
                throw new IllegalArgumentException("Wrong value of what will be shifted, it is not a day or month or year");
        }

        Date date2 = calendar.getDateTime().toDate();
        java.util.Calendar cal2 = new GregorianCalendar();
        cal2.setTime(date2);

        assertTrue(cal1.after(cal2), "It was not shifted to the past!");
        switch (whatIsShifted) {
            case java.util.Calendar.DAY_OF_MONTH:
                assertTrue(cal1.get(java.util.Calendar.DAY_OF_MONTH) == (cal2.get(java.util.Calendar.DAY_OF_MONTH) + 1),
                    "It should be shifted by one day to past!");
            case java.util.Calendar.MONTH:
                assertTrue(cal1.get(java.util.Calendar.MONTH) == (cal2.get(java.util.Calendar.MONTH) + 1),
                    "It should be shifted by one month to past!");
            case java.util.Calendar.YEAR:
                assertTrue(cal1.get(java.util.Calendar.YEAR) == (cal2.get(java.util.Calendar.YEAR) + 1),
                    "It should be shifted by one year to past!");
        }
    }

    /**
     * Checks methods for shifting date to the future
     * 
     * @param whatIsShifted can has these values: <code>java.util.Calendar.DAY_OF_MONTH</code>,
     *        <code>java.util.Calendar.MONTH</code>, <code>java.util.Calendar.YEAR</code>
     */
    private void checkGotoNextMethods(int whatIsShifted) {

        Date date1 = new Date(System.currentTimeMillis());
        calendar.gotoDateTime(new DateTime(date1));
        java.util.Calendar cal1 = new GregorianCalendar();
        cal1.setTime(date1);

        switch (whatIsShifted) {
            case java.util.Calendar.DAY_OF_MONTH:
                calendar.gotoNextDay();
                break;
            case java.util.Calendar.MONTH:
                calendar.gotoNextMonth();
                break;
            case java.util.Calendar.YEAR:
                calendar.gotoNextYear();
                break;
            default:
                throw new IllegalArgumentException("Wrong value of what will be shifted, it is not a day or month or year");
        }

        Date date2 = calendar.getDateTime().toDate();
        java.util.Calendar cal2 = new GregorianCalendar();
        cal2.setTime(date2);

        assertTrue(cal1.before(cal2), "It was not shifted to the future!");
        switch (whatIsShifted) {
            case java.util.Calendar.DAY_OF_MONTH:
                assertTrue(cal1.get(java.util.Calendar.DAY_OF_MONTH) == (cal2.get(java.util.Calendar.DAY_OF_MONTH) - 1),
                    "It should be shifted by one day to future!");
            case java.util.Calendar.MONTH:
                assertTrue(cal1.get(java.util.Calendar.MONTH) == (cal2.get(java.util.Calendar.MONTH) - 1),
                    "It should be shifted by one month to future!");
            case java.util.Calendar.YEAR:
                assertTrue(cal1.get(java.util.Calendar.YEAR) == (cal2.get(java.util.Calendar.YEAR) - 1),
                    "It should be shifted by one year to future!");
        }
    }

    /**
     * Parse the <code>string</code> value and returns the Date, the date is in format <code>DATE_FORMAT</code>
     * 
     * @param string
     * @return
     * @throws ParseException
     */
    private Date getDateFromString(String string) {
        if (string.trim().length() == 0) {
            throw new RuntimeException("You want get a date from an empty string!");
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        Date date;
        try {
            date = format.parse(string);
        } catch (ParseException e) {
            throw new RuntimeException("error while parsing the date!");
        }

        return date;
    }
}
