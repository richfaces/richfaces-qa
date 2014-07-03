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
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.ui.output.chart.PlotClickEvent;
import org.richfaces.ui.output.chart.UIChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:chart, contains event handlers and initializes data for chart.
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@ManagedBean(name = "chartBarBean")
@SessionScoped
public class RichChartBarBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger logger;
    private Attributes attributes;

    // string msg from server side event
    private String msg;

    private List<GDPRecord> gdp;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIChart.class, getClass());
        attributes.setAttribute("title", "GDP sector composition");
        attributes.setAttribute("styleClass", "graf");
        attributes.setAttribute("onplotclick", "log(event)");
        attributes.setAttribute("onplothover", "hover(event)");
        attributes.setAttribute("onmouseout", "clear()");
        attributes.setAttribute("rendered", true);

        attributes.remove("plotClickListener");
        attributes.remove("zoom");

        // init server side msg
        msg = "no server-side event";

        gdp = new LinkedList<GDPRecord>();
        gdp.add(new GDPRecord("United States", 188217, 2995787, 12500746));
        gdp.add(new GDPRecord("China", 830931, 3726848, 3669259));
        gdp.add(new GDPRecord("Japan", 71568, 1640091, 4258274));
        gdp.add(new GDPRecord("Germany", 27205, 955563, 2417812));
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

    public List<GDPRecord> getGdp() {
        return gdp;
    }

    public class GDPRecord {
        private final String state;
        private final int agricult;
        private final int industry;
        private final int service;

        public GDPRecord(String country, int agricult, int industry, int service) {
            this.state = country;
            this.agricult = agricult;
            this.industry = industry;
            this.service = service;
        }

        public String getState() {
            return state;
        }

        public int getAgricult() {
            return agricult;
        }

        public int getIndustry() {
            return industry;
        }

        public int getService() {
            return service;
        }

    }
}
