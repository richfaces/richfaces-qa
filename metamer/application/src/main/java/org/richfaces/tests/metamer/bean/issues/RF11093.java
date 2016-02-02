/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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

import static javax.faces.component.visit.VisitContext.createVisitContext;
import static javax.faces.component.visit.VisitResult.ACCEPT;
import static javax.faces.context.FacesContext.getCurrentInstance;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.tests.metamer.bean.RichBean;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@ManagedBean(name = "rf11093")
@SessionScoped
public class RF11093 {

    private List<String> values;

    public List<String> getValues() {
        return values;
    }

    @PostConstruct
    public void initialize() {
        values = Lists.newArrayList("one", "two", "three", "four");
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String someAction() {
        VisitContext vc = createVisitContext(getCurrentInstance());
        FacesContext.getCurrentInstance().getViewRoot().visitTree(vc, new VisitCallback() {

            @Override
            public VisitResult visit(VisitContext vc, UIComponent uic) {
                return ACCEPT;
            }
        });
        FacesContext context = FacesContext.getCurrentInstance();
        String item = context.getApplication().evaluateExpressionGet(context, "#{item}", String.class);
        RichBean.logToPage("item: " + item);
        return "";
    }
}
