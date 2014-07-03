/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.ui.output.chart.ChartDataModel.ChartType;
import org.richfaces.ui.output.chart.PlotClickEvent;
import org.richfaces.ui.output.chart.StringChartDataModel;
import org.richfaces.ui.output.chart.UIChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:chart, contains event handlers and initializes data for chart.
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@ManagedBean(name = "chartPieBean")
@SessionScoped
public class RichChartPieBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger logger;
    private Attributes attributes;

    // string msg from server side event
    private String msg;

    private StringChartDataModel pie;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIChart.class, getClass());
        attributes.setAttribute("title", "USA GDP");
        attributes.setAttribute("styleClass", "graf");
        attributes.setAttribute("onplotclick", "log(event)");
        attributes.setAttribute("onplothover", "hover(event)");
        attributes.setAttribute("onmouseout", "clear()");
        attributes.setAttribute("rendered", true);

        attributes.remove("plotClickListener");
        attributes.remove("zoom");

        // init server side msg
        msg = "no server-side event";

        pie = new StringChartDataModel(ChartType.pie);
        pie.put("Industrial sector", 2995787);
        pie.put("Agricultural sector", 188217);
        pie.put("Service sector", 12500746);
    }

    public StringChartDataModel getPie() {
        return pie;
    }

    public void handler(PlotClickEvent event) {
        msg = event.toString();
    }

    public String getMsg() {
        return msg;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}
