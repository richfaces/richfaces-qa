package org.richfaces.tests.metamer.ftest.richTabPanel;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.NullFragment;
import org.richfaces.fragment.dataTable.RichFacesDataTable;
import org.richfaces.fragment.tabPanel.RichFacesTabPanel;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

public class TestRF12491 extends AbstractWebDriverTest {

    private final Attributes<TabPanelAttributes> attributes = getAttributes();

    @FindBy(css = "[id$='firstGetterInvokedPhases']")
    private WebElement firstGetterInvokedPhases;
    @FindBy(css = "[id$='resetCounts']")
    private WebElement resetCounts;
    @FindBy(css = "[id$='refreshCounts']")
    private WebElement refreshCounts;
    @FindBy(css = "[id$='secondGetterInvokedPhases']")
    private WebElement secondGetterInvokedPhases;
    @FindBy(css = "div[id$='tabPanel']")
    private RichFacesTabPanel tabPanel;
    @FindBy(css = "[id$='table1']")
    private SimpleDT table1;
    @FindBy(css = "[id$='table2']")
    private SimpleDT table2;

    @Override
    public String getComponentTestPagePath() {
        return "richTabPanel/rf-12491.xhtml";
    }

    private void refreshCounts() {
        Graphene.guardAjax(refreshCounts).click();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12491")
    public void testGetterForOtherDataTableIsNotInvoked_switchTypeAjax() {
        attributes.set(TabPanelAttributes.switchType, "ajax");
        refreshCounts();
        assertEquals(firstGetterInvokedPhases.getText(), "1xRENDER_RESPONSE(6)");
        assertEquals(secondGetterInvokedPhases.getText(), "", "Second getter should not be invoked at all!");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12491")
    public void testGetterForOtherDataTableIsNotInvoked_switchTypeServer() {
        attributes.set(TabPanelAttributes.switchType, "server");
        refreshCounts();
        assertEquals(firstGetterInvokedPhases.getText(), "2xRENDER_RESPONSE(6)");
        assertEquals(secondGetterInvokedPhases.getText(), "", "Second getter should not be invoked at all!");
    }

    private class SimpleDT extends RichFacesDataTable<NullFragment, NullFragment, NullFragment> {
    }
}
