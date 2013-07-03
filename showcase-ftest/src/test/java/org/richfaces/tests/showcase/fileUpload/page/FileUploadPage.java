/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.fileUpload.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author pmensik
 */
public class FileUploadPage {

    @FindBy(className = ".rf-fu-btn-add")
    public WebElement addButton;

    @FindBy(css = "div[id$='upload']")
    public WebElement uploadArea;

    @FindBy(css = "div[id$='info']")
    public WebElement uploadFilesInfo;

    @FindBy(css = "div[class='rf-p-b info']")
    public WebElement divWithUploadFilesMessage;
}
