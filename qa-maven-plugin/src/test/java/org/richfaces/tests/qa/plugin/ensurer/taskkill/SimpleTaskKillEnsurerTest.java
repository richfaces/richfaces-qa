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
package org.richfaces.tests.qa.plugin.ensurer.taskkill;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.richfaces.tests.qa.plugin.ensurer.taskkill.killer.TaskKiller;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimpleTaskKillEnsurerTest {

    private SimpleTaskKillEnsurer ensurer;
    @Mock
    private Log log;
    @Mock
    private PropertiesProvider pp;
    @Mock
    private TaskKiller tk;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(pp.getLog()).thenReturn(log);
        ensurer = new SimpleTaskKillEnsurer(pp, tk);
    }

    @Test
    public void testEnsure_whenASProfileActivated_cleanupProcesses() {
        when(pp.isJBossASProfileActivated()).thenReturn(true);
        when(pp.isGlassFishProfileActivated()).thenReturn(false);
        when(pp.isTomcatProfileActivated()).thenReturn(false);
        when(pp.isRemoteProfileActivated()).thenReturn(false);

        ensurer.ensure();
        verify(tk, Mockito.times(1)).killJBossAS();
        verify(tk, Mockito.times(1)).killChromeDriver();
        verify(tk, Mockito.times(1)).killIEDriver();
        verify(tk, Mockito.times(1)).killTomcat();
        verify(tk, Mockito.times(1)).killGlassFish();
    }

    @Test
    public void testEnsure_whenGlassFishProfileActivated_cleanupProcesses() {
        when(pp.isJBossASProfileActivated()).thenReturn(false);
        when(pp.isGlassFishProfileActivated()).thenReturn(true);
        when(pp.isTomcatProfileActivated()).thenReturn(false);
        when(pp.isRemoteProfileActivated()).thenReturn(false);

        ensurer.ensure();
        verify(tk, Mockito.times(1)).killJBossAS();
        verify(tk, Mockito.times(1)).killChromeDriver();
        verify(tk, Mockito.times(1)).killIEDriver();
        verify(tk, Mockito.times(1)).killTomcat();
        verify(tk, Mockito.times(1)).killGlassFish();
    }

    @Test
    public void testEnsure_whenNoContainerProfileActivated_skipCleanupAndLogMessage() {
        when(pp.isJBossASProfileActivated()).thenReturn(false);
        when(pp.isGlassFishProfileActivated()).thenReturn(false);
        when(pp.isTomcatProfileActivated()).thenReturn(false);

        ensurer.ensure();
        verify(tk, Mockito.times(0)).execute(Matchers.any(String.class));
        verify(log, Mockito.times(1)).info(Matchers.any(String.class));
    }

    @Test
    public void testEnsure_whenRemoteASProfileActivated_cleanupOtherProcesses() {
        when(pp.isJBossASProfileActivated()).thenReturn(true);
        when(pp.isGlassFishProfileActivated()).thenReturn(false);
        when(pp.isTomcatProfileActivated()).thenReturn(false);
        when(pp.isRemoteProfileActivated()).thenReturn(true);

        ensurer.ensure();
        verify(tk, Mockito.times(0)).killJBossAS();
        verify(tk, Mockito.times(1)).killChromeDriver();
        verify(tk, Mockito.times(1)).killIEDriver();
        verify(tk, Mockito.times(1)).killTomcat();
        verify(tk, Mockito.times(1)).killGlassFish();
    }

    @Test
    public void testEnsure_whenRemoteGlassFishProfileActivated_cleanupOtherProcesses() {
        when(pp.isJBossASProfileActivated()).thenReturn(false);
        when(pp.isGlassFishProfileActivated()).thenReturn(true);
        when(pp.isTomcatProfileActivated()).thenReturn(false);
        when(pp.isRemoteProfileActivated()).thenReturn(true);

        ensurer.ensure();
        verify(tk, Mockito.times(1)).killJBossAS();
        verify(tk, Mockito.times(1)).killChromeDriver();
        verify(tk, Mockito.times(1)).killIEDriver();
        verify(tk, Mockito.times(1)).killTomcat();
        verify(tk, Mockito.times(0)).killGlassFish();
    }

    @Test
    public void testEnsure_whenRemoteTomcatProfileActivated_cleanupOtherProcesses() {
        when(pp.isJBossASProfileActivated()).thenReturn(false);
        when(pp.isGlassFishProfileActivated()).thenReturn(false);
        when(pp.isTomcatProfileActivated()).thenReturn(true);
        when(pp.isRemoteProfileActivated()).thenReturn(true);

        ensurer.ensure();
        verify(tk, Mockito.times(1)).killJBossAS();
        verify(tk, Mockito.times(1)).killChromeDriver();
        verify(tk, Mockito.times(1)).killIEDriver();
        verify(tk, Mockito.times(0)).killTomcat();
        verify(tk, Mockito.times(1)).killGlassFish();
    }

    @Test
    public void testEnsure_whenTomcatProfileActivated_cleanupProcesses() {
        when(pp.isJBossASProfileActivated()).thenReturn(false);
        when(pp.isGlassFishProfileActivated()).thenReturn(false);
        when(pp.isTomcatProfileActivated()).thenReturn(true);
        when(pp.isRemoteProfileActivated()).thenReturn(false);

        ensurer.ensure();
        verify(tk, Mockito.times(1)).killJBossAS();
        verify(tk, Mockito.times(1)).killChromeDriver();
        verify(tk, Mockito.times(1)).killIEDriver();
        verify(tk, Mockito.times(1)).killTomcat();
        verify(tk, Mockito.times(1)).killGlassFish();
    }
}
