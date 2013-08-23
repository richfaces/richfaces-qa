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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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

    public static class ByIndexChoicePicker implements ChoicePicker, MultipleChoicePicker {

        private final Set<FindCommand> commands = Sets.newHashSet();

        private ByIndexChoicePicker() {
        }

        public ByIndexChoicePicker beforeLast(final int positionsBeforeLast) {
            commands.add(new FindCommand() {

                @Override
                public boolean equals(Object obj) {
                    if (obj instanceof FindCommand) {
                        FindCommand cmd = (FindCommand) obj;
                        return cmd.hashCode() == this.hashCode();
                    }
                    return FALSE;
                }

                @Override
                public WebElement find(List<WebElement> list) {
                    return list.get(list.size() - 1 - positionsBeforeLast);
                }

                @Override
                public int hashCode() {
                    return Integer.MAX_VALUE - positionsBeforeLast;
                }

                @Override
                public String toString() {
                    return "lastIndex";
                }

            });
            return this;
        }

        public ByIndexChoicePicker first() {
            commands.add(new FindCommand() {

                @Override
                public boolean equals(Object obj) {
                    if (obj instanceof FindCommand) {
                        FindCommand cmd = (FindCommand) obj;
                        return cmd.hashCode() == this.hashCode();
                    }
                    return FALSE;
                }

                @Override
                public WebElement find(List<WebElement> list) {
                    return list.get(0);
                }

                @Override
                public int hashCode() {
                    return 0;
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
                public boolean equals(Object obj) {
                    if (obj instanceof FindCommand) {
                        FindCommand cmd = (FindCommand) obj;
                        return cmd.hashCode() == this.hashCode();
                    }
                    return FALSE;
                }

                @Override
                public WebElement find(List<WebElement> list) {
                    return list.get(index);
                }

                @Override
                public int hashCode() {
                    return index;
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

        private static final int CONTAINS = 89;
        private static final int MATCHES = 47;
        private static final int STARTS = 97;
        private static final int ENDS = 37;

        private final Set<Predicate> filters = Sets.newHashSet();
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
                public boolean equals(Object obj) {
                    if (obj instanceof Predicate) {
                        Predicate<WebElement> p = (Predicate<WebElement>) obj;
                        return p.hashCode() == this.hashCode();
                    }
                    return FALSE;
                }

                @Override
                public int hashCode() {
                    int hash = 3;
                    hash = 89 * hash + CONTAINS;
                    hash = 89 * hash + (str != null ? str.hashCode() : 0);
                    return hash;
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
                public boolean equals(Object obj) {
                    if (obj instanceof Predicate) {
                        Predicate<WebElement> p = (Predicate<WebElement>) obj;
                        return p.hashCode() == this.hashCode();
                    }
                    return FALSE;
                }

                @Override
                public int hashCode() {
                    int hash = 5;
                    hash = 97 * hash + ENDS;
                    hash = 97 * hash + (str != null ? str.hashCode() : 0);
                    return hash;
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
                public boolean equals(Object obj) {
                    if (obj instanceof Predicate) {
                        Predicate<WebElement> p = (Predicate<WebElement>) obj;
                        return p.hashCode() == this.hashCode();
                    }
                    return FALSE;
                }

                @Override
                public int hashCode() {
                    int hash = 3;
                    hash = 59 * hash + MATCHES;
                    hash = 59 * hash + (str != null ? str.hashCode() : 0);
                    return hash;
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
            return (elements.isEmpty() ? null : elements.get(0));
        }

        private List<WebElement> pickInner(List<WebElement> options, boolean pickFirst) {
            Preconditions.checkNotNull(options, "Options cannot be null.");
            Preconditions.checkArgument(!filters.isEmpty(), "No filters specified.");
            if (options.isEmpty()) {
                return Collections.EMPTY_LIST;
            }

            List<WebElement> result = pickFirst
                ? Lists.newArrayList(Iterables.find(options, new PickPredicate()))
                : Lists.newArrayList(Iterables.filter(options, new PickPredicate()));
            return result;
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
            new Function<WebElement, WebElement>() {

                @Override
                public WebElement apply(WebElement input) {
                    return input.findElements(By.tagName("span")).get(1);
                }
            };
        }

        public ByVisibleTextChoicePicker startsWith(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().startsWith(str);
                }

                @Override
                public boolean equals(Object obj) {
                    if (obj instanceof Predicate) {
                        Predicate<WebElement> p = (Predicate<WebElement>) obj;
                        return p.hashCode() == this.hashCode();
                    }
                    return FALSE;
                }

                @Override
                public int hashCode() {
                    int hash = 3;
                    hash = 53 * hash + STARTS;
                    hash = 53 * hash + (str != null ? str.hashCode() : 0);
                    return hash;
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
}
