/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Exadel, Inc.,
 *           2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.richfaces.tests.metamer.Template;
import org.richfaces.tests.metamer.TemplatesList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@SessionScoped
public class TemplateBean implements Serializable {

    private static final long serialVersionUID = 5078700314562231363L;
    private static Logger logger = LoggerFactory.getLogger(TemplateBean.class);
    private TemplatesList templates;
    private int templateIndex = 0;
    private boolean renderForm;

    @PostConstruct
    public void init() {
        templates = new TemplatesList();
        renderForm = true;
    }

    /**
     * @return the templates
     */
    public ArrayList<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(ArrayList<Template> templates) {
        if (templates instanceof TemplatesList) {
            this.templates = (TemplatesList) templates;
            return;
        }

        this.templates = new TemplatesList();
        for (Template t : templates) {
            this.templates.add(t);
        }
    }

    public String getComponentPrefix() {
        StringBuilder prefix = new StringBuilder("form:");
        for (Template template : templates) {
            prefix.append(template.getNestedComponentPrefix());
        }
        return prefix.toString();
    }

    public List<SelectItem> getAvailableTemplates() {
        List<SelectItem> retVal = new ArrayList<SelectItem>();
        for (Template template : Template.values()) {
            retVal.add(new SelectItem(template.toString(), template.getDesc()));
        }

        return retVal;
    }

    public String getFirstTemplate() {
        templateIndex = 0;
        return templates.get(0).toString();
    }

    public String getNextTemplate() {
        return templates.get(++templateIndex).toString();
    }

    public boolean isRenderForm() {
        return renderForm;
    }

    public void setRenderForm(boolean renderForm) {
        this.renderForm = renderForm;
    }
}
