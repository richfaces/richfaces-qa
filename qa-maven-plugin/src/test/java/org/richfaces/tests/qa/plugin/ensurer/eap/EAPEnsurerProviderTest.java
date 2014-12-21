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
package org.richfaces.tests.qa.plugin.ensurer.eap;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;
import org.richfaces.tests.qa.plugin.properties.eap.EAPProperties;
import org.richfaces.tests.qa.plugin.utils.Servant;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class EAPEnsurerProviderTest {

    @Mock
    private EAPProperties eapProperties;
    @Mock
    private PropertiesProvider pp;
    private EAPEnsurerProvider provider;

    @Mock
    private Servant servant;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        provider = new EAPEnsurerProvider(eapProperties, pp, servant);
    }

    @Test
    public void testGet_notOnJenkinsNotUsingEAPProfile_returnsNull() {
        when(pp.isOnJenkins()).thenReturn(false);
        when(pp.isEAPProfileActivated()).thenReturn(false);

        Assert.assertNull(provider.get());
    }

    @Test
    public void testGet_notOnJenkinsUsingEAPProfile_returnsLocalEAPEnsurer() {
        when(pp.isOnJenkins()).thenReturn(false);
        when(pp.isEAPProfileActivated()).thenReturn(true);

        Assert.assertTrue(provider.get() instanceof LocalEAPEnsurer);
    }

    @Test
    public void testGet_onJenkinsNotUsingEAPProfile_returnsNull() {
        when(pp.isOnJenkins()).thenReturn(true);
        when(pp.isEAPProfileActivated()).thenReturn(false);

        Assert.assertNull(provider.get());
    }

    @Test
    public void testGet_onJenkinsUsingEAPProfile_returnsJenkinsEAPEnsurer() {
        when(pp.isOnJenkins()).thenReturn(true);
        when(pp.isEAPProfileActivated()).thenReturn(true);

        Assert.assertTrue(provider.get() instanceof JenkinsEAPEnsurer);
    }
}
