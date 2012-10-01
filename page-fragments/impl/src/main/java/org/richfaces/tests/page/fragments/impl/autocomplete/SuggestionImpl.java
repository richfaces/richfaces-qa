package org.richfaces.tests.page.fragments.impl.autocomplete;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.graphene.component.object.api.autocomplete.Suggestion;

public class SuggestionImpl<T> implements Suggestion<T> {

    private T value;
    private List<String> inputValues;

    public SuggestionImpl() {
        inputValues = new ArrayList<String>();
    }

    public SuggestionImpl(T value) {
        inputValues = new ArrayList<String>();
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public List<String> getInputs() {
        return inputValues;
    }

    @Override
    public void setInput(String value) {
        inputValues.add(value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SuggestionImpl other = (SuggestionImpl) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SuggestionImpl [value=" + value + "]";
    }
}
