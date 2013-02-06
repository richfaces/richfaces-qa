/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.archetypes.ftest;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.tests.archetypes.AbstractWebDriverTest;
import org.richfaces.tests.archetypes.TestConfiguration;

/**
 * <p>
 * Tests that input reacts to keyup events by sending XHR request and rerendering output as greeting to given name.
 * </p>
 *
 * <p>
 * If input has empty value, output is also empty.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractTestInput extends AbstractWebDriverTest<WithInputPage> {

    private final String inputName = "RichFaces Fan";

    protected AbstractTestInput(TestConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected WithInputPage createPage() {
        return new WithInputPage();
    }

    protected void testTypeName() {
        getPage().getInput().click();
        getPage().getInput().clear();
        getPage().getInput().sendKeys(inputName);
        Graphene.waitGui().withMessage("The output text doesn't match.")
            .until(Graphene.element(getPage().getOutput()).textEquals("Hello " + inputName + "!"));

    }

}
