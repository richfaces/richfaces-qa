/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.attachQueue.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author <a href="mailto:pmensik@redhat.com">Petr Mensik</a>
 */
public class AttachQueuePage {

    @FindBy(jquery = "input[type='text']:visible")
    public WebElement input;

    @FindBy(jquery = "input[type='submit']:eq(0)")
    public WebElement submit;

    @FindBy(css = "span[class*='rf-st-start']")
    public WebElement ajaxRequestProcessing;
}
