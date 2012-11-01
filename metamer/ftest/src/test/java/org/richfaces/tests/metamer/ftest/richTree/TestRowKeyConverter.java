package org.richfaces.tests.metamer.ftest.richTree;

import java.net.URL;
import java.util.List;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRowKeyConverter extends AbstractWebDriverTest<TestRowKeyConverter.RowKeyConverterPage> {

    @Inject
    @Use(strings = { "rowKeyConverterSwingTreeNode", "rowKeyConverterRichFacesTreeNode", "rowKeyConverterRichFacesTreeDataModel" })
    private String sample;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTree/" + sample + ".xhtml");
    }

    @Test
    public void testToggle() {
        page.toggle();
        Assert.assertEquals(page.getNumberOfExpandedNodes(), 1, "Number of expanded nodes after expanding one node doesn't match.");
        page.toggle();
        Assert.assertEquals(page.getNumberOfExpandedNodes(), 0, "Number of expanded nodes after collapsing one node doesn't match.");
    }

    public static class RowKeyConverterPage extends MetamerPage {

        @FindBy(className="rf-tr-nd-exp")
        private List<WebElement> expanded;
        @FindBy(className="rf-trn-hnd")
        private WebElement toggle;

        public int getNumberOfExpandedNodes() {
            return expanded.size();
        }

        public void toggle() {
            final int before = expanded.size();
            toggle.click();
            Graphene.waitAjax().until(new ExpectedCondition<Boolean>(){
                @Override
                public Boolean apply(WebDriver input) {
                    return expanded.size() != before;
                }
            });
        }
    }

}
