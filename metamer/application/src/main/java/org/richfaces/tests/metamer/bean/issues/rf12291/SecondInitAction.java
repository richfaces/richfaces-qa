package org.richfaces.tests.metamer.bean.issues.rf12291;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = SecondInitAction.BEAN_NAME)
@RequestScoped
public class SecondInitAction extends AbstractAction implements InitAction {

    public static final String BEAN_NAME = "secondInitAction";

    @Override
    public String getNavigationOutcome() {
        return "/module/core/test?faces-redirect=false";
    }

    @Override
    public String getIncludePath() {
        return "/module/core/secondInclude.xhtml";
    }

    public void init(Object initArg) {
        logger.info("Initting second action");
        PaginatedTableBean paginatedTableBean = new PaginatedTableBean();
        jsfUtil.storeToSession(HttpSessionConstant.MY_BEAN, paginatedTableBean);
    }

}