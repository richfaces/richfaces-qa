/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jAttachQueue;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class AttachQueuePage {

    @FindByJQuery("span[id$=output1]")
    private WebElement output1;

    @FindByJQuery("span[id$=output2]")
    private WebElement output2;

    @FindByJQuery("input[id$=input1]")
    private WebElement input1;

    @FindByJQuery("input[id$=input2]")
    private WebElement input2;

    public WebElement getOutput1() {
        return output1;
    }

    public WebElement getInput1() {
        return input1;
    }

    public WebElement getInput2() {
        return input2;
    }

    public WebElement getOutput2() {
        return output2;
    }
}
