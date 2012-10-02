package org.richfaces.tests.metamer.bean.issues.rf12291;

public final class UserInfo {

    public static final String BEAN_NAME = "userInfo";

    private String username = null;

    public String getActiveTabId() {
        return activeTabId;
    }

    public void setActiveTabId(String activeTabId) {
        this.activeTabId = activeTabId;
    }

    public String getActiveActionBeanId() {
        return activeActionBeanId;
    }

    public void setActiveActionBeanId(String activeActionBeanId) {
        this.activeActionBeanId = activeActionBeanId;
    }

    private String activeTabId;
    private String activeActionBeanId;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

}
