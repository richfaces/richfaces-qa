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
package org.richfaces.tests.page.fragments.impl.log;

import java.util.List;

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class RichFacesLog implements Log {

    @Root
    private WebElement root;
    //
    @FindBy(css = "div.rf-log-contents > div")
    private List<RichFacesLogEntry> logEntries;
    @FindBy(xpath = ".//div[span[contains(@class, 'rf-log-entry-lbl-error')]]")
    private List<RichFacesLogEntry> errorLogEntries;
    @FindBy(xpath = ".//div[span[contains(@class, 'rf-log-entry-lbl-warn')]]")
    private List<RichFacesLogEntry> warnLogEntries;
    @FindBy(xpath = ".//div[span[contains(@class, 'rf-log-entry-lbl-info')]]")
    private List<RichFacesLogEntry> infoLogEntries;
    @FindBy(xpath = ".//div[span[contains(@class, 'rf-log-entry-lbl-debug')]]")
    private List<RichFacesLogEntry> debugLogEntries;
    //
    @FindBy(tagName = "button")
    private WebElement clearButton;
    @FindBy(tagName = "select")
    private WebElement levelSelect;

    @Override
    public void clear() {
        clearButton.click();
    }

    @Override
    public void changeLevel(LogEntryLevel level) {
        LogEntryLevel convertedLevel = (level.equals(LogEntryLevel.ALL) ? LogEntryLevel.DEBUG
                : level.equals(LogEntryLevel.FATAL) ? LogEntryLevel.ERROR : level);
        new Select(levelSelect).selectByValue(convertedLevel.getLevelName());
    }

    @Override
    public LogEntries getLogEntries(LogEntryLevel level) {
        switch (level) {
            case DEBUG:
                return new LogEntries(debugLogEntries);
            case INFO:
                return new LogEntries(infoLogEntries);
            case WARN:
                return new LogEntries(warnLogEntries);
            case ERROR:
                return new LogEntries(errorLogEntries);
            case ALL:
                return new LogEntries(logEntries);
            case FATAL:
                return new LogEntries();//empty list
            default:
                throw new UnsupportedOperationException("Unknown level " + level);
        }
    }

    @Override
    public LogEntries getLogEntries(LogFilterBuilder fb) {
        return new LogEntries(logEntries).filter(fb);
    }
}
