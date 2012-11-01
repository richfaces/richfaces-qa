package org.richfaces.tests.page.fragments.ftest.log;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.ftest.AbstractTest;
import org.richfaces.tests.page.fragments.impl.log.LogEntry;
import org.richfaces.tests.page.fragments.impl.log.LogEntryLevel;
import org.richfaces.tests.page.fragments.impl.log.LogImpl;
import org.testng.annotations.Test;

public class TestLogComponent extends AbstractTest {

    @Page
    private LogPage logPage;

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return createDeployment(TestLogComponent.class);
    }

    private static final String DEBUG_MSG = "Debug Message";
    private static final String INFO_MSG = "Info Message";
    private static final String WARN_MSG = "Warn Message";
    private static final String ERROR_MSG = "Error Message";

    @Test
    public void testGetAllLogEntries() {
        logPage.getDebugButton().click();
        logPage.getInfoButton().click();
        logPage.getWarnButton().click();
        logPage.getErrorButton().click();

        int expected = 4;
        List<LogEntry> allEntries = logPage.getLog().getAllLogEntries();
        assertEquals(allEntries.size(), expected, "There should be different number of entries!");
    }

    @Test
    public void testGetAllDebugEntries() {
        int expected = 5;
        logPage.generateLogEntry(LogEntryLevel.DEBUG, expected);
        logPage.generateLogEntry(LogEntryLevel.INFO, expected - 1);

        List<LogEntry> debugEntries = logPage.getLog().getAllDebugEntries();
        assertEquals(debugEntries.size(), expected, "The number of debug logs is different!");
        logPage.assertLogEntries(LogEntryLevel.DEBUG, debugEntries);
    }

    @Test
    public void testGetAllInfoEntries() {
        int expected = 5;
        logPage.generateLogEntry(LogEntryLevel.INFO, expected);
        logPage.generateLogEntry(LogEntryLevel.ERROR, expected - 1);

        List<LogEntry> infoEntries = logPage.getLog().getAllInfoEntries();
        assertEquals(infoEntries.size(), expected, "The number of info logs is different!");
        logPage.assertLogEntries(LogEntryLevel.INFO, infoEntries);
    }

    @Test
    public void testGetAllWarnEntries() {
        int expected = 5;
        logPage.generateLogEntry(LogEntryLevel.WARN, expected);
        logPage.generateLogEntry(LogEntryLevel.INFO, expected - 1);

        List<LogEntry> warnEntries = logPage.getLog().getAllWarnEntries();
        assertEquals(warnEntries.size(), expected, "The number of warn logs is different!");
        logPage.assertLogEntries(LogEntryLevel.WARN, warnEntries);
    }

    @Test
    public void testGetAllErrorEntries() {
        int expected = 5;
        logPage.generateLogEntry(LogEntryLevel.ERROR, expected);
        logPage.generateLogEntry(LogEntryLevel.INFO, expected - 1);

        List<LogEntry> errorEntries = logPage.getLog().getAllErrorEntries();
        assertEquals(errorEntries.size(), expected, "The number of error logs is different!");
        logPage.assertLogEntries(LogEntryLevel.ERROR, errorEntries);
    }

    public class LogPage {

        @FindBy(id = "richfaces.log")
        private LogImpl log;

        @FindBy(id = "debugButton")
        private WebElement debugButton;

        @FindBy(id = "infoButton")
        private WebElement infoButton;

        @FindBy(id = "warnButton")
        private WebElement warnButton;

        @FindBy(id = "errorButton")
        private WebElement errorButton;

        public void assertLogEntries(LogEntryLevel lvl, List<LogEntry> entries) {
            for (LogEntry entry : entries) {
                assertEquals(entry.getLevel(), lvl, "The log entry has wrong lvl");

                String content = entry.getContent();
                String errorMsg = "The entry has wrong content set!";

                switch (lvl) {
                    case DEBUG:
                        assertEquals(content, DEBUG_MSG, errorMsg);
                        break;
                    case INFO:
                        assertEquals(content, INFO_MSG, errorMsg);
                        break;
                    case WARN:
                        assertEquals(content, WARN_MSG, errorMsg);
                        break;
                    case ERROR:
                        assertEquals(content, ERROR_MSG, errorMsg);
                        break;
                }
            }
        }

        public void generateLogEntry(LogEntryLevel lvl, int howMany) {
            for (int i = 0; i < howMany; i++) {
                switch (lvl) {
                    case DEBUG:
                        debugButton.click();
                        break;
                    case INFO:
                        infoButton.click();
                        break;
                    case WARN:
                        warnButton.click();
                        break;
                    case ERROR:
                        errorButton.click();
                        break;
                }
            }
        }

        public LogImpl getLog() {
            return log;
        }

        public void setLog(LogImpl log) {
            this.log = log;
        }

        public WebElement getDebugButton() {
            return debugButton;
        }

        public void setDebugButton(WebElement debugButton) {
            this.debugButton = debugButton;
        }

        public WebElement getInfoButton() {
            return infoButton;
        }

        public void setInfoButton(WebElement infoButton) {
            this.infoButton = infoButton;
        }

        public WebElement getWarnButton() {
            return warnButton;
        }

        public void setWarnButton(WebElement warnButton) {
            this.warnButton = warnButton;
        }

        public WebElement getErrorButton() {
            return errorButton;
        }

        public void setErrorButton(WebElement errorButton) {
            this.errorButton = errorButton;
        }

    }

}
