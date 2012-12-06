/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.input.inplaceInput;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesControls implements Controls {

    @Root
    private WebElement root;
    //
    private WebDriver driver = GrapheneContext.getProxy();
    //
    @FindBy(css = "input[id$=Okbtn]")
    private WebElement okButtonElement;
    @FindBy(css = "input[id$=Cancelbtn]")
    private WebElement cancelButtonElement;

    @Override
    public void cancel() {
        if (isNotVisibleCondition().apply(driver)) {
            throw new RuntimeException("Cannot interact with Control buttons. They are not visible.");
        }
        if (Graphene.element(cancelButtonElement).not().isVisible().apply(driver)) {
            throw new RuntimeException("Cannot interact with Cancel button. It is not visible.");
        }
        String elementID = cancelButtonElement.getAttribute("id");
        String jQueryCmd = String.format("$(\"[id='%s']\").trigger('%s')", elementID, "mousedown");
        ((JavascriptExecutor) driver).executeScript(jQueryCmd);
        //after https://issues.jboss.org/browse/RF-9872 is solved, uncomment next line
        // and delete previous lines;
        //cancelButtonElement.click();
        //after https://issues.jboss.org/browse/RF-12609 is solved uncomment next line
        //Graphene.waitGui().until(isNotVisibleCondition());
    }

    @Override
    public WebElement getCancelButtonElement() {
        return cancelButtonElement;
    }

    @Override
    public WebElement getOkButtonElement() {
        return okButtonElement;
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }

    @Override
    public void ok() {
        if (isNotVisibleCondition().apply(driver)) {
            throw new RuntimeException("Cannot interact with Control buttons. They are not visible.");
        }
        if (Graphene.element(okButtonElement).not().isVisible().apply(driver)) {
            throw new RuntimeException("Cannot interact with OK button. It is not visible.");
        }
        String elementID = okButtonElement.getAttribute("id");
        String jQueryCmd = String.format("$(\"[id='%s']\").trigger('%s')", elementID, "mousedown");

        ((JavascriptExecutor) driver).executeScript(jQueryCmd);
        // after https://issues.jboss.org/browse/RF-9872 is solved, uncomment next line
        // and delete previous lines;
        //okButtonElement.click();
        // after https://issues.jboss.org/browse/RF-12609 is solved uncomment next line
        //Graphene.waitGui().until(isNotVisibleCondition());
    }
}
