/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.page.fragments.impl.utils.picker;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.WebElementPicking.WebElementPicker;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public final class ChoicePickerHelper {

    private ChoicePickerHelper() {
    }

    /**
     * @return Returns ChoicePicker for picking choices by index.
     */
    public static ByIndexChoicePicker byIndex() {
        return new ByIndexChoicePicker();
    }

    /**
     * @return Returns ChoicePicker for picking choices by visible text. The picker can have multiple conditions/filters (e.g. startsWith('xy').endsWith('z')).
     */
    public static ByVisibleTextChoicePicker byVisibleText() {
        return new ByVisibleTextChoicePicker();
    }

    /**
     * @return Returns ChoicePicker for picking choices by WebElement methods/properties.
     */
    public static WebElementPicker byWebElement() {
        return new WebElementPickerImpl();
    }

    public static class ByIndexChoicePicker implements ChoicePicker, MultipleChoicePicker {

        private final List<FindCommand> commands = Lists.newArrayList();

        private ByIndexChoicePicker() {
        }

        public ByIndexChoicePicker beforeLast(final int positionsBeforeLast) {
            commands.add(new FindCommand() {

                @Override
                public WebElement find(List<WebElement> list) {
                    return list.get(list.size() - 1 - positionsBeforeLast);
                }

                @Override
                public String toString() {
                    return "beforeLast(" + positionsBeforeLast + ")";
                }

            });
            return this;
        }

        public ByIndexChoicePicker first() {
            commands.add(new FindCommand() {

                @Override
                public WebElement find(List<WebElement> list) {
                    return list.get(0);
                }

                @Override
                public String toString() {
                    return "firstIndex";
                }
            });
            return this;
        }

        public ByIndexChoicePicker index(final int index) {
            commands.add(new FindCommand() {

                @Override
                public WebElement find(List<WebElement> list) {
                    return list.get(index);
                }

                @Override
                public String toString() {
                    return "index(" + index + ")";
                }
            });
            return this;
        }

        public ByIndexChoicePicker last() {
            return beforeLast(0);
        }

        @Override
        public WebElement pick(List<WebElement> options) {
            List<WebElement> elements = pickInner(options, TRUE);
            return (elements.isEmpty() ? null : elements.get(0));
        }

        private List<WebElement> pickInner(List<WebElement> options, boolean pickFirst) {
            Preconditions.checkNotNull(options, "Options cannot be null.");
            Preconditions.checkArgument(!commands.isEmpty(), "No filter specified.");
            if (options.isEmpty()) {
                return Collections.EMPTY_LIST;
            }
            Set<WebElement> result = Sets.newHashSet();
            for (FindCommand command : commands) {
                result.add(command.find(options));
                if (pickFirst) {
                    break;
                }
            }
            return Lists.newArrayList(result);
        }

        @Override
        public List<WebElement> pickMultiple(List<WebElement> options) {
            return pickInner(options, FALSE);
        }

        @Override
        public String toString() {
            return (commands.isEmpty() ? "unknown index picking" : commands.toString());
        }

        private interface FindCommand {

            WebElement find(List<WebElement> list);
        }
    };

    public static class ByVisibleTextChoicePicker implements ChoicePicker, MultipleChoicePicker {

        private final List<Predicate> filters = Lists.newArrayList();
        private boolean allRulesMustPass = Boolean.TRUE;
        private Function<WebElement, WebElement> transformationFunction;

        private ByVisibleTextChoicePicker() {
        }

        public ByVisibleTextChoicePicker addFilter(Predicate<WebElement> filter) {
            filters.add(filter);
            return this;
        }

        /**
         * If true, then all rules/filters must pass to pick an element.
         * If false, then if at least one rule/filter passes, element will be picked.
         * Default value is true.
         * @param allRulesMustPass
         */
        public ByVisibleTextChoicePicker allRulesMustPass(boolean allRulesMustPass) {
            this.allRulesMustPass = allRulesMustPass;
            return this;
        }

        public ByVisibleTextChoicePicker contains(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().contains(str);
                }

                @Override
                public String toString() {
                    return "contains('" + str + "')";
                }

            });
        }

        public ByVisibleTextChoicePicker endsWith(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().endsWith(str);
                }

                @Override
                public String toString() {
                    return "endsWith('" + str + "')";
                }
            });
        }

        public ByVisibleTextChoicePicker match(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().matches(str);
                }

                @Override
                public String toString() {
                    return "matches('" + str + "')";
                }
            });
        }

        @Override
        public WebElement pick(List<WebElement> options) {
            List<WebElement> elements = pickInner(options, TRUE);
            return (elements == null || (elements.isEmpty()) ? null : elements.get(0));
        }

        private List<WebElement> pickInner(List<WebElement> options, boolean pickFirst) {
            Preconditions.checkNotNull(options, "Options cannot be null.");
            Preconditions.checkArgument(!filters.isEmpty(), "No filters specified.");
            if (options.isEmpty()) {
                return Collections.EMPTY_LIST;
            }

            Set<WebElement> result = null;
            try {
                result = pickFirst
                    ? Sets.newHashSet(Iterables.find(options, new PickPredicate()))
                    : Sets.newHashSet(Iterables.filter(options, new PickPredicate()));
            } catch(NoSuchElementException ex) {
                return Collections.EMPTY_LIST;
            }
            return Lists.newArrayList(result);
        }

        @Override
        public List<WebElement> pickMultiple(List<WebElement> options) {
            return pickInner(options, FALSE);
        }

        /**
         * Sets a transformation function, that will be used to transform each WebElement from list of possible choices
         * to another WebElement.
         *
         * Example:
         * This picker will be picking and comparing text from such divs:
         * <code>
         *  <div>
         *      <span>text1</span>
         *      <span>text2</span>
         *  </div>
         * </code> ,
         * but you want to compare the text only with the second span.
         * The only thing you need to do is to add this function:
         * <code>
         * new Function<WebElement, WebElement>() {
         *      @Override
         *      public WebElement apply(WebElement input) {
         *          return input.findElements(By.tagName("span")).get(1);
         *      }
         *  }
         * </code>
         *
         * @param transformationFunction
         */
        public void setTransformationFunction(Function<WebElement, WebElement> transformationFunction) {
            this.transformationFunction = transformationFunction;
        }

        public ByVisibleTextChoicePicker startsWith(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().startsWith(str);
                }

                @Override
                public String toString() {
                    return "startsWith('" + str + "')";
                }
            });
        }

        @Override
        public String toString() {
            return (filters.isEmpty() ? "no filters specified" : filters.toString());
        }

        private WebElement transFormIfNeeded(WebElement input) {
            return (transformationFunction == null ? input : transformationFunction.apply(input));
        }

        private class PickPredicate implements Predicate<WebElement> {

            @Override
            public boolean apply(WebElement input) {
                WebElement element = transFormIfNeeded(input);
                if (allRulesMustPass) {
                    for (Predicate predicate : filters) {
                        if (!predicate.apply(element)) {
                            return FALSE;
                        }
                    }
                    return TRUE;
                } else {
                    for (Predicate predicate : filters) {
                        if (predicate.apply(element)) {
                            return TRUE;
                        }
                    }
                    return FALSE;
                }
            }
        }
    };

    public interface WebElementPicking {

        ComparationBy text();

        ComparationBy attribute(String attributeName);

        public interface ComparationBy {

            LogicalOperation endsWith(String str);

            LogicalOperation equalTo(String str);

            LogicalOperation contains(String str);

            LogicalOperation matches(String str);

            LogicalOperation starstWith(String str);
        }

        public interface LogicalOperation extends ChoicePicker, MultipleChoicePicker {

            WebElementPicking and();

            WebElementPicking or();
        }

        public interface WebElementPicker extends ChoicePicker, MultipleChoicePicker, WebElementPicking {
        }
    }

    public static class WebElementPickerImpl implements WebElementPicker {

        private final Deque<Predicate<WebElement>> stackWithPredicates = new ArrayDeque<Predicate<WebElement>>();
        private final Deque<LogicalFunctions> stackWithLogicalFunctions = new ArrayDeque<LogicalFunctions>();

        private final ComparationBy comparation = new ComparationByImpl();
        private final LogicalOperation operation = new LogicalOperationImpl();

        private Function<WebElement, WebElement> transformationFunction;
        private Function<WebElement, String> webElementFunction;

        private enum LogicalFunctions {

            OR, AND;
        }

        @Override
        public ComparationBy attribute(String attributeName) {
            webElementFunction = new GetAttributeFunction(attributeName);
            return comparation;
        }

        @Override
        public WebElement pick(List<WebElement> options) {
            Preconditions.checkArgument(!stackWithPredicates.isEmpty());
            Preconditions.checkArgument(stackWithPredicates.size() - 1 == stackWithLogicalFunctions.size());
            return Iterables.find(options, new FinalPredicate());
        }

        @Override
        public List<WebElement> pickMultiple(List<WebElement> options) {
            Preconditions.checkArgument(!stackWithPredicates.isEmpty());
            Preconditions.checkArgument(stackWithPredicates.size() - 1 == stackWithLogicalFunctions.size());
            return Lists.newArrayList(Iterables.filter(options, new FinalPredicate()));
        }

        @Override
        public ComparationBy text() {
            webElementFunction = new GetTextFunction();
            return comparation;
        }

        private WebElement transFormIfNeeded(WebElement input) {
            return (transformationFunction == null ? input : transformationFunction.apply(input));
        }

        private class FinalPredicate implements Predicate<WebElement> {

            @Override
            public boolean apply(WebElement input) {
                WebElement transformed = transFormIfNeeded(input);
                Deque<Predicate<WebElement>> predicates = new ArrayDeque<Predicate<WebElement>>(stackWithPredicates);
                Deque<LogicalFunctions> logicalFunctions = new ArrayDeque<LogicalFunctions>(stackWithLogicalFunctions);
                if (logicalFunctions.isEmpty()) {
                    return predicates.pop().apply(transformed);
                } else {
                    boolean result = predicates.pop().apply(transformed);
                    while (!logicalFunctions.isEmpty()) {
                        switch (logicalFunctions.pop()) {
                            case AND:
                                result &= predicates.pop().apply(transformed);
                                break;
                            case OR:
                                result |= predicates.pop().apply(transformed);
                                break;
                        }
                    }
                    return result;
                }
            }
        }

        private class ComparationByImpl implements ComparationBy {

            @Override
            public LogicalOperation contains(String str) {
                stackWithPredicates.push(new MergingPredicate(webElementFunction, new ContainsFunction(str)));
                return operation;
            }

            @Override
            public LogicalOperation endsWith(String str) {
                stackWithPredicates.push(new MergingPredicate(webElementFunction, new EndsWithFunction(str)));
                return operation;
            }

            @Override
            public LogicalOperation equalTo(String str) {
                stackWithPredicates.push(new MergingPredicate(webElementFunction, new EqualsToFunction(str)));
                return operation;
            }

            @Override
            public LogicalOperation matches(String str) {
                stackWithPredicates.push(new MergingPredicate(webElementFunction, new MatchesFunction(str)));
                return operation;
            }

            @Override
            public LogicalOperation starstWith(String str) {
                stackWithPredicates.push(new MergingPredicate(webElementFunction, new StartsWithFunction(str)));
                return operation;
            }
        }

        private class LogicalOperationImpl implements LogicalOperation {

            @Override
            public WebElementPicking and() {
                stackWithLogicalFunctions.push(LogicalFunctions.AND);
                return WebElementPickerImpl.this;
            }

            @Override
            public WebElementPicking or() {
                stackWithLogicalFunctions.push(LogicalFunctions.OR);
                return WebElementPickerImpl.this;
            }

            @Override
            public WebElement pick(List<WebElement> options) {
                return WebElementPickerImpl.this.pick(options);
            }

            @Override
            public List<WebElement> pickMultiple(List<WebElement> options) {
                return WebElementPickerImpl.this.pickMultiple(options);
            }
        }

        private static class MergingPredicate implements Predicate<WebElement> {

            private final Function<WebElement, String> elementToString;
            private final Function<String, Boolean> stringToBoolean;

            public MergingPredicate(Function<WebElement, String> elementToString, Function<String, Boolean> stringToBoolean) {
                this.elementToString = elementToString;
                this.stringToBoolean = stringToBoolean;
            }

            @Override
            public boolean apply(WebElement input) {
                return stringToBoolean.apply(elementToString.apply(input));
            }
        }

        private class GetTextFunction implements Function<WebElement, String> {

            @Override
            public String apply(WebElement input) {
                return input.getText();
            }
        }

        private static class GetAttributeFunction implements Function<WebElement, String> {

            private final String attName;

            public GetAttributeFunction(String attName) {
                this.attName = attName;
            }

            @Override
            public String apply(WebElement input) {
                return input.getAttribute(attName);
            }
        }

        private static class ContainsFunction implements Function<String, Boolean> {

            private final String compareTo;

            public ContainsFunction(String compareTo) {
                this.compareTo = compareTo;
            }

            @Override
            public Boolean apply(String input) {
                return input.contains(compareTo);
            }
        }

        private static class EndsWithFunction implements Function<String, Boolean> {

            private final String compareTo;

            public EndsWithFunction(String compareTo) {
                this.compareTo = compareTo;
            }

            @Override
            public Boolean apply(String input) {
                return input.endsWith(compareTo);
            }
        }

        private static class EqualsToFunction implements Function<String, Boolean> {

            private final String compareTo;

            public EqualsToFunction(String compareTo) {
                this.compareTo = compareTo;
            }

            @Override
            public Boolean apply(String input) {
                return input.equals(compareTo);
            }
        }

        private static class MatchesFunction implements Function<String, Boolean> {

            private final String compareTo;

            public MatchesFunction(String compareTo) {
                this.compareTo = compareTo;
            }

            @Override
            public Boolean apply(String input) {
                return input.matches(compareTo);
            }
        }

        private static class StartsWithFunction implements Function<String, Boolean> {

            private final String compareTo;

            public StartsWithFunction(String compareTo) {
                this.compareTo = compareTo;
            }

            @Override
            public Boolean apply(String input) {
                return input.startsWith(compareTo);
            }
        }
    }
}
