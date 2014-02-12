/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

package org.richfaces.tests.metamer.ftest.model;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.testng.Assert.fail;

import org.jboss.arquillian.ajocado.Graphene;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.conditions.TextEquals;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;

/**
 * Provides DataScroller control methods.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision: 23043 $
 */
public class DataScroller extends AbstractModel<JQueryLocator> {

    protected static final String CLASS_DISABLED = "rf-ds-dis";

    protected GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

    ReferencedLocator<JQueryLocator> numberedPages = ref(root, "> .rf-ds-nmb-btn");
    ReferencedLocator<JQueryLocator> specificNumberedPage = ref(root, "> .rf-ds-nmb-btn:textEquals('{0}')");

    ReferencedLocator<JQueryLocator> firstPageButton = ref(root, "> .rf-ds-btn-first");
    ReferencedLocator<JQueryLocator> fastRewindButton = ref(root, "> .rf-ds-btn-fastrwd");
    ReferencedLocator<JQueryLocator> fastForwardButton = ref(root, "> .rf-ds-btn-fastfwd");
    ReferencedLocator<JQueryLocator> nextButton = ref(root, "> .rf-ds-btn-next");
    ReferencedLocator<JQueryLocator> previousButton = ref(root, "> .rf-ds-btn-prev");
    ReferencedLocator<JQueryLocator> rewindButton = ref(root, "> .rf-ds-btn-fastfwd");
    ReferencedLocator<JQueryLocator> lastPageButton = ref(root, "> .rf-ds-btn-last");

    ReferencedLocator<JQueryLocator> firstVisiblePage = ref(root, "> .rf-ds-nmb-btn:first");
    ReferencedLocator<JQueryLocator> lastVisiblePage = ref(root, "> .rf-ds-nmb-btn:last");
    ReferencedLocator<JQueryLocator> currentPage = ref(root, "> .rf-ds-act");

    // buttons with direct operation bound to rich:componentControll
    JQueryLocator firstJsApiBtn = jq("input[id$=buttonStpFirst{0}]");
    JQueryLocator previousJsApiBtn = jq("input[id$=buttonStpPrev{0}]");
    JQueryLocator nextJsApiBtn = jq("input[id$=buttonStpNext{0}]");
    JQueryLocator lastJsApiBtn = jq("input[id$=buttonStpLast{0}]");

    // buttons with "switchToPage" action bound to rich:componentControll and
    // operation specified by param
    JQueryLocator firstStpJsApiBtn = jq("input[id$=buttonStpFirst{0}]");
    JQueryLocator previousStpJsApiBtn = jq("input[id$=buttonStpPrev{0}]");
    JQueryLocator nextStpJsApiBtn = jq("input[id$=buttonStpNext{0}]");
    JQueryLocator lastStpJsApiBtn = jq("input[id$=buttonStpLast{0}]");

    Integer fastStep = null;
    Integer lastPage = null;

    public DataScroller(JQueryLocator root) {
        super(root);
    }

    public DataScroller(String name, JQueryLocator root) {
        super(name, root);
    }

    public void setFastStep(int fastStep) {
        this.fastStep = fastStep;
    }

    public Integer getFastStep() {
        return fastStep;
    }

