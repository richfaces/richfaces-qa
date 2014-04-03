package org.richfaces.fragment.dataTable;

import java.util.List;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TypeResolver;

public abstract class RichFacesDataTableWithHeaderAndFooter<HEADER, ROW, FOOTER> extends RichFacesDataTable<ROW> implements DataTableWithHeaderAndFooter<HEADER, ROW, FOOTER> {

    @SuppressWarnings("unchecked")
    private final Class<HEADER> headerClass = (Class<HEADER>) TypeResolver.resolveRawArguments(DataTableWithHeaderAndFooter.class, getClass())[0];

    @SuppressWarnings("unchecked")
    private final Class<FOOTER> footerClass = (Class<FOOTER>) TypeResolver.resolveRawArguments(DataTableWithHeaderAndFooter.class, getClass())[2];

    @FindBy(className = "rf-dt-thd")
    private WebElement wholeTableHeader;

    @FindBy(className = "rf-dt-tft")
    private WebElement wholeTableFooter;

    @FindBy(css = "th.rf-dt-hdr-c")
    private WebElement headerElement;

    @FindBy(className = "rf-dt-ftr-c")
    private WebElement footerElement;

    @FindBy(className = "rf-dt-shdr-c")
    private List<WebElement> columnHeaders;

    @FindBy(className = "rf-dt-sftr-c")
    private List<WebElement> columnFooters;

    @Drone
    private WebDriver browser;

    public HEADER getHeader() {
        return Graphene.createPageFragment(headerClass, wholeTableHeader);
    }

    public FOOTER getFooter() {
        return Graphene.createPageFragment(footerClass, wholeTableFooter);
    }

    private final AdvancedDataTableWithHeaderAndFooterInteractions advancedInteractions = new AdvancedDataTableWithHeaderAndFooterInteractions();

    @Override
    public AdvancedDataTableWithHeaderAndFooterInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedDataTableWithHeaderAndFooterInteractions extends AdvancedDataTableInteractions {

        public WebElement getHeaderElement() {
            return headerElement;
        }

        public WebElement getFooterElement() {
            return footerElement;
        }

        public WebElement getColumnHeader(int column) {
            return columnHeaders.get(column);
        }

        public WebElement getColumnFooter(int column) {
            return columnFooters.get(column);
        }
    }
}