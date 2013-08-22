package org.richfaces.tests.page.fragments.impl.inputNumberSlider;

public interface NumberInput {

    void increase();

    void decrease();

    void increase(int n);

    void decrease(int n);

    void setValue(double value);

    double getValue();
}
