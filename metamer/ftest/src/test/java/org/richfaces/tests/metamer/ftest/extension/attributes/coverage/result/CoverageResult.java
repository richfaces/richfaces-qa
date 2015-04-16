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
package org.richfaces.tests.metamer.ftest.extension.attributes.coverage.result;

import java.util.EnumSet;

import org.apache.commons.lang.math.Fraction;

/**
 * Representation of attributes coverage result.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface CoverageResult {

    /**
     * Returns EnumSet of all covered attributes.
     */
    EnumSet<?> getCovered();

    /**
     * Fraction of (covered attributes)/(all attributes).
     */
    Fraction getCoveredFraction();

    /**
     * Returns EnumSet of all ignored attributes.
     */
    EnumSet<?> getIgnored();

    /**
     * Fraction of (ignored attributes)/(all attributes).
     */
    Fraction getIgnoredFraction();

    /**
     * Returns EnumSet of all not covered attributes.
     */
    EnumSet<?> getNotCovered();

    /**
     * Fraction of (not covered attributes)/(all attributes).
     */
    Fraction getNotCoveredFraction();

    /**
     * Return name of the component. E.g. ActionListener, Tree.
     */
    String getComponentName();

    /**
     * Returns printable report.
     */
    String getReport();
}
