package org.richfaces.tests.metamer.ftest.utils;

import java.io.File;
import java.io.IOException;

import org.wildfly.extras.creaper.core.ManagementClient;
import org.wildfly.extras.creaper.core.offline.OfflineManagementClient;
import org.wildfly.extras.creaper.core.offline.OfflineOptions;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.OnlineOptions;

/**
 * Provider for Creaper's OnlineManagementClient
 */
public class ManagementClientProvider {

    /**
     * Creates OnlineManagementClient for standalone server
     * @return Initialized OnlineManagementClient, don't forget to close it
     */
    public static OnlineManagementClient createOnlineManagementClientForStandaloneServer() {
        return ManagementClient.onlineLazy(OnlineOptions.standalone()
                .hostAndPort("localhost", 9990)
                .build());
    }

    public static OfflineManagementClient createOfflineManagementClientForStandaloneServer() throws IOException {
        return ManagementClient.offline(OfflineOptions.standalone()
                .rootDirectory(new File(System.getProperty("JBOSS_HOME")))
                .configurationFile("standalone.xml")
                .build());
    }

}
