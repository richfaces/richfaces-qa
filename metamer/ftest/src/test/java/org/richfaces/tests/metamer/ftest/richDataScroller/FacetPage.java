package org.richfaces.tests.metamer.ftest.richDataScroller;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.dataScroller.RichFacesDataScroller;

public class FacetPage extends MetamerPage {
    @Root
    private WebElement root;

    @FindByJQuery(".rf-ds:eq(0)")
    private RichFacesDataScroller topScroller;

    @FindByJQuery(".rf-ds:eq(1)")
    private RichFacesDataScroller bottomScroller;

    public RichFacesDataScroller getTopScroller() {
        return topScroller;
    }

    public RichFacesDataScroller getBottomScroller() {
        return bottomScroller;
    }

}
