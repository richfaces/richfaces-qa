package org.jboss.test.selenium.support.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.directory.NoSuchAttributeException;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class AttributeContains implements ExpectedCondition<Boolean> {

    private WebElement element;
    private String attribute;
    private String attrValue;

    public static AttributeContains getInstance() {
        return new AttributeContains();
    }

    public Boolean apply(WebDriver driver) {
        try {
            String tempAttribute = element.getAttribute(attribute);
            if (tempAttribute != null) {
                return tempAttribute.contains(attrValue);
            } else {
                throw new NoSuchAttributeException("@" + attribute + " not found" );
            }
        } catch(NoSuchElementException ignored) {
            return false;
        } catch (NoSuchAttributeException ignored2) {
            return false;
        }
    }

    public AttributeContains element(WebElement element) {
        AttributeContains copy = copy();
        copy.element = element;
        return copy;
    }

    public AttributeContains attributeName(String attributeName) {
        AttributeContains copy = copy();
        copy.attribute = attributeName;
        return copy;
    }

    public AttributeContains attributeValue(String attributeValue) {
        AttributeContains copy = copy();
        copy.attrValue = attributeValue;
        return copy;
    }

    private AttributeContains copy() {
        AttributeContains copy = new AttributeContains();
        copy.element = this.element;
        copy.attribute = this.attribute;
        copy.attrValue = this.attrValue;
        return copy;
    }
}
