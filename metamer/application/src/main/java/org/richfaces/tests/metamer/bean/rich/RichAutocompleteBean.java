/**
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
 */
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.bean.RichBean;
import org.richfaces.tests.metamer.bean.abstractions.StringInputValidationBean;
import org.richfaces.tests.metamer.bean.abstractions.StringInputValidationBeanImpl;
import org.richfaces.tests.metamer.model.Capital;
import org.richfaces.ui.input.autocomplete.UIAutocomplete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:autocomplete.
 * http://community.jboss.org/wiki/richfacesautocompletecomponentbehavior
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "richAutocompleteBean")
@ViewScoped
// cannot be view-scoped (see https://jira.jboss.org/browse/RF-9287)
public class RichAutocompleteBean extends StringInputValidationBeanImpl implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final Logger logger = LoggerFactory.getLogger(RichAutocompleteBean.class);
    private Attributes ajaxAttributes;
    @ManagedProperty("#{model.capitals}")
    private List<Capital> capitals;
    private String randomString;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromFacesConfig(UIAutocomplete.class, getClass());
        attributes.setAttribute("converterMessage", "converter message");
        attributes.setAttribute("mode", "ajax");
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("required", true);
        attributes.setAttribute("requiredMessage", StringInputValidationBean.REQUIRED_VALIDATION_MSG);
        attributes.setAttribute("tokens", ", ");
        attributes.setAttribute("validatorMessage", "validator message");

        ajaxAttributes = Attributes.getEmptyAttributes(getClass());
        ajaxAttributes.setAttribute("render", "output");
        ajaxAttributes.setAttribute("execute", "autocomplete");

        attributes.remove("var"); // this attr is supposed to be used with @fetchValue, and cannot be changed
        attributes.remove("valueChangeListener"); // unnecessary attribute for client

        // since this bean is session scoped, valueX should be reset explicitly
    }

    public Attributes getAjaxAttributes() {
        return ajaxAttributes;
    }

    public void setAjaxAttributes(Attributes ajaxAttributes) {
        this.ajaxAttributes = ajaxAttributes;
    }

    public List<String> autocomplete(String prefix) {
        ArrayList<String> result = new ArrayList<String>();
        if (prefix != null && prefix.length() > 0) {
            Iterator<Capital> iterator = capitals.iterator();
            while (iterator.hasNext()) {
                Capital elem = ((Capital) iterator.next());
                if ((elem.getState() != null && elem.getState().toLowerCase().indexOf(prefix.toLowerCase()) == 0)
                        || "".equals(prefix)) {
                    result.add(elem.getState());
                }
            }
        } else {
            for (Capital capital : capitals) {
                result.add(capital.getState());
            }
        }
        return result;
    }

    /**
     * Since @fetchValue introduced to rich:autocomplete component,
     *  there is possible to serve objects instead of simple string,
     *  and provide more flexible UI
     *
     * With @fetchValue is possible display additional information,
     * even the letters typed into autocomplete input are different
     * @param prefix
     * @return List<Capital> with matching prefix
     */
    public List<Capital> autocompleteCapital(String prefix) {
        ArrayList<Capital> result = new ArrayList<Capital>();
        if (prefix != null && prefix.length() > 0) {
            Iterator<Capital> iterator = capitals.iterator();
            while (iterator.hasNext()) {
                Capital elem = ((Capital) iterator.next());
                if ((elem.getState() != null && elem.getState().toLowerCase().indexOf(prefix.toLowerCase()) == 0)
                        || "".equals(prefix)) {
                    result.add(elem);
                }
            }
        } else {
            for (Capital capital : capitals) {
                result.add(capital);
            }
        }

        randomString = String.valueOf(Math.random());

        return result;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }

    public List<Capital> getCapitals() {
        return capitals;
    }

    public String getRandomString() {
        return randomString;
    }

    public void setRandomString(String random) {
        this.randomString = random;
    }

    public void onblurListener1() {
        RichBean.logToPage("* on blur listener 1");
    }

    public void onblurListener2() {
        RichBean.logToPage("* on blur listener 2");
    }
}
