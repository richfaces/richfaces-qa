package org.richfaces.tests.metamer.bean.issues;

import java.math.BigDecimal;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "rf13842")
@SessionScoped
public class RF13842 {

    private BigDecimal value1 = BigDecimal.ZERO;
    private BigDecimal value2 = BigDecimal.ZERO;
    private BigDecimal result1 = BigDecimal.ZERO;
    private BigDecimal result2 = BigDecimal.ZERO;

    public void calcResult1() {
        if (value1 == null) {
            result1 = null;
        }
        else {
            result1 = value1.multiply(BigDecimal.TEN);
        }
    }
    
    public void calcResult2() {
        if (value2 == null) {
            result2 = null;
        }
        else {
            result2 = value2.multiply(BigDecimal.TEN);
        }
    }
    
    public String save() {
        return null;
    }


    public BigDecimal getValue1() {
        return value1;
    }

    public void setValue1(BigDecimal value1) {
        this.value1 = value1;
    }
    
    public BigDecimal getValue2() {
        return value2;
    }

    public void setValue2(BigDecimal value2) {
        this.value2 = value2;
    }

    public BigDecimal getResult1() {
        return result1;
    }

    public void setResult1(BigDecimal result1) {
        this.result1 = result1;
    }
    
    public BigDecimal getResult2() {
        return result2;
    }

    public void setResult2(BigDecimal result2) {
        this.result2 = result2;
    }

}

