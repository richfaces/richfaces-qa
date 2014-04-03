/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.richfaces.fragment.dataTable;

/**
 *
 * @author jhuska
 */
public interface DataTableWithHeaderAndFooter<HEADER, ROW, FOOTER> extends DataTable<ROW> {

    HEADER getHeader();

    FOOTER getFooter();
}
