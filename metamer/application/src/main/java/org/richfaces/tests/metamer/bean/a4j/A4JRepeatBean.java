/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.bean.a4j;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.UIRepeat;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for a4j:repeat.
 *
 * @author Nick Belaevski, <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22460 $
 */
@ManagedBean(name = "a4jRepeatBean")
@ViewScoped
public class A4JRepeatBean implements Serializable {

    private static final long serialVersionUID = 4864439475400649809L;
    private static final int MATRIX_DIMENSION = 4;
    private static Logger logger;
    private Attributes attributes;
    private List<Data> dataList;
    private Data selectedDataItem = null;
    private List<MatrixRow> matrixRows;

    public static final class MatrixCell implements Serializable {

        private static final long serialVersionUID = -5911659561854593681L;
        private int value = 0;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public void clearValueAction() {
            setValue(0);
        }

        public void increaseValueAction() {
            value++;
        }

        public void decreaseValueAction() {
            value--;
        }
    }

    public static final class MatrixRow implements Serializable {

        private static final long serialVersionUID = -5051037819565283283L;
        private List<MatrixCell> cells = new ArrayList<MatrixCell>();

        public List<MatrixCell> getCells() {
            return cells;
        }

        public void addCell(MatrixCell cell) {
            cells.add(cell);
        }
    }

    public static final class Data implements Serializable {

        private static final long serialVersionUID = -1461777632529492912L;
        private String text;

        /**
         * @return the text
         */
        public String getText() {
            return text;
        }

        /**
         * @param text the text to set
         */
        public void setText(String text) {
            this.text = text;
        }
    }

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        // initialize model for page simple.xhtml
        dataList = new ArrayList<Data>();
        for (int i = 0; i < 20; i++) {
            Data data = new Data();
            data.setText(MessageFormat.format("Item {0}", i));
            dataList.add(data);
        }

        // initialize model for page matrix.xhtml
        matrixRows = new ArrayList<MatrixRow>();
        for (int i = 0; i < MATRIX_DIMENSION; i++) {
            MatrixRow matrixRow = new MatrixRow();

            for (int j = 0; j < MATRIX_DIMENSION; j++) {
                MatrixCell matrixCell = new MatrixCell();
                matrixRow.addCell(matrixCell);
            }

            matrixRows.add(matrixRow);
        }

        // initialize attributes
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIRepeat.class, getClass());
        attributes.setAttribute("rendered", true);
        // TODO has to be tested in other way
        attributes.remove("componentState");
        attributes.remove("iterationState");
        attributes.remove("iterationStatusVar");
        attributes.remove("rowKeyVar");
        attributes.remove("rowKeyConverter");
        attributes.remove("value");
        attributes.remove("stateVar");
        attributes.remove("var");

        // should be hidden
        attributes.remove("relativeRowIndex");
        attributes.remove("rowAvailable");
        attributes.remove("rowCount");
        attributes.remove("rowData");
        attributes.remove("rowIndex");
        attributes.remove("rowKey");
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the data
     */
    public List<Data> getDataList() {
        return dataList;
    }

    /**
     * @return the selectedDataItem
     */
    public Data getSelectedDataItem() {
        return selectedDataItem;
    }

    /**
     * @param selectedDataItem the selectedDataItem to set
     */
    public void setSelectedDataItem(Data selectedDataItem) {
        this.selectedDataItem = selectedDataItem;
    }

    public List<MatrixRow> getMatrixRows() {
        return matrixRows;
    }
}
