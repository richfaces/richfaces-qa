package org.richfaces.tests.metamer.bean.issues;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.richfaces.push.MessageException;
import org.richfaces.push.TopicKey;
import org.richfaces.push.TopicsContext;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@ManagedBean(name = "rf13592Bean")
@SessionScoped
public class RF13592 {

    private static final String ADDRESS1 = "TEST_ADDRESS1";
    private boolean enabled1 = false;

    private static final String ADDRESS2 = "TEST_ADDRESS2";
    private boolean enabled2 = false;

    private static final String ADDRESS3 = "TEST_ADDRESS3";
    private boolean enabled3 = false;

    public boolean isEnabled1() {
        return enabled1;
    }

    public void startPush1() {
        enabled1 = true;
    }

    public void stopPush1() {
        enabled1 = false;
    }

    public void sendMessage1() throws MessageException {
        TopicsContext.lookup().publish(new TopicKey(ADDRESS1), "Hello World 1!");
    }

    public boolean isEnabled2() {
        return enabled2;
    }

    public void startPush2() {
        enabled2 = true;
    }

    public void stopPush2() {
        enabled2 = false;
    }

    public void sendMessage2() throws MessageException {
        TopicsContext.lookup().publish(new TopicKey(ADDRESS2), "Hello World 2!");
    }

    public boolean isEnabled3() {
        return enabled3;
    }

    public void startPush3() {
        enabled3 = true;
    }

    public void stopPush3() {
        enabled3 = false;
    }

    public void sendMessage3() throws MessageException {
        TopicsContext.lookup().publish(new TopicKey(ADDRESS3), "Hello World 3!");
    }
}
