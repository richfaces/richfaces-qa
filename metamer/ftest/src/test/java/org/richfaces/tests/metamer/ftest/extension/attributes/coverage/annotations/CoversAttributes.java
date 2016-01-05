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
package org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;

/**
 * Used for marking of all tested attributes in test method.
 * Used for collecting RF's component's attribute coverage in {@link org.richfaces.tests.metamer.ftest.extension.attributes.coverage.CoverageCollector CoverageCollector} *
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Target({ METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CoversAttributes {

    /**
     * Specify attribute(s) covered by this test.
     */
    String[] value();

    /**
     * Specify AttributeEnum class or it will be automatically detected from actual test package.
     */
    Class<? extends AttributeEnum> attributeEnumClass() default DetectFromPackage.class;

    public static final class DetectFromPackage implements AttributeEnum {
    }
}
