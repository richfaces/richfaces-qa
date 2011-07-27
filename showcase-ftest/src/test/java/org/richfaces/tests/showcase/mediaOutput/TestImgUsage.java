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
package org.richfaces.tests.showcase.mediaOutput;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.ajocado.locator.option.OptionLocatorFactory;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestImgUsage extends AbstractAjocadoTest {
	
	/* *******************************************************************************************************
	 * Locators
	 * ******************************************************************
	 * *************************************
	 */
	
	private JQueryLocator firstSelect = jq("select:eq(0)"); // the color on the right side of the picture
	private JQueryLocator secondSelect = jq("select:eq(1)"); // the color on the left side of the picture
	private JQueryLocator thirdSelect = jq("select:eq(2)"); // the color of the text
	private JQueryLocator processTheImageButton = jq("input[type=submit]");
	private JQueryLocator image = jq("img[id$=img]");
	
	/* *****************************************************************************************************
	 * Constants
	 *******************************************************************************************************/
	
	//indexes of colors in selections
	private final int INDEX_OF_RED = 0;
	private final int INDEX_OF_DARK_BLUE = 1;
	private final int INDEX_OF_GREEN = 2;
	private final int INDEX_OF_YELLOW = 3;
	private final int INDEX_OF_BLUE = 4;
	
	// these are constants for initial state STATE0 
	// the rgb number of colors when the combination is red, dark blue, green
	//the order of colors is same as the order of selects on the page
	private final long RED_RGB_STATE0 = -851957;
	private final long DARK_BLUE_STATE0 = -16449286;
	private final long GREEN_RGB_STATE0 = -16712192;
	
	//these are constants for STATE1
	//the rgb number of colors when the combination is green, yellow, red
	//the order of colors is same as the order of selects on the page
	private final long GREEN_RGB_STATE1 = -15991296;
	private final long YELLOW_RGB_STATE1 = -327936;
	private final long RED_RGB_STATE1 = -131072;
	
	//these are constants for STATE2
	//the rgb number of colors when the combination is blue, red, dark blue
	//the order of colors is same as the order of selects
	private final long BLUE_RGB_STATE2 = -15993869;
	private final long RED_RGB_STATE2 = -391931;
	private final long DARK_BLUE_RGB_STATE2 = -16776962;
	
	/* ********************************************************************************************************
	 * Tests
	 * *********************************************************************
	 * ***********************************
	 */
	
	/**
	 * STATE0 is the initial state and the colors are deined in the constants section
	 */
	@Test
	public void testSTATE0() {
		
		testTheImage(RED_RGB_STATE0, DARK_BLUE_STATE0, GREEN_RGB_STATE0);
		
	}
	
	/**
	 * STATE1 is defined in the constants section
	 */
	@Test
	public void testSTATE1() {
	
		selectColorsAndClickOnTheButton(INDEX_OF_GREEN, INDEX_OF_YELLOW, INDEX_OF_RED);
		
		testTheImage(GREEN_RGB_STATE1, YELLOW_RGB_STATE1, RED_RGB_STATE1);
	}
	
	/**
	 * STATE2 is defined in the constants section
	 */
	@Test
	public void testSTATE2() {
		
		selectColorsAndClickOnTheButton(INDEX_OF_BLUE, INDEX_OF_RED, INDEX_OF_DARK_BLUE);
		
		testTheImage(BLUE_RGB_STATE2, RED_RGB_STATE2, DARK_BLUE_RGB_STATE2);
	}
	
	/**
	 * Test the corners and the text colors according to params
	 * 
	 * @param expectedRightBottomCornerColor
	 * @param expectedLeftTopCornerColor
	 * @param expectedTextColor
	 */
	private void testTheImage(long expectedRightBottomCornerColor, long expectedLeftTopCornerColor, 
			long expectedTextColor) {
		
		AttributeLocator<?> imageURLAttr = image.getAttribute(Attribute.SRC);
		URL imageURL = URLUtils.buildUrl(contextRoot, selenium.getAttribute(imageURLAttr));
		
		BufferedImage bufferedImage = null; 
		
		try {
			bufferedImage = ImageIO.read(imageURL);
		} catch (IOException ex) {
            fail("Could not download image from URL " + imageURL.getPath());
        }
		
		int widthOfImage = bufferedImage.getWidth();
		int heightOfImage = bufferedImage.getHeight();
		
		assertEquals(bufferedImage.getRGB(0, 0), expectedLeftTopCornerColor, 
				"The top-left corner should be different");
		assertEquals(bufferedImage.getRGB(widthOfImage - 1, heightOfImage - 1), 
				expectedRightBottomCornerColor, "The bottom-right corner should be different");
		assertEquals(bufferedImage.getRGB(95, 45), expectedTextColor, 
				"The text color should be different");
	}
	
	/**
	 * Select colors which are options in selects according to params
	 * 
	 * @param indexOfColorFirstSelect
	 * @param indexOfColorSecondSelect
	 * @param indexOfColorThirdSelect
	 */
	private void selectColorsAndClickOnTheButton(int indexOfColorFirstSelect, int indexOfColorSecondSelect, 
			int indexOfColorThirdSelect) {
		
		selenium.select(firstSelect, OptionLocatorFactory.optionIndex(indexOfColorFirstSelect));
        selenium.select(secondSelect, OptionLocatorFactory.optionIndex(indexOfColorSecondSelect));
        selenium.select(thirdSelect, OptionLocatorFactory.optionIndex(indexOfColorThirdSelect));
        
        guardXhr(selenium).click(processTheImageButton);
	}

}
