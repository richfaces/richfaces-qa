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
package org.richfaces.tests.metamer.model.treeAdaptor;

import java.io.Serializable;

import org.richfaces.tests.metamer.bean.rich.RichTreeModelRecursiveAdaptorBean;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22493 $
 */
public abstract class AbstractNode implements Serializable, LazyLoadable, Node {

    private static final long serialVersionUID = 1L;

    private boolean isRoot;
    private int number;
    private String parentLabel;

    protected AbstractNode(boolean isRoot, int number, String parentLabel) {
        this.number = number;
        this.parentLabel = parentLabel;
        this.isRoot = isRoot;
    }

    /* (non-Javadoc)
     * @see org.richfaces.tests.metamer.model.treeAdaptor.INode#isRoot()
     */
    @Override
    public boolean isRoot() {
        return isRoot;
    }

    /* (non-Javadoc)
     * @see org.richfaces.tests.metamer.model.treeAdaptor.INode#getNumber()
     */
    @Override
    public int getNumber() {
        return number;
    }

    /* (non-Javadoc)
     * @see org.richfaces.tests.metamer.model.treeAdaptor.INode#getParentLabel()
     */
    @Override
    public String getParentLabel() {
        return parentLabel;
    }

    /* (non-Javadoc)
     * @see org.richfaces.tests.metamer.model.treeAdaptor.INode#getLabel()
     */
    @Override
    public abstract String getLabel();

    @Override
    public void notifyLoaded() {
        RichTreeModelRecursiveAdaptorBean bean = RichTreeModelRecursiveAdaptorBean.getFacesContext();
        bean.getLazyInitializedNodes().add(this.getLabel());
    }
}
