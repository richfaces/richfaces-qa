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
package org.richfaces.tests.metamer;

import javax.faces.event.PhaseId;

/**
 * Representation of a JSF lifecycle phase.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21299 $
 */
public class Phase {

    private String name;
    private int ordinal;

    public Phase(PhaseId phase) {
        ordinal = phase.getOrdinal();

        switch (phase.getOrdinal()) {
            case 1:
                name = "RESTORE_VIEW";
                break;
            case 2:
                name = "APPLY_REQUEST_VALUES";
                break;
            case 3:
                name = "PROCESS_VALIDATIONS";
                break;
            case 4:
                name = "UPDATE_MODEL_VALUES";
                break;
            case 5:
                name = "INVOKE_APPLICATION";
                break;
            case 6:
                name = "RENDER_RESPONSE";
                break;
            default:
                name = "UNKNOWN_PHASE";
        }
    }

    public String getName() {
        return name;
    }

    public int getOrdinal() {
        return ordinal;
    }

    @Override
    public String toString() {
        return name + " " + ordinal;
    }
}
