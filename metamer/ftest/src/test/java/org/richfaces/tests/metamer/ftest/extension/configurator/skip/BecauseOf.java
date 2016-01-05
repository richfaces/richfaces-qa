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
package org.richfaces.tests.metamer.ftest.extension.configurator.skip;

/**
 * For examples see {@link org.richfaces.tests.metamer.ftest.extension.configurator.skip.SkipConfigurator SkipConfigurator}.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class BecauseOf {

    private static class SkipOnIssue implements SkipOn {

        private final OrExpression expression;

        /**
         * Creates SkipOn object with multiple cases. With the logical OR between each case.
         */
        private SkipOnIssue(Class<? extends SkipOn>... cases) {
            this(new OrExpression(cases));
        }

        /**
         * Creates SkipOn object with multiple cases. With the logical OR between each case.
         */
        private SkipOnIssue(Expression... expressions) {
            this.expression = new OrExpression(expressions);
        }

        @Override
        public boolean apply() {
            return expression.apply();
        }
    }

    private interface Expression {

        boolean apply();
    }

    private static class SkipOnExpression implements Expression {

        private final SkipOnResultsCache cache = SkipOnResultsCache.getInstance();

        private final Class<? extends SkipOn> skipOnCase;

        public SkipOnExpression(Class<? extends SkipOn> skipOnCase) {
            this.skipOnCase = skipOnCase;
        }

        @Override
        public boolean apply() {
            return cache.getResultFor(skipOnCase);
        }
    }

    private static class AndExpression implements Expression {

        private final Expression[] cases;

        private AndExpression(Expression... cases) {
            this.cases = cases;
        }

        private AndExpression(Class<? extends SkipOn>... cases) {
            this.cases = new Expression[cases.length];
            for (int i = 0; i < cases.length; i++) {
                this.cases[i] = new SkipOnExpression(cases[i]);
            }
        }

        @Override
        public boolean apply() {
            boolean result = true;
            for (Expression expression : cases) {
                result = result && expression.apply();
            }
            return result;
        }
    }

    private static class OrExpression implements Expression {

        private final Expression[] cases;

        private OrExpression(Expression... cases) {
            this.cases = cases;
        }

        private OrExpression(Class<? extends SkipOn>... cases) {
            this.cases = new Expression[cases.length];
            for (int i = 0; i < cases.length; i++) {
                this.cases[i] = new SkipOnExpression(cases[i]);
            }
        }

        @Override
        public boolean apply() {
            boolean result = false;
            for (Expression expression : cases) {
                result = result || expression.apply();
            }
            return result;
        }
    }

    public static class UIRepeatSetIndexIssue extends SkipOnIssue {

        public UIRepeatSetIndexIssue() {
            super(On.Container.WildFly81.class, On.Container.WildFly82.class, On.Container.WildFly90.class, On.Container.Tomcat7.class, On.Container.Tomcat8.class);
        }
    }

//    sample implementation:
//
//    public static class Issue1234 extends SkipOnIssue {
//
//        public Issue1234() {
//            // (using WildFly 8.1 || using WildFly 8.2) || (on linux && using chrome)
//            super(
//                new OrExpression(On.Container.WildFly81.class, On.Container.WildFly82.class),
//                new AndExpression(On.OS.Linux.class, On.Browser.Chrome.class)
//            );
//        }
//    }
}
