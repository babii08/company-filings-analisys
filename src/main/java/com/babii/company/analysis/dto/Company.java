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
}
