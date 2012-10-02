package org.richfaces.tests.metamer.bean.issues.rf12291;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = TestAction.BEAN_NAME)
@RequestScoped
public class TestAction extends AbstractAction {

    public static final String BEAN_NAME = "testAction";

    public String startAction() {
        logger.info("I have called startAction after click on the command link!");
        UserInfo userInfo = new UserInfo();
        userInfo.setActiveTabId("firstTabId");
        jsfUtil.storeToSession(HttpSessionConstant.USER_INFO, userInfo);
        return super.doInitAction("firstInitAction", true, (Object) null);
    }

}
