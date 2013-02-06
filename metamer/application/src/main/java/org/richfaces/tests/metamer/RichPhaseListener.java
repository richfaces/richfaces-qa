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
package org.richfaces.tests.metamer;

import java.util.List;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A phase listener that notifies at the beginning and ending of processing for each standard phase of the request
 * processing lifecycle.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21299 $
 */
public class RichPhaseListener implements javax.faces.event.PhaseListener {

    private static final long serialVersionUID = 9026842190917014131L;
    private Logger logger = LoggerFactory.getLogger(RichPhaseListener.class);

    /**
     * {@inheritDoc}
     */
    public void afterPhase(PhaseEvent event) {
        logger.debug("AFTER - " + event.getPhaseId());
    }

    /**
     * {@inheritDoc}
     */
    public void beforePhase(PhaseEvent event) {
        logger.debug("BEFORE - " + event.getPhaseId());

        FacesContext ctx = event.getFacesContext();
        ExpressionFactory factory = ctx.getApplication().getExpressionFactory();
        ValueExpression exp = factory.createValueExpression(ctx.getELContext(), "#{phasesBean.phases}", List.class);
        List<String> phases = (List<String>) exp.getValue(ctx.getELContext());
        phases.add(new Phase(event.getPhaseId()).toString());
    }

    /**
     * {@inheritDoc}
     */
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}
