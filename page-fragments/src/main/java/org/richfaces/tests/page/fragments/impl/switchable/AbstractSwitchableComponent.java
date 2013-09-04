package org.richfaces.tests.page.fragments.impl.switchable;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

public abstract class AbstractSwitchableComponent<T extends ComponentContainer> implements SwitchableComponent<T> {

    @Root
    private WebElement root;

    private SwitchType switchType = SwitchType.AJAX;
    private AdvancedInteractions advancedInteractions;

    @Override
    @SuppressWarnings("unchecked")
    public T switchTo(ChoicePicker picker) {
        WebElement switcher = picker.pick(getSwitcherControllerElements());
        if (switcher == null) {
            throw new IllegalArgumentException("No such accordion item which fulfill the conditions from picker: " + picker);
        }
        switchTo(switcher);
        Class<T> containerClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
            .getActualTypeArguments()[0];
        return Graphene.createPageFragment(containerClass, getRootOfContainerElement());
    }

    @Override
    public T switchTo(String header) {
        return switchTo(ChoicePickerHelper.byVisibleText().match(header));
    }

    @Override
    public T switchTo(int index) {
        return switchTo(ChoicePickerHelper.byIndex().index(index));
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    protected abstract List<WebElement> getSwitcherControllerElements();

    protected abstract WebElement getRootOfContainerElement();

    private void switchTo(WebElement switcher) {
        switch (switchType) {
            case CLIENT:
                switcher.click();
                Graphene.waitGui();
                break;
            case AJAX:
                guardAjax(switcher).click();
                break;
            case SERVER:
                guardHttp(switcher).click();
                break;
        }
    }

    public class AdvancedInteractions {

        public void setSwitcherType(SwitchType newSwitchType) {
            switchType = newSwitchType;
        }

        public WebElement getRootElement() {
            return root;
        }
    }
}
