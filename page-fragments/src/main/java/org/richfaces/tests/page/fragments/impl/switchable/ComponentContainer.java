package org.richfaces.tests.page.fragments.impl.switchable;

public interface ComponentContainer {

    <T> T getContent(Class<T> clazz);
}