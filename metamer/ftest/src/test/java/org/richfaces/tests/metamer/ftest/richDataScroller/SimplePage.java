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

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class SimplePage extends MetamerPage {

    public enum ScrollerPosition {
        DATA_SCROLLER_OUTSIDE_TABLE, DATA_SCROLLER_IN_TABLE_FOOTER;
    }

    @Root
    private WebElement root;
    @FindBy(css = "span.rf-ds[id$=scroller1]")
    private RichFacesDataScroller scrollerOutsideTable;
    @FindBy(css = "span.rf-ds[id$=scroller2]")
    private RichFacesDataScroller scrollerInTableFooter;

    @FindBy(css = "input[id$=buttonStpFirst1]")
    private GrapheneElement jsApiButtonSwitchToFirst1;
    @FindBy(css = "input[id$=buttonStpPrev1]")
    private GrapheneElement jsApiButtonSwitchToPrev1;
    @FindBy(css = "input[id$=buttonStpNext1]")
    private GrapheneElement jsApiButtonSwitchToNext1;
    @FindBy(css = "input[id$=buttonStpLast1]")
    private GrapheneElement jsApiButtonSwitchToLast1;

    @FindBy(css = "input[id$=buttonFirst1]")
    private GrapheneElement jsApiButtonFirst1;
    @FindBy(css = "input[id$=buttonPrev1]")
    private GrapheneElement jsApiButtonPrev1;
    @FindBy(css = "input[id$=buttonNext1]")
    private GrapheneElement jsApiButtonNext1;
    @FindBy(css = "input[id$=buttonLast1]")
    private GrapheneElement jsApiButtonLast1;

    @FindBy(css = "input[id$=buttonStpFirst2]")
    private GrapheneElement jsApiButtonSwitchToFirst2;
    @FindBy(css = "input[id$=buttonStpPrev2]")
    private GrapheneElement jsApiButtonSwitchToPrev2;
    @FindBy(css = "input[id$=buttonStpNext2]")
    private GrapheneElement jsApiButtonSwitchToNext2;
    @FindBy(css = "input[id$=buttonStpLast2]")
    private GrapheneElement jsApiButtonSwitchToLast2;

    @FindBy(css = "input[id$=buttonFirst2]")
    private GrapheneElement jsApiButtonFirst2;
    @FindBy(css = "input[id$=buttonPrev2]")
    private GrapheneElement jsApiButtonPrev2;
    @FindBy(css = "input[id$=buttonNext2]")
    private GrapheneElement jsApiButtonNext2;
    @FindBy(css = "input[id$=buttonLast2]")
    private GrapheneElement jsApiButtonLast2;

    public RichFacesDataScroller getScroller(ScrollerPosition scrollerPosition) {
        if (scrollerPosition == ScrollerPosition.DATA_SCROLLER_IN_TABLE_FOOTER) {
            return scrollerInTableFooter;
        } else {
            return scrollerOutsideTable;
        }
    }

    public GrapheneElement getJsApiButtonSwitchToFirst(ScrollerPosition scrollerPosition) {
        if (scrollerPosition == ScrollerPosition.DATA_SCROLLER_OUTSIDE_TABLE) {
            return jsApiButtonSwitchToFirst1;
        } else {
            return jsApiButtonSwitchToFirst2;
        }
    }

    public GrapheneElement getJsApiButtonSwitchToPrev(ScrollerPosition scrollerPosition) {
        if (scrollerPosition == ScrollerPosition.DATA_SCROLLER_OUTSIDE_TABLE) {
            return jsApiButtonSwitchToPrev1;
        } else {
            return jsApiButtonSwitchToPrev2;
        }
    }

    public GrapheneElement getJsApiButtonSwitchToNext(ScrollerPosition scrollerPosition) {
        if (scrollerPosition == ScrollerPosition.DATA_SCROLLER_OUTSIDE_TABLE) {
            return jsApiButtonSwitchToNext1;
        } else {
            return jsApiButtonSwitchToNext2;
        }
    }

    public GrapheneElement getJsApiButtonSwitchToLast(ScrollerPosition scrollerPosition) {
        if (scrollerPosition == ScrollerPosition.DATA_SCROLLER_OUTSIDE_TABLE) {
            return jsApiButtonSwitchToLast1;
        } else {
            return jsApiButtonSwitchToLast2;
        }
    }

    public GrapheneElement getJsApiButtonFirst(ScrollerPosition scrollerPosition) {
        if (scrollerPosition == ScrollerPosition.DATA_SCROLLER_OUTSIDE_TABLE) {
            return jsApiButtonFirst1;
        } else {
            return jsApiButtonFirst2;
        }
    }

    public GrapheneElement getJsApiButtonPrev(ScrollerPosition scrollerPosition) {
        if (scrollerPosition == ScrollerPosition.DATA_SCROLLER_OUTSIDE_TABLE) {
            return jsApiButtonPrev1;
        } else {
            return jsApiButtonPrev2;
        }
    }

    public GrapheneElement getJsApiButtonNext(ScrollerPosition scrollerPosition) {
        if (scrollerPosition == ScrollerPosition.DATA_SCROLLER_OUTSIDE_TABLE) {
            return jsApiButtonNext1;
        } else {
            return jsApiButtonNext2;
        }
    }

    public GrapheneElement getJsApiButtonLast(ScrollerPosition scrollerPosition) {
        if (scrollerPosition == ScrollerPosition.DATA_SCROLLER_OUTSIDE_TABLE) {
            return jsApiButtonLast1;
        } else {
            return jsApiButtonLast2;
        }
    }
}
