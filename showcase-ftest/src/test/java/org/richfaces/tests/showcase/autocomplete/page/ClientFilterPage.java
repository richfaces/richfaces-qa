/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.autocomplete.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author <a href="mailto:pmensik@redhat.com">Petr Mensik</a>
 */
public class ClientFilterPage {

    @FindBy(jquery = "input[type='text']:eq(0)")
    public WebElement input;

    @FindBy(css = "div[class='rf-au-itm']")
    public WebElement selection;
}
