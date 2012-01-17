/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richTooltip;

import static org.jboss.arquillian.ajocado.Ajocado.elementVisible;
import static org.jboss.arquillian.ajocado.Ajocado.waitAjax;

import static org.jboss.arquillian.ajocado.guard.RequestGuardFactory.guard;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.richfaces.TooltipMode;
import org.richfaces.tests.metamer.ftest.model.AbstractModel;



/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public class TooltipModel extends AbstractModel<JQueryLocator> {

    private AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    private TooltipMode mode = TooltipMode.client;
    private JQueryLocator target;

    public TooltipModel(JQueryLocator root, JQueryLocator target) {
        super(root);
        this.target = target;
    }

    public void setMode(TooltipMode mode) {
        this.mode = mode;
    }

    public TooltipMode getMode() {
        return mode;
    }

    public JQueryLocator getTarget() {
        return target;
    }

    public void recall() {
        recall(5, 5);
    }

    public void recall(int x, int y) {
        if (selenium.isVisible(getRoot())) {
            guard(selenium, getRequestType()).mouseMoveAt(target, new Point(x, y));
        } else {
            guard(selenium, getRequestType()).mouseOverAt(target, new Point(x, y));
            selenium.mouseMoveAt(target, new Point(x, y));
        }
waitAjax.dontFail().interval(50).timeout(2000).until(elementVisible.locator(this));
    }

    public void hide() {
        hide(-1, -1);
    }

    public void hide(int x, int y) {
        guard(selenium, getRequestType()).mouseOutAt(target, new Point(x, y));
    }

    private RequestType getRequestType() {
        switch (mode) {
            case ajax:
                return RequestType.XHR;
            default:
                return null;
        }
    }

}
