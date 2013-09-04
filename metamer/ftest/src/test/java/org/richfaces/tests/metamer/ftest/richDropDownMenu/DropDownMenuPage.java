package org.richfaces.tests.metamer.ftest.richDropDownMenu;

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.dropDownMenuAttributes;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.dropDownMenu.RichFacesDropDownMenu;

/**
 * Universal page for both - side and top drop down menu testing
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 *
 */
public class DropDownMenuPage extends MetamerPage {
    @FindBy(jquery = ".rf-tb-itm:eq(0)")
    private RichFacesDropDownMenu fileDropDownMenuTop;

    @FindBy(jquery = ".optionList:eq(0)")
    private RichFacesDropDownMenu fileDropDownMenuSide;

    @FindBy(jquery = ".rf-ddm-lbl-dec:eq(0)")
    private WebElement target1;

    @FindBy(jquery = "span[id$=output]")
    private WebElement output;

    @FindBy(css = "div.rf-ddm-lst")
    private WebElement dropDownMenuContent;

    @FindBy(jquery = "img:contains('File')")
    private WebElement fileMenuLabel;

    @FindBy(jquery = "div[id$=menu1]")
    private WebElement fileMenu;

    @FindBy(jquery = "div[id$=menu1_list]")
    private WebElement fileMenuList;

    @FindBy(jquery = "div[id$=menuGroup4]")
    private WebElement group;

    @FindBy(jquery = "div[id$=menuGroup4_list]")
    private WebElement groupList;

    @FindBy(jquery = "div[id$=menu1] img.pic")
    private WebElement icon;

    @FindBy(jquery = "div[id$=menu1] span.rf-ddm-itm-ic > div.rf-ddm-emptyIcon")
    private WebElement emptyIcon;

    @FindBy(jquery = "div[id$=menuItem41]")
    private WebElement menuItem41;

    @FindBy(jquery = " div[id$=menu1] div.rf-ddm-lbl-dec")
    private WebElement fileMenuLabelOriginal;

    @FindBy(tagName = "body")
    private WebElement body;

    public WebElement getBody() {
        return body;
    }

    public WebElement getDropDownMenuContent() {
        return dropDownMenuContent;
    }

    public WebElement getEmptyIcon() {
        return emptyIcon;
    }

    /**
     * Returns RichFacesDropDownMenu page fragment based on current url. This is because locators on both pages slightly differ.
     *
     * @param testedPage Adress of tested page as string
     * @return Drop down menu page fragment
     */
    public RichFacesDropDownMenu getFileDropDownMenu(String testedPage) {
        if (testedPage.contains("sideMenu.xhtml")) {
            return fileDropDownMenuSide;
        } else {
            return fileDropDownMenuTop;
        }
    }

    public WebElement getFileMenu() {
        return fileMenu;
    }

    public WebElement getFileMenuLabel() {
        return fileMenuLabel;
    }

    public WebElement getFileMenuLabelOriginal() {
        return fileMenuLabelOriginal;
    }

    public WebElement getFileMenuList() {
        return fileMenuList;
    }

    public WebElement getGroup() {
        return group;
    }

    public WebElement getGroupList() {
        return groupList;
    }

    public WebElement getIcon() {
        return icon;
    }

    public WebElement getMenuItem41() {
        return menuItem41;
    }

    public WebElement getOutput() {
        return output;
    }

    public WebElement getTarget1() {
        return target1;
    }

    public String returnPopupWidth(String minWidth, RichFacesDropDownMenu dropDownMenu) {
        dropDownMenuAttributes.set(DropDownMenuAttributes.popupWidth, minWidth);
        dropDownMenu.advanced().invoke(target1);
        return dropDownMenuContent.getCssValue("min-width");
    }
}
