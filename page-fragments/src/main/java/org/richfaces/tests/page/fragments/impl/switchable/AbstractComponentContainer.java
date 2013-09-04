package org.richfaces.tests.page.fragments.impl.switchable;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;

public class AbstractComponentContainer implements ComponentContainer {

    @Root
    private WebElement root;

    @Override
    public <T> T getContent(Class<T> clazz) {
        return Graphene.createPageFragment(clazz, root);
    }

}