    public void setLastPage(int pageNumber) {
        this.lastPage = pageNumber;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void gotoFirstPage() {
        if (!isFirstPage()) {
            clickFirstPageButton();
            Graphene.waitAjax.until(TextEquals.getInstance().locator(currentPage).text("1"));
        }
    }

    public void gotoLastPage() {
        if (!isLastPage()) {
            clickLastPageButton();
            Graphene.waitAjax.until(TextEquals.getInstance().locator(currentPage).text(String.valueOf(getLastVisiblePage())));
        }
    }

    public void gotoPage(int pageNumber) {
        if (lastPage != null && (pageNumber < 1 || pageNumber > lastPage)) {
            throw new IllegalStateException(SimplifiedFormat.format("The given pageNumber '{0}' is out of range of pages <1,{1}>", pageNumber, lastPage));
        }

        int counter = 50; // to prevent infinite loops
        while (pageNumber > getLastVisiblePage() && counter > 0) {
            fastForward(pageNumber);
            counter--;
        }

        if (counter == 0) {
            fail("Scroller doesn't change pages.");
        }

        counter = 50; // to prevent inifinite loops
        while (pageNumber < getFirstVisiblePage() && counter > 0) {
            fastRewind(pageNumber);
            counter--;
        }

        if (counter == 0) {
            fail("Scroller doesn't change pages.");
        }

        if (pageNumber == getCurrentPage()) {
            return;
        }

        clickPageButton(pageNumber);
        Graphene.waitAjax.until(TextEquals.getInstance().locator(currentPage).text(String.valueOf(pageNumber)));
    }

    public void fastForward(Integer pageNumber) {
        if (selenium.belongsClass(fastForwardButton, CLASS_DISABLED)) {
            if (fastStep != null && lastPage != null) {
                if (getCurrentPage() + fastStep > lastPage) {
                    gotoPage(getLastVisiblePage());
                } else {
                    throw new AssertionError("fast forward button disabled");
                }
            } else {
                gotoPage(getLastVisiblePage());
            }
        } else {
            if (pageNumber != null && lastPage != null) {
                if (Math.abs(getLastVisiblePage() - pageNumber) > Math.abs(lastPage - pageNumber)) {
                    clickLastPageButton();
                    return;
                }
            }
            if (fastStep == null) {
                gotoPage(getLastVisiblePage());
                return;
            }
            clickFastForward();
        }
    }

    public void fastRewind(Integer pageNumber) {
        if (selenium.belongsClass(fastRewindButton, CLASS_DISABLED)) {
            if (fastStep != null) {
                if (getCurrentPage() - fastStep <= 0) {
                    gotoPage(getFirstVisiblePage());
                } else {
                    throw new AssertionError("fast forward button disabled");
                }
            } else {
                gotoPage(getFirstVisiblePage());
            }
        } else {
            if (pageNumber != null) {
                if (Math.abs(getFirstVisiblePage() - pageNumber) > pageNumber) {
                    clickFirstPageButton();
                    return;
                }
            }
            if (fastStep == null) {
                gotoPage(getFirstVisiblePage());
                return;
            }
            clickFastRewind();

        }
    }

    public int getCountOfVisiblePages() {
        return selenium.getCount(numberedPages);
    }

    public boolean hasPages() {
        return selenium.isElementPresent(lastVisiblePage);
    }

    public int getFirstVisiblePage() {
        if (!hasPages()) {
            return 1;
        }
        return integer(selenium.getText(firstVisiblePage));
    }

    public int getLastVisiblePage() {
        if (!hasPages()) {
            return 1;
        }
        return integer(selenium.getText(lastVisiblePage));
    }

    public int obtainLastPage() {
        if (!hasPages()) {
            return 1;
        }
        if (isLastPage()) {
            return getCurrentPage();
        }
        int startPage = getCurrentPage();
        clickLastPageButton();
        lastPage = getCurrentPage();
        if (startPage == 1) {
            clickFirstPageButton();
        } else {
            gotoPage(startPage);
        }
        return lastPage;
    }

    public int getCurrentPage() {
        if (!hasPages()) {
            return 1;
        }
        return integer(selenium.getText(currentPage));
    }

    public boolean isFastForwardButtonPresent() {
        return selenium.isElementPresent(fastForwardButton);
    }

    public boolean isFastRewindButtonPresent() {
        return selenium.isElementPresent(fastRewindButton);
    }

    public boolean isFirstPage() {
        return getCurrentPage() == 1;
    }

    public boolean isFirstPageButtonPresent() {
        return selenium.isElementPresent(firstPageButton);
    }

    public boolean isLastPage() {
        return getCurrentPage() == getLastVisiblePage();
    }

    public boolean isLastPageButtonPresent() {
        return selenium.isElementPresent(lastPageButton);
    }

    public boolean isNextButtonPresent() {
        return selenium.isElementPresent(previousButton);
    }

    public boolean isPreviousButtonPresent() {
        return selenium.isElementPresent(previousButton);
    }

    public boolean isPresent() {
        return selenium.isElementPresent(root.getLocator()) && selenium.isVisible(root.getLocator());
    }

    public static int integer(String string) {
        return Integer.valueOf(string);
    }

    public void clickLastPageButton() {
        guardXhr(selenium).click(lastPageButton);
    }

    public void clickFirstPageButton() {
        guardXhr(selenium).click(firstPageButton);
    }

    public void clickPageButton(int pageNumber) {
        guardXhr(selenium).click(specificNumberedPage.format(pageNumber));
    }

    public void clickFastForward() {
        guardXhr(selenium).click(fastForwardButton);
    }

    public void clickFastRewind() {
        guardXhr(selenium).click(fastRewindButton);
    }

    public void clickStepForward() {
        guardXhr(selenium).click(nextButton);
    }

    public void clickStepPrevious() {
        guardXhr(selenium).click(previousButton);
    }

    // actions performed on buttons with JS API operation

    public void clickJsApiFirst(int scrollerNo) {
        guardXhr(selenium).click(firstJsApiBtn.format(scrollerNo));
    }

    public void clickJsApiStpFirst(int scrollerNo) {
        guardXhr(selenium).click(firstStpJsApiBtn.format(scrollerNo));
    }

    public void clickJsApiPrevious(int scrollerNo) {
        guardXhr(selenium).click(previousJsApiBtn.format(scrollerNo));
    }

    public void clickJsApiStpPrevious(int scrollerNo) {
        guardXhr(selenium).click(previousStpJsApiBtn.format(scrollerNo));
    }

    public void clickJsApiNext(int scrollerNo) {
        guardXhr(selenium).click(nextJsApiBtn.format(scrollerNo));
    }

    public void clickJsApiStpNext(int scrollerNo) {
        guardXhr(selenium).click(nextStpJsApiBtn.format(scrollerNo));
    }

    public void clickJsApiLast(int scrollerNo) {
        guardXhr(selenium).click(lastJsApiBtn.format(scrollerNo));
    }

    public void clickJsApiStpLast(int scrollerNo) {
        guardXhr(selenium).click(lastStpJsApiBtn.format(scrollerNo));
    }
}
