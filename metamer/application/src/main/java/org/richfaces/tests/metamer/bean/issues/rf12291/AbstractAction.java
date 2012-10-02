package org.richfaces.tests.metamer.bean.issues.rf12291;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAction {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractAction.class);

    protected ManagedBeanFactory managedBeanFactory = new ManagedBeanFactoryImpl();

    protected JsfUtil jsfUtil = new JsfUtilImpl();

    public void activateTab(String tabId, String activeInitActionBeanId) {
        logger.info("Activating tab with id: " + tabId);
        UserInfo userInfo = jsfUtil.getUserInfo();
        userInfo.setActiveTabId(tabId);

        if (activeInitActionBeanId != null && !activeInitActionBeanId.trim().isEmpty()) {
            logger.info("Executing doInitAction on bean: " + activeInitActionBeanId);
            doInitAction(activeInitActionBeanId, false);
        }
    }

    public String getTabIncludePath(String srcTabId) {

        logger.info("Getting tab include path for srcTabId: " + srcTabId + "...");
        UserInfo userInfo = jsfUtil.getUserInfo();

        if (srcTabId.equals(userInfo.getActiveTabId())) {
            InitAction initAction = (InitAction) managedBeanFactory.getManagedBeanById(userInfo.getActiveActionBeanId());
            logger.info("Got tab include path for srcTabId: " + srcTabId + ": " + initAction.getIncludePath());
            return initAction.getIncludePath();
        } else {
            logger.info("Got empty include path for srcTabId: " + srcTabId);
            return CoreJsfConstant.EMPTY_INCLUDE_PATH;
        }

    }

    public String getIncludePath(String activeInitActionBeanId) {
        InitAction initAction = (InitAction) managedBeanFactory.getManagedBeanById(activeInitActionBeanId);
        return initAction.getIncludePath();
    }

    public String doInitAction(String activeInitActionBeanId, Boolean asNavigation, Object initActionArg) {
        jsfUtil.getUserInfo().setActiveActionBeanId(activeInitActionBeanId);

        InitAction initAction = (InitAction) managedBeanFactory.getManagedBeanById(activeInitActionBeanId);
        initAction.init(initActionArg);

        return asNavigation ? initAction.getNavigationOutcome() : null;
    }

    public String doInitAction(String activeInitActionBeanId, Boolean asNavigation) {
        return doInitAction(activeInitActionBeanId, asNavigation, (Object) null);
    }

}