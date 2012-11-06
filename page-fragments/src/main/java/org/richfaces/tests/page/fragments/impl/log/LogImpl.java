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
package org.richfaces.tests.page.fragments.impl.log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.joda.time.DateTime;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class LogImpl implements Log {

    @Root
    private WebElement root;

    @FindBy(css = ".rf-log-entry-lbl-debug")
    private List<WebElement> debugEntryTimeStamps;

    @FindBy(css = ".rf-log-entry-msg-debug")
    private List<WebElement> debugEntryContent;

    @FindBy(css = ".rf-log-entry-lbl-info")
    private List<WebElement> infoEntryTimeStamps;

    @FindBy(css = ".rf-log-entry-msg-info")
    private List<WebElement> infoEntryContent;

    @FindBy(css = ".rf-log-entry-lbl-warn")
    private List<WebElement> warnEntryTimeStamps;

    @FindBy(css = ".rf-log-entry-msg-info")
    private List<WebElement> warnEntryContent;

    @FindBy(css = ".rf-log-entry-lbl-error")
    private List<WebElement> errorEntryTimeStamps;

    @FindBy(css = ".rf-log-entry-msg-error")
    private List<WebElement> errorEntryContent;

    @FindBy(tagName = "button")
    private WebElement clearButton;

    @FindBy(tagName = "select")
    private WebElement levelSelect;

    @FindBy(tagName = "option")
    private List<WebElement> levelSelectOptions;

    @Override
    public List<LogEntry> getAllLogEntries() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<LogEntry> getAllDebugEntries() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<LogEntry> getAllInfoEntries() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<LogEntry> getAllWarnEntries() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<LogEntry> getAllErrorEntries() {
        List<LogEntry> errorEntries = new ArrayList<LogEntry>();

        for (int i = 0; i < errorEntryTimeStamps.size(); i++) {
            LogEntry logEntry = new LogEntry();

            logEntry.setLevel(LogEntryLevel.ERROR);
            logEntry.setTimeStamp(parseTimeStamp(errorEntryTimeStamps.get(i).getText()));
            logEntry.setContent(errorEntryContent.get(i).getText());

            errorEntries.add(logEntry);
        }

        return errorEntries;
    }

    private DateTime parseTimeStamp(String text) {
        Date date = null;

        String timeStamp = text.substring(text.indexOf('[') + 1, text.indexOf(']'));
        SimpleDateFormat formatter = new SimpleDateFormat("HH:m:s.S");
        try {
            date = formatter.parse(timeStamp);
        } catch (ParseException e) {
            throw new RuntimeException("Something went wrong with parsing of log entry timestamp!", e);
        }

        return new DateTime(date);
    }

    @Override
    public void clear() {
        clearButton.click();
    }

    @Override
    public void changeLevel(LogEntryLevel lvl) {
        throw new UnsupportedOperationException();
    }

}
