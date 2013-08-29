package org.richfaces.tests.page.fragments.impl.panel;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.jodah.typetools.TypeResolver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Add to the final doc that there is example implementation in TextualRichFacesPanel, as the most used panel.
 * @author jhuska
 *
 * @param <HEADER>
 * @param <BODY>
 */
public abstract class RichFacesPanel<HEADER, BODY> extends Panel<HEADER, BODY> {

    @FindBy(css = "div.rf-p-hdr")
    private WebElement header;

    @FindBy(css = "div.rf-p-b")
    private WebElement body;

    private AdvancedInteractions advancedInteractions;

    @Drone
    private WebDriver browser;

    @Root
    public WebElement root;

    @Override
    @SuppressWarnings("unchecked")
    public HEADER getHeaderContent() {
        if (!new WebElementConditionFactory(header).isPresent().apply(browser)) {
            throw new IllegalStateException("You are trying to get header content of the panel which does not have header!");
        }
        Class<HEADER> containerClass = (Class<HEADER>) TypeResolver.resolveRawArguments(Panel.class, getClass())[0];
        return Graphene.createPageFragment(containerClass, header);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BODY getBodyContent() {
        Class<BODY> containerClass = (Class<BODY>) TypeResolver.resolveRawArguments(Panel.class, getClass())[1];
        return Graphene.createPageFragment(containerClass, body);
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions {

        public WebElement getRootElement() {
            return root;
        }

        public WebElement getHeaderElement() {
            return header;
        }

        public WebElement getBodyElement() {
            return body;
        }
    }
}