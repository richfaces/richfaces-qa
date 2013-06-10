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
package org.richfaces.tests.metamer.ftest.richDataScroller;

import static java.lang.Math.max;
import static java.lang.Math.min;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.google.common.base.Predicate;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.page.fragments.impl.dataScroller.RichFacesDataScroller;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public abstract class PaginationTesterWebDriver {

    protected AssertingAndWaitingDataScroller dataScroller;
    private int[] testPages;

    public void initializeTestedPages(int lastPage) {
        int l = lastPage;
        testPages = new int[]{ 3, l, 1, l - 2, l, 2, l - 2, l - 1 };
        for (int i = 0; i < testPages.length; i++) {
            testPages[i] = min(l, max(1, testPages[i]));
        }
    }

    public void setDataScroller(AssertingAndWaitingDataScroller dataScroller) {
        this.dataScroller = dataScroller;
    }

    public void testNumberedPages() {
        Integer lastNumber = null;
        for (int pageNumber : testPages) {
            if (lastNumber == (Integer) pageNumber) {
                continue;
            }
            verifyBeforeScrolling();
            dataScroller.switchTo(pageNumber);
            verifyAfterScrolling();
            lastNumber = pageNumber;
        }
    }

    protected abstract void verifyBeforeScrolling();

    protected abstract void verifyAfterScrolling();

    public static class AssertingAndWaitingDataScroller extends RichFacesDataScroller {

        private Integer fastStep = null;
        private Integer lastPage = null;

        public int getFastStep() {
            return fastStep;
        }

        public Integer getLastPage() {
            return lastPage;
        }

        public int obtainLastPage() {
            if (numberedPages.isEmpty()) {
                return 1;
            }
            if (isLastPage()) {
                return getActPageNumber();
            }
            int startPage = getActPageNumber();
            switchTo(DataScrollerSwitchButton.LAST);
            lastPage = getActPageNumber();
            if (startPage == 1) {
                switchTo(DataScrollerSwitchButton.FIRST);
            } else {
                switchTo(startPage);
            }
            return lastPage;
        }

        public void setFastStep(int fastStep) {
            this.fastStep = fastStep;
        }

        public void setLastPage(Integer lastPage) {
            this.lastPage = lastPage;
        }

        private void _switchTo(final int pageNum) {
            super.switchTo(pageNum);
        }

        @Override
        public void switchTo(final int pageNum) {
            int startCount = numberedPages.size();
            Graphene.guardAjax(new SwicthPageByNumberAction(pageNum)).perform();
            Graphene.waitModel().until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver input) {
                    return getActPageNumber() == pageNum;
                }
            });
            int currentPage = getActPageNumber();
            assertEquals(numberedPages.size(), startCount);
            assertEquals(currentPage, pageNum);

            assertEquals(isButtonDisabled(DataScrollerSwitchButton.FIRST), this.isFirstPage());
            assertEquals(isButtonDisabled(DataScrollerSwitchButton.LAST), this.isLastPage());

            if (fastStep != null) {
                assertEquals(isButtonDisabled(DataScrollerSwitchButton.FAST_REWIND), currentPage - fastStep < 1);
                if (lastPage != null) {
                    assertEquals(isButtonDisabled(DataScrollerSwitchButton.FAST_FORWARD), currentPage + fastStep > lastPage);
                }
            }
        }

        private void _switchTo(DataScrollerSwitchButton btn) {
            super.switchTo(btn);
        }

        @Override
        public void switchTo(final DataScrollerSwitchButton btn) {
            Graphene.guardAjax(new SwitchPageByButtonAction(btn)).perform();
            switch (btn) {
                case FIRST:
                    Graphene.waitModel().until(new Predicate<WebDriver>() {
                        @Override
                        public boolean apply(WebDriver input) {
                            return isFirstPage();
                        }
                    });
                    assertTrue(isButtonDisabled(DataScrollerSwitchButton.FAST_REWIND));
                    assertTrue(isButtonDisabled(DataScrollerSwitchButton.FIRST));
                    break;
                case LAST:
                    Graphene.waitModel().until(new Predicate<WebDriver>() {
                        @Override
                        public boolean apply(WebDriver input) {
                            return isLastPage();
                        }
                    });
                    assertTrue(isButtonDisabled(DataScrollerSwitchButton.FAST_FORWARD));
                    assertTrue(isButtonDisabled(DataScrollerSwitchButton.LAST));
                    break;
                default:
                    break;
            }
        }

        public class SwitchPageByButtonAction implements Action {

            private final DataScrollerSwitchButton btn;

            public SwitchPageByButtonAction(DataScrollerSwitchButton btn) {
                this.btn = btn;
            }

            @Override
            public void perform() {
                _switchTo(btn);
            }
        }

        public class SwicthPageByNumberAction implements Action {

            private final int pageNum;

            public SwicthPageByNumberAction(int pageNum) {
                this.pageNum = pageNum;
            }

            @Override
            public void perform() {
                _switchTo(pageNum);
            }
        }
    }
}
