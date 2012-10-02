package org.richfaces.tests.metamer.bean.issues.rf12291;

public interface JsfUtil {

    String BEAN_NAME = "jsfUtil";

    UserInfo getUserInfo();

    void storeToSession(String attributeName, Object obj);

}