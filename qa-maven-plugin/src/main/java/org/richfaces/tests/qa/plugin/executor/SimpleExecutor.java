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

import org.richfaces.tests.qa.plugin.ensurer.Ensurer;
import org.richfaces.tests.qa.plugin.ensurer.EnsurersProvider;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Singleton
public class SimpleExecutor implements Executor {

    private final EnsurersProvider ensurersProvider;

    @Inject
    public SimpleExecutor(EnsurersProvider ensurersProvider) {
        this.ensurersProvider = ensurersProvider;
    }

    @Override
    public void execute() {
        for (Ensurer ensurer : ensurersProvider.get()) {
            ensurer.ensure();
        }
    }
}
