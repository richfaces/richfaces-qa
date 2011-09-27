package org.jboss.test.selenium.android;

/**
 * {@link http://thecodeartist.blogspot.com/2011/03/simulating-keyevents-on-android-device.html}
 * 
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public enum Key {

    UP_ARROW(19),
    DOWN_ARROW(20),
    LEFT_ARROW(21),
    RIGHT_ARROW(22),
    ENTER(66);
    
    private int code;
    
    private Key(int code) {
        this.code = code;
    }
    
    /**
     * Returns a key code which is used by Android SDK 
     * (https://lh4.googleusercontent.com/-kb19V3eCj1g/TYIaEklPgmI/AAAAAAAAAXQ/cz1mEj9Wr_s/s1600/Android-input-keycodes.png)
     * 
     * @return a key code 
     */
    public int getCode() {
        return code;
    }
    
}