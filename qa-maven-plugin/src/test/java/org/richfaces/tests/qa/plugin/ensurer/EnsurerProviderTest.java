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
package org.richfaces.tests.qa.plugin.ensurer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.richfaces.tests.qa.plugin.ensurer.browser.BrowserEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.eap.EAPEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.taskkill.TaskKillEnsurer;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class EnsurerProviderTest {

    @Mock
    private BrowserEnsurer browserEnsurer;
    @Mock
    private EAPEnsurer eapEnsurer;
    private Collection<Ensurer> ensurers;
    @Mock
    private Log log;
    @Mock
    private PropertiesProvider pp;
    private SimpleEnsurersProvider provider;
    @Mock
    private TaskKillEnsurer tke;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        when(pp.getLog()).thenReturn(log);
        provider = new SimpleEnsurersProvider(browserEnsurer, eapEnsurer, pp, tke);
    }

    @Test
    public void testGet_allEnsurersDisabled_returnsNoEnsurersLogs3Messages() {
        when(pp.isEnsureBrowser()).thenReturn(false);
        when(pp.isEnsureEAP()).thenReturn(false);
        when(pp.isEnsureTasksCleanup()).thenReturn(false);

        assertEquals(0, provider.get().size());
        verify(log, Mockito.times(3)).info(Matchers.any(String.class));// 3 messages should be logged
    }

    @Test
    public void testGet_allEnsurersEnabledButAllProvidersReturnNull_returnsNoEnsurersLogs3Messages() {
        when(pp.isEnsureBrowser()).thenReturn(true);
        when(pp.isEnsureEAP()).thenReturn(true);
        when(pp.isEnsureTasksCleanup()).thenReturn(true);

        provider = new SimpleEnsurersProvider(null, null, pp, null);

        assertEquals(0, provider.get().size());
        verify(log, Mockito.times(3)).info(Matchers.any(String.class));// 3 messages should be logged
    }

    @Test
    public void testGet_allEnsurersEnabled_returnsEnsurersLogs0Messages() {
        when(pp.isEnsureBrowser()).thenReturn(true);
        when(pp.isEnsureEAP()).thenReturn(true);
        when(pp.isEnsureTasksCleanup()).thenReturn(true);

        assertEquals(3, provider.get().size());
        assertTrue(provider.get().contains(browserEnsurer));
        assertTrue(provider.get().contains(eapEnsurer));
        assertTrue(provider.get().contains(tke));
        verify(log, Mockito.times(0)).info(Matchers.any(String.class));// no messages should be logged
    }

    @Test
    public void testGet_disabledBrowserEnsuring_returns2EnsurersLogs1Message() {
        when(pp.isEnsureBrowser()).thenReturn(false);
        when(pp.isEnsureEAP()).thenReturn(true);
        when(pp.isEnsureTasksCleanup()).thenReturn(true);

        ensurers = provider.get();
        assertEquals(2, ensurers.size());
        assertFalse(ensurers.contains(browserEnsurer));
        assertTrue(ensurers.contains(eapEnsurer));
        assertTrue(ensurers.contains(tke));
        verify(log, Mockito.times(1)).info(Matchers.any(String.class));// 1 message should be logged

    }

    @Test
    public void testGet_disabledEAPEnsuring_returns2EnsurersLogs1Message() {
        when(pp.isEnsureBrowser()).thenReturn(true);
        when(pp.isEnsureEAP()).thenReturn(false);
        when(pp.isEnsureTasksCleanup()).thenReturn(true);

        ensurers = provider.get();
        assertEquals(2, ensurers.size());
        assertTrue(ensurers.contains(browserEnsurer));
        assertFalse(ensurers.contains(eapEnsurer));
        assertTrue(ensurers.contains(tke));
        verify(log, Mockito.times(1)).info(Matchers.any(String.class));// 1 message should be logged
    }

    @Test
    public void testGet_disabledTasksCleanupEnsuring_returns2EnsurersLogs1Message() {
        when(pp.isEnsureBrowser()).thenReturn(true);
        when(pp.isEnsureEAP()).thenReturn(true);
        when(pp.isEnsureTasksCleanup()).thenReturn(false);

        ensurers = provider.get();
        assertEquals(2, ensurers.size());
        assertTrue(ensurers.contains(browserEnsurer));
        assertTrue(ensurers.contains(eapEnsurer));
        assertFalse(ensurers.contains(tke));
        verify(log, Mockito.times(1)).info(Matchers.any(String.class));// 1 message should be logged
    }
}
