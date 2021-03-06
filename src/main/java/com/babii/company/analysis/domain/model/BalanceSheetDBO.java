package com.babii.company.analysis.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "Balance_Sheet")
@NoArgsConstructor
@AllArgsConstructor
public class BalanceSheetDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    //Assets

    @Column(name = "CASH_AND_CASH_EQUIVALENTS")
    private BigDecimal cash;

    @Column(name = "TOTAL_CURRENT_ASSETS")
    private BigDecimal totalCurrentAssets;

    @Column(name = "GOODWILL")
    private BigDecimal goodwill;

    @Column(name = "TOTAL_ASSETS")
    private BigDecimal totalAssets;

    //Liabilities

    @Column(name = "ACCOUNTS_PAYABLE")
    private BigDecimal accountsPayable;

    @Column(name = "TOTAL_CURRENT_LIABILITIES")
    private BigDecimal totalCurrentLiabilities;

    @Column(name = "totalLiabilities")
    private BigDecimal totalLiabilities;

    @Column(name = "COMMON_STOCK")
    private BigDecimal commonStock;

    @Column(name = "SHAREHOLDERS_EQUITY")
    private BigDecimal shareholdersEquity;

    @Transient
    @Formula("totalLiabilities::numeric + shareholdersEquity::numeric")
    private BigDecimal totalLiabilitiesAndEquity;

    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyDBO company;

    @Column(name = "DATE")
    private LocalDate date;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "QUARTER")
    private Quarter quarter;

    @Column(name = "QUARTER_LINKS")
    private String link;
}



