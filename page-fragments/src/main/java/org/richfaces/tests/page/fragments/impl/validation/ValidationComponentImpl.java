package org.richfaces.tests.page.fragments.impl.validation;

import java.util.List;

import org.jboss.arquillian.graphene.component.object.api.validation.ValidationComponent;

public class ValidationComponentImpl implements ValidationComponent {

    @Override
    public List<Message> getAllMessages() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ErrorMessage> getAllErrorMessages() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ErrorMessage> getAllInfoMessages() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isMessageRendered(Message msg) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMessageRendered(StaticMessagePart msg) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isThereAnyErrorMessage() {
        // TODO Auto-generated method stub
        return false;
    }

}
