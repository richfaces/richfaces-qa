package org.jsf.reproducers;

import java.io.Serializable;

public class JSF3545ChildBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String value;

    public JSF3545ChildBean(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {// ignored
    }
}
