package com.scm.forms;

import lombok.Getter;
import lombok.Setter;


public class ContactSearchForm {

    private String field;
    private String value;

    //getters and setters

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
