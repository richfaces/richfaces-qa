/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.param;

import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSkinChange extends AbstractGrapheneTest {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    protected JQueryLocator deepMarineSkin = jq("fieldset form a:first");
    protected JQueryLocator blueSky = jq("fieldset form a:last");
    protected JQueryLocator currentSkin = jq("div[class$=right-controls] ul li[class$=current-skin]");

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testChangeSkin() {

        // This method of testing is not ideal
        // but selenium 1 is not ableto click on the link, therefore I choose this primitive way of testing
        //String hrefOfLink1 = selenium.getAttribute(deepMarineSkin.getAttribute(Attribute.HREF));
        guardHttp(selenium).click(deepMarineSkin);
        assertEquals(selenium.getText(currentSkin), "deepMarine", "Skin didn't changed to Deep Marine!");

        guardHttp(selenium).click(blueSky);
        assertEquals(selenium.getText(currentSkin), "blueSky", "Skind didn't changed to Blue Sky!");
        /*   assertTrue(hrefOfLink1.contains("skin=deepMarine"), "The link should contains deep marine");

        String hrefOfLink2 = selenium.getAttribute(blueSky.getAttribute(Attribute.HREF));

        assertTrue(hrefOfLink2.contains("skin=blueSky"), "The link should contains blue sky");*/

    }

    @Override
    protected String getSampleLabel() {
        return "Parameters for non-Ajax components";
    }

}
