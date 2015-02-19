/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richColumn;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;
import java.text.MessageFormat;

import org.richfaces.tests.metamer.bean.issues.RF13978;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF13978 extends AbstractWebDriverTest {

    private static final String UNEXPECTED_MESSAGE = RF13978.MESSAGE.substring(2);

    private String page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, MessageFormat.format("faces/components/richColumn/rf-13978.xhtml", page));
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13978")
    public void testUnrenderedDataTableIsNotVisited() {
        getMetamerPage().assertNoListener(UNEXPECTED_MESSAGE);
    }
}
