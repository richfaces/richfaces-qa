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
package org.richfaces.tests.qa.plugin.ensurer.taskkill.killer;

import java.io.IOException;

import org.richfaces.tests.qa.plugin.ensurer.taskkill.builder.KillCommandBuilder;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;

import com.google.inject.Inject;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimpleTaskKiller implements TaskKiller {

    private final KillCommandBuilder kcb;
    private final PropertiesProvider pp;

    @Inject
    public SimpleTaskKiller(PropertiesProvider pp, KillCommandBuilder kcb) {
        this.pp = pp;
        this.kcb = kcb;
    }

    @Override
    public void execute(String... args) {
        if (args.length > 0) {
            for (String string : args) {
                kcb.addSearchArgument(string);
            }
            try {
                kcb.searchAndKill();
            } catch (IOException ex) {
                pp.getLog().error(ex);
            }
        } else {
            pp.getLog().info("no arguments specified, skipping");
        }
    }

    @Override
    public void killChromeDriver() {
        execute("chromedriver");
    }

    @Override
    public void killGlassFish() {
        execute("java", "felix.fileinstall");
    }

    @Override
    public void killIEDriver() {
        execute("IEDriverServer");
    }

    @Override
    public void killJBossAS() {
        execute("java", "org.jboss");
    }

    @Override
    public void killTomcat() {
        execute("java", "catalina.home");
    }
}
