package com.babii.company.analysis.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Company {
    private long cik;
    private String symbol;
    private String city;
    private String state;
    private String street;
    private String phone;
    private String name;
    private String sector;
    private String industry;
}
