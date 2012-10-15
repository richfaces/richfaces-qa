/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.model.Capital;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractAutocompleteTest<Page extends MetamerPage> extends AbstractWebDriverTest<Page> {

    private List<Capital> capitals = Model.unmarshallCapitals();


    protected List<Capital> getCapitals() {
        return Collections.unmodifiableList(capitals);
    }

    protected List<String> getStatesByPrefix(String prefix) {
        List<String> states = new LinkedList<String>();

        for (Capital cap : capitals) {
            if (cap.getState().toLowerCase().startsWith(prefix)) {
                states.add(cap.getState());
            }
        }

        return states;
    }

    public String getExpectedStateForPrefix(String prefix, boolean selectFirst) {
        if (selectFirst && prefix.length() > 0) {
            return getStatesByPrefix(prefix).get(0);
        }

        return prefix;
    }

}
