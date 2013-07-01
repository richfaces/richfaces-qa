/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.popup.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author pmensik
 */
public class LoginPage {

    @FindBy(jquery = "a:contains('Login'):eq(0)")
    public WebElement loginOnToolbar;

    @FindBy(jquery = "a:contains('Login'):eq(1)")
    public WebElement loginOnPopup;

    @FindBy(jquery = "a:contains('Search'):eq(0)")
    public WebElement searchOnToolbar;

    @FindBy(jquery = "a:contains('Search'):eq(1)")
    public WebElement searchOnPopup;
}
