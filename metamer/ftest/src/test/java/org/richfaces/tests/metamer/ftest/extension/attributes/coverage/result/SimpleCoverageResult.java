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
package org.richfaces.tests.metamer.ftest.extension.attributes.coverage.result;

import static org.richfaces.tests.metamer.ftest.extension.attributes.coverage.collect.SimpleAttributesCoverageCollector.ATTRIBUTES;

import java.text.MessageFormat;
import java.util.EnumSet;

import org.apache.commons.lang.math.Fraction;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimpleCoverageResult implements CoverageResult {

    private static final String[] IGNORED_ATTS = { "binding", "id", "var", "value", };

    private final Class<? extends Enum> componentEnumClass;
    private final EnumSet<?> covered;
    private final EnumSet<?> ignored;
    private final EnumSet<?> notCovered;
    private final EnumSet<?> whole;

    @SuppressWarnings("unchecked")
    public SimpleCoverageResult(Class<? extends Enum> componentEnumClass, EnumSet covered) {
        this.componentEnumClass = componentEnumClass;
        this.covered = covered;
        this.whole = EnumSet.allOf(componentEnumClass);
        ignored = getDefaultIgnoredValues(componentEnumClass);
        ignored.removeAll(covered);
        this.notCovered = getNotCovered(covered, this.ignored);
    }

    @Override
    public String getComponentName() {
        return getComponentNameFromEnumClass();
    }

    private String getComponentNameFromEnumClass() {
        return componentEnumClass.getSimpleName().replace(ATTRIBUTES, "");
    }

    @Override
    public EnumSet<?> getCovered() {
        return covered;
    }

    @Override
    public Fraction getCoveredFraction() {
        return getFractionFromEnumSets(covered, whole);
    }

    private EnumSet getDefaultIgnoredValues(Class<? extends Enum> enumClass) {
        EnumSet result = EnumSet.noneOf(enumClass);
        for (String ignoredAtt : IGNORED_ATTS) {
            try {
                result.add(Enum.valueOf(enumClass, ignoredAtt));
            } catch (IllegalArgumentException e) {
            }
        }
        return result;
    }

    private Fraction getFractionFromEnumSets(EnumSet numerator, EnumSet denominator) {
        return Fraction.getFraction(numerator.size(), denominator.size());
    }

    private String getFractionFromEnumSetsAsString(EnumSet numerator, EnumSet denominator) {
        return MessageFormat.format("({0})", getFractionFromEnumSets(numerator, denominator).toString());
    }

    @Override
    public EnumSet<?> getIgnored() {
        return ignored;
    }

    @Override
    public Fraction getIgnoredFraction() {
        return getFractionFromEnumSets(ignored, whole);
    }

    private EnumSet getNotCovered(EnumSet covered, EnumSet ignored) {
        EnumSet notCovered = EnumSet.complementOf(covered);
        notCovered.removeAll(ignored);
        return notCovered;
    }

    @Override
    public EnumSet<?> getNotCovered() {
        return notCovered;
    }

    @Override
    public Fraction getNotCoveredFraction() {
        return getFractionFromEnumSets(notCovered, whole);
    }

    @Override
    public String getReport() {
        return toString();
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} attributes:\n  *  not covered: {1} {2}\n  *  ignored: {3} {4}\n  *  covered: {5} {6}",
            getComponentNameFromEnumClass(), notCovered, getFractionFromEnumSetsAsString(notCovered, whole), ignored, getFractionFromEnumSetsAsString(ignored, whole), covered, getFractionFromEnumSetsAsString(covered, whole));
    }
}
