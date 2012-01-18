package org.richfaces.tests.metamer.ftest.model;

import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;

public class CollapsibleSubTableToggler extends AbstractModel<JQueryLocator> {

    ReferencedLocator<JQueryLocator> collapsedTogglers = ref(root, "> span[id$=collapsed]");
    ReferencedLocator<JQueryLocator> expandedTogglers = ref(root, "> span[id$=expanded]");
    
    public CollapsibleSubTableToggler(JQueryLocator root) {
        super(root);
    }
    
    public ExtendedLocator<JQueryLocator> getCollapsed() {
        return collapsedTogglers;
    }

    public ExtendedLocator<JQueryLocator> getExpanded() {
        return expandedTogglers;
    }

}
