/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.richfaces.component.SwitchType;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.model.Capital;

import com.google.common.collect.Maps;

@SessionScoped
@ManagedBean(name = "rf12491")
public class RF12491 implements Serializable {

    private static final char BRACKET_LEFT = '(';

    private static final char SPACE = ' ';
    private static final List<Capital> capitals = Model.unmarshallCapitals();
    private static final long serialVersionUID = 1L;

    private Map<PhaseId, Integer> getterForTable1InvokedInPhases;
    private Map<PhaseId, Integer> getterForTable2InvokedInPhases;
    private SwitchType switchType;
    private Set<SwitchType> switchTypes;
    private List<Capital> table1;
    private List<Capital> table2;

    private String createStringFromMap(Map<PhaseId, Integer> map) {
        StringBuilder sb = new StringBuilder();
        for (PhaseId key : map.keySet()) {
            sb.append(map.get(key))
                .append('x')
                .append(getPhaseName(key))
                .append(BRACKET_LEFT)
                .append(key.getOrdinal())
                .append("), ");
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 2);
        } else {
            return "";
        }
    }

    public String getGetterForTable1InvokedInPhases() {
        return createStringFromMap(getterForTable1InvokedInPhases);
    }

    public String getGetterForTable2InvokedInPhases() {
        return createStringFromMap(getterForTable2InvokedInPhases);
    }

    /**
     * Get phase name from toString method (the PhaseId#getName was added in JSF 2.2).
     * Also gets rid of phase ordinal in MyFaces.
     */
    private String getPhaseName(PhaseId phase) {
        String result = phase.toString();
        result = result.substring(0, Math.max(result.indexOf(SPACE), result.indexOf(BRACKET_LEFT)));
        return result;
    }

    public SwitchType getSwitchType() {
        return switchType;
    }

    public Set<SwitchType> getSwitchTypes() {
        return switchTypes;
    }

    public List<Capital> getTable1() {
        updateMap(getterForTable1InvokedInPhases);
        return table1;
    }

    public List<Capital> getTable2() {
        updateMap(getterForTable2InvokedInPhases);
        return table2;
    }

    @PostConstruct
    public void init() {
        table1 = capitals.subList(0, 10);
        table2 = capitals.subList(11, 20);
        switchTypes = EnumSet.allOf(SwitchType.class);
        switchType = SwitchType.ajax;
        resetInvokedPhases();
    }

    public void resetAll() {
        resetInvokedPhases();
    }

    public void resetInvokedPhases() {
        this.getterForTable1InvokedInPhases = Maps.newHashMap();
        this.getterForTable2InvokedInPhases = Maps.newHashMap();
    }

    public void setSwitchType(SwitchType switchType) {
        this.switchType = switchType;
    }

    public void setSwitchTypes(Set<SwitchType> switchTypes) {
        this.switchTypes = switchTypes;
    }

    public void setTable1(List<Capital> table1) {
        this.table1 = table1;
    }

    public void setTable2(List<Capital> table2) {
        this.table2 = table2;
    }

    private void updateMap(Map<PhaseId, Integer> map) {
        PhaseId id = FacesContext.getCurrentInstance().getCurrentPhaseId();
        boolean containsKey = map.containsKey(id);
        if (containsKey) {
            map.put(id, map.get(id) + 1);
        } else {
            map.put(id, 1);
        }
    }
}
