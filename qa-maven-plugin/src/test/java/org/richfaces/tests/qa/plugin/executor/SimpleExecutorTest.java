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
package org.richfaces.tests.qa.plugin.executor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.richfaces.tests.qa.plugin.ensurer.SimpleEnsurersProvider;
import org.richfaces.tests.qa.plugin.ensurer.browser.BrowserEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.eap.EAPEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.taskkill.TaskKillEnsurer;

import com.google.common.collect.Lists;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimpleExecutorTest {

    @Mock
    private BrowserEnsurer bEnsurer;
    @Mock
    private EAPEnsurer eapEnsurer;
    private SimpleExecutor executor;
    @Mock
    private SimpleEnsurersProvider provider;
    @Mock
    private TaskKillEnsurer tkEnsurer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_AllProvidedEnsurersAreInvoked() {
        executor = new SimpleExecutor(provider);
        when(provider.get()).thenReturn(Lists.newArrayList(bEnsurer, tkEnsurer, eapEnsurer));

        executor.execute();
        verify(bEnsurer, Mockito.times(1)).ensure();
        verify(tkEnsurer, Mockito.times(1)).ensure();
        verify(eapEnsurer, Mockito.times(1)).ensure();
    }

}
