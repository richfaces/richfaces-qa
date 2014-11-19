package org.richfaces.tests.metamer.ftest.richDataScroller;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

public class FacetPage extends MetamerPage {

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
