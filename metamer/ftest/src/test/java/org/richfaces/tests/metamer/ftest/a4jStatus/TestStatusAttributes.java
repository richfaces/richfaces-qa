/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.a4jStatus;

import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestStatusAttributes extends AbstractStatusTest {

    private static final String ERRORTEXT = "error text";
    private static final String STOPTEXT = "stop text";
    private static final String STARTTEXT = "start text";

    @Override
    public String getComponentTestPagePath() {
        return "a4jStatus/withoutFacets.xhtml";
    }

    @Override
    public void setupDelay() {
    }

    @Test
    @CoversAttributes("errorStyle")
    @Templates("plain")
    public void testErrorStyle() {
        testStyle(getStatus().advanced().getErrorElement(), BasicAttributes.errorStyle);
    }

    @Test
    @CoversAttributes("errorStyleClass")
    @Templates("plain")
    public void testErrorStyleClass() {
        testStyleClass(getStatus().advanced().getErrorElement(), BasicAttributes.errorStyleClass);
    }

    @Test
    @CoversAttributes({ "startText", "errorText" })
    @Templates("plain")
    public void testErrorText() {
        getStatusAttributes().set(StatusAttributes.startText, STARTTEXT);
        getStatusAttributes().set(StatusAttributes.errorText, ERRORTEXT);
        super.setupDelay();
        checkStatus(getButtonError(), STARTTEXT, ERRORTEXT);
    }

    @Test
    @CoversAttributes({ "startText", "stopText" })
    @Templates("plain")
    public void testStartAndStopText() {
        getStatusAttributes().set(StatusAttributes.startText, STARTTEXT);
        getStatusAttributes().set(StatusAttributes.stopText, STOPTEXT);
        super.setupDelay();
        checkStatus(getButton1(), STARTTEXT, STOPTEXT);
    }

    @Test
    @CoversAttributes("startStyle")
    @Templates("plain")
    public void testStartStyle() {
        testStyle(getStatus().advanced().getStartElement(), BasicAttributes.startStyle);
    }

    @Test
    @CoversAttributes("startStyleClass")
    @Templates("plain")
    public void testStartStyleClass() {
        testStyleClass(getStatus().advanced().getStartElement(), BasicAttributes.startStyleClass);
    }

    @Test
    @CoversAttributes("stopStyle")
    @Templates("plain")
    public void testStopStyle() {
        testStyle(getStatus().advanced().getStopElement(), BasicAttributes.stopStyle);
    }

    @Test
    @CoversAttributes("stopStyleClass")
    @Templates("plain")
    public void testStopStyleClass() {
        testStyleClass(getStatus().advanced().getStopElement(), BasicAttributes.stopStyleClass);
    }
}
