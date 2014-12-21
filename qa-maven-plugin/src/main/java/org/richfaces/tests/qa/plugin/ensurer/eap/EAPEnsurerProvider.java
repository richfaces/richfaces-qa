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

import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;
import org.richfaces.tests.qa.plugin.properties.eap.EAPProperties;
import org.richfaces.tests.qa.plugin.utils.Servant;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class EAPEnsurerProvider implements Provider<EAPEnsurer> {

    private final EAPProperties eapProperties;
    private final PropertiesProvider pp;
    private final Servant servant;

    @Inject
    public EAPEnsurerProvider(EAPProperties eapProperties, PropertiesProvider pp, Servant servant) {
        this.eapProperties = eapProperties;
        this.pp = pp;
        this.servant = servant;
    }

    @Override
    public EAPEnsurer get() {
        if (pp.isEAPProfileActivated()) {
            if (pp.isOnJenkins()) {
                return new JenkinsEAPEnsurer(eapProperties, pp, servant);
            } else {
                return new LocalEAPEnsurer(eapProperties, pp, servant);
            }
        } else {
            return null;
        }
    }
}
