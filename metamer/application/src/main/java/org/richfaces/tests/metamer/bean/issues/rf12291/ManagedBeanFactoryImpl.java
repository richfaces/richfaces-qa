package org.richfaces.tests.metamer.bean.issues.rf12291;

import javax.el.ELContext;
import javax.faces.context.FacesContext;

public class ManagedBeanFactoryImpl implements ManagedBeanFactory {

    @Override
    public Object getManagedBeanById(String id) {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        return FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, id);
    }

}
