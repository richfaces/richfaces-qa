/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.showcase.tree;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestTree extends AbstractTreeTest {

    /* *************************************************************************************************
     * Constants ***************************************************************** ********************************
     */

    private final String CURRENT_SELECTION_PANEL = "div[class*=rf-p-b]";

    /* ****************************************************************************************************
     * Locators ****************************************************************** **********************************
     */

    private JQueryLocator countryNodeLabel = jq(FIRST_LVL_NODE + " > div > span > span.rf-trn-lbl");

    /* *************************************************************************************************
     * Tests ********************************************************************* ****************************
     */

    @Test
    public void testCountAllCollapsedAndExpandAllAndCountAllExpanded() {
        if (selenium.isElementPresent(allExpandedHnd)) {

            int allExpanded = selenium.getCount(allExpandedHnd);

            collapseOrExpandAllNodes(allExpanded, allExpandedHnd);

        }

        int countOfAllCollapsed = selenium.getCount(allColapsedHnd);

        assertTrue(countOfAllCollapsed > 0, "The number of collapsed nodes should be higher than 0");

        collapseOrExpandAllNodes(countOfAllCollapsed, allColapsedHnd);

        assertFalse(selenium.isElementPresent(allColapsedHnd), "All nodes should be now expanded");

        int countOfAllExpanded = selenium.getCount(allExpandedHnd);

        assertTrue(countOfAllExpanded > 0, "The number of expanded nodes should be higher than 0");

        assertEquals(countOfAllCollapsed, countOfAllExpanded,
            "The number of all collapsed should be equals to the number of all expanded " + "after clicking on them!");

        collapseOrExpandAllNodes(countOfAllExpanded, allExpandedHnd);
    }

    @Test
    public void testSelectCountry() {
        collapseOrExpandAllNodes(allExpandedHnd);

        int countOfCountries = selenium.getCount(countryNodeLabel);

        for (int i = 0; i < countOfCountries; i++) {

            JQueryLocator labelFromTree = jq(FIRST_LVL_NODE + ":eq(" + i + ") > " + LABEL_NODE);

            String labelFromTreeString = selenium.getText(labelFromTree);

            guardXhr(selenium).clickAt(labelFromTree, new Point(0, 0));

            String labelFromCurrentSelectionPanel = getNameFromSelections();

            assertEquals(labelFromTreeString, labelFromCurrentSelectionPanel,
                "The selected country should be in the panel where the current selection is shown");
        }
    }

    @Test
    public void testSelectCompany() {
        collapseOrExpandAllNodes(allExpandedHnd);

        int countOfCountries = selenium.getCount(countryNodeLabel);

        for (int j = 0; j < countOfCountries; j++) {

            JQueryLocator companyNodeLabel = jq(FIRST_LVL_NODE + ":eq(" + j + ") >" + SECOND_LVL_NODE + " > "
                + LABEL_NODE);

            int countOfCompanies = selenium.getCount(companyNodeLabel);

            for (int i = 0; i < countOfCompanies; i++) {

                if (i == 0) {

                    selenium.click(jq(FIRST_LVL_NODE + ":eq(" + j + ") > " + EXPAND_SIGN));
                }

                companyNodeLabel = jq(FIRST_LVL_NODE + ":eq(" + j + ") >" + SECOND_LVL_NODE + ":eq(" + i + ")" + " > "
                    + LABEL_NODE);

                String companyNodeLabelStringFromTree = selenium.getText(companyNodeLabel);

                guardXhr(selenium).clickAt(companyNodeLabel, new Point(0, 0));

                String companyLabelCurrentSelectionPanel = getNameFromSelections();

                assertEquals(companyNodeLabelStringFromTree, companyLabelCurrentSelectionPanel,
                    "The selected company should be in the panel where the current selection is shown");
            }

        }

    }

    @Test
    public void testSelectCD() {
        collapseOrExpandAllNodes(allExpandedHnd);

        int countOfCountries = selenium.getCount(countryNodeLabel);

        for (int j = 0; j < countOfCountries; j++) {

            String country = selenium.getText(jq(FIRST_LVL_NODE + ":eq(" + j + ") >" + LABEL_NODE));

            JQueryLocator companyNodeExpandSign = jq(FIRST_LVL_NODE + ":eq(" + j + ") >" + SECOND_LVL_NODE + " > "
                + EXPAND_SIGN);

            int countOfCompanies = selenium.getCount(companyNodeExpandSign);

            for (int i = 0; i < countOfCompanies; i++) {

                if (i == 0) {

                    selenium.click(jq(FIRST_LVL_NODE + ":eq(" + j + ") > " + EXPAND_SIGN));
                }

                String company = selenium.getText(jq(FIRST_LVL_NODE + ":eq(" + j + ") > " + SECOND_LVL_NODE + ":eq("
                    + i + ") > " + LABEL_NODE));

                companyNodeExpandSign = jq(FIRST_LVL_NODE + ":eq(" + j + ") > " + SECOND_LVL_NODE + ":eq(" + i + ") > "
                    + EXPAND_SIGN);

                selenium.click(companyNodeExpandSign);

                JQueryLocator cd = jq(FIRST_LVL_NODE + ":eq(" + j + ") > " + SECOND_LVL_NODE + ":eq(" + i + ") > "
                    + LEAF + " > " + LABEL_NODE);

                int countOfCD = selenium.getCount(cd);

                for (int k = 0; k < countOfCD; k++) {

                    cd = jq(FIRST_LVL_NODE + ":eq(" + j + ") > " + SECOND_LVL_NODE + ":eq(" + i + ") > " + LEAF
                        + ":eq(" + k + ") > " + LABEL_NODE);

                    String cdLabel = selenium.getText(cd);
                    String[] partsOfCDLabel = cdLabel.split("-");

                    CD cdFromTree = new CD();
                    cdFromTree.setCountry(country.trim());
                    cdFromTree.setCompany(company.trim());
                    cdFromTree.setArtist(partsOfCDLabel[0].trim());
                    cdFromTree.setName(partsOfCDLabel[1].trim());
                    cdFromTree.setYear(partsOfCDLabel[2].trim());

                    guardXhr(selenium).clickAt(cd, new Point(0, 0));

                    CD cdFromCurrentSelection = retrieveCDFromCurrentSelection();

                    assertEquals(cdFromTree, cdFromCurrentSelection,
                        "The information about cd should be the same in the tree and in the "
                            + "current selection panel!");

                }
            }

        }

    }

    @Test
    public void testRememberExpandedNodeWhenParentIsCollapsed() {
        JQueryLocator firstCountryExpandSign = jq(FIRST_LVL_NODE + ":eq(0) > " + EXPAND_SIGN);

        selenium.click(firstCountryExpandSign);

        JQueryLocator firstCountryFirstCompanyExpSign = jq(FIRST_LVL_NODE + ":eq(0) > " + SECOND_LVL_NODE + ":eq(0) > "
            + EXPAND_SIGN);

        selenium.click(firstCountryFirstCompanyExpSign);

        JQueryLocator firstCountryFirstCompanyFirstCD = jq(FIRST_LVL_NODE + ":eq(0) > " + SECOND_LVL_NODE + ":eq(0) > "
            + LEAF + ":eq(0) > " + LABEL_NODE);

        guardXhr(selenium).clickAt(firstCountryFirstCompanyFirstCD, new Point(0, 0));

        CD cdBeforeCollapsing = retrieveCDFromCurrentSelection();

        JQueryLocator firstCountryCollapseSign = jq(FIRST_LVL_NODE + ":eq(0) > " + COLLAPSE_SIGN);
        selenium.click(firstCountryCollapseSign);

        JQueryLocator secondCountryExpandSign = jq(FIRST_LVL_NODE + ":eq(1) > " + EXPAND_SIGN);
        selenium.click(secondCountryExpandSign);

        CD cdAfterCollapsing = retrieveCDFromCurrentSelection();

        assertEquals(cdBeforeCollapsing, cdAfterCollapsing,
            "The selected cd should be remained when the branch was closed!");

        selenium.click(firstCountryExpandSign);

        JQueryLocator firstCountryFirstCompanyCollapseSign = jq(FIRST_LVL_NODE + ":eq(0) > " + SECOND_LVL_NODE
            + ":eq(0) > " + COLLAPSE_SIGN);
        assertTrue(selenium.isElementPresent(firstCountryFirstCompanyCollapseSign),
            "The first country first company node should remain expanded " + "when the first country was collapsed!");
    }

    /* *****************************************************************************************************************************************
     * Help methods ************************************************************** ************
     * ***************************************************************
     */

    /**
     * Collapses or expands all nodes
     *
     * @param numberOfNodes
     *            the number of nodes which will be collapsed or expanded
     * @param collapseOrExpand
     *            the locator of all expaned or collapsed nodes, when it is allExpandedHnd it collapse all nodes, and
     *            vice versa
     */
    private void collapseOrExpandAllNodes(int numberOfNodes, JQueryLocator collapseOrExpand) {
        for (int i = 0; i < numberOfNodes; i++) {

            selenium.click(collapseOrExpand);
        }
    }

    /**
     * Gets the name from the panel where the current selection from tree is shown
     *
     * @return the name from the panel where the current selection from tree is shown
     */
    private String getNameFromSelections() {
        String selectionText = selenium.getText(jq(CURRENT_SELECTION_PANEL));

        String[] partsOfSelectionText = selectionText.split(":");

        return partsOfSelectionText[1].trim();
    }

    /**
     * Retrieves the CD from current selection panel
     *
     * @return CD with information from current selection panel
     */
    private CD retrieveCDFromCurrentSelection() {
        CD cd = new CD();

        String[] nameParts = selenium.getText(jq(CURRENT_SELECTION_PANEL)).split(":")[1].split(" ");
        String name = "";

        for (int i = 0; i < nameParts.length - 2; i++) {

            name += nameParts[i] + " ";
        }

        cd.setName(name.trim());

        for (int i = 0; i < 5; i++) {

            JQueryLocator row = jq(CURRENT_SELECTION_PANEL + " > fieldset > table > tbody > tr:eq(" + i + ")");

            String rowString = selenium.getText(row);
            String[] partsOfRow = rowString.split(":");

            switch (i) {

                case 0:
                    cd.setCountry(partsOfRow[1].trim());
                    break;
                case 1:
                    cd.setCompany(partsOfRow[1].trim());
                    break;
                case 2:
                    cd.setArtist(partsOfRow[1].trim());
                    break;
                case 3:
                    cd.setPrice(partsOfRow[1].trim());
                    break;
                case 4:
                    cd.setYear(partsOfRow[1].trim());
                    break;
                default:
                    fail("Error when proccessing of current selection panel");
            }

        }

        return cd;
    }

    /* *********************************************************************************************************
     * Inner class ***************************************************************
     * *******************************************
     */

    protected class CD {

        private String country = null;
        private String company = null;
        private String artist = null;
        private String name = null;
        private String price = null;
        private String year = null;

        public CD(String country, String company, String artist, String name, String price, String year) {
            this.country = country;
            this.company = company;
            this.artist = artist;
            this.name = name;
            this.price = price;
            this.year = year;
        }

        public CD() {
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {

            String priceWithoutDollarSign = price.substring(1);

            Double priceDouble = new Double(priceWithoutDollarSign);

            if (priceDouble <= 0) {

                fail("The price should be higher than 0");
            }
            this.price = price;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((artist == null) ? 0 : artist.hashCode());
            result = prime * result + ((company == null) ? 0 : company.hashCode());
            result = prime * result + ((country == null) ? 0 : country.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((year == null) ? 0 : year.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CD other = (CD) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (artist == null) {
                if (other.artist != null)
                    return false;
            } else if (!artist.equals(other.artist))
                return false;
            if (company == null) {
                if (other.company != null)
                    return false;
            } else if (!company.equals(other.company))
                return false;
            if (country == null) {
                if (other.country != null)
                    return false;
            } else if (!country.equals(other.country))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (year == null) {
                if (other.year != null)
                    return false;
            } else if (!year.equals(other.year))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "CD [country=" + country + ", company=" + company + ", artist=" + artist + ", name=" + name
                + ", price=" + price + ", year=" + year + "]";
        }

        private TestTree getOuterType() {
            return TestTree.this;
        }
    }

}
