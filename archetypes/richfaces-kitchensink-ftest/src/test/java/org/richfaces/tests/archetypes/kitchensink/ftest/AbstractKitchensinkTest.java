package org.richfaces.tests.archetypes.kitchensink.ftest;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.android.AndroidDriver;
import org.testng.annotations.BeforeMethod;

public class AbstractKitchensinkTest extends Arquillian {

    @ArquillianResource
    protected URL contextRoot;

    @Drone
    protected WebDriver webDriver;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {

        String pathToAUT = System.getProperty("application.war");

        WebArchive war = ShrinkWrap.createFromZipFile(WebArchive.class, new File(pathToAUT));
        return war;
    }

    @BeforeMethod
    public void loadPage() throws MalformedURLException {

        webDriver.get(getDeployedURL().toString());
    }

    protected URL getDeployedURL() throws MalformedURLException {
        if (!(webDriver instanceof AndroidDriver)) {
            return contextRoot;
        } else {
            String host = System.getProperty("host");
            return new URL(contextRoot.toString().replace(contextRoot.getHost(), host));
        }
    }
}
