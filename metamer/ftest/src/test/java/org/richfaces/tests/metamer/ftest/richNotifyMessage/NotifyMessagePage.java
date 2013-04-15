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
package org.richfaces.tests.metamer.ftest.richNotifyMessage;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.message.MessageComponentTestPage;
import org.richfaces.tests.page.fragments.impl.notify.NotifyMessage;
import org.richfaces.tests.page.fragments.impl.notify.RichFacesNotify;
import org.richfaces.tests.page.fragments.impl.notify.RichFacesNotifyMessage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class NotifyMessagePage extends MessageComponentTestPage<NotifyMessage> {

    /**
     * Since the NotifyMessage component does not contain for which input it was created,
     * each message component has its own styleClass in the sample.
     */
    @FindBy(css = ".rf-ntf.forFirstInput")
    private RichFacesNotifyMessage messageComponentForFirstInput;
    @FindBy(css = ".rf-ntf.forSecondInput")
    private RichFacesNotifyMessage messageComponentForSecondInput;
    @FindBy(css = ".rf-ntf.forSelectableInput")
    private RichFacesNotifyMessage messageComponentForSelectableInput;
    @FindBy(tagName = "body")
    private RichFacesNotify globalNotify;

    public RichFacesNotify getGlobalNotify() {
        return globalNotify;
    }

    @Override
    public NotifyMessage getMessageComponentForFirstInput() {
        return messageComponentForFirstInput;
    }

    @Override
    public NotifyMessage getMessageComponentForSecondInput() {
        return messageComponentForSecondInput;
    }

    @Override
    public NotifyMessage getMessageComponentForSelectableInput() {
        return messageComponentForSelectableInput;
    }
}
