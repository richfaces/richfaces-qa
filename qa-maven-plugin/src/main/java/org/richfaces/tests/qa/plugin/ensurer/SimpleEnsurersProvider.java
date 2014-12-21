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

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nullable;

import org.richfaces.tests.qa.plugin.ensurer.browser.BrowserEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.eap.EAPEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.taskkill.TaskKillEnsurer;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimpleEnsurersProvider implements EnsurersProvider {

    private final BrowserEnsurer browserEnsurer;
    private final EAPEnsurer eapEnsurer;
    private final PropertiesProvider pp;
    private final TaskKillEnsurer taskKillEnsurer;

    @Inject
    public SimpleEnsurersProvider(@Nullable BrowserEnsurer browserEnsurer, @Nullable EAPEnsurer eapEnsurer, PropertiesProvider pp, @Nullable TaskKillEnsurer taskKillEnsurer) {
        this.browserEnsurer = browserEnsurer;
        this.eapEnsurer = eapEnsurer;
        this.pp = pp;
        this.taskKillEnsurer = taskKillEnsurer;
    }

    @Override
    public Collection<Ensurer> get() {
        ArrayList<Ensurer> list = Lists.newArrayList();
        if (pp.isEnsureTasksCleanup() && taskKillEnsurer != null) {
            list.add(taskKillEnsurer);
        } else {
            pp.getLog().info("Skipping tasks cleanup.");
        }
        if (pp.isEnsureEAP() && eapEnsurer != null) {
            list.add(eapEnsurer);
        } else {
            pp.getLog().info("Skipping ensuring of EAP.");
        }
        if (pp.isEnsureBrowser() && browserEnsurer != null) {
            list.add(browserEnsurer);
        } else {
            pp.getLog().info("Skipping ensuring of browser.");
        }
        return list;
    }
}
