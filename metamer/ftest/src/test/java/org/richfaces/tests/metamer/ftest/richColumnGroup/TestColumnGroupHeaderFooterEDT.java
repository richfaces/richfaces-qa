package org.richfaces.tests.metamer.ftest.richColumnGroup;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.ColumnGroupEDT;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:mtomasek@redhat.com">Martin Tomasek</a>
 *
 */
public class TestColumnGroupHeaderFooterEDT extends TestColumnGroupHeaderFooter{

    @FindBy(css = ".rf-edt[id$=richEDT]")
    private ColumnGroupEDT table;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richColumnGroup/headerFooterEDT.xhtml");
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-11749")
    public void testRendered() {
       //After fix remove future annotation and test it on localhost, maybe EDT component wont work as expected.
       super.testRendered();
    }

    @Override
    protected ColumnGroupEDT getTable() {
        return table;
    }

}
