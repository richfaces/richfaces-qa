/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richTree;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.model.AbstractModel;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public class TreeNodeHandle extends AbstractModel<JQueryLocator> {

AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    String classHandleLeaf = "rf-trn-hnd-lf";
    String classHandleExpanded = "rf-trn-hnd-exp";
    String classHandleCollapsed = "rf-trn-hnd-colps";

    public TreeNodeHandle(JQueryLocator root) {
        super(root);
    }

    public boolean isExpanded() {
        return isVisible() && selenium.belongsClass(this, classHandleExpanded);
    }

    public boolean isCollapsed() {
        return isVisible() && selenium.belongsClass(this, classHandleCollapsed);
    }

    public boolean isLeaf() {
        return isVisible() && selenium.belongsClass(this, classHandleLeaf);
    }

    private boolean isVisible() {
        return selenium.isVisible(this);
    }
}
