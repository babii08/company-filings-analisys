package com.babii.company.analysis.dto;

import com.babii.company.analysis.domain.model.CompanyDBO;
import com.babii.company.analysis.domain.model.Quarter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class BalanceSheet {

    private long id;
    private BigDecimal cashAndEquivalents;
    private BigDecimal totalCurrentAssets;
    private BigDecimal goodwill;
    private BigDecimal totalAssets;
    private BigDecimal accountsPayable;
    private BigDecimal totalCurrentLiabilities;
    private BigDecimal totalLiabilities;
    private BigDecimal commonStock;
    private BigDecimal shareholdersEquity;
    private BigDecimal totalLiabilitiesAndEquity;
    private Long companyCik;
    private LocalDate date;
    private Quarter quarter;
    private String link;
}
