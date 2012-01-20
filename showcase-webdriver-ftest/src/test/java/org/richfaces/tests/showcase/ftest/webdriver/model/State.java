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
package org.richfaces.tests.showcase.ftest.webdriver.model;

import org.apache.commons.lang.Validate;

/**
* @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
*/
public class State {

    private String capital;
    private String state;
    private String timeZone;

    public State(String state, String capital, String timeZone) {
        Validate.notNull(state);
        Validate.notNull(capital);
        Validate.notNull(timeZone);
        this.state = state;
        this.capital = capital;
        this.timeZone = timeZone;
    }

    public String getCapital() {
        return capital;
    }

    public String getState() {
        return state;
    }

    public String getTimeZone() {
        return timeZone;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((capital == null) ? 0 : capital.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((timeZone == null) ? 0 : timeZone.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        State other = (State) obj;
        if (capital == null) {
            if (other.capital != null)
                return false;
        } else if (!capital.equals(other.capital))
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        if (timeZone == null) {
            if (other.timeZone != null)
                return false;
        } else if (!timeZone.equals(other.timeZone))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return this.state + " " + this.capital + " " + this.timeZone;
    }

}
