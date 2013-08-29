package org.richfaces.tests.page.fragments.impl.panel;

public abstract class Panel<HEADER, BODY> {

    public abstract HEADER getHeaderContent();

    public abstract BODY getBodyContent();
}
