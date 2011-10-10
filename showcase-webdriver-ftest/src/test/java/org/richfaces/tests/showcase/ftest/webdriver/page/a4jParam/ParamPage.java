/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.showcase.ftest.webdriver.page.a4jParam;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ParamPage implements ShowcasePage {

    @FindBy(xpath = "//*[@class='example-cnt']//div[@class='rf-p-b ']/span")
    private WebElement output;
    @FindBy(xpath = "//*[@class='example-cnt']//input[@value='Set Name to Alex']")
    private WebElement setAlex;
    @FindBy(xpath = "//*[@class='example-cnt']//input[@value='Set Name to John']")
    private WebElement setJohn;

    @Override
    public String getDemoName() {
        return "param";
    }

    public WebElement getOutput() {
        return output;
    }

    @Override
    public String getSampleName() {
        return "param";
    }

    public void setName(Name name) {
        switch(name) {
            case ALEX:
                setAlex.click();
                break;
            case JOHN:
                setJohn.click();
                break;
        }
    }
    
    public static enum Name {
        ALEX("Alex"), JOHN("John");
        private String name;

        private Name(String name) {
            this.name = name;
        }

        public static Name[] getAll() {
            return new Name[] {ALEX, JOHN};
        }
        
        public String toString() {
            return name;
        }
    }

}