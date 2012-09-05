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
package org.richfaces.tests.metamer.ftest.a4jAjax;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jAjax/hSelectManyMenu.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public class TestHSelectManyMenu extends AbstractTestSelectMany {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jAjax/hSelectManyMenu.xhtml");
    }

    @Test
    public void testSimpleClick() {
        testClick(page.selectManyMenu);
    }

    @Test
    public void testBypassUpdates() {
        testBypassUpdates(page.selectManyMenu);
    }

    @Test
    public void testData() {
        testData(page.selectManyMenu);
    }

    @Test
    public void testDisabled() {
        testDisabled(page.selectManyMenu);
    }

    @Test
    public void testExecute() {
        testExecute(page.selectManyMenu);
    }

    @Test
    public void testImmediate() {
        testImmediate(page.selectManyMenu);
    }

    @Test
    public void testImmediateBypassUpdates() {
        testImmediateBypassUpdates(page.selectManyMenu);
    }

    @Test
    public void testLimitRender() {
        testLimitRender(page.selectManyMenu);
    }

    @Test
    public void testEvents() {
        testEvents(page.selectManyMenu);
    }

    @Test
    public void testRender() {
        testRender(page.selectManyMenu);
    }

    @Test
    public void testStatus() {
        testStatus(page.selectManyMenu);
    }
}
