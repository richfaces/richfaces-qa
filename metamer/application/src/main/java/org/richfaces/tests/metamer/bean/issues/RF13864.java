package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.richfaces.component.UISelect;

@ViewScoped
@ManagedBean(name = "rf13864")
public class RF13864 implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> selectLOV;

    private String selectVal;
    private String result;

    @PostConstruct
    public void postContruct() {
        selectLOV = new ArrayList<String>();
        selectLOV.add("One");
        selectLOV.add("Two");
        selectLOV.add("Three");
    }

    public void valueChanged(AjaxBehaviorEvent event) {
        String strValue = (String) ((UISelect) event.getComponent()).getValue();
        setSelectVal(strValue);
        setResult("Result  is  :  " + strValue);

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return the selectLOV
     */
    public List<String> getSelectLOV() {
        return selectLOV;
    }

    /**
     * @param selectLOV the selectLOV to set
     */
    public void setSelectLOV(List<String> selectLOV) {
        this.selectLOV = selectLOV;
    }

    public String getSelectVal() {
        return selectVal;
    }

    public void setSelectVal(String selectVal) {
        this.selectVal = selectVal;
    }

}
