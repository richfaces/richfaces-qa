package org.richfaces.tests.metamer.bean.issues.rf12291;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AbortProcessingException;

import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = TestTabPanelItemChangeListener.BEAN_NAME)
@ApplicationScoped
public class TestTabPanelItemChangeListener implements ItemChangeListener {

    public static final String BEAN_NAME = "testTabPanelItemChangeListener";

    protected static final Logger logger = LoggerFactory.getLogger(TestTabPanelItemChangeListener.class);

    private ManagedBeanFactory managedBeanFactory = new ManagedBeanFactoryImpl();

    private Map<String, String> activeTabToInitAction = new HashMap<String, String>();

    public TestTabPanelItemChangeListener() {
        activeTabToInitAction.put("firstTabId", "firstInitAction");
        activeTabToInitAction.put("secondTabId", "secondInitAction");
    }

    @Override
    public void processItemChange(ItemChangeEvent event) throws AbortProcessingException {
        logger.info("'processItemChange' was called. Old item name: " + event.getOldItemName() + ". New item name: "
            + event.getNewItemName());
        TestAction testAction = (TestAction) managedBeanFactory.getManagedBeanById(TestAction.BEAN_NAME);
        testAction.activateTab(event.getNewItemName(), activeTabToInitAction.get(event.getNewItemName()));
    }

}