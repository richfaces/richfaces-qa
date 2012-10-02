package org.richfaces.tests.metamer.bean.issues.rf12291;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public final class JsfUtilImpl implements JsfUtil {

    @Override
    public UserInfo getUserInfo() {
        HttpSession session = getHttpSession();
        Object obj = session.getAttribute(HttpSessionConstant.USER_INFO);
        if (obj == null) {
            throw new IllegalStateException("No UserInfoDO was found in session under name " + HttpSessionConstant.USER_INFO);
        }
        try {
            return (UserInfo) obj;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Session attribute having name " + HttpSessionConstant.USER_INFO
                + " should be an instance of UserInfo class", cce);
        }
    }

    @Override
    public void storeToSession(final String attributeName, final Object obj) {
        getHttpSession().setAttribute(attributeName, obj);
    }

    private HttpSession getHttpSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

}
