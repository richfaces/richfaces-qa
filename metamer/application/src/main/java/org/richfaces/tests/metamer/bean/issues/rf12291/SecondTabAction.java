package org.richfaces.tests.metamer.bean.issues.rf12291;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = SecondTabAction.BEAN_NAME)
@RequestScoped
public class SecondTabAction extends AbstractAction {

    public static final String BEAN_NAME = "secondTabAction";

    private LazyDataModel<String> paginatedTableDataModel;

    public SecondTabAction() {
        SecondTabDataProvider secondTabDataProvider = new SecondTabDataProvider();
        paginatedTableDataModel = new LazyDataModel<String>(secondTabDataProvider);
    }

    // public void init() {
    // SecondTabDataProvider secondTabDataProvider = beansFactory.getBean(SecondTabDataProvider.class);
    // paginatedTableDataModel = (LazyDataModel<String>) beansFactory.getBean(
    // LazyDataModel.BEAN_NAME, new Object[] {secondTabDataProvider});
    // }

    public LazyDataModel<String> getPaginatedTableDataModel() {
        return paginatedTableDataModel;
    }

}
