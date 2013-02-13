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

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.log.Log.LogEntryLevel;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesLogEntry implements LogEntry {

    @Root
    private WebElement rootElement;
    @FindBy(css = "span.rf-log-entry-lbl")
    private WebElement labelElement;
    @FindBy(css = "span.rf-log-entry-msg")
    private WebElement messageElement;

    @Override
    public String getContent() {
        return messageElement.getText();
    }

    public WebElement getLabelElement() {
        return labelElement;
    }

    @Override
    public LogEntryLevel getLevel() {
        return RichFacesLogEntryLevel.getLevelFromLabel(labelElement);
    }

    public WebElement getMessageElement() {
        return messageElement;
    }

    public WebElement getRootElement() {
        return rootElement;
    }

    @Override
    public DateTime getTimeStamp() {
        DateTime dt = null;
        String text = labelElement.getText();
        String timeStamp = text.substring(text.indexOf('[') + 1, text.indexOf(']'));
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:m:s.S");
        try {
            dt = formatter.parseDateTime(timeStamp);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Something went wrong with parsing of log entry timestamp!", e);
        }
        return dt;
    }
}
