package com.babii.company.analysis.domain.model;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Quarter {
    Q1("QTR1") ,
    Q2("QTR2"),
    Q3("QTR3"),
    Q4("QTR4"),
    UNKNOWN("");

    private String text;

    Quarter(String text) {
        this.text = text;
    }

    public static Quarter getQuarter(String value) {
        return Stream.of(values()).filter(val -> val.getText().equals(value)).findFirst().orElse(null);
    }
}
