/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.panel;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.jboss.arquillian.ajocado.dom.Attribute.STYLE;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestLookCustomization extends AbstractPanelTest {
	
	/* **********************************************************************************
	 * Constants
	 *************************************************************************************/
	
	protected final String outputPanelChangingStyleSyn1 = "fieldset td div.rf-p:eq(0)";
	protected final String outputPanelChangingStyleSyn2 = "fieldset td div.rf-p:eq(1)";
	protected final String outputPanelJavaScript = "fieldset td div.rf-p:eq(2)";
	protected final String outputPanelScrolling = "fieldset td div.rf-p:eq(3)";
	protected final String outputPanelWithoutHeader = "fieldset td div.rf-p:eq(4)";
	
	// the order of panels is from top left to bottom right corner of sample page with panels
	protected final String PANEL1_HEADER = "Panel #1. Changing Style Synchronously";
	protected final String PANEL1_BODY = "Each component in RichFaces has a pre-defined set " +
			"of CSS classes you can manipulate. If defined, those classes overwrite the " +
			"ones that come from the skin.";
	protected final String PANEL2_HEADER = "Panel #2. Changing Style Synchronously";
	protected final String PANEL2_BODY = "In this example, we define header color using " +
			"the .rf-panel-header class and all panels located on the same page inherit " +
			"this color";
	protected final String PANEL3_HEADER = "Panel header";
	protected final String PANEL3_BODY = "Base on the previous layout, but with javascript visual effects added.";
	protected final String PANEL4_HEADER = "Scrolling Text Panel";
	protected final String PANEL4_BODY = "Long Text Long Text Long Text Long Text Long " +
			"Text Long Text Long Text Long Text Long Text Long Text Long Text Long " +
			"Text Long Text Long Text Long Text Long Text Long Text Long Text " +
			"Long Text Long Text Long Text Long Text Long Text Long Text " +
			"Long Text Long Text Long Text Long Text Long Text Long Text " +
			"Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long " +
			"Text Long Text";
	protected final String PANEL5_BODY = "This is a panel without the header";
	
	/* **********************************************************************************
	 * Tests
	 ************************************************************************************/
	
	@Test
	public void testPanelsAreNotEmpty() {
		
		checkContentOfPanel(outputPanelChangingStyleSyn1 + " > " + HEADER, PANEL1_HEADER);
		checkContentOfPanel(outputPanelChangingStyleSyn1 + " > " + BODY, PANEL1_BODY);
		
		checkContentOfPanel( outputPanelChangingStyleSyn2 + " > " + HEADER, PANEL2_HEADER);
		checkContentOfPanel( outputPanelChangingStyleSyn2 + " > " + BODY, PANEL2_BODY);
		
		checkContentOfPanel( outputPanelJavaScript + " > " + HEADER, PANEL3_HEADER);
		checkContentOfPanel(outputPanelJavaScript + " > " + BODY, PANEL3_BODY);
		
		checkContentOfPanel( outputPanelScrolling + " > " + HEADER, PANEL4_HEADER);
		checkContentOfPanel( outputPanelScrolling + " > " + BODY, PANEL4_BODY);
		
		assertFalse( selenium.isElementPresent( jq(outputPanelWithoutHeader + HEADER) ), "There " +
				"should not be the header!");
		checkContentOfPanel( outputPanelWithoutHeader , PANEL5_BODY);
				
	}
	
	@Test
	public void testPanelWithJavaScript() {
		
		JQueryLocator panelWithJavaScript = jq(outputPanelJavaScript);
		
		selenium.mouseOver( panelWithJavaScript );
		selenium.mouseOut( panelWithJavaScript );
		
		String styleOfHeaderBefore = 
			selenium.getAttribute( jq( outputPanelJavaScript + " > " + HEADER).getAttribute( STYLE )); 
		
		String styleOfBodyBefore = 
			selenium.getAttribute( jq( outputPanelJavaScript + " > " + BODY).getAttribute( STYLE ));
		
		selenium.mouseOver( panelWithJavaScript );
		
		String styleOfHeaderAfter = 
			selenium.getAttribute( jq( outputPanelJavaScript + " > " + HEADER).getAttribute( STYLE ));
		
		String styleOfBodyAfter = 
			selenium.getAttribute( jq( outputPanelJavaScript + " > " + BODY).getAttribute( STYLE ));
		
		assertFalse( styleOfHeaderBefore.equals(styleOfHeaderAfter), "The style of header should be different" +
				"after mouseover, the the rgb should be different");
		assertFalse( styleOfBodyBefore.equals(styleOfBodyAfter), "The style of body should be different" +
				"after mouseover, the the rgb should be different");
		
		selenium.mouseOut(panelWithJavaScript);
		
		styleOfHeaderAfter = 
			selenium.getAttribute( jq( outputPanelJavaScript + " > " + HEADER).getAttribute( STYLE ));
		
		styleOfBodyAfter = 
			selenium.getAttribute( jq( outputPanelJavaScript + " > " + BODY).getAttribute( STYLE ));
		
		assertEquals( styleOfHeaderAfter, styleOfHeaderBefore, "The style of header should be returned to the " +
				"value on the mouseout state");
		assertEquals( styleOfBodyAfter, styleOfBodyBefore, "The style of body should be returned to the " +
		"value on the mouseout state");
		
	}
}