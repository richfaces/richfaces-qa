package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

@ManagedBean(name = "validationTest")
@ViewScoped
public class RF13595 implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean inputVisible;
    private Date input;

    public void validateInput(FacesContext ctx, UIComponent comp, Object value) throws ValidatorException {
        if (value instanceof String) {
            Date text = (Date) value;
            System.out.println("Validating input: " + text);
            // create a yesterday Date and validate that selected date is after this new date (the test selects current date)
            Calendar c = new GregorianCalendar();
            c.add(Calendar.DAY_OF_MONTH, -1);
            if (text.after(c.getTime())) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Must not be after current time!",
                    null));
            }
        } else {
            System.out.println("Input value is no String!");
        }
    }

    public Date getInput() {
        return input;
    }

    public void setInput(Date input) {
        this.input = input;
    }

    public boolean isInputVisible() {
        return inputVisible;
    }

    public synchronized void changeInputVisibility(ActionEvent event) {
        System.out.println("Visibility change event");
        inputVisible = !inputVisible;
    }

}
