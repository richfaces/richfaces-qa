package org.richfaces.tests.metamer.bean.issues.rf12291;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = FirstInitAction.BEAN_NAME)
@RequestScoped
public class FirstInitAction extends AbstractAction implements InitAction {

    public static final String BEAN_NAME = "firstInitAction";

    @Override
    public String getNavigationOutcome() {
        return "/module/core/test?faces-redirect=false";
    }

    @Override
    public String getIncludePath() {
        return "/module/core/firstInclude.xhtml";
    }

    public void init(Object initArg) {
        logger.info("Initting first action");
        RegularTableBean regularTableBean = new RegularTableBean();
        jsfUtil.storeToSession(HttpSessionConstant.MY_BEAN, regularTableBean);
    }

}
