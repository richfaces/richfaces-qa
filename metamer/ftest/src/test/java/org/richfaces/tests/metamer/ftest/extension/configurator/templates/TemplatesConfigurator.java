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
package org.richfaces.tests.metamer.ftest.extension.configurator.templates;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.richfaces.tests.metamer.Template;
import org.richfaces.tests.metamer.TemplatesList;
import org.richfaces.tests.metamer.ftest.extension.configurator.ConfiguratorExtension;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.Config;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.FieldConfig;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.utils.ReflectionUtils;

import com.google.common.collect.Lists;

/**
 * The templates lists are separated with ',', more templates in a template list are connected with '+'.
 * <code>mvn verify -Dtemplates=*,accordion,accordion+tabPanel</code>
 * Will run all (possible) tests in: 1) all templates, 2) accordion, 3) tabPanel inside accordion (all tests that can run in both of them).
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TemplatesConfigurator implements ConfiguratorExtension {

    public static final String[] ALL_TEMPLATES_STRINGS = { "all", "*" };
    private static final String PLAIN = "plain";
    private static final TemplatesList PLAIN_TEMPLATE = TemplatesList.parseFrom(PLAIN);
    private static final String TEMPLATE_LIST_SEPARATOR = ",";
    private static final String TEMPLATE_PROPERTY_NAME = "templates";

    private static String getTemplatePropertyValue() {
        return System.getProperty(TEMPLATE_PROPERTY_NAME, PLAIN);
    }

    private List<TemplatesList> addTemplatesListForEachTestedTemplate(EnumSet<Template> defaultTestedTemplates) {
        List<TemplatesList> result = Lists.newLinkedList();
        for (Template template : defaultTestedTemplates) {
            result.add(removeRedundantPlainTemplate(TemplatesList.fromTemplate(template)));
        }
        return result;
    }

    private void checkIfAnnotationIsValid(Templates annotation) throws IllegalStateException {
        boolean exludedIsEmpty = true, valuesIsEmpty = true;
        String[] values;
        if (annotation.exclude() != null) {
            values = annotation.exclude();
            for (String string : values) {
                if (!string.isEmpty()) {
                    exludedIsEmpty = false;
                }
            }
        }
        if (annotation.value() != null) {
            values = annotation.value();
            for (String string : values) {
                if (!string.isEmpty()) {
                    valuesIsEmpty = false;
                }
            }
        }
        if ((!valuesIsEmpty && !exludedIsEmpty) || (valuesIsEmpty && exludedIsEmpty)) {
            throw new IllegalArgumentException("More annotation fields are set or both fields are empty. Consider using only 'values' or 'excluded'");
        }
    }

    @Override
    public List<Config> createConfigurations(Method m, Object testInstance) {
        List<Config> result = Lists.newLinkedList();

        Templates annotationOnMethod = m.getAnnotation(Templates.class);
        Templates annotationOnTestClass = testInstance.getClass().getAnnotation(Templates.class);
        Templates annotation = (annotationOnMethod != null ? annotationOnMethod : annotationOnTestClass);
        Field templateField = ReflectionUtils.getFirstFieldAnnotatedWith(Templates.class, testInstance);

        // if Skip annotation is present, no @Templates is specified and template property (-Dtemplates) is set to all
        // templates, then run test only in plain template
        Skip skipAnnotation = m.getAnnotation(Skip.class);
        if (annotation == null && skipAnnotation != null && isAllTemplateString(getTemplatePropertyValue())) {
            result.add(new FieldConfig(testInstance, PLAIN_TEMPLATE, templateField));
            return result;
        }

        EnumSet<Template> defaultTestedTemplates = getTestedTemplatesFromField(templateField);
        EnumSet<Template> possibleTemplates = getAllPossibleTemplatesFromAnnotation(annotation, defaultTestedTemplates);
        if (possibleTemplates.isEmpty()) {
            throw new IllegalStateException("No possible templates configured!");
        }

        for (TemplatesList templatesList : getRunnableTemplatesLists(parseTemplatesListFromSystemProperty(defaultTestedTemplates), possibleTemplates)) {
            result.add(new FieldConfig(testInstance, templatesList, templateField));
        }
        return result;
    }

    private EnumSet<Template> getAllPossibleTemplatesFromAnnotation(Templates annotationOnMethodOrOnClass, EnumSet<Template> defaultTestedTemplates) {
        if (annotationOnMethodOrOnClass == null) {// annotation is null >>> ALL templates are possible
            return defaultTestedTemplates.clone();
        }
        checkIfAnnotationIsValid(annotationOnMethodOrOnClass);
        EnumSet<Template> set = null;
        if (annotationOnMethodOrOnClass.value() != null && !Arrays.asList(annotationOnMethodOrOnClass.value()).isEmpty()) {
            set = EnumSet.copyOf(parseTemplates(annotationOnMethodOrOnClass.value()));
        } else if (annotationOnMethodOrOnClass.exclude() != null && !Arrays.asList(annotationOnMethodOrOnClass.exclude()).isEmpty()) {
            set = defaultTestedTemplates.clone();
            set.removeAll(parseTemplates(annotationOnMethodOrOnClass.exclude()));
        } else {
            throw new IllegalStateException("All of the annotation values are null!");
        }
        return set;
    }

    private List<TemplatesList> getRunnableTemplatesLists(List<TemplatesList> listOfTemplates, EnumSet<Template> possibleTemplates) {
        List<TemplatesList> willRunInTemplatesLists = Lists.newLinkedList();
        for (TemplatesList templatesList : listOfTemplates) {
            if (isTemplatesListValid(templatesList, possibleTemplates)) {
                willRunInTemplatesLists.add(templatesList);
            } else {
//                System.out.println("IGNORED templates list: " + templatesList);
            }
        }
        return willRunInTemplatesLists;
    }

    private EnumSet<Template> getTestedTemplatesFromField(Field fielWithTemplatesAnnotation) {
        EnumSet<Template> result = EnumSet.noneOf(Template.class);
        String[] value = fielWithTemplatesAnnotation.getAnnotation(Templates.class).value();
        for (String string : value) {
            result.add(Template.valueFrom(string));
        }
        return result;
    }

    @Override
    public boolean ignoreConfigurations() {
        return Boolean.FALSE;
    }

    private boolean isAllTemplateString(String templatesListString) throws IllegalArgumentException {
        for (String allTemplateString : ALL_TEMPLATES_STRINGS) {
            if (templatesListString.contains(allTemplateString)) {
                if (templatesListString.equalsIgnoreCase(allTemplateString)) {
                    return true;
                } else {
                    throw new IllegalArgumentException("Cannot have a special ALL templates string in a TemplatesList containing more templates");
                }
            }
        }
        return false;
    }

    private boolean isTemplatesListValid(TemplatesList templatesList, EnumSet<Template> possibleTemplates) {
        for (Template template : templatesList) {
            if (!possibleTemplates.contains(template)) {
                return false;
            }
        }
        return true;
    }

    private List<Template> parseTemplates(String[] templates) {
        List<Template> result = Lists.newLinkedList();
        for (String template : templates) {
            result.add(Template.valueFrom(template));
        }
        return result;
    }

    private List<TemplatesList> parseTemplatesListFromSystemProperty(EnumSet<Template> defaultTestedTemplates) {
        List<TemplatesList> result = Lists.newLinkedList();
        String[] split = getTemplatePropertyValue().split(TEMPLATE_LIST_SEPARATOR);
        for (String templatesListString : split) {
            if (isAllTemplateString(templatesListString)) {
                result.addAll(addTemplatesListForEachTestedTemplate(defaultTestedTemplates));
            } else {
                result.add(removeRedundantPlainTemplate(TemplatesList.parseFrom(templatesListString)));
            }
        }
        if (result.isEmpty()) {
            throw new IllegalStateException("No templates to run in were specified.");
        }
        return result;
    }

    private TemplatesList removeRedundantPlainTemplate(TemplatesList tList) {
        if (tList.size() > 1 && tList.get(tList.size() - 1).equals(Template.PLAIN)) {
            // the TemplateList implementation inserts PLAIN template as the last record => delete it
            tList.remove(tList.size() - 1);
        }
        return tList;
    }

    @Override
    public boolean skipTestIfNoConfiguration() {
        return Boolean.TRUE;
    }
}
