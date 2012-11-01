package org.richfaces.tests.metamer.ftest.checker;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Checker for icon attributes (e.g. rich:accordion, rich:panelMenu)
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class IconsCheckerWebdriver<A extends AttributeEnum> {

    private final Attributes<A> attributes;
    private final WebDriver driver;
    private final String iconPrefix;
    private final String iconSuffix;

    /**
     * Create a new instance of icons checker
     *
     * @param driver instance of WebDriver
     * @param attributes Attributes instance used to set component attributes
     * @param iconPrefix prefix used for icon elements
     * @param iconSuffix suffix used for icon elements
     */
    public IconsCheckerWebdriver(WebDriver driver, Attributes<A> attributes, String iconPrefix, String iconSuffix) {
        Validate.notNull(driver);
        Validate.notNull(attributes);
        Validate.notNull(iconPrefix);
        Validate.notNull(iconSuffix);
        this.driver = driver;
        this.attributes = attributes;
        this.iconPrefix = iconPrefix;
        this.iconSuffix = iconSuffix;
    }

    /**
     * Checks whether icons controlled by CSS work properly (only icons which produce an image)
     *
     * @param attribute icon attribute
     * @param icon
     * @param classSuffix
     */
    public void checkCssImageIcons(A attribute, By icon, String classSuffix) {
        String imageNameSuffix = "";
        if (classSuffix.contains("dis")) {
            imageNameSuffix = "Disabled";
        }
        // option -> css class
        Map<String, String> cssImageIcons = new HashMap<String, String>();
        cssImageIcons.put("chevron", "chevron");
        cssImageIcons.put("chevronDown", "chevron-down");
        cssImageIcons.put("chevronUp", "chevron-up");
        cssImageIcons.put("disc", "disc");
        cssImageIcons.put("grid", "grid");
        cssImageIcons.put("triangle", "triangle");
        cssImageIcons.put("triangleDown", "triangle-down");
        cssImageIcons.put("triangleUp", "triangle-up");
        for(String cssIcon : cssImageIcons.keySet()) {
            if (!setAttributeSilently(attribute, cssIcon)) {
                continue;
            }
            assertTrue(driver.findElement(icon).getAttribute("class").contains(iconPrefix + cssImageIcons.get(cssIcon) + iconSuffix + classSuffix),
                    "Div should have set class " + iconPrefix + cssImageIcons.get(cssIcon) + iconSuffix + classSuffix + ".");
            assertTrue(driver.findElement(icon).getCssValue(CssProperty.BACKGROUND_IMAGE.getPropertyName()).contains(cssIcon + imageNameSuffix),
                    "Icon should contain a " + cssIcon + ".");
        }
    }

    /**
     * Checks whether icons controlled by CSS work properly (only icons which don't produce any image)
     *
     * @param attribute icon attribute
     * @param icon
     * @param classSuffix
     */
    public void checkCssNoImageIcons(A attribute, By icon, String classSuffix) {
        String[] cssNoImageIcons = new String[] { "transparent" };
        for(String cssIcon : cssNoImageIcons) {
            if (!setAttributeSilently(attribute, cssIcon)) {
                continue;
            }
            assertTrue(driver.findElement(icon).getAttribute("class").contains(iconPrefix + cssIcon + iconSuffix + classSuffix),
                "Div should have set class " + iconPrefix + cssIcon + iconSuffix + classSuffix +".");
            assertTrue(driver.findElement(icon).getCssValue(CssProperty.BACKGROUND_IMAGE.getPropertyName()).equals("none"),
                    "Icon should not contain any image.");
        }
    }

    /**
     * Checks whether icon with custom URL works properly
     *
     * @param attribute icon attribute
     * @param icon
     * @param image
     * @param classSuffix
     */
    public void checkImageIcons(A attribute, By icon, By image, String classSuffix) {
        checkImageIcons(attribute, icon, image, classSuffix, true);
    }

    /**
     * Checks whether icon with custom URL works properly
     *
     * @param attribute icon attribute
     * @param icon
     * @param image
     * @param classSuffix
     * @param disableIcon set to TRUE if the presence of image element causes that icon element shouldn't be present
     */
    public void checkImageIcons(A attribute, By icon, By image, String classSuffix, boolean disableIcon) {
        // option -> image
        Map<String, String> imageIcons = new HashMap<String, String>();
        imageIcons.put("nonexisting", "nonexisting");
        imageIcons.put("star", "star.png");
        for(String imageIcon : imageIcons.keySet()) {
            if (!setAttributeSilently(attribute, imageIcon)) {
                continue;
            }
            if (disableIcon) {
                assertFalse(Graphene.element(icon).isPresent().apply(driver), "Icon's div (" + icon + ") should not be present when icon=" + imageIcon + ".");
            }
            assertTrue(Graphene.element(image).isPresent().apply(driver), "Icon's image should be rendered (" + image + ") when icon=" + imageIcon + ".");
            assertTrue(driver.findElement(image).getAttribute("src").contains(imageIcons.get(imageIcon)),
                    "Icon's src attribute (" + image + ") should contain " + imageIcons.get(imageIcon) + " when icon=" + imageIcon + ".");
        }
    }

    public void checkNone(A attribute, By icon, String classSuffix) {
        if (!setAttributeSilently(attribute, "none")) {
            return;
        }
        assertFalse(Graphene.element(icon).isPresent().apply(driver), "Icon should not be present when icon=none.");
    }

    private boolean setAttributeSilently(A attribute, String value) {

        try {
            attributes.set(attribute, value);
            return true;
        } catch(RuntimeException ignored) {
            return false;
        }
    }

}
