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
package org.richfaces.tests.metamer.ftest.extension.configurator.skip;

import static java.text.MessageFormat.format;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.richfaces.tests.metamer.ftest.extension.configurator.ConfiguratorExtension;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.Config;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.EmptyConfig;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.AndExpression;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;

/**
 * Configurator for not running test on specified circumstances. To use this extension, mark test method with @Skip annotation.<br/>
 * Configurator can be turned off by system property <code>configurator.skip.enabled=false</code>.<br/>
 * Configurator can run in reverse mode, activated by system property <code>configurator.skip.reverse</code>. In reverse mode, only those tests, which should be skipped by this configurator, will run.<br/>
 * Configurator can run only a specific skip case, specified by system property <code>configurator.skip.case=CASE</code>. Only those tests, which simple name of the first class specified in @Skip annotation matches expression <code>.*CASE.*</code>, will run. This specific configuration ignores reverse mode.<br/>
 * Some examples:<br/>
 * To always skip the test:<br/>
 * <code>@Skip</code><br/>
 * Some already predefined conditions can be found in {@link org.richfaces.tests.metamer.ftest.extension.configurator.skip.On On}.<br/>
 * To skip the test on Firefox browser:<br/>
 * <code>@Skip(On.Browser.Firefox.class)</code><br/>
 * To skip the test on Firefox browser on the Windows OS:<br/>
 * <code>@Skip({ On.Browser.Firefox.class, On.OS.Windows.class })</code>
 * To skip the test on Firefox or Chrome browser and on the Windows OS:<br/>
 * <code>@Skip(expressions = { @AndExpression({ On.Browser.Firefox.class, On.OS.Windows.class }), @AndExpression({ On.Browser.Chrome.class, On.OS.Windows.class }) })</code><br/>
 * To skip the test with custom condition implement an interface {@link org.richfaces.tests.metamer.ftest.extension.configurator.skip.SkipOn SkipOn} and add a no arguments constructor.
 * Other option is to extend predefined {@link org.richfaces.tests.metamer.ftest.extension.configurator.skip.BecauseOf.SkipOnIssue BecauseOf.SkipOnIssue} (and leave it in same class) to reuse already defined conditions from {@link org.richfaces.tests.metamer.ftest.extension.configurator.skip.On On}, like:<br/>
 * <code>@Skip(BecauseOf.Issue1234.class)</code><br/>
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SkipConfigurator implements ConfiguratorExtension {

    private static final List<Config> LIST_WITH_EMPTY_CONFIG = new LinkedList<Config>();
    public static String PROPERTY_CASE = "configurator.skip.case";
    public static String PROPERTY_ENABLED = "configurator.skip.enabled";
    public static String PROPERTY_REVERSE = "configurator.skip.reverse";

    private final SkipOnResultsCache cache = SkipOnResultsCache.getInstance();

    static {
        LIST_WITH_EMPTY_CONFIG.add(EmptyConfig.getInstance());
    }

    public List<Config> _createConfigurations(Method m, Object testInstance) {
        Skip annotationOnMethod = m.getAnnotation(Skip.class);
        if (annotationOnMethod == null) {
            return getNotSkippedConfiguration();
        }
        // the expressions takes precedence over value (since it contains default value)
        if (annotationOnMethod.expressions() != null && annotationOnMethod.expressions().length > 0) {
            boolean willSkip = false;
            for (AndExpression expression : annotationOnMethod.expressions()) {
                willSkip = willSkip || handleAndExpressions(expression.value());
            }
            return willSkip ? getSkippedConfiguration() : getNotSkippedConfiguration();
        } else if (annotationOnMethod.value() != null && annotationOnMethod.value().length > 0) {
            if (handleAndExpressions(annotationOnMethod.value())) {
                return getSkippedConfiguration();
            }
        } else {
            throw new RuntimeException("The Skip annotation cannot be empty. Either specify value or expressions!");
        }
        return getNotSkippedConfiguration();
    }

    private boolean applyCondition(Class<? extends SkipOn> skipOnClass) {
        return cache.getResultFor(skipOnClass);
    }

    @Override
    public List<Config> createConfigurations(Method m, Object testInstance) {
        if (isPropertyNotPresentOrTrue(PROPERTY_ENABLED)) {// is configurator enabled?
            String caseProperty = getCaseProperty();
            if (caseProperty != null) {
                return getConfigurationForGivenCase(m, caseProperty);
            }
            List<Config> result = _createConfigurations(m, testInstance);
            return isPropertyPresentOrTrue(PROPERTY_REVERSE)// is reverse mode enabled?
                ? (result.isEmpty()
                    ? getNotSkippedConfiguration()
                    : getSkippedConfiguration())
                : result;
        }
        return getNotSkippedConfiguration();
    }

    private String getCaseProperty() {
        return System.getProperty(PROPERTY_CASE);
    }

    private List<Config> getConfigurationForGivenCase(Method m, String caseProperty) {
        Skip skipAnnotation = m.getAnnotation(Skip.class);
        if (skipAnnotation != null) {
            if (skipAnnotation.value() != null) {
                return skipAnnotation.value()[0].getSimpleName().matches(format(".*{0}.*", caseProperty))
                    ? getNotSkippedConfiguration()
                    : getSkippedConfiguration();
            } else {
                return getSkippedConfiguration();
            }
        } else {
            return getSkippedConfiguration();
        }
    }

    private List<Config> getNotSkippedConfiguration() {
        return LIST_WITH_EMPTY_CONFIG;
    }

    @SuppressWarnings("unchecked")
    private List<Config> getSkippedConfiguration() {
        return Collections.EMPTY_LIST;
    }

    private boolean handleAndExpressions(Class<? extends SkipOn>[] andValues) {
        if (andValues.length > 0) {
            boolean b = true;
            for (Class<? extends SkipOn> skipOnClass : andValues) {
                b = b && applyCondition(skipOnClass);
            }
            return b;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean ignoreConfigurations() {
        return Boolean.TRUE;
    }

    private boolean isPropertyNotPresentOrTrue(String name) {
        return Boolean.parseBoolean(System.getProperty(name, "true"));
    }

    private boolean isPropertyPresentOrTrue(String name) {
        return Boolean.parseBoolean(System.getProperty(name, "false"));
    }

    @Override
    public boolean skipTestIfNoConfiguration() {
        return Boolean.TRUE;
    }
}
