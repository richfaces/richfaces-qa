package org.richfaces.tests.page.fragments.impl.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jboss.arquillian.graphene.component.object.api.calendar.CalendarPopupComponent;
import org.jboss.arquillian.graphene.component.object.api.scrolling.ScrollingType;
import org.jboss.arquillian.graphene.fragment.Root;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RichFacesCalendarPopupComponent implements CalendarPopupComponent {

    @Root
    private WebElement root;

    @FindBy(className = "rf-cal-inp")
    private WebElement input;

    @FindBy(css = "td[class=\"rf-cal-hdr-month\"] > div")
    private WebElement showYearAndMonthEditorButton;

    @FindBy(css = "img:nth-of-type(1)")
    private WebElement showCalendarButton;

    @FindBy(className = "rf-cal-day-lbl")
    private WebElement popupWithCalendar;

    @FindBy(css = "div[class=\"rf-cal-time-btn\"]:nth-of-type(1)")
    private WebElement okButton;

    @FindBy(css = "table[class=\"rf-cal-monthpicker-cnt\"] td:nth-of-type(4) > div")
    private WebElement nextDecade;

    @FindBy(css = "table[class=\"rf-cal-monthpicker-cnt\"] td:nth-of-type(3) > div")
    private WebElement previousDecade;

    @FindBy(css = "td[class='rf-cal-tl']:nth-of-type(3)")
    private WebElement nextMonthScroller;

    private final String YEAR_AND_MONTH_LOCATOR_CSS = "div[class=\"rf-cal-edtr-btn\"]";
    private final String DAY_LOCATOR_CLASS = "rf-cal-c";

    /**
     * The format of date displayed on the calendar input, default dd/M/yyhh:mma
     */
    private String dateFormat = "dd/M/yy hh:mm a";

    public WebElement getProxiedRoot() {
        return root;
    }

    @Override
    public void showCalendar() {

        if (!popupWithCalendar.isDisplayed()) {
            showCalendarButton.click();

            long end = System.currentTimeMillis() + 2000;
            while (System.currentTimeMillis() < end) {
                if (popupWithCalendar.isDisplayed()) {
                    break;
                }
            }
        }
    }

    @Override
    public void hideCalendar() {

        showCalendar();
    }

    private void gotoDate(Date date) {
        showCalendar();

        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        int wishedYear = cal.get(Calendar.YEAR);
        // month is indexed from 0!
        int wishedMonth = cal.get(Calendar.MONTH);
        int wishedDay = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(new Date(System.currentTimeMillis()));

        int todayYear = cal.get(Calendar.YEAR);
        int todayMonth = cal.get(Calendar.MONTH);
        // int todayDay = cal.get(Calendar.DAY_OF_MONTH);

        showYearAndMonthEditorButton.click();

        if ((wishedYear != todayYear) || (wishedMonth != todayMonth)) {
            List<WebElement> years;
            String txt;

            if (todayYear > wishedYear) {
                int howManyDecadesLessOrMore = (todayYear - wishedYear) / 10;

                for (int i = 0; i < howManyDecadesLessOrMore; i++) {
                    previousDecade.click();
                }
            }

            if (todayYear < wishedYear) {
                int howManyDecadesLessOrMore = (wishedYear - todayYear) / 10;

                for (int i = 0; i < howManyDecadesLessOrMore; i++) {
                    nextDecade.click();
                }
            }

            selectYear(wishedYear);

            years = root.findElements(By.cssSelector(YEAR_AND_MONTH_LOCATOR_CSS));

            for (WebElement i : years) {
                txt = i.getText().trim();

                if (txt.matches("[a-zA-Z]+?")) {
                    if (txt.equals("Jan") && wishedMonth == 0) {
                        i.click();
                        // break;
                    } else if (txt.equals("Feb") && wishedMonth == 1) {
                        i.click();
                        // break;
                    } else if (txt.equals("Mar") && wishedMonth == 2) {
                        i.click();
                        // break;
                    } else if (txt.equals("Apr") && wishedMonth == 3) {
                        i.click();
                        // break;
                    } else if (txt.equals("May") && wishedMonth == 4) {
                        i.click();
                        // break;
                    } else if (txt.equals("Jun") && wishedMonth == 5) {
                        i.click();
                        // break;
                    } else if (txt.equals("Jul") && wishedMonth == 6) {
                        i.click();
                        // break;
                    } else if (txt.equals("Aug") && wishedMonth == 7) {
                        i.click();
                        // break;
                    } else if (txt.equals("Sep") && wishedMonth == 8) {
                        i.click();
                        // break;
                    } else if (txt.equals("Oct") && wishedMonth == 9) {
                        i.click();
                        // break;
                    } else if (txt.equals("Nov") && wishedMonth == 10) {
                        i.click();
                        // break;
                    } else if (txt.equals("Dec") && wishedMonth == 11) {
                        i.click();
                        // break;
                    }
                }
            }

            okButton.click();
        }

        List<WebElement> days = root.findElements(By.className(DAY_LOCATOR_CLASS));
        String txt;
        for (WebElement i : days) {
            txt = i.getText().trim();
            int day = new Integer(txt);
            if (day == wishedDay) {
                i.click();
                break;
            }
        }
    }

    /**
     * Selects the year on the calendar, note that the month and year editor has to be shown already
     *
     * @param wishedYear the year you want to set
     * @return true if the year was successfully set, false otherwise
     */
    private boolean selectYear(int wishedYear) {
        List<WebElement> years = root.findElements(By.cssSelector(YEAR_AND_MONTH_LOCATOR_CSS));
        String txt;

        for (WebElement i : years) {

            txt = i.getText().trim();
            int year;

            if (txt.matches("\\d+?")) {
                year = new Integer(txt);

                if (wishedYear == year) {
                    i.click();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void gotoDateTime(DateTime dateTime) {
        Date date = dateTime.toDate();
        gotoDate(date);
    }

    @Override
    public void gotoDateTime(DateTime dateTime, ScrollingType type) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    private Date getDate() {
        String dateString = input.getAttribute("value");
        if (dateString.trim().length() == 0) {
            return null;
        }

        if (dateFormat == null) {
            throw new RuntimeException("You have to set date format first to get date!");
        }
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Parse date exception: did you provided the right dateFormat?");
        }

        return date;
    }

    @Override
    public DateTime getDateTime() {
        DateTime dateTime = new DateTime(this.getDate());

        return dateTime;
    }

    @Override
    public CalendarDay gotoNextDay() {
        Date date = getDateTime().toDate();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.roll(Calendar.DAY_OF_MONTH, true);

        gotoDate(cal.getTime());

        // CalendarDay day = new CalendarDayImpl();
        return null;
    }

    @Override
    public CalendarMonth gotoNextMonth() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarYear gotoNextYear() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarDay gotoPreviousDay() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarMonth gotoPreviousMonth() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarYear gotoPreviousYear() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarMonth getMonth() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarMonth getMonth(int month) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CalendarMonth> getMonths() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarYear getYear() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarYear getYear(int year) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CalendarYear> getYears(int from, int to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarDay getDayOfMonth() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarDay getDayOfMonth(int day) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CalendarDay> getDaysOfMonth() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarDay getDayOfWeek() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarDay getDayOfWeek(int day) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CalendarDay> getDaysOfWeek() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarDay getDayOfYear() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarDay getDayOfYear(int day) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CalendarDay> getDaysOfYear() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarWeek getWeekOfMonth() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarWeek getWeekOfMonth(int week) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CalendarWeek> getWeeksOfMonth() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarWeek getWeekOfYear() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarWeek getWeekOfYear(int week) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CalendarWeek> getWeeksOfYear() {
        // TODO Auto-generated method stub
        return null;
    }

    public WebElement getCalendarInput() {
        return input;
    }

    public void setCalendarInput(WebElement calendarInput) {
        this.input = calendarInput;
    }

    public WebElement getPopupWithCalendar() {
        return popupWithCalendar;
    }

    public void setPopupWithCalendar(WebElement popupWithCalendar) {
        this.popupWithCalendar = popupWithCalendar;
    }

    /**
     * @return the showCalendarButton
     */
    public WebElement getShowCalendarButton() {
        return showCalendarButton;
    }

    public String getYearLocator() {
        return YEAR_AND_MONTH_LOCATOR_CSS;
    }

    /**
     * @param showCalendarButton the showCalendarButton to set
     */
    public void setShowCalendarButton(WebElement showCalendarButton) {
        this.showCalendarButton = showCalendarButton;
    }

    public WebElement getOkButton() {
        return okButton;
    }

    public WebElement getNextDecade() {
        return nextDecade;
    }

    public WebElement getPreviousDecade() {
        return previousDecade;
    }

    public String getYEAR_AND_MONTH_LOCATOR_CSS() {
        return YEAR_AND_MONTH_LOCATOR_CSS;
    }

    public String getDAY_LOCATOR_CLASS() {
        return DAY_LOCATOR_CLASS;
    }

    public void setOkButton(WebElement okButton) {
        this.okButton = okButton;
    }

    public void setNextDecade(WebElement nextDecade) {
        this.nextDecade = nextDecade;
    }

    public void setPreviousDecade(WebElement previousDecade) {
        this.previousDecade = previousDecade;
    }

    public WebElement getInput() {
        return input;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setInput(WebElement input) {
        this.input = input;
    }

    public void setDateFormat(String dateFormat) {
        if (dateFormat == null) {
            throw new IllegalArgumentException("You can not set null as dateFormat!");
        }
        this.dateFormat = dateFormat;
    }

    public WebElement getShowYearAndMonthEditorButton() {
        return showYearAndMonthEditorButton;
    }

    public void setShowYearAndMonthEditorButton(WebElement showYearAndMonthEditorButton) {
        this.showYearAndMonthEditorButton = showYearAndMonthEditorButton;
    }

    @Override
    public void clearInput() {
        // TODO Auto-generated method stub

    }

    public WebElement getNextMonthScroller() {
        return nextMonthScroller;
    }

    // public class TimeUnitImpl implements TimeUnit {
    //
    // @Override
    // public int toInt() {
    // // TODO Auto-generated method stub
    // return 0;
    // }
    //
    // }
    //
    // public class CalendarDayImpl implements CalendarDay {
    //
    // @Override
    // public boolean isEnabled() {
    // // TODO Auto-generated method stub
    // return false;
    // }
    //
    // @Override
    // public CalendarMonth whichMonth() {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public CalendarYear whichYear() {
    // // TODO Auto-generated method stub
    // return null;
    // }
    // }
    //
    // public class CalendarWeekImpl implements CalendarWeek {
    //
    // @Override
    // public List<CalendarDay> getDays() {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public CalendarDay getDay(int day) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public CalendarMonth whichMonth() {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public CalendarYear whichYear() {
    // // TODO Auto-generated method stub
    // return null;
    // }
    // }
    //
    // public class CalendarMonthImpl implements CalendarMonth {
    //
    // @Override
    // public int toInt() {
    // // TODO Auto-generated method stub
    // return 0;
    // }
    //
    // @Override
    // public List<CalendarDay> getDays() {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public List<CalendarWeek> getWeeks() {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public CalendarDay getDay(int day) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public CalendarWeek getWeek(int week) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public CalendarYear whichYear() {
    // // TODO Auto-generated method stub
    // return null;
    // }
    // }
    //
    // public class CalendarYearImpl implements CalendarYear {
    //
    // @Override
    // public int toInt() {
    // // TODO Auto-generated method stub
    // return 0;
    // }
    //
    // @Override
    // public List<CalendarDay> getDays() {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public List<CalendarWeek> getWeeks() {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public List<CalendarMonth> getMonths() {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public CalendarDay getDay(int day) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public CalendarWeek getWeek(int week) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public CalendarMonth getMonth(int month) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // }
}
