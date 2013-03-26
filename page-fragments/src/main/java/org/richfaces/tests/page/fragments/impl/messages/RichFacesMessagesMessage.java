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
package org.richfaces.tests.page.fragments.impl.messages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.message.AbstractMessage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
class RichFacesMessagesMessage extends AbstractMessage {

    @FindBy(className = "rf-msgs-det")
    private WebElement messageDetailElement;
    @FindBy(className = "rf-msgs-sum")
    private WebElement messageSummaryElement;

    @Override
    protected String getCssClass(MessageType type) {
        switch (type) {
            case ERROR:
                return "rf-msgs-err";
            case FATAL:
                return "rf-msgs-ftl";
            case INFORMATION:
                return "rf-msgs-inf";
            case OK:
                return "rf-msgs-ok";
            case WARNING:
                return "rf-msgs-wrn";
            default:
                throw new UnsupportedOperationException("Unknown message type " + type);
        }
    }

    @Override
    public WebElement getMessageDetailElement() {
        return messageDetailElement;
    }

    @Override
    public WebElement getMessageSummaryElement() {
        return messageSummaryElement;
    }
}
