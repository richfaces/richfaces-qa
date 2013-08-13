package org.richfaces.tests.page.fragments.impl.accordion;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.switchable.AbstractSwitchableComponent;

public class RichFacesAccordion extends AbstractSwitchableComponent<RichFacesAccordionItem> {

    @FindBy(className = "rf-ac-itm-hdr")
    private List<WebElement> switcherControllerElements;

    @FindBy(className = "rf-ac-itm")
    private List<RichFacesAccordionItem> accordionItems;

    @FindBy(jquery = ".rf-ac-itm-cnt:visible")
    private WebElement visibleContent;

    private AdvancedInteractions advancedInteractions;

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public int getNumberOfAccordionItems() {
        return accordionItems.size();
    }

    public class AdvancedInteractions extends AbstractSwitchableComponent<RichFacesAccordionItem>.AdvancedInteractions {

        public List<RichFacesAccordionItem> getAccordionItems() {
            return Collections.unmodifiableList(accordionItems);
        }

        public RichFacesAccordionItem getActiveItem() {
            for (RichFacesAccordionItem item : accordionItems) {
                if (item.advanced().isActive()) {
                    return item;
                }
            }
            return null;
        }
    }

    @Override
    protected List<WebElement> getSwitcherControllerElements() {
        return switcherControllerElements;
    }

    @Override
    protected WebElement getRootOfContainerElement() {
        return visibleContent;
    }
}
