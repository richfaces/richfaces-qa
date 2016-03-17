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
package org.richfaces.tests.qa.plugin.ensurer.taskkill.builder;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class KillCommandBuilderProviderTest {

    @Mock
    private PropertiesProvider pp;
    @Mock
    private WindowsKillCommandBuilder wkcb;
    @Mock
    private UnixKillCommandBuilder ukcb;
    private KillCommandBuilderProvider provider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        provider = new KillCommandBuilderProvider(pp, wkcb, ukcb);
    }

    @Test
    public void testGet_onWindows_returnsWindowsKCB() {
        when(pp.isOnWindows()).thenReturn(true);
        Assert.assertTrue(provider.get() instanceof WindowsKillCommandBuilder);
    }

    @Test
    public void testGet_notOnWindows_returnsUnixKCB() {
        when(pp.isOnWindows()).thenReturn(false);
        Assert.assertTrue(provider.get() instanceof UnixKillCommandBuilder);
    }

}
